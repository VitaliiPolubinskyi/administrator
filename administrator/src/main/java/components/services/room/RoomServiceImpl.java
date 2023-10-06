package components.services.room;

import components.dao.room.RoomDAO;
import components.entities.Room;
import components.services.ServicesHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@AllArgsConstructor
@Service
public class RoomServiceImpl extends ServicesHelper implements RoomService {

    private RoomDAO roomDAO;


    @Transactional(readOnly = true)
    @Override
    public Optional<List<Room>> showAllRooms() {
        return Optional.ofNullable(roomDAO.showAllRooms());
    }

    @Transactional(readOnly = true)
    @Override
    public Room showRoomById(int id) {
        return roomDAO.showRoomById(id);
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<List<Room>> showRoomsByParam(Map<String, String> paramMap) {

        String[] param = getParams(paramMap);
        String hql;

        if (param[1] != null && !param[1].isEmpty()) {
            hql = String.format("from Room where %s = :%s", param[0], param[0]);
            return Optional.ofNullable(roomDAO.showRoomsByParam(hql, param[0], param[1]));
        }

        hql = String.format("from Room where %s is null", param[0]);
        return Optional.ofNullable(roomDAO.showRoomsByParam(hql));
    }




    @Transactional(readOnly = true)
    @Override
    public Optional<List<Room>> showFreeRooms(String in, String out, Map<String, String> paramMap) {


        StringBuilder builder = new StringBuilder("from Room r where r.id not in " +
                "(select b.roomId from BookingTable b  " +
                "where (:in between b.checkIn and b.checkOut " +
                "or :out between b.checkIn and b.checkOut) " +
                "or (b.checkIn between :in and :out " +
                "or b.checkOut between :in and :out))");

        if (!paramMap.isEmpty()) {
            String[] param = getParams(paramMap);

            String column = null;
            switch (param[0]) {
                case "roomNumber":
                    column = "roomNumber";
                    break;
                case "classType":
                    column = "classType";
                    break;
            }

            String str = String.format(" and %s = :%s order by price", column, column);
            builder.append(str);
            String hql = builder.toString();
            return Optional.ofNullable(roomDAO.showFreeRooms(hql, in, out, param[0], param[1]));
        }

        builder.append(" order by price");
        String hql = builder.toString();
        return Optional.ofNullable(roomDAO.showFreeRooms(hql, in, out));

    }


    @Transactional
    @Override
    public void deleteRoom(int id) {
        roomDAO.deleteRoom(id);
    }

    @Transactional
    @Override
    public void updateRoom(int id, Room room) {
        roomDAO.updateRoom(room);
    }

    @Transactional
    @Override
    public void saveNewRoom(Room room) {
        roomDAO.saveNewRoom(room);
    }

    @Transactional(readOnly = true)
    @Override
    public double getTotalPrice(String in, String out, String roomNumber) {
        return roomDAO.getTotalPrice(in, out, roomNumber);
    }
}
