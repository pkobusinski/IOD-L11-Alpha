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

@RestController
@RequestMapping("/buildings")
public class BuildingController {

    private static final Logger logger = LoggerFactory.getLogger(BuildingController.class);

    public List<Building> buildings = new ArrayList<Building>();

    // Example buildings adding method
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

    @GetMapping("/all-buildings")
    public List<Building> getAllBuildings() {
        logger.info("Retrieving all buildings. Total count: {}", buildings.size());
        return buildings;
    }

    @RequestMapping(value = "/{buildingId}", method = RequestMethod.GET, produces = "application/json")
    public Building getBuilding(@PathVariable int buildingId) {
        logger.debug("Building with ID: {}", buildingId);
        return buildings.stream().filter(b -> b.getId() == buildingId).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Building with ID: " + buildingId + " not found"));
    }

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

    @PutMapping("/{buildingId}")
    public Building updateBuilding(@PathVariable int buildingId, @RequestBody Building updatedBuilding) {
        logger.info("Updating building with ID: {}", buildingId);
        Building building = getBuilding(buildingId);
        building.setName(updatedBuilding.getName());
        building.setLevelsInBuilding(updatedBuilding.getLevelsInBuilding());
        logger.info("Building with ID: {} updated successfully.", buildingId);
        return building;
    }

    @DeleteMapping("/{buildingId}")
    public void deleteBuilding(@PathVariable int buildingId) {
        logger.info("Deleting building with ID: {}", buildingId);
        Building building = getBuilding(buildingId);
        buildings.remove(building);
        logger.info("Building with ID: {} deleted successfully. Remaining buildings: {}", buildingId, buildings.size());
    }

    @RequestMapping(value="/{buildingId}/calculate/area", method = RequestMethod.GET, produces="application/json")
    public double calculateAreaOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        logger.debug("Calculating total area for building ID: {}", buildingId);
        return building.calculateAreaOfBuilding();
    }

    @RequestMapping(value="/{buildingId}/calculate/cube", method = RequestMethod.GET, produces="application/json")
    public double calculateCubeOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        logger.debug("Calculating total cube for building ID: {}", buildingId);
        return building.calculateCubeOfBuilding();
    }

    @RequestMapping(value="/{buildingId}/calculate/light-power", method = RequestMethod.GET, produces="application/json")
    public double calculateLightPowerOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        logger.debug("Calculating total light power for building ID: {}", buildingId);
        return building.calculateLightPowerOfBuilding();
    }

    @RequestMapping(value="/{buildingId}/calculate/energy-consumption", method = RequestMethod.GET, produces="application/json")
    public double calculateEnergyConsumptionOfBuilding(@PathVariable int buildingId) {
        Building building = getBuilding(buildingId);
        logger.debug("Calculating total energy consumption for building ID: {}", buildingId);
        return building.calculateEnergyConsumptionOfBuilding();
    }
}


