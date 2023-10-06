package components.services.booking;


import components.entities.Guest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookingTableService {

    Optional<List<Object[]>> showAction(String arg1, String arg2);

    Optional<List<Object[]>> showBookingTable();

    Optional<List<Object[]>> showBookingsByParam(Map<String, String> paramMap);

    Optional<List<Object[]>> showRoomBookingInfo(String roomNumber, Map<String, String> paramMap);

    Optional<List<Object[]>> showGuestBookingInfo(String fullName, Map<String, String> paramMap);

    void bookRoom(Guest guest, Map<String, String> paramMap);

    Object[] showBookingById(int id);

    void deleteRoom(int id);

    void updateBooking(int id, Map<String, String> paramMap);
}
