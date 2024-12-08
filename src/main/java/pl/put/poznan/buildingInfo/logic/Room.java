package pl.put.poznan.buildingInfo.logic;

public class Room extends Location {

    private double area;
    private double cube;
    private float heating;
    private double light;


    public Room(int id, String name, double area, double cube, float heating, double light) {
        super(id, name);
        this.area = area;
        this.cube = cube;
        this.heating = heating;
        this.light = light;
    }

    @Override
    public void add(Location location) {
        throw new UnsupportedOperationException("Cannot add location to Room.");
    }

    @Override
    public void remove(Location location) {
        throw new UnsupportedOperationException("Cannot remove location from Room.");
    }

    @Override
    public void display() {
        System.out.println("Room " + name + " with id " + id + ": area: " + area + ", cube: " + cube + ", heating: " + heating + ", light: " + light);
    }

    public double calculateLightPower () {
        return this.light / this.cube;
    }

    public double calculateEnergyConsumption () {
        return this.heating / this.cube;
    }

    public double getArea() {
        return area;
    }

    public double getCube() {
        return cube;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

}
