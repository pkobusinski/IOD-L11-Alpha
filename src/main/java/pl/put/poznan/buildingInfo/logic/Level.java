package pl.put.poznan.buildingInfo.logic;

import java.util.ArrayList;
import java.util.List;

public class Level extends Location {

    private List<Room> roomsOnLevel;

    public Level(int id, String name) {
        super(id, name);
        this.roomsOnLevel = new ArrayList<Room>();
    }

    @Override
    public void add(Location location) {
        if (location instanceof Room) {
            roomsOnLevel.add((Room) location);
        } else {
            throw new IllegalArgumentException("Provided location is not a Room. Only Rooms can be added to Level.");
        }
    }

    @Override
    public void remove(Location location) {
        if (location instanceof Room) {
            roomsOnLevel.remove((Room) location);
        } else {
            throw new IllegalArgumentException("Provided location is not a Room. Only Rooms can be removed from Level.");
        }
    }

    @Override
    public void display() {
        System.out.println("Level " + name + " with id " + id + ":");
        for (Room room : roomsOnLevel) {
            room.display();
        }
    }

    public double calculateAreaOnLevel() {
        double area = 0;
        for (Room room : roomsOnLevel) {
            area += room.getArea();
        }
        return area;
    }

    public double calculateCubeOnLevel() {
        double cube = 0;
        for (Room room : roomsOnLevel) {
            cube += room.getCube();
        }
        return cube;
    }

    public double calculateLightPowerOnLevel() {
        double lightPower = 0;
        for (Room room : roomsOnLevel) {
            lightPower += room.calculateLightPower();
        }
        return lightPower;
    }

    public double calculateEnergyConsumptionOnLevel() {
        double energyConsumption = 0;
        for (Room room : roomsOnLevel) {
            energyConsumption += room.calculateEnergyConsumption();
        }
        return energyConsumption;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Room> getRoomsOnLevel() {
        return roomsOnLevel;
    }

    public void setRoomsOnLevel(List<Room> roomsOnLevel) {
        this.roomsOnLevel = roomsOnLevel;
    }
}
