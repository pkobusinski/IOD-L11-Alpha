package pl.put.poznan.buildingInfo.rest;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import pl.put.poznan.buildingInfo.logic.Building;
import pl.put.poznan.buildingInfo.logic.Level;
import pl.put.poznan.buildingInfo.logic.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



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

    @RequestMapping(value = "/all-rooms", method = RequestMethod.GET, produces = "application/json")
    public List<Room> getAllLevels(@PathVariable int buildingId, @PathVariable int levelId) {
        logger.debug("Entering getAllRooms method for Building with ID: {}", buildingId);

        Level level = levelController.getLevel(buildingId, levelId);

        if (level == null) {
            logger.debug("Level with ID: {} not found", buildingId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Level with ID: " + buildingId + " not found");
        }

        logger.debug("Level found: {}", level);
        logger.debug("Levels in building before getting: {}", level.getRoomsOnLevel());

        rooms = level.getRoomsOnLevel();

        logger.debug("Getting all rooms on level: {} in Building with ID: {}", levelId, buildingId);
        logger.debug("Rooms: {}", rooms);

        return rooms;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
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

    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId, @RequestBody Room updatedRoom) {
        logger.info("Updating room with ID: {}", roomId);
        Level level = levelController.getLevel(buildingId, levelId);

        Room room = level.getRoomsOnLevel()
                .stream()
                .filter(r -> r.getId() == roomId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room with ID: " + roomId + " not found"));

        room.setName(updatedRoom.getName());
        room.setCube(updatedRoom.getCube());
        room.setArea(updatedRoom.getArea());
        logger.info("Room with ID: {} updated successfully.", roomId);
        return room;
    }

    @DeleteMapping("/{roomId}")
    public void deleteBuilding(@PathVariable int roomId, @PathVariable int buildingId, @PathVariable int levelId) {
        logger.info("Deleting room with ID: {}",roomId);
        Level level = levelController.getLevel(buildingId, levelId);
        Room room = level.getRoomsOnLevel()
                .stream()
                .filter(r -> r.getId() == roomId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room with ID: " + roomId + " not found"));

        level.remove(room);
        logger.info("Room with ID: {} deleted successfully. Remaining rooms: {}", roomId, rooms.size());
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

    @RequestMapping(value="/{roomId}/area", method = RequestMethod.GET, produces="application/json")
    public double getAreaOfRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        logger.debug("Calculating total area for room ID: {}", roomId);
        return room.getArea();
    }

    @RequestMapping(value="/{roomId}/cube", method = RequestMethod.GET, produces="application/json")
    public double getCubeOfRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        logger.debug("Calculating total cube for room ID: {}", roomId);
        return room.getCube();
    }

    @RequestMapping(value="/{roomId}/light-power", method = RequestMethod.GET, produces="application/json")
    public double calculateLightPowerOfBuilding(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        logger.debug("Calculating total light power for room ID: {}", roomId);
        return room.calculateLightPower();
    }

    @RequestMapping(value="/{roomId}/energy-consumption", method = RequestMethod.GET, produces="application/json")
    public double calculateEnergyConsumptionOfBuilding(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        logger.debug("Calculating total energy consumption for room ID: {}", roomId);
        return room.calculateEnergyConsumption();
    }

}
