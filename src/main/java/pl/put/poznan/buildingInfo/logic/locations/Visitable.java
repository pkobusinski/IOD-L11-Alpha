package pl.put.poznan.buildingInfo.logic.locations;

import pl.put.poznan.buildingInfo.logic.visitors.Visitor;

public interface Visitable {
    /**
     * Funkcja pozwala na zaakceptowanie wizytatora odwiedzajacego klas
     * podlokacji w budynku.
     * Konkretna implementacja interface'u jest dostepna w klasach go uzywajacego.
     * @param visitor obiekt wizytatora
     * @return liczbe double
     */
    double accept(Visitor visitor);
}
