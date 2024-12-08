package pl.put.poznan.buildingInfo.logic;

import java.util.ArrayList;
import java.util.List;

public class Building extends Location {

    public List<Level> levelsInBuilding;

    public Building(int id, String name) {
        super(id, name);
        this.levelsInBuilding = new ArrayList<Level>();
    }

    @Override
    public void add(Location location) {
        if (location instanceof Level) {
            levelsInBuilding.add((Level) location);
        } else {
            throw new IllegalArgumentException("Provided location is not a Level. Only Levels can be added to Building.");
        }
    }

    @Override
    public void remove(Location location) {
        if (location instanceof Level) {
            levelsInBuilding.remove((Level) location);
        } else {
            throw new IllegalArgumentException("Provided location is not a Level. Only Levels can be removed from Building.");
        }
    }

    @Override
    public void display() {
        System.out.println("Building " + name + " with id " + id + ":");
        for (Level level : levelsInBuilding) {
            level.display();
        }
    }


    public double calculateAreaOfBuilding() {
        double area = 0;
        for (Level level : levelsInBuilding) {
            area += level.calculateAreaOnLevel();
        }
        return area;
    }

    public double calculateCubeOfBuilding() {
        double cube = 0;
        for (Level level : levelsInBuilding) {
            cube += level.calculateCubeOnLevel();
        }
        return cube;
    }

    public double calculateLightPowerOfBuilding() {
        double lightPower = 0;
        for (Level level : levelsInBuilding) {
            lightPower += level.calculateLightPowerOnLevel();
        }
        return lightPower;
    }

    public double calculateEnergyConsumptionOfBuilding() {
        double energyConsumption = 0;
        for (Level level : levelsInBuilding) {
            energyConsumption += level.calculateEnergyConsumptionOnLevel();
        }
        return energyConsumption;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Level> getLevelsInBuilding() {
        System.out.println("Levels in building: " + levelsInBuilding);
        return levelsInBuilding;
    }

    public void setLevelsInBuilding(List<Level> levelsInBuilding) {
        this.levelsInBuilding = levelsInBuilding;
    }
}
