package racegame.model;

import lombok.Getter;
import lombok.Setter;

public class Lobby
{
    @Getter @Setter
    int id;
    @Getter @Setter
    Car[] users = new Car[2];

    public Lobby()
    {
        users[0] = new Car();
        users[1] = new Car();
    }

    public void CreateLobby(String userName, int Id)
    {
        id = Id;

        if(users[0].getUser().getUsername().equals(""))
        {
            users[0].getUser().setUsername(userName);
        }
        else
        {
            users[1].getUser().setUsername(userName);
        }
    }

    public void JoinLobby(String userName)
    {
        if(users[0].getUser().getUsername().equals(""))
        {
            users[0].getUser().setUsername(userName);
        }
        else
        {
            users[1].getUser().setUsername(userName);
        }
    }

}
