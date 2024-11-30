package eumsun.backend.controller.tokenController;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.toService.CreateGPTRequest;
import eumsun.backend.dto.request.service.GPTCompletionRequest;
import eumsun.backend.dto.toService.GPTRiskDto;
import eumsun.backend.dto.toService.SaveMessageDto;
import eumsun.backend.service.MessageService;
import eumsun.backend.service.gpt.GPTRiskJudgment;
import eumsun.backend.service.gpt.GPTService;
import eumsun.backend.service.gpt.GptTokenCalculate;
import eumsun.backend.util.UserDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("offspring/chat")
@RequiredArgsConstructor
public class GptController {
    private final GPTRiskJudgment gptRiskJudgment;
    private final GptTokenCalculate gptTokenCalculate;
    private final GPTService gptService;
    private final MessageService messageService;

    @PostMapping("/stream")
    public ResponseEntity<SseEmitter> handleTextMessage(@RequestBody String message) {
        UserData user = UserDataUtil.getUserData();
        GPTCompletionRequest completionRequest = new GPTCompletionRequest(message);
        GPTRiskDto dto = new GPTRiskDto(completionRequest, message, user);

        asyncGptRiskJudgment(dto);
        SseEmitter emitter = asyncGpt(message, dto, user);

        return ResponseEntity.ok(emitter);
    }

    private void asyncGptRiskJudgment(GPTRiskDto dto) {
        gptRiskJudgment.judgmentRiskController(dto);
    }

    @NotNull
    private SseEmitter asyncGpt(String message, GPTRiskDto dto, UserData user) {
        SseEmitter emitter = new SseEmitter((long) (5 * 60 * 1000));
        CreateGPTRequest request = createGPTRequest(dto);

        gptService.callGptStreamApi(request.chatCompletionRequest(), emitter, request.stringBuffer());
        removeSuffix(request);

        emitter.complete();
        saveMessages(message, user, request);
        return emitter;
    }

    private @NotNull CreateGPTRequest createGPTRequest(GPTRiskDto dto) {
        StringBuffer stringBuffer = new StringBuffer();
        UserData offspring = dto.offspring();
        GPTCompletionRequest request = dto.request();

        ChatCompletionRequest chatCompletionRequest = GPTCompletionRequest.of(request,
                gptService.getConversation(offspring));
        return new CreateGPTRequest(request, stringBuffer, chatCompletionRequest);
    }

    private void removeSuffix(CreateGPTRequest request) {
        if (!request.stringBuffer().isEmpty()) {
            request.stringBuffer().delete(request.stringBuffer().length() - 4, request.stringBuffer().length());
        }
    }

    private void saveMessages(String message, UserData user, @NotNull CreateGPTRequest request) {
        Integer token = gptTokenCalculate.calculateToken(request.chatCompletionRequest());

        messageService.saveUserMessage(new SaveMessageDto(user, message, token));
        log.info(request.stringBuffer().toString());
        messageService.saveAssistantMessage(new SaveMessageDto(user, request.stringBuffer().toString(), 0));
    }
}
