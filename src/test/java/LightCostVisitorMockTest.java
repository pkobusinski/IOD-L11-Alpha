import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;
import pl.put.poznan.buildingInfo.logic.visitors.LightCostVisitor;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LightCostVisitorMockTest {

    private LightCostVisitor visitor;
    private List<Room> rooms = new ArrayList<>();
    private List<Level> levels = new ArrayList<>();

    private Room mockRoom = mock(Room.class);
    private Level mockLevelWithoutRooms = mock(Level.class);
    private Level mockLevel1 = mock(Level.class);
    private Level mockLevel2 = mock(Level.class);
    private Building mockEmptyBuilding = mock(Building.class);
    private Building mockBuilding1 = mock(Building.class);

    @BeforeEach
    void setUp() {
        visitor = new LightCostVisitor(2.0); // Cost per unit of light = 2.0

        when(mockRoom.getLight()).thenReturn(100.0);

        for (int i = 0; i < 5; i++) {
            rooms.add(mockRoom);
        }

        when(mockLevel1.getRoomsOnLevel()).thenReturn(rooms);
        when(mockLevel2.getRoomsOnLevel()).thenReturn(rooms);
        levels.add(mockLevel1);
        levels.add(mockLevel2);

        when(mockBuilding1.getLevelsInBuilding()).thenReturn(levels);
    }

    @Test
    void testCalculateLightCostOfRoom() {
        assertEquals(200.0, visitor.visit(mockRoom)); // 100.0 light * 2.0 cost
    }

    @Test
    void testCalculateLightCostOfLevelWithoutRooms() {
        when(mockLevelWithoutRooms.getRoomsOnLevel()).thenReturn(new ArrayList<>());
        assertEquals(0, visitor.visit(mockLevelWithoutRooms));
    }

    @Test
    void testCalculateLightCostOfLevelWithRooms() {
        assertEquals(1000.0, visitor.visit(mockLevel1)); // 5 rooms * 100.0 light * 2.0 cost
    }

    @Test
    void testCalculateLightCostOfBuilding() {
        assertEquals(2000.0, visitor.visit(mockBuilding1)); // 2 levels * (5 rooms * 100.0 light * 2.0 cost)
    }

    @Test
    void testCalculateLightCostOfEmptyBuilding() {
        when(mockEmptyBuilding.getLevelsInBuilding()).thenReturn(new ArrayList<>());
        assertEquals(0, visitor.visit(mockEmptyBuilding));
    }
}