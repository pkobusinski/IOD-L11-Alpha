package pl.put.poznan.buildingInfo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* Główna klasa aplikacji BuildingInfo.
* <p>
* Klasa ta jest punktem wejscia aplikacji. Odpowiada za inicjalizacje
* i uruchomienie całej aplikacji, w tym załadowanie konfiguracji oraz komponentów
* w zadanum pakiecie.
**/
@SpringBootApplication(scanBasePackages = {"pl.put.poznan.buildingInfo.rest"})
public class BuildingInfoApplication {

    /**
     * Uruchamia aplikacje BuilidingInfo.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(BuildingInfoApplication.class, args);
    }
}
