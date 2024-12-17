package pl.put.poznan.buildingInfo.logic.visitors;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;

/**
 * Klasa obliczajaca laczne zuzycie energii dla pomieszczen, poziomow i
 * budynkow.
 * 
 * Zuzycie energii jest wyliczane jako stosunek ogrzewania do kubatury
 * pomieszczen.
 * Implementacja wzorca odwiedzajacego (Visitor).
 * 
 */
public class EnergyVisitor implements Visitor {
    @Override
    public double visit(Room room) {
        return room.getHeating() / room.getCube();
    }

    @Override
    public double visit(Level level) {
        double energy = 0;
        for (Room room : level.getRoomsOnLevel()) {
            energy += visit(room);
        }
        return energy;
    }

    @Override
    public double visit(Building building) {
        double energy = 0;
        for (Level level : building.getLevelsInBuilding()) {
            energy += visit(level);
        }
        return energy;
    }
}
