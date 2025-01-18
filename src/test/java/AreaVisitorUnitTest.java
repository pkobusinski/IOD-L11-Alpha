import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;
import pl.put.poznan.buildingInfo.logic.visitors.AreaVisitor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AreaVisitorUnitTest {

    private AreaVisitor visitor;
    private List<Room> rooms;
    private List<Room> rooms2;

    private Level level;
    private Level level2;
    private Level levelWithoutRooms;

    private Building building;
    private Building buildingWithoutLevels;

    @BeforeEach
    void setUp() {
        visitor = new AreaVisitor();
        rooms = new ArrayList<Room>();
        rooms2 = new ArrayList<Room>();

        for (int i = 0; i < 5; i++) {
            rooms.add(new Room(i, "Room " + String.valueOf(i), 100, 100, 100, 100));
            rooms2.add(new Room(i, "Room " + String.valueOf(i), 100, 100, 100, 100));
        }

        level = new Level(1, "Level 1");
        for (Room room : rooms)
            level.add(room);
        level2 = new Level(2, "Level 2");
        for (Room room : rooms2)
            level2.add(room);
        levelWithoutRooms = new Level(2, "Level 2");

        building = new Building(1, "Building 1");
        building.add(level);
        building.add(level2);

        buildingWithoutLevels = new Building(2, "Building 2");
    }

    @Test
    void testCountAreaOfLevelWithoutRooms() {
        assertEquals(0, visitor.visit(levelWithoutRooms));
    }

    @Test
    void testCountAreaOfLevelWithRooms() {
        assertEquals(500, visitor.visit(level));
    }

    @Test
    void testCountAreaOfRoom() {
        assertEquals(100, visitor.visit(rooms.get(0)));
    }

    @Test
    void testCountAreaOfBuilding() {
        assertEquals(1000, visitor.visit(building));
    }

    @Test
    void testCountAreaOfBuildingWithoutLevels() {
        assertEquals(0, visitor.visit(buildingWithoutLevels));
    }

}
