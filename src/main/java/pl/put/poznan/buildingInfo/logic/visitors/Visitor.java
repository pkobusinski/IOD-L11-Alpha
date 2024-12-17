package pl.put.poznan.buildingInfo.logic.visitors;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;

/**
 * Interfejs definiujacy wzorzec odwiedzajacy (Visitor) dla hierarchii obiektow budynku.
 * 
 * Umozliwia implementacje operacji dla roznych poziomow struktury: pomieszczen (Room), 
 * poziomow (Level) oraz budynkow (Building).
 * 
 */
public interface Visitor {
    /**
     * Odwiedza pomieszczenie i wykonuje na nim okreslona operacje.
     *
     * @param room pomieszczenie do odwiedzenia
     * @return wynik operacji dla pomieszczenia
     */
    double visit(Room room);

    /**
     * Odwiedza poziom i wykonuje na nim okreslona operacje.
     *
     * @param level poziom do odwiedzenia
     * @return wynik operacji dla poziomu
     */
    double visit(Level level);

    /**
     * Odwiedza budynek i wykonuje na nim okreslona operacje.
     *
     * @param building budynek do odwiedzenia
     * @return wynik operacji dla budynku
     */
    double visit(Building building);
}
