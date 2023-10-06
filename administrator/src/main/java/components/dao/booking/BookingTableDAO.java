package components.dao.booking;

import components.entities.Guest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingTableDAO {

    List<Object[]> showBookingTable();

    List<Object[]> showAction(String column, LocalDate... dates);

    List<Object[]> showBookingsByParam(String... args);

    List<Object[]> showRoomBookingInfo(String... args);

    List<Object[]> showRoomBookingInfoParameterized(String[] param, String... args);

    List<Object[]> showGuestBookingInfo(String... args);

    List<Object[]> showGuestBookingInfoParameterized(Map<String, String> paramMap, String... args);

    void bookRoom(Guest guest, Map<String, String> paramMap);

    Object[] showBookingById(int id);

    void deleteRoom(int id);

    void updateBooking(int id, Map<String, String> paramMap);
}
