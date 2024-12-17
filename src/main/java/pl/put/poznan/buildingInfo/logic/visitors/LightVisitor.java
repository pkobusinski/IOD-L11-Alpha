package pl.put.poznan.buildingInfo.logic.visitors;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;

/**
 * Klasa obliczajaca stosunek mocy oswietlenia do powierzchni pomieszczen, poziomow i budynkow.
 * 
 * Implementacja wzorca odwiedzajacego (Visitor), ktora pozwala na rekurencyjne 
 * sumowanie wspolczynnika oswietlenia dla calej struktury budynku.
 * 
 */
public class LightVisitor implements Visitor {
    @Override
    public double visit(Room room) {
        return room.getLight() / room.getArea();
    }

    @Override
    public double visit(Level level) {
        double lightPower = 0;
        for(Room room : level.getRoomsOnLevel()) {
            lightPower += visit(room);
        }
        return lightPower;
    }

    @Override
    public double visit(Building building) {
        double lightPower = 0;
        for(Level level : building.getLevelsInBuilding()) {
            lightPower += visit(level);
        }
        return lightPower;
    }
}
