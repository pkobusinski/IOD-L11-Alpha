package pl.put.poznan.buildingInfo.rest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Room;
import pl.put.poznan.buildingInfo.logic.visitors.*;

import javax.annotation.PostConstruct;

/**
 * Kontroler obslugujący operacje CRUD dla budynkow oraz dodatkowe obliczenia związane z ich wlaściwościami.
 * Umozliwia zarządzanie budynkami, w tym dodawanie, aktualizowanie i usuwanie,
 * a takze obliczanie sumarycznej powierzchni, kubatury, mocy oświetlenia i zuzycia energii.
 */
@RestController
@RequestMapping("/buildings")
public class BuildingController {

    /**
     * Logger odpowiedzialny za rejestrowanie informacji i ostrzezen
     * w operacjach związanych z kontrolerem budynkow.
     **/
     private static final Logger logger = LoggerFactory.getLogger(BuildingController.class);

    /**
     * Lista wszystkich budynkow zarządzanych przez kontroler.
     */
    public List<Building> buildings = new ArrayList<Building>();

    /**
     * Inicjalizuje przykladowe dane dotyczące budynkow po uruchomieniu aplikacji.
     */
    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Building> buildingsFromFile;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("buildings.json");

            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: buildings.json");
            }

            buildingsFromFile = objectMapper.readValue(inputStream, new TypeReference<List<Building>>() {});

            for (Building building : buildingsFromFile) {
                buildings.add(building);
                logger.info("Added building: {} with {} levels.", building.getId(), building.getLevelsInBuilding().size());
            }
            logger.info("Successfully loaded {} buildings from JSON.", buildings.size());
        } catch (Exception e) {
            logger.debug("Failed to load buildings data from JSON", e);
        }
    }

    /**
     * Pobiera liste wszystkich budynkow.
     *
     * @return lista wszystkich budynkow
     */
    @GetMapping("/all-buildings")
    public List<Building> getAllBuildings() {
        logger.info("Retrieving all buildings. Total count: {}", buildings.size());
        return buildings;
    }

    /**
     * Pobiera szczegoly konkretnego budynku na podstawie jego identyfikatora.
     *
     * @param buildingId identyfikator budynku
     * @return szczegoly budynku
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value = "/{buildingId}", method = RequestMethod.GET, produces = "application/json")
    public Building getBuilding(@PathVariable int buildingId) {
        logger.info("Building with ID: {}", buildingId);
        return buildings.stream().filter(b -> b.getId() == buildingId).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Building with ID: " + buildingId + " not found"));
    }

    /**
     * Dodaje nowy budynek do listy.
     *
     * @param building obiekt budynku do dodania
     * @return dodany budynek
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze juz istnieje
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
     * Oblicza lączną powierzchnie budynku i zwraca ją w formacie JSON.
     *
     * @param buildingId identyfikator budynku, dla ktorego ma zostac obliczona powierzchnia
     * @return ResponseEntity zawierający wartośc sumarycznej powierzchni budynku w formacie JSON
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value = "/{buildingId}/area", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getAreaOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        AreaVisitor areaVisitor = new AreaVisitor();
        logger.debug("Calculating total area for building ID: {}", buildingId);

        double area = areaVisitor.visit(building);
        logger.info("Total area for building ID {}: {}", buildingId, area);

        Map<String, Object> response = new HashMap<>();
        response.put("area:", area);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Oblicza lączną kubature budynku i zwraca ją w formacie JSON.
     *
     * @param buildingId identyfikator budynku, dla ktorego ma zostac obliczona kubatura
     * @return ResponseEntity zawierający wartośc sumarycznej kubatury budynku w formacie JSON
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value = "/{buildingId}/cube", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getCubeOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        CubeVisitor cubeVisitor = new CubeVisitor();
        logger.debug("Calculating total cube for building ID: {}", buildingId);

        double cube = cubeVisitor.visit(building);
        logger.info("Total cube for building ID {}: {}", buildingId, cube);

        Map<String, Object> response = new HashMap<>();
        response.put("cube:", cube);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Oblicza lączną moc oświetlenia budynku i zwraca ją w formacie JSON.
     *
     * @param buildingId identyfikator budynku, dla ktorego ma zostac obliczona moc oświetlenia.
     * @return ResponseEntity zawierający wartośc sumarycznej mocy oświetlenia budynku w formacie JSON
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value = "/{buildingId}/light-power", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getLightPowerOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        LightVisitor lightVisitor = new LightVisitor();
        logger.debug("Calculating total light power for building ID: {}", buildingId);

        double light = lightVisitor.visit(building);
        logger.info("Total light power for building ID {}: {}", buildingId, light);

        Map<String, Object> response = new HashMap<>();
        response.put("light power:", light);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Oblicza lączne zuzycie energii na ogrzewanie w budynku i zwraca je w formacie JSON.
     *
     * @param buildingId identyfikator budynku, dla ktorego ma zostac obliczone zuzycie energii na ogrzewanie.
     * @return ResponseEntity zawierający wartośc sumarycznego zuzycia energii na ogrzewanie w budynku w formacie JSON
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value = "/{buildingId}/energy-consumption", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getEnergyConsumption(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        EnergyVisitor energyVisitor = new EnergyVisitor();
        logger.debug("Calculating total energy consumption for building ID: {}", buildingId);

        double energy = energyVisitor.visit(building);
        logger.info("Total energy consumption for building ID {}: {}", buildingId, energy);

        Map<String, Object> response = new HashMap<>();
        response.put("energy consumption:", energy);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Oblicza zuzycie energii cieplnej na m³ dla kazdego pomieszczenia w budynku i zwraca te,
     * ktore przekraczają zadany limit.
     *
     * @param buildingId ID budynku.
     * @param energyLimit Wartośc graniczna zuzycia energii cieplnej na m³.
     * @return Mapa z informacjami o pomieszczeniach przekraczających limit.
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value = "/{buildingId}/exceeding-heating", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getRoomsExceedingHeating(@PathVariable int buildingId, @RequestParam double energyLimit) {
        Building building = getBuilding(buildingId);
        logger.debug("Checking rooms exceeding energy limit for building ID: {}", buildingId);
        ExceedingHeatingVisitor visitor = new ExceedingHeatingVisitor(energyLimit);

        building.accept(visitor);

        Map<String, Object> response = new HashMap<>();
        response.put("exceedingRooms", visitor.getRoomsExceedingLimit());

        logger.info("Found {} rooms exceeding energy limit in building ID: {}", visitor.getRoomsExceedingLimit().size(), buildingId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/{buildingId}/light-cost", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getLightCostInBuilding(@PathVariable int buildingId, @RequestParam double lightCost) {
        Building building = getBuilding(buildingId);
        logger.debug("Checking the cost of light for building ID: {}", buildingId);
        LightCostVisitor visitor = new LightCostVisitor(lightCost);

        building.accept(visitor);

        Map<String, Object> response = new HashMap<>();
        response.put("cost of lighting", visitor.visit(building));
        logger.info("The cost of lighting for the building with ID: {} is {}", buildingId, visitor.visit(building));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/{buildingId}/energy-cost", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getEnergyCostInBuilding(@PathVariable int buildingId, @RequestParam double energyCost) {
        Building building = getBuilding(buildingId);
        logger.debug("Checking the cost of energy for building ID: {}", buildingId);
        EnergyCostVisitor visitor = new EnergyCostVisitor(energyCost);

        building.accept(visitor);

        Map<String, Object> response = new HashMap<>();
        response.put("cost of energy", visitor.visit(building));
        logger.info("The cost of energy for the building with ID: {} is {}", buildingId, visitor.visit(building));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}


