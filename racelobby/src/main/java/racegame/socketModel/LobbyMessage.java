package racegame.socketModel;

import lombok.Getter;
import lombok.Setter;
import racegame.model.Lobby;

public class LobbyMessage
{
    @Setter @Getter
    private MessageType type;
    @Getter @Setter
    private Lobby[] content;
    @Getter @Setter
    private String sender;
}
