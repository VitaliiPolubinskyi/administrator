package components.dao.tourist;

import components.entities.Guest;

import java.util.List;


public interface GuestDAO {

    List<Guest> showAllGuests();

    List<Guest> showCurrentGuests(String str);

    Guest showGuestById(int id);

    List<Guest> showGuestsByParam(String... args);

    void deleteGuest(int id);

    void updateGuest(Guest guest);


}
