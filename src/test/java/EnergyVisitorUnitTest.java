import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;
import pl.put.poznan.buildingInfo.logic.visitors.EnergyVisitor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnergyVisitorUnitTest {

    private EnergyVisitor visitor;
    private List<Room> rooms;

    private Level levelWithoutRooms;
    private Level level1;
    private Level level2;
    private Building emptyBuilding;
    private Building building1;

    @BeforeEach
    void setUp() {
        visitor = new EnergyVisitor();
        rooms = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            rooms.add(new Room(i+1, "Room " + String.valueOf(i+1), 10.0, 10.0, 100.0, 10000.0));
        }

        levelWithoutRooms = new Level();
        level1 = new Level();
        level2 = new Level();

        for (Room room : rooms) {
            level1.add(room);
            level2.add(room);
        }

        emptyBuilding = new Building();
        building1 = new Building();
        building1.add(level1);
        building1.add(level2);
    }

    @Test
    void testCalculateEnergyConsumptionOfRoom() {
        assertEquals(10.0, visitor.visit(rooms.get(0)));
    }

    @Test
    void testCalculateEnergyConsumptionOfLevelWithoutRooms() {
        assertEquals(0.0, visitor.visit(levelWithoutRooms));
    }

    @Test
    void testCalculateEnergyConsumptionOfLevelWithRooms() {
        assertEquals(50.0, visitor.visit(level1));
    }

    @Test
    void testCalculateEnergyConsumptionOfBuilding() {
        assertEquals(100.0, visitor.visit(building1));
    }

    @Test
    void testCalculateEnergyConsumptionOfEmptyBuilding() {
        assertEquals(0.0, visitor.visit(emptyBuilding));
    }
}