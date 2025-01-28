package pl.put.poznan.buildingInfo.logic.visitors;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;

/**
 * Klasa obliczająca koszt oświetlenia dla pomieszczeń, poziomów i budynków.
 * 
 * Implementacja wzorca odwiedzającego (Visitor), pozwalająca na rekurencyjne
 * obliczanie kosztu oświetlenia dla całej struktury budynku.
 */
public class LightCostVisitor implements Visitor {

    private double lightCost;
    private LightVisitor lightVisitor = new LightVisitor();

    public LightCostVisitor(double lightCost) {
        this.lightCost = lightCost;
    }

    @Override
    public double visit(Room room) {
        return lightVisitor.visit(room) * lightCost;
    }

    @Override
    public double visit(Level level) {
        return lightVisitor.visit(level) * lightCost;
    }

    @Override
    public double visit(Building building) {
        return lightVisitor.visit(building) * lightCost;
    }
}
