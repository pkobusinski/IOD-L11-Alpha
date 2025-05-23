package pl.put.poznan.buildingInfo.logic.visitors;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;

/**
 * Klasa obliczajaca laczna powierzchnie dla pomieszczen, poziomow i budynkow.
 * 
 * Implementacja wzorca odwiedzajacego (Visitor), pozwalajaca na rekurencyjne 
 * sumowanie powierzchni dla calej struktury budynku.
 * 
 */
public class AreaVisitor implements Visitor {

    @Override
    public double visit(Room room) {
        return room.getArea();
    }

    @Override
    public double visit(Level level) {
        double area = 0;
        for (Room room : level.getRoomsOnLevel()) {
            area += visit(room);
        }
        return area;
    }

    @Override
    public double visit(Building building) {
        double area = 0;
        for (Level level : building.getLevelsInBuilding()) {
            area += visit(level);
        }
        return area;
    }
}

