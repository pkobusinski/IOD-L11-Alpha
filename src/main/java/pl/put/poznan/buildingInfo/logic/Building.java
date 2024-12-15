package pl.put.poznan.buildingInfo.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa ktora reprezentuje budynek, najwyzej w hierarchii lokalizacji
 *
 * Budynek moze sie skladac z wielu pieter, ktore sa reprezentacjami
 * klasy Level.
 * Klasa pozwala na zarzadzenie lokalizacjami podrzednimi poprzez
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
     * Funkaja pozwala usuwac poziomy klasy Level z budynku.
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
     * Funkcja wyswietla informacje o budynku i rekurencyjnie
     * o wszystkich lokacjach podrzednych.
     */
    @Override
    public void display() {
        System.out.println("Building " + name + " with id " + id + ":");
        for (Level level : levelsInBuilding) {
            level.display();
        }
    }

    /**
     * Podlicza laczna powierzchnie budynku.
     * @return double
     */
    public double calculateAreaOfBuilding() {
        double area = 0;
        for (Level level : levelsInBuilding) {
            area += level.calculateAreaOnLevel();
        }
        return area;
    }

    /**
     * Podlicza laczna kubature budynku.
     * @return double
     */
    public double calculateCubeOfBuilding() {
        double cube = 0;
        for (Level level : levelsInBuilding) {
            cube += level.calculateCubeOnLevel();
        }
        return cube;
    }

    /**
     * Podlicza laczna moc oswietlenia budynku.
     * @return double
     */
    public double calculateLightPowerOfBuilding() {
        double lightPower = 0;
        for (Level level : levelsInBuilding) {
            lightPower += level.calculateLightPowerOnLevel();
        }
        return lightPower;
    }

    /**
     * Podlicza laczne zurzycie energii na ogrzewanie w budynku.
     * @return double
     */
    public double calculateEnergyConsumptionOfBuilding() {
        double energyConsumption = 0;
        for (Level level : levelsInBuilding) {
            energyConsumption += level.calculateEnergyConsumptionOnLevel();
        }
        return energyConsumption;
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
}
