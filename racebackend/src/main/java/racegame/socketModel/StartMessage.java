package racegame.socketModel;

import lombok.Getter;
import lombok.Setter;
import racegame.model.Car;

public class StartMessage {
    @Getter @Setter
    private MessageType type;
    @Getter @Setter
    private int content;
    @Getter @Setter
    private String sender;
}
