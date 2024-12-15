package pl.put.poznan.buildingInfo.logic.locations;

import pl.put.poznan.buildingInfo.logic.visitors.Visitor;

/**
 * Klasa ktora reprezentuje pokoj, najnizej w hierarchii lokalizacji
 *
 * Pokoj moze sie znajdowac na jedym z pieter.
 * Klasa pozwala na zarzadzenie parametrami pokoju, takimi jak
 * nazwa, powierzchnia, kubatura, koszt zuzycia energii, moc oswietlenia
 * poprzez ich dodawanie, ustawianie i usuwanie.
 */
public class Room extends Location {

    /**
     * Powierzchnia pokoju
     */
    private double area;
    /**
     * Kubatura pokoju.
     */
    private double cube;
    /**
     * Zuzycie energii na ogrzewanie w pokoju.
     */
    private double heating;
    /**
     * Moc oswietlenia w pokoju.
     */
    private double light;

    /**
     * Konstruktor klasy Room.
     * @param id
     * @param name
     * @param area
     * @param cube
     * @param heating
     * @param light
     */
    public Room(int id, String name, double area, double cube, double heating, double light) {
        super(id, name);
        this.area = area;
        this.cube = cube;
        this.heating = heating;
        this.light = light;
    }

    /**
     * Pokoj nie pozwala na podanie lokacji podrzednych.
     * @param location
     * @throws UnsupportedOperationException Pokoj nie posiada lokacji podrzednych.
     */
    @Override
    public void add(Location location) {
        throw new UnsupportedOperationException("Cannot add location to Room.");
    }

    /**
     * Pokoj nie posiada lokacji podrzednych.
     * @param location
     * @throws UnsupportedOperationException Pokoj nie posiada lokacji podrzednych.
     */
    @Override
    public void remove(Location location) {
        throw new UnsupportedOperationException("Cannot remove location from Room.");
    }

    /**
     * Zwraca powierzchnie pokoju.
     * @return
     */
    public double getArea() {
        return area;
    }
    /**
     * Zwraca kubature pomieszczenia
     * @return
     */
    public double getCube() {
        return cube;
    }

    public double getHeating() {
        return heating;
    }

    public void setHeating(double heating) {
        this.heating = heating;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    /**
     * @return id pokoju.
     */
    public int getId() {
        return this.id;
    }
    /**
     * @return nazwe pokoju.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Pozwala na ustawienie nazwy pokoju.
     * @param name
     */
    public void setName(String name) {this.name = name;}

    /**
     * Pozwala na ustawienie powierzchni pokoju.
     * @param area
     */
    public void setArea(double area) {this.area = area;}

    /**
     * Pozwala na ustawienie kubatury pokoju.
     * @param cube
     */
    public void setCube(double cube) {
        this.cube = cube;
    }

    @Override
    public double accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
