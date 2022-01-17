package racegame.model;

import lombok.Getter;
import lombok.Setter;

public class Track
{
    private int distance = 2;
    private int borderMinX = 0;
    private int borderMaxX = 750;
    private int borderMinY = 0;
    private int borderMaxY = 750;
    @Getter @Setter
    Car[] users = new Car[2];

    public Track()
    {
        users[0] = new Car();
        users[1] = new Car();
    }

    public void Newuser(String userName)
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

    public void CarMove(String arrowKey, String userName)
    {
        if(users[0].getUser().getUsername().equals(userName))
        {
            MoveDirection(Arrowkey.valueOf(arrowKey), users[0]);
        }
        else
        {
            MoveDirection(Arrowkey.valueOf(arrowKey), users[1]);
        }
    }

    private void MoveDirection(Arrowkey arrowKey, Car user)
    {
        if(user.getUser().getUsername().equals(users[0].getUser().getUsername()))
        {
            borderMinY = 0;
            borderMaxY = 750;
        }
        else if(user.getUser().getUsername().equals(users[1].getUser().getUsername()))
        {
            borderMinY = -46;
            borderMaxY = 704;
        }
        switch(arrowKey)
        {
            case LEFT:
                user.setXpos(user.getXpos()-distance);
                if(user.getXpos() < borderMinX)
                    user.setXpos(borderMinX);
                break;
            case RIGHT:
                user.setXpos(user.getXpos()+distance);
                if(user.getXpos() > borderMaxX)
                    user.setXpos(borderMaxX);
                break;
            case UP:
                user.setYpos(user.getYpos()-distance);
                if(user.getYpos() < borderMinY)
                    user.setYpos(borderMinY);
                break;
            case DOWN:
                user.setYpos(user.getYpos()+distance);
                if(user.getYpos() > borderMaxY)
                    user.setYpos(borderMaxY);
                break;
        }
    }
}
