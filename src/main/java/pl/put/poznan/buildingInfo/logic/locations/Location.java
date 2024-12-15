package pl.put.poznan.buildingInfo.logic.locations;

/**
 * Klasa abstrakcyjna, która reprezentuje lokalizacje, ktora moze byc budynkiem,
 * poziomem lub pokojem.
 *
 * Sluzy jako baza do implementacji roznych typow lokalizacji.
 */
public abstract class Location implements Visitable {

    /**
     * Identyfikator lokalizacji.
     */
    protected int id;

    /**
     * Nazwa lokalizacji.
     */
    protected String name;

    /**
     * Konstruktor klasy Location.
     * @param id
     * @param name
     */
    public Location(int id, String name) {
        this.id = id;
        this.name = name;
    }
    /**
     * Dodaje podlokacje do lokacji.
     *
     * Metoda abstrakcyjna, ktora posiada implementacje
     * w konkretnej klasie dziedziczacej.
     * Sluzy do zarzadzania struktura hierarchiczna.
     */
    public abstract void add(Location location);

    /**
     * Usuwa podlokacje z lokacji
     *
     * Metoda abstrakcyjna, ktora posiada implementacje
     * w konkretnej klasie dziedziczacej.
     * Sluzy do zarzadzania struktura hierarchiczna.
     */
    public abstract void remove(Location location);


}
