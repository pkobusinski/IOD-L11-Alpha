package pl.put.poznan.buildingInfo.logic;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasa reprezentujaca poziom (pietro) w hierarchii lokalizacji.
 *
 * Budynek ({@link Building}) moze skladac sie z wielu poziomow, a kazdy poziom
 * ({@link Level}) moze posiadac wiele pomieszczen ({@link Room}).
 * Klasa pozwala na zarzadzanie lokalizacjami podrzednymi poprzez dodawanie i usuwanie pomieszczen,
 * jak rowniez obliczanie sumarycznych parametrow takich jak powierzchnia, kubatura,
 * zuzycie energii i oswietlenia dla calego poziomu.
 */
public class Level extends Location {
    /**
     * Lista pokojow znajdujacych sie na danym poziomie.
     */
    private List<Room> roomsOnLevel;

    /**
     * Konstruktor klasy Level.
     *
     * @param id   unikalny identyfikator poziomu
     * @param name nazwa poziomu
     */
    public Level(int id, String name) {
        super(id, name);
        this.roomsOnLevel = new ArrayList<Room>();
    }

    /**
     * Dodaje pokoj do poziomu.
     *
     * @param location obiekt {@link Room}, ktory ma zostac dodany do poziomu
     * @throws IllegalArgumentException jesli dostarczona lokalizacja nie jest instancja klasy {@link Room}
     */
    @Override
    public void add(Location location) {
        if (location instanceof Room) {
            roomsOnLevel.add((Room) location);
        } else {
            throw new IllegalArgumentException("Provided location is not a Room. Only Rooms can be added to Level.");
        }
    }

    /**
     * Usuwa pokoj z poziomu.
     *
     * @param location obiekt {@link Room}, ktory ma zostac usuniety z poziomu
     * @throws IllegalArgumentException jesli dostarczona lokalizacja nie jest instancja klasy {@link Room}
     */
    @Override
    public void remove(Location location) {
        if (location instanceof Room) {
            roomsOnLevel.remove((Room) location);
        } else {
            throw new IllegalArgumentException("Provided location is not a Room. Only Rooms can be removed from Level.");
        }
    }

    /**
     * Wyswietla informacje o poziomie oraz wszystkich jego pomieszczeniach.
     */
    @Override
    public void display() {
        System.out.println("Level " + name + " with id " + id + ":");
        for (Room room : roomsOnLevel) {
            room.display();
        }
    }

    /**
     * Oblicza sumaryczna powierzchnie wszystkich pokojow na poziomie.
     *
     * @return laczna powierzchnia (w metrach kwadratowych)
     */
    public double calculateAreaOnLevel() {
        double area = 0;
        for (Room room : roomsOnLevel) {
            area += room.getArea();
        }
        return area;
    }

    /**
     * Oblicza sumaryczna kubature wszystkich pokojow na poziomie.
     *
     * @return laczna kubatura (w metrach szesciennych)
     */
    public double calculateCubeOnLevel() {
        double cube = 0;
        for (Room room : roomsOnLevel) {
            cube += room.getCube();
        }
        return cube;
    }
    /**
     * Oblicza sumaryczna moc oswietlenia dla wszystkich pokojow na poziomie.
     *
     * @return laczna moc oswietlenia
     */
    public double calculateLightPowerOnLevel() {
        double lightPower = 0;
        for (Room room : roomsOnLevel) {
            lightPower += room.calculateLightPower();
        }
        return lightPower;
    }

    /**
     * Oblicza sumaryczne zuzycie energii na ogrzewanie dla wszystkich pokojow na poziomie.
     *
     * @return laczne zuzycie energii
     */
    public double calculateEnergyConsumptionOnLevel() {
        double energyConsumption = 0;
        for (Room room : roomsOnLevel) {
            energyConsumption += room.calculateEnergyConsumption();
        }
        return energyConsumption;
    }

    /**
     * Zwraca identyfikator poziomu.
     *
     * @return identyfikator poziomu
     */
    public int getId() {
        return this.id;
    }

    /**
     * Zwraca nazwe poziomu.
     *
     * @return nazwa poziomu
     */
    public String getName() {
        return this.name;
    }

    /**
     * Zwraca liste pokojow znajdujacych sie na poziomie.
     *
     * @return lista pokojow na poziomie
     */
    public List<Room> getRoomsOnLevel() {
        return roomsOnLevel;
    }

    /**
     * Ustawia nowa liste pokojow na poziomie.
     *
     * @param roomsOnLevel lista pokojow, ktora ma zostac przypisana do poziomu
     */
    public void setRoomsOnLevel(List<Room> roomsOnLevel) {
        this.roomsOnLevel = roomsOnLevel;
    }
}
