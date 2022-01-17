package racegame.socketModel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import racegame.model.Car;

@Builder
public class UserMessage {
    @Getter
    private MessageType type;
    @Getter @Setter
    private Car[] content;
    @Getter
    private String sender;

}
