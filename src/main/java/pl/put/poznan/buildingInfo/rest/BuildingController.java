package pl.put.poznan.buildingInfo.rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.put.poznan.buildingInfo.logic.Building;

import java.util.ArrayList;
import java.util.List;


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

    @GetMapping("/all-buildings")
    public List<Building> getAllBuildings() {
        return buildings;
    }

    @RequestMapping(value = "/{buildingId}", method = RequestMethod.GET, produces = "application/json")
    public Building getBuilding(@PathVariable int buildingId) {
        logger.debug("Building with ID: {}", buildingId);
        return buildings.stream().filter(b -> b.getId() == buildingId).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Building with ID: " + buildingId + " not found"));
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

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Building addBuilding(@RequestBody Building building) {

        logger.debug("Adding building: {}", building);
        buildings.add(building);
        return building;
    }

}


