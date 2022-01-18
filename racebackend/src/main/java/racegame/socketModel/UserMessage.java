package racegame.socketModel;

import lombok.Getter;
import lombok.Setter;
import racegame.model.Car;

public class UserMessage {
    @Getter @Setter
    private MessageType type;
    @Getter @Setter
    private Car[] content;
    @Getter @Setter
    private String sender;

}
