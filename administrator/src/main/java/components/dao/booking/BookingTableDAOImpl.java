package components.dao.booking;


import components.entities.BookingTable;
import components.entities.Guest;
import components.entities.Room;
import components.services.MyBlockedException;
import components.services.RoomIsNotEmptyException;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@AllArgsConstructor
@Repository
public class BookingTableDAOImpl implements BookingTableDAO {

    private SessionFactory sessionFactory;

    @Override
    public List<Object[]> showBookingTable() {
        String hql = "select  g.fullName, r.roomNumber, b.checkIn, b.checkOut, g.id, r.id, b.id " +
                "from BookingTable b join Room r on b.roomId = r.id " +
                "join Guest g on b.guestId = g.id order by b.checkIn";
        TypedQuery<Object[]> query = sessionFactory.getCurrentSession().createQuery(hql, Object[].class);
        return query.getResultList();
    }

    // use Criteria API just as practice
    // it can be replaced with a common hql query
    @Override
    public List<Object[]> showAction(String column, LocalDate... dates) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<BookingTable> bookingRoot = query.from(BookingTable.class);
        Join<BookingTable, Guest> guestJoin = bookingRoot.join("guestId");
        Join<BookingTable, Room> roomJoin = bookingRoot.join("roomId");

        query.multiselect(
                guestJoin.get("fullName"),
                roomJoin.get("roomNumber"),
                bookingRoot.get("checkIn"),
                bookingRoot.get("checkOut"),
                guestJoin.get("id"),
                roomJoin.get("id")
        );

        Expression<Date> dateColumn = bookingRoot.get(column);

        if (dateColumn != null) {
            Predicate datePredicate = null;
            if (dates.length == 1) {
                datePredicate = cb.equal(
                        dateColumn.as(Date.class),
                        Date.valueOf(dates[0]) // today
                );
            } else if (dates.length == 2) {
                datePredicate = cb.between(
                        dateColumn.as(Date.class),
                        Date.valueOf(dates[0]), // today
                        Date.valueOf(dates[1]) // day after tomorrow
                );
            }
            query.where(datePredicate);
        }
        query.orderBy(cb.asc(bookingRoot.get("checkIn")));
        TypedQuery<Object[]> typedQuery = sessionFactory.getCurrentSession().createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public List<Object[]> showBookingsByParam(String... args) {

        TypedQuery<Object[]> query = sessionFactory.getCurrentSession().createQuery(args[0], Object[].class);
        if (args.length > 1) {
            switch (args[1]) {
                case "b.checkIn":
                case "b.checkOut":
                    query.setParameter(args[2], LocalDate.parse(args[3]));
                    break;
                case "r.roomNumber":
                case "g.fullName":
                    query.setParameter(args[2], args[3]);
                    break;
            }
        }
        return query.getResultList();
    }

    @Override
    public List<Object[]> showRoomBookingInfo(String... args) {
        // check if the room is present first
        if (isRoomPresent(args[1])) {
            throw new NoResultException();
        }

        TypedQuery<Object[]> query = sessionFactory.getCurrentSession().createQuery(args[0], Object[].class);
        query.setParameter("roomNumber", args[1]);
        return query.getResultList();
    }

    @Override
    public List<Object[]> showRoomBookingInfoParameterized(String[] param, String... args) {
        // check if the room is present first
        if (isRoomPresent(args[1])) {
            throw new NoResultException();
        }

        TypedQuery<Object[]> query = sessionFactory.getCurrentSession().createQuery(args[0], Object[].class);
        query.setParameter("roomNumber", args[1]);
        query.setParameter(param[0], LocalDate.parse(param[1]));

        if (param.length == 4) {
            query.setParameter(param[2], LocalDate.parse(param[3]));
        }

        return query.getResultList();
    }

    @Override
    public List<Object[]> showGuestBookingInfo(String... args) {
        // check if the guest is present first
        if (isGuestPresent(args[1])) {
            throw new NoResultException();
        }

        TypedQuery<Object[]> query = sessionFactory.getCurrentSession().createQuery(args[0], Object[].class);
        query.setParameter("fullName", args[1]);
        return query.getResultList();
    }

    @Override
    public List<Object[]> showGuestBookingInfoParameterized(Map<String, String> paramMap, String... args) {
        // check if the guest is present first
        if (isGuestPresent(args[1])) {
            throw new NoResultException();
        }

        TypedQuery<Object[]> query = sessionFactory.getCurrentSession().createQuery(args[0], Object[].class);
        query.setParameter("fullName", args[1]);
        if (paramMap.containsKey("roomNumber")) {
            query.setParameter("roomNumber", paramMap.get("roomNumber"));
        }
        if (paramMap.containsKey("in")) {
            query.setParameter("in", LocalDate.parse(paramMap.get("in")));
        }
        if (paramMap.containsKey("out")) {
            query.setParameter("out", LocalDate.parse(paramMap.get("out")));
        }
        return query.getResultList();
    }

    @Override
    public void bookRoom(Guest guest, Map<String, String> paramMap) {
        String fullName = guest.getFullName();
        String passport = guest.getPassport();

        Session session = sessionFactory.getCurrentSession();

        String checkQuery = "select id from Guest where fullName = :fullName and passport = :passport";
        int guestId = 0;
        try {
            guestId = session.createQuery(checkQuery, Integer.class)
                    .setParameter("fullName", fullName)
                    .setParameter("passport", passport)
                    .getSingleResult();
        } catch (NoResultException e) {
        }

        if (guestId == 0) {
            guestId = (int) session.save(guest);

        } else {
            Guest presentGuest = session.find(Guest.class, guestId);
            if (Objects.equals(presentGuest.getNotes(), "blocked")) {
                throw new MyBlockedException();
            }
            presentGuest.setPhoneNumber(guest.getPhoneNumber());
            presentGuest.setEmail(guest.getEmail());
            presentGuest.setStatus(guest.getStatus());
            presentGuest.setNotes(guest.getNotes());
            session.update(presentGuest);
        }
        session.flush();
        session.clear();

        String hql = "select id from Room where roomNumber = :roomNumber";
        int roomId = session.createQuery(hql, Integer.class)
                .setParameter("roomNumber", paramMap.get("roomNumber"))
                .getSingleResult();

        Guest savedGuest = session.find(Guest.class, guestId);
        Room savedRoom = session.find(Room.class, roomId);

        BookingTable bookingTable = new BookingTable();
        bookingTable.setRoomId(savedRoom);
        bookingTable.setGuestId(savedGuest);
        bookingTable.setCheckIn(LocalDate.parse(paramMap.get("in")));
        bookingTable.setCheckOut(LocalDate.parse(paramMap.get("out")));

        session.save(bookingTable);
    }

    @Override
    public Object[] showBookingById(int id) {
        String hql = "select b.id, g.fullName, r.roomNumber, b.checkIn, b.checkOut, g.id, r.id  " +
                "from BookingTable b join Room r on b.roomId = r.id " +
                "join Guest g on b.guestId = g.id where b.id = :id";
        TypedQuery<Object[]> query = sessionFactory.getCurrentSession().createQuery(hql, Object[].class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void deleteRoom(int id) {
        Session session = sessionFactory.getCurrentSession();
        BookingTable bookingTable = session.find(BookingTable.class, id);
        session.remove(bookingTable);
    }

    @Override
    public void updateBooking(int id, Map<String, String> paramMap) {
        Session session = sessionFactory.getCurrentSession();

        int guestId = Integer.parseInt(paramMap.get("guestId"));
        int roomId = Integer.parseInt(paramMap.get("roomId"));
        LocalDate in = LocalDate.parse(paramMap.get("in"));
        LocalDate out = LocalDate.parse(paramMap.get("out"));

        String hql = "from BookingTable b join Room r on r.id = b.roomId join Guest g on " +
                "g.id = b.guestId where r.id = :roomId " +
                "and g.id != :guestId "+
                "and ((:in between b.checkIn and b.checkOut " +
                "or :out between b.checkIn and b.checkOut) " +
                "or (b.checkIn between :in and :out " +
                "or b.checkOut between :in and :out))";

        TypedQuery<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("roomId", roomId);
        query.setParameter("guestId", guestId);
        query.setParameter("in", in);
        query.setParameter("out", out);
        List<Object[]> list = query.getResultList();

       if (!list.isEmpty()) {
           throw new RoomIsNotEmptyException();
       }

       BookingTable bookingTable = session.find(BookingTable.class, id);
       bookingTable.setCheckIn(in);
       bookingTable.setCheckOut(out);
       session.update(bookingTable);
    }

    private boolean isGuestPresent(String fullName) {
        String checkQuery = "select count(*) from Guest where fullName = :fullName";
        long count = sessionFactory.getCurrentSession()
                .createQuery(checkQuery, Long.class)
                .setParameter("fullName", fullName)
                .getSingleResult();
        return count == 0;
    }

    private boolean isRoomPresent(String roomNumber) {
        String checkQuery = "select count(*) from Room where roomNumber = :roomNumber";
        long count = sessionFactory.getCurrentSession()
                .createQuery(checkQuery, Long.class)
                .setParameter("roomNumber", roomNumber)
                .getSingleResult();
        return count == 0;
    }

}

