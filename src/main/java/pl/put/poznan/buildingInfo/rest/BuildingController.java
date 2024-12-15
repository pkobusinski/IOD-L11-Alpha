package pl.put.poznan.buildingInfo.rest;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import pl.put.poznan.buildingInfo.logic.Building;
import pl.put.poznan.buildingInfo.logic.Level;
import pl.put.poznan.buildingInfo.logic.Room;

import javax.annotation.PostConstruct;


//@RestController
//@RequestMapping("/{text}")
//public class BuildingInfoController {
//
//    private static final Logger logger = LoggerFactory.getLogger(BuildingInfoController.class);
//
//    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
//    public String get(@PathVariable String text,
//                              @RequestParam(value="transforms", defaultValue="upper,escape") String[] transforms) {
//
//        // log the parameters
//        logger.debug(text);
//        logger.debug(Arrays.toString(transforms));
//
//        // perform the transformation, you should run your logic here, below is just a silly example
//        TextTransformer transformer = new TextTransformer(transforms);
//        return transformer.transform(text);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
//    public String post(@PathVariable String text,
//                      @RequestBody String[] transforms) {
//
//        // log the parameters
//        logger.debug(text);
//        logger.debug(Arrays.toString(transforms));
//
//        // perform the transformation, you should run your logic here, below is just a silly example
//        TextTransformer transformer = new TextTransformer(transforms);
//        return transformer.transform(text);
//    }
//
//
//
//}

/**
 * Kontroler obsługujący operacje CRUD dla budynków oraz dodatkowe obliczenia związane z ich właściwościami.
 *
 * Umożliwia zarządzanie budynkami, w tym dodawanie, aktualizowanie i usuwanie,
 * a także obliczanie sumarycznej powierzchni, kubatury, mocy oświetlenia i zużycia energii.
 */
@RestController
@RequestMapping("/buildings")
public class BuildingController {

    private static final Logger logger = LoggerFactory.getLogger(BuildingController.class);

    /**
     * Lista wszystkich budynków zarządzanych przez kontroler.
     */
    public List<Building> buildings = new ArrayList<Building>();

    /**
     * Inicjalizuje przykładowe dane dotyczące budynków po uruchomieniu aplikacji.
     */
    @PostConstruct
    public void init() {
        Room room1 = new Room(1001, "1A", 50.5, 120.0, 10.5, 20.0);
        Room room2 = new Room(1002, "1B", 60.0, 150.0, 15.0, 30.0);
        Level level1 = new Level(101, "Level 1");
        level1.add(room1);
        level1.add(room2);

        Room room3 = new Room(2001, "2A", 70.0, 200.0, 20.0, 40.0);
        Level level2 = new Level(102, "Level 2");
        level2.add(room3);

        Building building = new Building(1, "Building A");
        building.add(level1);
        building.add(level2);

        buildings.add(building);

        logger.info("Initialized buildings data with {} buildings.", buildings.size());
    }

    /**
     * Pobiera listę wszystkich budynków.
     *
     * @return lista wszystkich budynków
     */
    @GetMapping("/all-buildings")
    public List<Building> getAllBuildings() {
        logger.info("Retrieving all buildings. Total count: {}", buildings.size());
        return buildings;
    }

    /**
     * Pobiera szczegóły konkretnego budynku na podstawie jego identyfikatora.
     *
     * @param buildingId identyfikator budynku
     * @return szczegóły budynku
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value = "/{buildingId}", method = RequestMethod.GET, produces = "application/json")
    public Building getBuilding(@PathVariable int buildingId) {
        logger.debug("Building with ID: {}", buildingId);
        return buildings.stream().filter(b -> b.getId() == buildingId).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Building with ID: " + buildingId + " not found"));
    }

    /**
     * Dodaje nowy budynek do listy.
     *
     * @param building obiekt budynku do dodania
     * @return dodany budynek
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze już istnieje
     */
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Building addBuilding(@RequestBody Building building) {
        logger.info("Adding new building: {}", building);
        if (buildings.stream().anyMatch(b -> b.getId() == building.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Building with ID: " + building.getId() + " already exists");
        }
        buildings.add(building);
        logger.info("Building added successfully. Total buildings: {}", buildings.size());
        return building;
    }

    /**
     * Aktualizuje dane istniejącego budynku.
     *
     * @param buildingId       identyfikator budynku do aktualizacji
     * @param updatedBuilding  zaktualizowane dane budynku
     * @return zaktualizowany budynek
     */
    @PutMapping("/{buildingId}")
    public Building updateBuilding(@PathVariable int buildingId, @RequestBody Building updatedBuilding) {
        logger.info("Updating building with ID: {}", buildingId);
        Building building = getBuilding(buildingId);
        building.setName(updatedBuilding.getName());
        building.setLevelsInBuilding(updatedBuilding.getLevelsInBuilding());
        logger.info("Building with ID: {} updated successfully.", buildingId);
        return building;
    }

    /**
     * Usuwa budynek na podstawie jego identyfikatora.
     *
     * @param buildingId identyfikator budynku
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze nie istnieje
     */
    @DeleteMapping("/{buildingId}")
    public void deleteBuilding(@PathVariable int buildingId) {
        logger.info("Deleting building with ID: {}", buildingId);
        Building building = getBuilding(buildingId);
        buildings.remove(building);
        logger.info("Building with ID: {} deleted successfully. Remaining buildings: {}", buildingId, buildings.size());
    }

    /**
     * Oblicza łączną powierzchnię budynku.
     *
     * @param buildingId identyfikator budynku
     * @return sumaryczna powierzchnia budynku
     */
    @RequestMapping(value="/{buildingId}/area", method = RequestMethod.GET, produces="application/json")
    public double calculateAreaOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        logger.debug("Calculating total area for building ID: {}", buildingId);
        return building.calculateAreaOfBuilding();
    }

    /**
     * Oblicza łączną kubaturę budynku.
     *
     * @param buildingId identyfikator budynku
     * @return sumaryczna kubatura budynku
     */
    @RequestMapping(value="/{buildingId}/cube", method = RequestMethod.GET, produces="application/json")
    public double calculateCubeOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        logger.debug("Calculating total cube for building ID: {}", buildingId);
        return building.calculateCubeOfBuilding();
    }

    /**
     * Oblicza łączną moc oświetlenia budynku.
     *
     * @param buildingId identyfikator budynku
     * @return sumaryczna moc oświetlenia budynku
     */
    @RequestMapping(value="/{buildingId}/light-power", method = RequestMethod.GET, produces="application/json")
    public double calculateLightPowerOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        logger.debug("Calculating total light power for building ID: {}", buildingId);
        return building.calculateLightPowerOfBuilding();
    }

    /**
     * Oblicza łączne zużycie energii na ogrzewanie w budynku.
     *
     * @param buildingId identyfikator budynku
     * @return sumaryczne zużycie energii na ogrzewanie w budynku
     */
    @RequestMapping(value="/{buildingId}/energy-consumption", method = RequestMethod.GET, produces="application/json")
    public double calculateEnergyConsumptionOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        logger.debug("Calculating total energy consumption for building ID: {}", buildingId);
        return building.calculateEnergyConsumptionOfBuilding();
    }
}


