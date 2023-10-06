package components.services.room;


import components.entities.Room;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RoomService {

    Optional<List<Room>> showAllRooms();

    Room showRoomById(int id);

    Optional<List<Room>> showRoomsByParam(Map<String, String> paramMap);

    Optional<List<Room>> showFreeRooms(String in, String out, Map<String, String> paramMap);

    void deleteRoom(int id);

    void updateRoom(int id, Room room);

    void saveNewRoom(Room room);

    double getTotalPrice(String in, String out, String roomNumber);
}
