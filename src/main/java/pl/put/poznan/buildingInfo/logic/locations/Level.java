package pl.put.poznan.buildingInfo.logic.locations;

import java.util.ArrayList;
import java.util.List;

import pl.put.poznan.buildingInfo.logic.visitors.Visitor;
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
     * Setter nazwy poziomu.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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

    @Override
    public double accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
