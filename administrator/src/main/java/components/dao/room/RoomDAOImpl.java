package components.dao.room;


import components.entities.Room;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@AllArgsConstructor
@Repository
public class RoomDAOImpl implements RoomDAO {

    private SessionFactory sessionFactory;

    @Override
    public List<Room> showAllRooms() {
        String hql = "from Room";
        TypedQuery<Room> query = sessionFactory.getCurrentSession().createQuery(hql, Room.class);
        return query.getResultList();
    }

    @Override
    public Room showRoomById(int id) {
        String hql = "from Room where id = :id";
        TypedQuery<Room> query = sessionFactory.getCurrentSession().createQuery(hql, Room.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Room> showRoomsByParam(String... args) {
        TypedQuery<Room> query = sessionFactory.getCurrentSession().createQuery(args[0], Room.class);
        if (args.length > 1) {
            switch (args[1]) {
                case "roomNumber":
                case "classType":
                case "phoneNumber":
                    query.setParameter(args[1], args[2]);
                    break;
                case "price":
                    query.setParameter(args[1], Double.parseDouble(args[2]));
                    break;
            }
        }
        return query.getResultList();
    }


    @Override
    public List<Room> showFreeRooms(String... args) {
        TypedQuery<Room> query = sessionFactory.getCurrentSession().createQuery(args[0], Room.class);
        query.setParameter("in", LocalDate.parse(args[1]));
        query.setParameter("out", LocalDate.parse(args[2]));

        if (args.length > 3) {
            query.setParameter(args[3], args[4]);
        }
        return query.getResultList();
    }

    @Override
    public void deleteRoom(int id) {
        Session session = sessionFactory.getCurrentSession();
        Room room = session.find(Room.class, id);
        session.remove(room);
    }

    @Override
    public void updateRoom(Room room) {
        sessionFactory.getCurrentSession().update(room);
    }

    @Override
    public void saveNewRoom(Room room) {
        sessionFactory.getCurrentSession().save(room);
    }

    @Override
    public double getTotalPrice(String in, String out, String roomNumber) {

        LocalDate date1 = LocalDate.parse(in);
        LocalDate date2 = LocalDate.parse(out);

        // +1 because of logic of booking - described in readme.txt
        long daysBetween = ChronoUnit.DAYS.between(date1, date2) + 1;

        String hql = "select price from Room where roomNumber = :roomNumber";
        double price = sessionFactory.getCurrentSession()
                .createQuery(hql, Double.class)
                .setParameter("roomNumber", roomNumber)
                .getSingleResult();

        return daysBetween*price;
    }




}
