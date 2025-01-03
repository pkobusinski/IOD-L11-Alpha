import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;

import java.util.ArrayList;
import java.util.List;

public class LevelUnitTest {

    private List<Room> rooms = new ArrayList<Room>();

    private Level level;
    private Level levelWithoutRooms;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 5; i++)
            rooms.add(new Room(i, "Room " + String.valueOf(i), 100, 100, 100, 100));
        level = new Level(1, "Level 1");
        for (Room room : rooms) {
            level.add(room);
        }
        levelWithoutRooms = new Level(2, "Level 2");
    }

    @Test
    void testCountAreaOfLevelWithoutRooms() {
        assertEquals(0, )
    }


}
