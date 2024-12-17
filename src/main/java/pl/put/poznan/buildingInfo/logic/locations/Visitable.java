package pl.put.poznan.buildingInfo.logic.locations;

import pl.put.poznan.buildingInfo.logic.visitors.Visitor;

public interface Visitable {
    /**
     * Funkcja pozwala na zaakceptowanie wizytatora odwiedzającego klas
     * podlokacji w budynku.
     * Konkretna implementacja interface'u jest dostępna w klasach go używającego.
     * @param visitor objekt wizytatora
     */
    double accept(Visitor visitor);
}
