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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.put.poznan.buildingInfo.logic.locations.Building;
import pl.put.poznan.buildingInfo.logic.locations.Level;
import pl.put.poznan.buildingInfo.logic.locations.Room;
import pl.put.poznan.buildingInfo.logic.visitors.AreaVisitor;
import pl.put.poznan.buildingInfo.logic.visitors.CubeVisitor;
import pl.put.poznan.buildingInfo.logic.visitors.EnergyVisitor;
import pl.put.poznan.buildingInfo.logic.visitors.LightVisitor;


/**
 * Kontroler obsługujący operacje CRUD dla pomieszczeń ({@link Room}) w ramach poziomów ({@link Level}) i budynków ({@link Building}).
 *
 * Umożliwia zarządzanie pokojami, w tym ich dodawanie, aktualizowanie, usuwanie i pobieranie.
 * Dodatkowo oferuje metody do obliczania parametrów takich jak powierzchnia, kubatura,
 * moc oświetlenia oraz zużycie energii dla konkretnego pokoju.
 */
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

    /**
     * Pobiera wszystkie pomieszczenia na wskazanym poziomie w budynku.
     *
     * @param buildingId identyfikator budynku, w którym znajduje się poziom
     * @param levelId identyfikator poziomu, z którego mają zostać pobrane pomieszczenia
     * @return lista pomieszczeń znajdujących się na poziomie
     * @throws ResponseStatusException jeśli poziom o podanym identyfikatorze nie istnieje
     */
    @RequestMapping(value = "/all-rooms", method = RequestMethod.GET, produces = "application/json")
    public List<Room> getAllRooms(@PathVariable int buildingId, @PathVariable int levelId) {
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

    /**
     * Dodaje nowe pomieszczenie do wskazanego poziomu w budynku.
     *
     * @param room obiekt pomieszczenia do dodania
     * @param buildingId identyfikator budynku, w którym znajduje się poziom
     * @param levelId identyfikator poziomu, do którego pomieszczenie ma zostać dodane
     * @return dodane pomieszczenie
     * @throws ResponseStatusException jeśli pomieszczenie o podanym identyfikatorze już istnieje
     */
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

    /**
     * Aktualizuje dane istniejącego pomieszczenia.
     *
     * @param buildingId identyfikator budynku, w którym znajduje się poziom
     * @param levelId identyfikator poziomu, w którym znajduje się pomieszczenie
     * @param roomId identyfikator pomieszczenia do aktualizacji
     * @param updatedRoom zaktualizowane dane pomieszczenia
     * @return zaktualizowane pomieszczenie
     * @throws ResponseStatusException jeśli pomieszczenie o podanym identyfikatorze nie istnieje
     */
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

    /**
     * Usuwa pomieszczenie na podstawie jego identyfikatora.
     *
     * @param roomId identyfikator pomieszczenia do usunięcia
     * @param buildingId identyfikator budynku, w którym znajduje się poziom
     * @param levelId identyfikator poziomu, w którym znajduje się pomieszczenie
     * @throws ResponseStatusException jeśli pomieszczenie o podanym identyfikatorze nie istnieje
     */
    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable int roomId, @PathVariable int buildingId, @PathVariable int levelId) {
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

    /**
     * Pobiera szczegóły konkretnego pomieszczenia na podstawie jego identyfikatora.
     *
     * @param buildingId identyfikator budynku, w którym znajduje się poziom
     * @param levelId identyfikator poziomu, w którym znajduje się pomieszczenie
     * @param roomId identyfikator pomieszczenia
     * @return szczegóły pomieszczenia
     * @throws ResponseStatusException jeśli pomieszczenie o podanym identyfikatorze nie istnieje
     */
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

    /**
     * Pobiera powierzchnię konkretnego pomieszczenia.
     *
     * @param buildingId identyfikator budynku, w którym znajduje się poziom
     * @param levelId identyfikator poziomu, w którym znajduje się pomieszczenie
     * @param roomId identyfikator pomieszczenia
     * @return powierzchnia pomieszczenia (w metrach kwadratowych)
     */
    @RequestMapping(value="/{roomId}/area", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String, Object>> getAreaOfRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        AreaVisitor areaVisitor = new AreaVisitor();
        logger.debug("Getting total area for room ID: {}", roomId);

        double area = areaVisitor.visit(room);
        logger.info("Area for room ID {}: {}", roomId, area);

        Map<String, Object> response = new HashMap<>();
        response.put("area:", area);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Pobiera kubaturę konkretnego pomieszczenia.
     *
     * @param buildingId identyfikator budynku, w którym znajduje się poziom
     * @param levelId identyfikator poziomu, w którym znajduje się pomieszczenie
     * @param roomId identyfikator pomieszczenia
     * @return kubatura pomieszczenia (w metrach sześciennych)
     */
    @RequestMapping(value="/{roomId}/cube", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String, Object>> getCubeOfRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        CubeVisitor cubeVisitor = new CubeVisitor();
        logger.debug("Getting total cube for room ID: {}", roomId);

        double cube = cubeVisitor.visit(room);
        logger.info("Cube for room ID {}: {}", roomId, cube);

        Map<String, Object> response = new HashMap<>();
        response.put("cube:", cube);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    /**
     * Oblicza całkowitą moc oświetlenia dla konkretnego pomieszczenia.
     *
     * @param buildingId identyfikator budynku, w którym znajduje się poziom
     * @param levelId identyfikator poziomu, w którym znajduje się pomieszczenie
     * @param roomId identyfikator pomieszczenia
     * @return całkowita moc oświetlenia pomieszczenia (w watach)
     */
    @RequestMapping(value="/{roomId}/light", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String, Object>> getLightPowerOfBuilding(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        LightVisitor lightVisitor = new LightVisitor();
        logger.debug("Calculating total light power for room ID: {}", roomId);

        double light = lightVisitor.visit(room);
        logger.info("Total light power for room ID {}: {}", roomId, light);

        Map<String, Object> response = new HashMap<>();
        response.put("light power:", light);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Oblicza całkowite zużycie energii dla konkretnego pomieszczenia.
     *
     * @param buildingId identyfikator budynku, w którym znajduje się poziom
     * @param levelId identyfikator poziomu, w którym znajduje się pomieszczenie
     * @param roomId identyfikator pomieszczenia
     * @return całkowite zużycie energii pomieszczenia (w kilowatogodzinach)
     */
    @RequestMapping(value="/{roomId}/energy-consumption", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String, Object>> getEnergyConsumptionOfBuilding(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        EnergyVisitor energyVisitor = new EnergyVisitor();
        logger.debug("Calculating total energy consumption for room ID: {}", roomId);

        double energy = energyVisitor.visit(room);
        logger.info("Total energy consumption for room ID {}: {}", roomId, energy);

        Map<String, Object> response = new HashMap<>();
        response.put("energy consumption:", energy);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
