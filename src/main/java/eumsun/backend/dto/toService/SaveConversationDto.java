package eumsun.backend.dto.toService;

import eumsun.backend.domain.Conversation;
import eumsun.backend.domain.Message;

public record SaveConversationDto(Conversation conversation, Message message) {
}
