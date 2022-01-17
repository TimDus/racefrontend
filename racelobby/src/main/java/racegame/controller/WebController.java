package racegame.controller;

import racegame.model.Track;
import racegame.model.User;
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

    @MessageMapping("/race.joinLobby")
    @SendTo("/race.newUser")
    public UserMessage newUser(@Payload final UserMessage userMessage, SimpMessageHeaderAccessor headerAccessor)
    {
        headerAccessor.getSessionAttributes().put("username", userMessage.getSender());
        track.Newuser(userMessage.getSender());
        userMessage.setContent(track.getUsers());
        System.out.println("direction: " + userMessage.getContent()[0]);
        return userMessage;
    }

    @MessageMapping("/race.userAdded")
    @SendTo("/topic/temp")
    public UserMessage sendMove(@Payload final UserMessage userMessage)
    {
        track.Newuser(userMessage.getSender());
        userMessage.setContent(track.getUsers());
        System.out.println("direction: " + userMessage.getContent()[0]);
        return userMessage;
    }
}
