package racegame.controller;

import racegame.model.Track;
import racegame.socketModel.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class WebController {
    Track track = new Track();

    @MessageMapping("/race.newUser")
    @SendTo("/topic/race")
    public UserMessage newUser(@Payload final StartMessage startMessage, SimpMessageHeaderAccessor headerAccessor)
    {
        UserMessage userMessage = new UserMessage();
        headerAccessor.getSessionAttributes().put("username", startMessage.getSender());
        track.Newuser(startMessage.getSender(), startMessage.getContent());
        userMessage.setContent(track.getUsers());
        userMessage.setType(MessageType.CONNECT);
        userMessage.setSender(userMessage.getSender());
        return userMessage;
    }

    @MessageMapping("/race.move")
    @SendTo("/topic/race")
    public TrackMessage sendMove(@Payload final MoveMessage moveMessage)
    {
        System.out.println("direction: " + moveMessage.getContent());
        track.CarMove(moveMessage.getContent(), moveMessage.getSender());
        TrackMessage trackMessage = new TrackMessage();
        trackMessage.setContent(track.getUsers());
        trackMessage.setSender(moveMessage.getSender());
        trackMessage.setType((MessageType.TRACK));
        trackMessage.setGameId(track.getId());
        return trackMessage;
    }
}
