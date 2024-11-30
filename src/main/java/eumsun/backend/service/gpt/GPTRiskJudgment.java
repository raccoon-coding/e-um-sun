package eumsun.backend.service.gpt;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.request.service.FCMDto;
import eumsun.backend.dto.toService.GPTRiskDto;
import eumsun.backend.dto.toService.CreateAlertDto;
import eumsun.backend.service.AlertService;
import eumsun.backend.service.FirebaseService;
import eumsun.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GPTRiskJudgment {
    private final UserService userService;
    private final GPTService gptService;
    private final FirebaseService firebaseService;
    private final AlertService alertService;

    private final String MESSAGE_TITLE = "연락 부탁드립니다!!!";
    private final String GPT_JUDGEMENT = "True";

    @Async("gpt Risk Judgment Async")
    public void judgmentRiskController(GPTRiskDto gptRiskDto) {
        UserData offspring = gptRiskDto.offspring();
        UserData parent = userService.findParent(offspring);

        ChatCompletionResult chatCompletionResult = gptService.callGptChatApi(gptRiskDto.request(), parent);

        verifyGptJudgment(gptRiskDto, chatCompletionResult, offspring, parent);
    }

    private void verifyGptJudgment(GPTRiskDto gptRiskDto, ChatCompletionResult chatCompletionResult,
                                   UserData offspring, UserData parent) {
        chatCompletionResult.getChoices().forEach(oneString -> {
            verifyRisk(gptRiskDto, offspring, parent, oneString);
        });
    }

    private void verifyRisk(GPTRiskDto gptRiskDto, UserData offspring, UserData parent, ChatCompletionChoice oneString) {
        if(Objects.equals(oneString.getMessage().getContent(), GPT_JUDGEMENT)){
            alertParent(offspring, parent, gptRiskDto.message());
            return;
        }
        log.info(oneString.getMessage().getContent());
    }

    private void alertParent(UserData offspring, UserData parent, String message) {
        try {
            sendingMessageParent(parent, message);
            saveAlert(offspring.getId(), parent.getId(), message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendingMessageParent(UserData parent, String message) throws IOException {
        firebaseService.sendMessageTo(FCMDto.builder()
                .token(parent.getFirebaseToken())
                .title(MESSAGE_TITLE)
                .body(message)
                .build());
    }

    private void saveAlert(String offspringId, String parentId, String message) {
        CreateAlertDto dto = new CreateAlertDto(offspringId, parentId, message);
        alertService.createAlert(dto);
    }
}
