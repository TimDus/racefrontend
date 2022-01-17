package racegame.model;

import lombok.Getter;
import lombok.Setter;

public class Car
{
    Car()
    {
        xpos = 0;
        ypos = 0;
        user.setUsername("");
    }

    Car(User userN)
    {
        user = userN;
    }

    @Getter @Setter
    int xpos;

    @Getter @Setter
    int ypos;

    @Getter @Setter
    User user = new User();
}
