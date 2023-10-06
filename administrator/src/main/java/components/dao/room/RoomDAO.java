package components.dao.room;


import components.entities.Room;

import java.util.List;


public interface RoomDAO {

    List<Room> showAllRooms();

    Room showRoomById(int id);

    List<Room> showRoomsByParam(String... args);

    List<Room> showFreeRooms(String... args);

    void deleteRoom(int id);

    void updateRoom(Room room);

    void saveNewRoom(Room room);

    double getTotalPrice(String in, String out, String roomNumber);
}
