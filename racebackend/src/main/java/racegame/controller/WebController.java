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
    @SendTo("/race.userAdded")
    public UserMessage newUser(@Payload final UserMessage userMessage, SimpMessageHeaderAccessor headerAccessor)
    {
        headerAccessor.getSessionAttributes().put("username", userMessage.getSender());
        track.Newuser(userMessage.getSender());
        userMessage.setContent(track.getUsers());
        System.out.println("direction: " + userMessage.getContent()[0]);
        return userMessage;
    }

    @MessageMapping("/race.move")
    @SendTo("/topic/public")
    public TrackMessage sendMove(@Payload final MoveMessage moveMessage)
    {
        System.out.println("direction: " + moveMessage.getContent());
        track.CarMove(moveMessage.getContent(), moveMessage.getSender());
        TrackMessage trackMessage = new TrackMessage();
        trackMessage.setContent(track.getUsers());
        trackMessage.setSender(moveMessage.getSender());
        trackMessage.setType((MessageType.TRACK));
        return trackMessage;
    }
}
