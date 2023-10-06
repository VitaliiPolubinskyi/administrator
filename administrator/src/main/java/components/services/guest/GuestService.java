package components.services.guest;

import components.entities.Guest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GuestService {

    Optional<List<Guest>> showAllGuests();

    Optional<List<Guest>> showCurrentGuests(String str);

    Guest showGuestById(int id);

    Optional<List<Guest>> showGuestsByParam(Map<String, String> paramMap);

    void deleteGuest(int id);

    void updateGuest(int id, Guest guest);


}
