import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;
import pl.put.poznan.buildingInfo.logic.visitors.CubeVisitor;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CubeVisitorUnitTest {

    private CubeVisitor cubeVisitor;
    private Room room1;
    private Room room2;
    private Room room3;
    private Level level1;
    private Level level2;
    private Building building;

    @BeforeEach
    void setUp() {
        cubeVisitor = new CubeVisitor();

        room1 = new Room(1, "Room1", 100, 100, 100, 100);
        room2 = new Room(2, "Room2", 100, 100, 100, 100);
        room3 = new Room(3, "Room3", 100, 100, 100, 100);

        level1 = new Level();
        level1.add(room1);

        level2 = new Level();
        level2.add(room2);
        level2.add(room3);

        building = new Building();
        building.add(level1);
        building.add(level2);
    }

    @Test
    void testCountCubeOfLevelWithOneRoom() {
        assertEquals(100.0, cubeVisitor.visit(level1));
    }

    @Test
    void testCountCubeOfLevelWithMultipleRooms() {
        assertEquals(200.0, cubeVisitor.visit(level2));
    }

    @Test
    void testCountCubeOfRoom() {
        assertEquals(100.0, cubeVisitor.visit(room1));
    }
    @Test
    void testCountCubeOfBuildingWithOneLevel() {
        Building singleLevelBuilding = new Building();
        singleLevelBuilding.add(level1);
        assertEquals(100.0, cubeVisitor.visit(singleLevelBuilding));
    }
    @Test
    void testCountCubeOfEmptyBuilding() {
        assertEquals(300.0, cubeVisitor.visit(building));
    }
}