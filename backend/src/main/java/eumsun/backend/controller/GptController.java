package eumsun.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import eumsun.backend.dto.request.GPTCompletionRequest;
import eumsun.backend.service.UserService;
import eumsun.backend.service.gpt.GPTRiskJudgmentService;
import eumsun.backend.service.gpt.GPTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
@RequestMapping("offspring/chat")
@RequiredArgsConstructor
public class GptController {

    private final ObjectMapper objectMapper;
    private final GPTService gptService;
    private final OpenAiService openAiService;
    private final UserService userService;
    private final GPTRiskJudgmentService gptRiskJudgmentService;

    @PostMapping("/stream")
    private ResponseEntity<SseEmitter> handleTextMessage(@RequestBody String message) {

        SseEmitter emitter = new SseEmitter((long) (5 * 60 * 1000));

        GPTCompletionRequest completionRequest = new GPTCompletionRequest(message);
        gptRiskJudgmentService.judgmentRisk(message, userService.findUserDataByToken());

        StringBuffer stringBuffer = new StringBuffer();

        ChatCompletionRequest chatCompletionRequest = GPTCompletionRequest.of(completionRequest, gptService.createSystemPrompt());

        openAiService.streamChatCompletion(chatCompletionRequest)
                .blockingForEach(completion -> {
                    String responseWord = objectMapper.writeValueAsString(completion);
                    emitter.send(SseEmitter.event()
                            .name("message")
                            .data(responseWord, MediaType.APPLICATION_JSON)
                    );
                    // 위의 completion이 OpenAi로부터 받은 데이터이다.
                    JsonNode contentsNode = objectMapper.readTree(responseWord).get("choices");
                    getContents(contentsNode, stringBuffer);
                });

        if (!stringBuffer.isEmpty()) {
            stringBuffer.delete(stringBuffer.length() - 4, stringBuffer.length());
        }

        emitter.complete();

        gptService.saveRequestMessage(message, chatCompletionRequest);
        log.info(stringBuffer.toString());
        gptService.saveResponseMessage(stringBuffer, completionRequest);

        return ResponseEntity.ok(emitter);
    }

    private void getContents(JsonNode contentNodes, StringBuffer stringBuffer) {
        contentNodes.forEach(contentNode -> {
            JsonNode messageNode = contentNode.get("message");
            if (messageNode != null && messageNode.has("content")) {
                String content = messageNode.get("content").asText();
                stringBuffer.append(content);
            }
        });
    }
}
