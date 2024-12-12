package pl.put.poznan.buildingInfo.rest;
import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.put.poznan.buildingInfo.logic.Building;
import pl.put.poznan.buildingInfo.logic.Level;



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


    @RequestMapping(value="/{levelId}/area", method = RequestMethod.GET, produces="application/json")
    public double calculateAreaOfLevel(@PathVariable int buildingId, @PathVariable int levelId) {
        Level level = getLevel(buildingId, levelId);
        logger.debug("Calculating total area for level ID: {} in Building with ID: {}", levelId, buildingId);
        return level.calculateAreaOnLevel();
    }

    @RequestMapping(value="/{levelId}/cube", method = RequestMethod.GET, produces="application/json")
    public double calculateCubeOfLevel(@PathVariable int buildingId, @PathVariable int levelId) {
        Level level = getLevel(buildingId, levelId);
        logger.debug("Calculating total cube for level ID: {} in Building with ID: {}", levelId, buildingId);
        return level.calculateCubeOnLevel();
    }

    @RequestMapping(value="/{levelId}/light-power", method = RequestMethod.GET, produces="application/json")
    public double calculateLightPowerOfLevel(@PathVariable int buildingId, @PathVariable int levelId) {
        Level level = getLevel(buildingId, levelId);
        logger.debug("Calculating total light power for level ID: {} in Building with ID: {}", levelId, buildingId);
        return level.calculateLightPowerOnLevel();
    }

    @RequestMapping(value="/{levelId}/energy-consumption", method = RequestMethod.GET, produces="application/json")
    public double calculateEnergyConsumptionOfLevel(@PathVariable int buildingId, @PathVariable int levelId) {
        Level level = getLevel(buildingId, levelId);
        logger.debug("Calculating total energy consumption for level ID: {} in Building with ID: {}", levelId, buildingId);
        return level.calculateEnergyConsumptionOnLevel();
    }

}
