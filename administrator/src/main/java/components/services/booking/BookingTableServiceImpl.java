package components.services.booking;


import components.dao.booking.BookingTableDAO;
import components.entities.Guest;
import components.services.ServicesHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BookingTableServiceImpl extends ServicesHelper implements BookingTableService {

    private BookingTableDAO bookingTableDAO;

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Object[]>> showBookingTable() {
        return Optional.ofNullable(bookingTableDAO.showBookingTable());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Object[]>> showBookingsByParam(Map<String, String> paramMap) {
        String[] param = getParams(paramMap);

        StringBuilder builder = new StringBuilder("select g.fullName, r.roomNumber, b.checkIn, " +
                "b.checkOut, g.id, r.id from BookingTable b " +
                "join Room r on b.roomId = r.id join Guest g on b.guestId = g.id");

        String column = null;
        switch (param[0]) {
            case "fullName":
                column = "g.fullName";
                break;
            case "roomNumber":
                column = "r.roomNumber";
                break;
            case "checkIn":
                column = "b.checkIn";
                break;
            case "checkOut":
                column = "b.checkOut";
                break;
        }

        if (param[1] != null && !param[1].isEmpty()) {
            String str = String.format(" where %s = :%s order by b.checkIn", column, param[0]);
            builder.append(str);
            String hql = builder.toString();
            return Optional.ofNullable(bookingTableDAO.showBookingsByParam(hql, column, param[0], param[1]));
        }

        String str = String.format(" where %s is null order by b.checkIn", column);
        builder.append(str);
        String hql = builder.toString();
        return Optional.ofNullable(bookingTableDAO.showBookingsByParam(hql));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Object[]>> showAction(String arg1, String arg2) {

        String column = null;
        LocalDate[] dates = null;

        if (arg2.equals("in")) {
            column = "checkIn";
        } else if (arg2.equals("out")) {
            column = "checkOut";
        }

        if (arg1.equals("today")) {
            dates = new LocalDate[]{LocalDate.now()};
        } else if (arg1.equals("soon")) {
            dates = new LocalDate[]{LocalDate.now(), LocalDate.now().plusDays(2)};
        }

        return Optional.ofNullable(bookingTableDAO.showAction(column, dates));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Object[]>> showRoomBookingInfo(String roomNumber, Map<String, String> paramMap) {

        StringBuilder builder = new StringBuilder("select g.fullName, r.roomNumber, b.checkIn, b.checkOut, g.id, r.id " +
                "from BookingTable b join Room r on b.roomId = r.id " +
                "join Guest g on b.guestId = g.id where r.roomNumber = :roomNumber");

        if (paramMap.isEmpty()) {
            String str = " order by b.checkIn";
            builder.append(str);
            String hql = builder.toString();
            return Optional.ofNullable(bookingTableDAO.showRoomBookingInfo(hql, roomNumber));
        }

        String[] param = getParams(paramMap);

        if (param.length == 2) {
            String str;
            switch (param[0]) {
                case "in":
                    str = String.format(" and b.checkIn >= :%s order by b.checkIn", param[0]);
                    builder.append(str);
                    break;
                case "out":
                    str = String.format(" and b.checkOut <= :%s order by b.checkIn", param[0]);
                    builder.append(str);
                    break;
            }
            String hql = builder.toString();
            return Optional.ofNullable(bookingTableDAO.showRoomBookingInfoParameterized(param, hql, roomNumber));
        }

        String str = " and (:in between b.checkIn and b.checkOut " +
                "or :out between b.checkIn and b.checkOut) " +
                "or (b.checkIn between :in and :out " +
                "or b.checkOut between :in and :out)) order by b.checkIn";
        builder.append(str);
        String hql = builder.toString();
        return Optional.ofNullable(bookingTableDAO.showRoomBookingInfoParameterized(param, hql, roomNumber));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Object[]>> showGuestBookingInfo(String fullName, Map<String, String> paramMap) {

        StringBuilder builder = new StringBuilder("select g.fullName, r.roomNumber, b.checkIn, b.checkOut, g.id, r.id " +
                "from BookingTable b join Room r on b.roomId = r.id " +
                "join Guest g on b.guestId = g.id where g.fullName = :fullName");

        if (paramMap.isEmpty()) {
            String str = " order by b.checkIn";
            builder.append(str);
            String hql = builder.toString();
            return Optional.ofNullable(bookingTableDAO.showGuestBookingInfo(hql, fullName));
        }

        String queryPart = null;

        if (paramMap.size() == 1 && paramMap.containsKey("roomNumber")) {
            queryPart = " and r.roomNumber = :roomNumber order by b.checkIn";
        } else if (paramMap.size() == 1 && !paramMap.containsKey("roomNumber")) {
            if (paramMap.containsKey("in")) {
                queryPart = " and b.checkIn >= :in order by b.checkIn";
            }
            if (paramMap.containsKey("out")) {
                queryPart = " and b.checkOut <= :out order by b.checkIn";
            }
        } else if (paramMap.size() == 2 && !paramMap.containsKey("roomNumber")) {
            queryPart = " and ((:in between b.checkIn and b.checkOut " +
                    "or :out between b.checkIn and b.checkOut) " +
                    "or (b.checkIn between :in and :out " +
                    "or b.checkOut between :in and :out)) order by b.checkIn";
        } else if (paramMap.size() == 2 && paramMap.containsKey("roomNumber")) {
            if (paramMap.containsKey("in")) {
                queryPart = " and r.roomNumber = :roomNumber and b.checkIn >= :in " +
                        "order by b.checkIn";
            }
            if (paramMap.containsKey("out")) {
                queryPart = " and r.roomNumber = :roomNumber and b.checkOut <= :out " +
                        "order by b.checkIn";
            }

        } else {
            queryPart = " and r.roomNumber = :roomNumber and ((:in between b.checkIn and b.checkOut " +
                    "or :out between b.checkIn and b.checkOut) " +
                    "or (b.checkIn between :in and :out " +
                    "or b.checkOut between :in and :out)) order by b.checkIn";
        }
        builder.append(queryPart);
        String hql = builder.toString();
        return Optional.ofNullable(bookingTableDAO.showGuestBookingInfoParameterized(paramMap, hql, fullName));
    }

    @Transactional
    @Override
    public void bookRoom(Guest guest, Map<String, String> paramMap) {
        bookingTableDAO.bookRoom(guest, paramMap);
    }

    @Transactional(readOnly = true)
    @Override
    public Object[] showBookingById(int id) {
        return bookingTableDAO.showBookingById(id);
    }

    @Transactional
    @Override
    public void deleteRoom(int id) {
        bookingTableDAO.deleteRoom(id);
    }

    @Transactional
    @Override
    public void updateBooking(int id, Map<String, String> paramMap) {
        bookingTableDAO.updateBooking(id, paramMap);
    }

}
