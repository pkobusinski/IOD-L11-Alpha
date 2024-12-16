package pl.put.poznan.buildingInfo.logic.locations;

import pl.put.poznan.buildingInfo.logic.visitors.Visitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa, która reprezentuje budynek, najwyżej w hierarchii lokalizacji
 *
 * Budynek moze sie skladac z wielu pieter, ktore sa reprezentacjami
 * klasy Level.
 * Klasa pozwala na zarzadzenie lokalizacjami podrzednymi poprzez
 * dodawanie i usuwanie.
 * Klasa pozwala na obliczanie powierzchni, kubatury, zuzycia energii i ciepla
 * poprzez zsumowanie tych jednostek z obiektow podrzednych.
 */
public class Building extends Location {

    public List<Level> levelsInBuilding;

    /**
     * Konstruktor klasy Building
     * @param id
     * @param name
     */
    public Building(int id, String name) {
        super(id, name);
        this.levelsInBuilding = new ArrayList<Level>();
    }

    public Building() {
        super(0, "");
        this.levelsInBuilding = new ArrayList<>();
    }

    /**
     * Funkcja pozwala dodawac poziomy klasy Level do budynku.
     * @param location
     */
    @Override
    public void add(Location location) {
        if (location instanceof Level) {
            levelsInBuilding.add((Level) location);
        } else {
            throw new IllegalArgumentException("Provided location is not a Level. Only Levels can be added to Building.");
        }
    }

    /**
     * Funkcja pozwala usuwac poziomy klasy Level z budynku.
     * @param location
     */
    @Override
    public void remove(Location location) {
        if (location instanceof Level) {
            levelsInBuilding.remove((Level) location);
        } else {
            throw new IllegalArgumentException("Provided location is not a Level. Only Levels can be removed from Building.");
        }
    }

    /**
     * Getter identyfikatora.
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * Getter nazwy budynku.
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter nazwy budynku.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Funkcja zwraca liste poziomow w budynku.
     * @return lista obiektow klasy Level, ktore sa podlokacjami budynku.
     */
    public List<Level> getLevelsInBuilding() {
        System.out.println("Levels in building: " + levelsInBuilding);
        return levelsInBuilding;
    }

    /**
     * Funkcja pozwala na natychmiastowe zdefiniowanie calej listy
     * podlokacji w budynku.
     * @param levelsInBuilding
     */
    public void setLevelsInBuilding(List<Level> levelsInBuilding) {
        this.levelsInBuilding = levelsInBuilding;
    }


    @Override
    public double accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
