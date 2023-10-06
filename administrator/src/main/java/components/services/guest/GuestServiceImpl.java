package components.services.guest;

import components.dao.tourist.GuestDAO;
import components.entities.Guest;
import components.services.ServicesHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GuestServiceImpl extends ServicesHelper implements GuestService {

    private GuestDAO guestDAO;

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Guest>> showAllGuests() {
        return Optional.ofNullable(guestDAO.showAllGuests());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Guest>> showCurrentGuests(String str) {
        return Optional.ofNullable(guestDAO.showCurrentGuests(str));
    }

    @Transactional(readOnly = true)
    @Override
    public Guest showGuestById(int id) {
        return guestDAO.showGuestById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Guest>> showGuestsByParam(Map<String, String> paramMap) {

        String[] param = getParams(paramMap);
        String hql;

        if (param[1] != null && !param[1].isEmpty()) {
            hql = String.format("from Guest where %s = :%s", param[0], param[0]);
            return Optional.ofNullable(guestDAO.showGuestsByParam(hql, param[0], param[1]));
        }
        hql = String.format("from Guest where %s is null", param[0]);
        return Optional.ofNullable(guestDAO.showGuestsByParam(hql));
    }

    @Transactional
    @Override
    public void deleteGuest(int id) {
        guestDAO.deleteGuest(id);
    }

    @Transactional
    @Override
    public void updateGuest(int id, Guest guest) {
        guestDAO.updateGuest(guest);
    }

}
