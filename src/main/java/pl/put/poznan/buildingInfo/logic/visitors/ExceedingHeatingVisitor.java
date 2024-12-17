package pl.put.poznan.buildingInfo.logic.visitors;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa wyszukujaca pomieszczenia, ktore przekraczaja zadany limit zuzycia energii.
 * 
 * Implementacja wzorca odwiedzajacego (Visitor), dodatkowo korzysta z klasy {@link EnergyVisitor}
 * do obliczenia zuzycia energii dla pomieszczen.
 * 
 */
public class ExceedingHeatingVisitor implements Visitor {

    private double energyLimit;
    private List<Room> roomsExceedingHeating = new ArrayList<>();
    private EnergyVisitor energyVisitor = new EnergyVisitor();

    /**
     * Tworzy instancje odwiedzajacego z okreslonym limitem zuzycia energii.
     *
     * @param energyLimit limit zuzycia energii
     */
    public ExceedingHeatingVisitor(double energyLimit) {
        this.energyLimit = energyLimit;
    }

    @Override
    public double visit(Room room) {
        double energy = energyVisitor.visit(room);
        if (energy > energyLimit) {
            roomsExceedingHeating.add(room);
        }
        return energy;
    }

    @Override
    public double visit(Level level) {
        double total = 0.0;
        for (Room room : level.getRoomsOnLevel()) {
            total += room.accept(this);
        }
        return total;
    }

    @Override
    public double visit(Building building) {
        double total = 0.0;
        for (Level level : building.getLevelsInBuilding()) {
            total += level.accept(this);
        }
        return total;
    }

    /**
     * Zwraca liste pomieszczen przekraczajacych limit zuzycia energii.
     *
     * @return lista pomieszczen przekraczajacych limit
     */
    public List<Room> getRoomsExceedingLimit() {
        return roomsExceedingHeating;
    }
}
