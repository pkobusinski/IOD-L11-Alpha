package pl.put.poznan.buildingInfo.logic;

public abstract class Location {

    protected int id;
    protected String name;

    public Location(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract void add(Location location);

    public abstract void remove(Location location);

    public abstract void display();


}
