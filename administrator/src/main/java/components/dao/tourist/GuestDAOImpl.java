package components.dao.tourist;

;
import components.entities.Guest;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import java.util.List;

@AllArgsConstructor
@Repository
public class GuestDAOImpl implements GuestDAO {

    private SessionFactory sessionFactory;


    @Override
    public List<Guest> showAllGuests() {
        String hql = "from Guest";
        TypedQuery<Guest> query = sessionFactory.getCurrentSession().createQuery(hql, Guest.class);
        return query.getResultList();
    }

    @Override
    public List<Guest> showCurrentGuests(String str) {
        String hql = "from Guest where status = :status";
        TypedQuery<Guest> query = sessionFactory.getCurrentSession().createQuery(hql, Guest.class);
        query.setParameter("status", str);
        return query.getResultList();
    }

    @Override
    public Guest showGuestById(int id) {
        String hql = "from Guest where id = :id";
        TypedQuery<Guest> query = sessionFactory.getCurrentSession().createQuery(hql, Guest.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Guest> showGuestsByParam(String... args) {
        TypedQuery<Guest> query = sessionFactory.getCurrentSession().createQuery(args[0], Guest.class);
        if (args.length > 1) {
            query.setParameter(args[1], args[2]);
        }
        return query.getResultList();
    }

    @Override
    public void deleteGuest(int id) {
        Session session = sessionFactory.getCurrentSession();
        Guest guest = session.find(Guest.class, id);
        session.remove(guest);
    }

    @Override
    public void updateGuest(Guest guest) {
        sessionFactory.getCurrentSession().update(guest);
    }




}
