import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;
import pl.put.poznan.buildingInfo.logic.visitors.LightVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LightVisitorUnitTest {

    private LightVisitor visitor = new LightVisitor();
    private List<Room> rooms= new ArrayList<Room>();;
    private List<Level> levels = new ArrayList<Level>();;

    private Room mockRoom = mock(Room.class);
    private Level mockLevelWithoutRooms = mock(Level.class);
    private Level mockLevel1 = mock(Level.class);
    private Level mockLevel2 = mock(Level.class);
    private Building mockEmptyBuilding = mock(Building.class);
    private Building mockBuilding1 = mock(Building.class);

    @BeforeEach
    void setUp() {
        when(mockRoom.getArea()).thenReturn(100.0);
        when(mockRoom.getCube()).thenReturn(100.0);
        when(mockRoom.getLight()).thenReturn(10000.0);
        when(mockRoom.getHeating()).thenReturn(100.0);

        for(int i = 0; i<5; i++)
            rooms.add(mockRoom);

        when(mockLevel1.getRoomsOnLevel()).thenReturn(rooms);
        when(mockLevel2.getRoomsOnLevel()).thenReturn(rooms);
        levels.add(mockLevel1);
        levels.add(mockLevel2);

        when(mockBuilding1.getLevelsInBuilding()).thenReturn(levels);
    }

    @Test
    void testCalculateLightPowerOfRoom() {
        assertEquals(100.0, visitor.visit(mockRoom));
    }

    @Test
    void testCalculateLightPowerOfLevelWithoutRooms() {
        assertEquals(0, visitor.visit(mockLevelWithoutRooms));
    }

    @Test
    void testCalculateLightPowerOfLevelWithRooms() {
        assertEquals(500.0, visitor.visit(mockLevel1));
    }
    @Test
    void testCalculateLightPowerOfBuilding() {
        assertEquals(1000, visitor.visit(mockBuilding1));
    }
    @Test
    void testCalculateLightPowerOfEmptyBuilding() {
        assertEquals(0, visitor.visit(mockEmptyBuilding));
    }
}