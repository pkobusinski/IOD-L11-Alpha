package pl.put.poznan.buildingInfo.rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.put.poznan.buildingInfo.logic.Building;
import pl.put.poznan.buildingInfo.logic.Level;
import pl.put.poznan.buildingInfo.logic.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/buildings/{buildingId}/{levelId}")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(LevelController.class);

    private final LevelController levelController;
    public List<Room> rooms = new ArrayList<Room>();

    @Autowired
    public RoomController(LevelController levelController) {
        this.levelController = levelController;
    }

    @RequestMapping(value = "/{roomId}", method = RequestMethod.GET, produces = "application/json")
    public Room getRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        logger.debug("Fetching Room with ID: {} on level: {} of building: {}", roomId, levelId, buildingId);


        Level level = levelController.getLevel(buildingId, levelId);
        logger.debug( "level found: {}", level.getName());

        Room room = level.getRoomsOnLevel().stream()
                .filter(r -> r.getId() == roomId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room with ID: " + levelId + " not found"));

        logger.debug("Room found: {}", level.getId());
        return room;
    }

    @RequestMapping(value="/{roomId}/get/area", method = RequestMethod.GET, produces="application/json")
    public double getAreaOfRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        logger.debug("Calculating total area for room ID: {}", roomId);
        return room.getArea();
    }

    @RequestMapping(value="/{roomId}/get/cube", method = RequestMethod.GET, produces="application/json")
    public double getCubeOfRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        logger.debug("Calculating total cube for room ID: {}", roomId);
        return room.getCube();
    }

    @RequestMapping(value="/{roomId}/calculate/light-power", method = RequestMethod.GET, produces="application/json")
    public double calculateLightPowerOfBuilding(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        logger.debug("Calculating total light power for room ID: {}", roomId);
        return room.calculateLightPower();
    }

    @RequestMapping(value="/{roomId}/calculate/energy-consumption", method = RequestMethod.GET, produces="application/json")
    public double calculateEnergyConsumptionOfBuilding(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        logger.debug("Calculating total energy consumption for room ID: {}", roomId);
        return room.calculateEnergyConsumption();
    }

    @RequestMapping(value ="/addRoom", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Room addRoom(@RequestBody Room room, @PathVariable int buildingId, @PathVariable int levelId) {
        logger.debug("Adding room: {} to level:{} in Building with ID: {}", room,  levelId, buildingId);

        Level level = levelController.getLevel(buildingId, levelId);
        for (Room rom : level.getRoomsOnLevel()) {
            if(rom.getId() == room.getId()) {
                logger.debug("Couldn't add room: {} to level:{} in Building with ID: {}, because it already exists", room,  levelId, buildingId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room with ID: " + room.getId() + " already exists");
            }
        }

        level.add(room);
        return room;
    }
}
