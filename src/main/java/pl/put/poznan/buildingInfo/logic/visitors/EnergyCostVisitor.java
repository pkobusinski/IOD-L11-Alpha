package pl.put.poznan.buildingInfo.logic.visitors;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;

/**
 * Klasa obliczająca koszt energii dla pomieszczeń, poziomów i budynków.
 * 
 * Implementacja wzorca odwiedzającego (Visitor), pozwalająca na rekurencyjne
 * obliczanie kosztu energii dla całej struktury budynku.
 */
public class EnergyCostVisitor implements Visitor {
    private double energyCost;
    private EnergyVisitor energyVisitor = new EnergyVisitor();

    public EnergyCostVisitor(double energyCost) {
        this.energyCost = energyCost;
    }

    @Override
    public double visit(Room room) {
        return energyVisitor.visit(room) * energyCost;
    }

    @Override
    public double visit(Level level) {
        return energyVisitor.visit(level) * energyCost;
    }

    @Override
    public double visit(Building building) {
        return energyVisitor.visit(building) * energyCost;
    }
}
