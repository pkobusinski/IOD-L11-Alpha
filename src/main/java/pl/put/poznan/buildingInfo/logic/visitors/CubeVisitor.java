package pl.put.poznan.buildingInfo.logic.visitors;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;

public class CubeVisitor implements Visitor {

    @Override
    public double visit(Room room) {
        return room.getCube();
    }

    @Override
    public double visit(Level level) {
        double cube = 0;
        for(Room room : level.getRoomsOnLevel()) {
            cube += visit(room);
        }
        return cube;
    }

    @Override
    public double visit(Building building) {
        double cube = 0;
        for (Level level : building.getLevelsInBuilding()) {
            cube += visit(level);
        }
        return cube;
    }
}
