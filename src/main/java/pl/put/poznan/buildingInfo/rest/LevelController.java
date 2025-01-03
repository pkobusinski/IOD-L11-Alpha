package pl.put.poznan.buildingInfo.rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.visitors.*;

/**
 * Kontroler obslugujący operacje CRUD dla poziomow ({@link Level}) w ramach budynkow ({@link Building}).
 *
 * Umozliwia zarządzanie poziomami w budynkach, w tym ich dodawanie, usuwanie i pobieranie,
 * a takze wykonywanie obliczen takich jak sumaryczna powierzchnia, kubatura,
 * moc oświetlenia oraz zuzycie energii na poziomach.
 *
 * Wszystkie operacje są wykonywane w kontekście konkretnego budynku, ktorego identyfikator
 * jest przekazywany jako cześc ściezki API.
 */

@RestController
@RequestMapping("/buildings/{buildingId}")
public class LevelController {

    private static final Logger logger = LoggerFactory.getLogger(LevelController.class);
    private final BuildingController buildingController;


    @Autowired
    public LevelController(BuildingController buildingController) {
        this.buildingController = buildingController;
    }

    public List<Level> levels = new ArrayList<Level>();

    /**
     * Pobiera wszystkie poziomy (levels) dla konkretnego budynku.
     *
     * @param buildingId identyfikator budynku, ktorego poziomy mają zostac pobrane
     * @return lista wszystkich poziomow w danym budynku
     * @throws ResponseStatusException jeśli budynek o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value = "/all-levels", method = RequestMethod.GET, produces = "application/json")
    public List<Level> getAllLevels(@PathVariable int buildingId) {
            logger.debug("Entering getAllLevels method for Building with ID: {}", buildingId);

            Building building = buildingController.getBuilding(buildingId);

            if (building == null) {
                logger.debug("Building with ID: {} not found", buildingId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Building with ID: " + buildingId + " not found");
            }

            logger.debug("Building found: {}", building);
            logger.debug("Levels in building before getting: {}", building.getLevelsInBuilding());

            levels = building.getLevelsInBuilding();

            logger.debug("Getting all levels in Building with ID: {}", buildingId);
            logger.debug("Levels: {}", levels);

            return levels;
    }

    /**
     * Dodaje nowy poziom do wskazanego budynku.
     *
     * @param level obiekt poziomu do dodania
     * @param buildingId identyfikator budynku, do ktorego poziom ma zostac dodany
     * @return dodany poziom
     * @throws ResponseStatusException jeśli poziom o podanym identyfikatorze juz istnieje w budynku
     */
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Level addLevel(@RequestBody Level level, @PathVariable int buildingId) {
        logger.debug("Adding level: {} to Building with ID: {}", level, buildingId);

        Building building = buildingController.getBuilding(buildingId);
        for (Level lvl :  building.getLevelsInBuilding()) {
            if(lvl.getId() == level.getId()) {
                logger.debug("Couldn't add level: {} to Building with ID: {}, because it already exists", level, buildingId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Level with ID: " + level.getId() + " already exists");
            }
        }

        building.add(level);
        return level;
    }


    /**
     * Aktualizuje dane istniejącego poziomu.
     *
     * @param buildingId       identyfikator budynku do w ktorym znajduje sie poziom do aktualizacji
     * @param levelId          identyfikator poziomu do aktualizacji
     * @param updatedLevel     zaktualizowane dane poziomu
     * @return zaktualizowany budynek
     */

    @PutMapping("/{levelId}")
    public Level updateLevel(@PathVariable int buildingId, @PathVariable int levelId, @RequestBody Level updatedLevel){
        logger.debug("Updating level: {} in Building with ID: {}", levelId, buildingId);

        Building building = buildingController.getBuilding(buildingId);
        Level level = building.getLevelsInBuilding().stream()
                .filter(r -> r.getId() == levelId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Level with ID: " + levelId + " not found"));        
        level.setRoomsOnLevel(updatedLevel.getRoomsOnLevel());
        level.setName(updatedLevel.getName());
        logger.info("Level with ID: {} updated successfully.", levelId);

        return level;
    }

    @DeleteMapping("/{levelId}")
    public void deleteLevel(@PathVariable int buildingId, @PathVariable int levelId){
        logger.debug("Deleting level: {} from Building with ID: {}", levelId, buildingId);

        Building building = buildingController.getBuilding(buildingId);
        Level level = this.getLevel(buildingId, levelId);
        building.remove(level);
        logger.info("Level with ID: {} deleted successfully.", levelId);
        
    }
        



    /**
     * Pobiera szczegoly konkretnego poziomu na podstawie jego identyfikatora i identyfikatora budynku.
     *
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu do pobrania
     * @return szczegoly poziomu
     * @throws ResponseStatusException jeśli poziom o podanym identyfikatorze nie istnieje w budynku
     */
    @RequestMapping(value = "/{levelId}", method = RequestMethod.GET, produces = "application/json")
    public Level getLevel(@PathVariable int buildingId, @PathVariable int levelId) {
        logger.debug("Fetching level: {} of building: {}", levelId, buildingId);

        Building building = buildingController.getBuilding(buildingId);
        logger.debug("Building found: {}", building.getName());

        Level level = building.getLevelsInBuilding().stream()
                .filter(r -> r.getId() == levelId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Level with ID: " + levelId + " not found"));

        logger.debug("Level found: {}", level.getId());
        return level;
    }


    /**
     * Oblicza calkowitą powierzchnie dla konkretnego poziomu w danym budynku.
     *
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, dla ktorego powierzchnia ma zostac obliczona
     * @return calkowita powierzchnia poziomu w metrach kwadratowych
     * @throws ResponseStatusException jeśli poziom o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value="/{levelId}/area", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String, Object>> getAreaOfLevel(@PathVariable int buildingId, @PathVariable int levelId) {
        Level level = getLevel(buildingId, levelId);
        AreaVisitor areaVisitor = new AreaVisitor();

        logger.debug("Geting total area for level ID: {} in Building with ID: {}", levelId, buildingId);
        double area = areaVisitor.visit(level);

        logger.info("Area for level ID {}: {}", levelId, area);

        Map<String, Object> response = Map.of("area", area);

        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Oblicza calkowitą kubature dla konkretnego poziomu w danym budynku.
     *
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, dla ktorego kubatura ma zostac obliczona
     * @return calkowita kubatura poziomu w metrach sześciennych
     * @throws ResponseStatusException jeśli poziom o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value="/{levelId}/cube", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String, Object>> getCubeOfLevel(@PathVariable int buildingId, @PathVariable int levelId) {
        Level level = getLevel(buildingId, levelId);
        CubeVisitor cubeVisitor = new CubeVisitor();
        logger.debug("Getting total cube for level ID: {} in Building with ID: {}", levelId, buildingId);
        
        double cube = cubeVisitor.visit(level);

        logger.info("Cube for level ID {}: {}", levelId, cube);

        Map<String, Object> response = Map.of("cube", cube);

        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Oblicza calkowitą moc oświetlenia dla konkretnego poziomu w danym budynku.
     *
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, dla ktorego moc oświetlenia ma zostac obliczona
     * @return calkowita moc oświetlenia poziomu w watach
     * @throws ResponseStatusException jeśli poziom o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value="/{levelId}/light-power", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String, Object>> getLightPowerOfLevel(@PathVariable int buildingId, @PathVariable int levelId) {
        Level level = getLevel(buildingId, levelId);
        LightVisitor lightVisitor = new LightVisitor();

        logger.debug("Getting total light power for level ID: {} in Building with ID: {}", levelId, buildingId);

        double lightPower = lightVisitor.visit(level);

        logger.info("Light power for level ID {}: {}", levelId, lightPower);

        Map<String, Object> response = Map.of("lightPower", lightPower);



        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Oblicza calkowite zuzycie energii na ogrzewanie dla konkretnego poziomu w danym budynku.
     *
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, dla ktorego zuzycie energii ma zostac obliczone
     * @return calkowite zuzycie energii poziomu w kilowatogodzinach
     * @throws ResponseStatusException jeśli poziom o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value="/{levelId}/energy-consumption", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String,Object>> getEnergyConsumptionOfLevel(@PathVariable int buildingId, @PathVariable int levelId) {
        Level level = getLevel(buildingId, levelId);
        EnergyVisitor energyVisitor = new EnergyVisitor();

        logger.debug("Getting total energy consumption for level ID: {} in Building with ID: {}", levelId, buildingId);

        double energyConsumption = energyVisitor.visit(level);

        logger.info("Energy consumption for level ID {}: {}", levelId, energyConsumption);

        Map<String, Object> response = Map.of("energyConsumption", energyConsumption);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/{levelId}/light-cost", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getLightCostOnLevel(@PathVariable int buildingId, @PathVariable int levelId, @RequestParam double lightCost) {
        Level level = getLevel(buildingId, levelId);
        logger.debug("Checking the cost of light for level with ID: {} in building with ID: {}", levelId, buildingId);
        LightCostVisitor visitor = new LightCostVisitor(lightCost);

        level.accept(visitor);

        Map<String, Object> response = new HashMap<>();
        response.put("cost of lighting", visitor.visit(level));
        logger.info("The cost of lighting for level with ID: {} in building with ID: {} is {}", levelId, buildingId, visitor.visit(level));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/{levelId}/energy-cost", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getEnergyCostOnLevel(@PathVariable int buildingId, @PathVariable int levelId, @RequestParam double energyCost) {
        Level level = getLevel(buildingId, levelId);
        logger.debug("Checking the cost of energy for level with ID: {} in building with ID: {}", levelId, buildingId);
        EnergyCostVisitor visitor = new EnergyCostVisitor(energyCost);

        level.accept(visitor);

        Map<String, Object> response = new HashMap<>();
        response.put("cost of energy", visitor.visit(level));
        logger.info("The cost of energy for level with ID: {} in building with ID: {} is {}", levelId, buildingId, visitor.visit(level));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
