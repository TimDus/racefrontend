package racegame.socketModel;

import lombok.Getter;
import lombok.Setter;

public class MoveMessage
{
    @Setter @Getter
    private MessageType type;
    @Getter @Setter
    private String content;
    @Getter @Setter
    private String sender;
}
