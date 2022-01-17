package racegame.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import racegame.model.Track;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrackTest
{
    private Track track;

    @BeforeEach
    void setUp()
    {
        track = new Track();
        track.Newuser("user1");
        track.Newuser("user2");
    }

    @Test
    void testCheckLeft()
    {
        track.CarMove("LEFT", "user1");

        assertEquals(0, track.getUsers()[0].getXpos());
    }

    @Test
    void testCheckRight()
    {
        track.CarMove("RIGHT", "user1");

        assertEquals(2, track.getUsers()[0].getXpos());
    }

    @Test
    void testCheckUp()
    {
        track.CarMove("UP", "user1");

        assertEquals(0, track.getUsers()[0].getYpos());
    }

    @Test
    void testCheckDown()
    {
        track.CarMove("DOWN", "user1");

        assertEquals(2, track.getUsers()[0].getYpos());
    }
}
