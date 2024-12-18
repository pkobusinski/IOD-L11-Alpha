package pl.put.poznan.buildingInfo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* Glowna klasa aplikacji BuildingInfo.
* <p>
* Klasa ta jest punktem wejscia aplikacji. Odpowiada za inicjalizacje
* i uruchomienie calej aplikacji, w tym zaladowanie konfiguracji oraz komponentow
* w zadanum pakiecie.
**/
@SpringBootApplication(scanBasePackages = {"pl.put.poznan.buildingInfo.rest"})
public class BuildingInfoApplication {

    /**
     * Uruchamia aplikacje BuilidingInfo.
     * @param args uruchom
     */
    public static void main(String[] args) {
        SpringApplication.run(BuildingInfoApplication.class, args);
    }
}
