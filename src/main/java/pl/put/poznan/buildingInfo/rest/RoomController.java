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
import pl.put.poznan.buildingInfo.logic.locations.Room;
import pl.put.poznan.buildingInfo.logic.visitors.*;


/**
 * Kontroler obslugujący operacje CRUD dla pomieszczen ({@link Room}) w ramach poziomow ({@link Level}) i budynkow ({@link Building}).
 *
 * Umozliwia zarządzanie pokojami, w tym ich dodawanie, aktualizowanie, usuwanie i pobieranie.
 * Dodatkowo oferuje metody do obliczania parametrow takich jak powierzchnia, kubatura,
 * moc oświetlenia oraz zuzycie energii dla konkretnego pokoju.
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
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, z ktorego mają zostac pobrane pomieszczenia
     * @return lista pomieszczen znajdujących sie na poziomie
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
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, do ktorego pomieszczenie ma zostac dodane
     * @return dodane pomieszczenie
     * @throws ResponseStatusException jeśli pomieszczenie o podanym identyfikatorze juz istnieje
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
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, w ktorym znajduje sie pomieszczenie
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
     * @param roomId identyfikator pomieszczenia do usuniecia
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, w ktorym znajduje sie pomieszczenie
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
     * Pobiera szczegoly konkretnego pomieszczenia na podstawie jego identyfikatora.
     *
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, w ktorym znajduje sie pomieszczenie
     * @param roomId identyfikator pomieszczenia
     * @return szczegoly pomieszczenia
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
     * Pobiera powierzchnie konkretnego pomieszczenia.
     *
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, w ktorym znajduje sie pomieszczenie
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
     * Pobiera kubature konkretnego pomieszczenia.
     *
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, w ktorym znajduje sie pomieszczenie
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
     * Oblicza calkowitą moc oświetlenia dla konkretnego pomieszczenia.
     *
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, w ktorym znajduje sie pomieszczenie
     * @param roomId identyfikator pomieszczenia
     * @return calkowita moc oświetlenia pomieszczenia (w watach)
     */
    @RequestMapping(value="/{roomId}/light-power", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String, Object>> getLightPowerOfRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
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
     * Oblicza calkowite zuzycie energii dla konkretnego pomieszczenia.
     *
     * @param buildingId identyfikator budynku, w ktorym znajduje sie poziom
     * @param levelId identyfikator poziomu, w ktorym znajduje sie pomieszczenie
     * @param roomId identyfikator pomieszczenia
     * @return calkowite zuzycie energii pomieszczenia (w kilowatogodzinach)
     */
    @RequestMapping(value="/{roomId}/energy-consumption", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Map<String, Object>> getEnergyConsumptionOfRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        Room room = getRoom(buildingId, levelId, roomId);
        EnergyVisitor energyVisitor = new EnergyVisitor();
        logger.debug("Calculating total energy consumption for room ID: {}", roomId);

        double energy = energyVisitor.visit(room);
        logger.info("Total energy consumption for room ID {}: {}", roomId, energy);

        Map<String, Object> response = new HashMap<>();
        response.put("energy consumption:", energy);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/{roomId}/light-cost", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getLightCostOfRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId, @RequestParam double lightCost) {
        Room room = getRoom(buildingId, levelId, roomId);
        logger.debug("Checking the cost of light of room with ID: {} on level with ID: {} in building with ID: {}", roomId, levelId, buildingId);
        LightCostVisitor visitor = new LightCostVisitor(lightCost);

        room.accept(visitor);

        Map<String, Object> response = new HashMap<>();
        response.put("cost of lighting", visitor.visit(room));
        logger.info("The cost of lighting of room with ID: {} on level with ID: {} in building with ID: {} is {}", roomId, levelId, buildingId, visitor.visit(room));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/{roomId}/energy-cost", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> getEnergyCostOfRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId, @RequestParam double energyCost) {
        Room room = getRoom(buildingId, levelId, roomId);
        logger.debug("Checking the cost of energy of room with ID: {} on level with ID: {} in building with ID: {}", roomId, levelId, buildingId);
        EnergyCostVisitor visitor = new EnergyCostVisitor(energyCost);

        room.accept(visitor);

        Map<String, Object> response = new HashMap<>();
        response.put("cost of energy", visitor.visit(room));
        logger.info("The cost of energy of room with ID: {} on level with ID: {} in building with ID: {} is {}", roomId, levelId, buildingId, visitor.visit(room));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
