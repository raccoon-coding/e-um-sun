package eumsun.backend.service.gpt;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import eumsun.backend.domain.Alert;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.request.GPTCompletionRequest;
import eumsun.backend.repository.AlertRepository;
import eumsun.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static eumsun.backend.config.util.GptUtil.SYSTEM;
import static eumsun.backend.config.util.GptUtil.SYSTEM_PROMPT_PARENT;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GPTRiskJudgmentService {

    private final OpenAiService openAiService;
    private final UserService userService;
    private final AlertRepository alertRepository;

    @Async("gptThreadPoolTask")
    public void judgmentRisk(String message, String offspringId) {

        GPTCompletionRequest completionRequest = new GPTCompletionRequest(message);
        ChatCompletionRequest chatCompletionRequest = GPTCompletionRequest.of(completionRequest, createSystemPrompt());

        ChatCompletionResult chatCompletionResult = openAiService.createChatCompletion(chatCompletionRequest);
        chatCompletionResult.getChoices().forEach(oneString -> {
            if(Objects.equals(oneString.getMessage().getContent(), "True")){
                saveAlert(message, offspringId);
            }
            else{
                log.info(oneString.getMessage().getContent());
            }
                }
        );
    }

    private List<ChatMessage> createSystemPrompt () {

        List<ChatMessage> chatMessageList = new ArrayList<>();
        chatMessageList.add(new ChatMessage(SYSTEM, SYSTEM_PROMPT_PARENT));
        return chatMessageList;
    }

    @Transactional
    private void saveAlert(String message, String offspringEmail) {

        UserData offspring = userService.findUserDataByEmail(offspringEmail);
        String parentId = userService.findParentIdByOffspringId(offspring.getId());

        Alert alert = Alert.builder()
                .offspringId(offspring.getId())
                .parentId(parentId)
                .content(message)
                .build();

        log.info(alert.getContent());

        alertRepository.save(alert);
    }
}
