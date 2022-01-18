package racegame.controller;

import racegame.model.Lobby;
import racegame.socketModel.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class LobbyController {
    List<Lobby> lobbies = new ArrayList<Lobby>();
    int idPick = 1;

    @MessageMapping("/lobby.createLobby")
    @SendTo("/topic/lobby")
    public CreateMessage NewLobby(@Payload final CreateMessage createMessage, SimpMessageHeaderAccessor headerAccessor)
    {
        headerAccessor.getSessionAttributes().put("username", createMessage.getSender());
        Lobby lobby = new Lobby();
        lobby.CreateLobby(createMessage.getSender(), idPick);
        createMessage.setType((MessageType.CREATE));
        createMessage.setContent(String.valueOf(idPick));
        lobbies.add(lobby);
        idPick++;
        return createMessage;
    }

    @MessageMapping("/lobby.refreshLobby")
    @SendTo("/topic/lobby")
    public LobbyMessage RefreshLobby(@Payload final LobbyMessage lobbyMessage)
    {
        Lobby[] lobbyIds = new Lobby[lobbies.size()];
        for (int i = 0; i < lobbies.size(); i++)
        {
            lobbyIds[i] = lobbies.get(i);
        }
        lobbyMessage.setType((MessageType.REFRESH));
        lobbyMessage.setContent(lobbyIds);
        return lobbyMessage;
    }

    @MessageMapping("/lobby.joinLobby")
    @SendTo("/topic/lobby")
    public CreateMessage JoinLobby(@Payload final CreateMessage createMessage)
    {
        Lobby lobby = lobbies.get(Integer.parseInt(createMessage.getContent())-1);
        lobby.JoinLobby(createMessage.getSender());
        createMessage.setType(MessageType.JOIN);
        return createMessage;
    }

    @MessageMapping("/lobby.startLobby")
    @SendTo("/topic/lobby")
    public CreateMessage StartLobby(@Payload final CreateMessage createMessage)
    {
        int number = Integer.parseInt(createMessage.getContent());
        Lobby lobby = lobbies.get(Integer.parseInt(createMessage.getContent())-1);
        lobby.JoinLobby(createMessage.getSender());
        createMessage.setType(MessageType.START);
        return createMessage;
    }
}
