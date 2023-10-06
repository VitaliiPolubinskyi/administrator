package components.conrtollers;


import components.entities.Guest;

import components.services.booking.BookingTableService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/hotel_california")
public class BookingTableController {

    private BookingTableService bookingTableService;


    @GetMapping("/booking")
    public String showBookingTable(Model model) {
        Optional<List<Object[]>> o = bookingTableService.showBookingTable();
        o.ifPresent(objects -> model.addAttribute("booking", objects));
        return "hotel/booking/show_booking_table";
    }


    @GetMapping("/booking/{str1}/{str2}")
    public String showAction(@PathVariable("str1") String str1,
                             @PathVariable("str2") String str2, Model model) {

        if ((str1.equals("today") || str1.equals("soon")) &&
                (str2.equals("in") || str2.equals("out"))) {
            Optional<List<Object[]>> o = bookingTableService.showAction(str1, str2);
            o.ifPresent(objects -> model.addAttribute("booking", objects));
            model.addAttribute("str1", str1);
            model.addAttribute("str2", str2);
            return "hotel/booking/show_booking_table";
        }
        return "/hotel/general/error";
    }

    @GetMapping("/booking_by")
    public String showBookingByTime(@RequestParam(name = "fullName", required = false) String fullName,
                                    @RequestParam(name = "roomNumber", required = false) String roomNumber,
                                    @RequestParam(name = "checkIn", required = false) String checkIn,
                                    @RequestParam(name = "checkOut", required = false) String checkOut,
                                    Model model) {

        Map<String, String> paramMap = new HashMap<>();

        if (fullName != null) {
            paramMap.put("fullName", fullName);
        } else if (roomNumber != null) {
            paramMap.put("roomNumber", roomNumber);
        } else if (checkIn != null) {
            paramMap.put("checkIn", checkIn);
        } else if (checkOut != null) {
            paramMap.put("checkOut", checkOut);
        }


        Optional<List<Object[]>> o = bookingTableService.showBookingsByParam(paramMap);
        o.ifPresent(objects -> model.addAttribute("booking", objects));
        model.addAttribute("str1", "found");
        return "hotel/booking/show_booking_table";
    }

    @GetMapping("/booking/room_info")
    public String showRoomBookingInfo(@RequestParam(name = "roomNumber", required = false) String roomNumber,
                                      @RequestParam(name = "in", required = false) String in,
                                      @RequestParam(name = "out", required = false) String out,
                                      Model model) {

        if (roomNumber != null) {
            Map<String, String> paramMap = new HashMap<>();

            if (in != null && !in.isEmpty()) {
                paramMap.put("in", in);
            }
            if (out != null && !out.isEmpty()) {
                paramMap.put("out", out);
            }

            Optional<List<Object[]>> r = bookingTableService.showRoomBookingInfo(roomNumber, paramMap);
            r.ifPresent(objects -> model.addAttribute("info", objects));
            model.addAttribute("roomNumber", roomNumber);
            return "hotel/booking/show_room_booking_info";
        }
        return "redirect:/hotel_california/error";
    }

    @GetMapping("/booking/guest_info")
    public String showGuestBookingInfo(@RequestParam(name = "fullName", required = false) String fullName,
                                       @RequestParam(name = "roomNumber", required = false) String roomNumber,
                                       @RequestParam(name = "in", required = false) String in,
                                       @RequestParam(name = "out", required = false) String out,
                                       Model model) {

        if (fullName != null) {
            Map<String, String> paramMap = new HashMap<>();

            if (roomNumber != null && !roomNumber.isEmpty()) {
                paramMap.put("roomNumber", roomNumber);
            }
            if (in != null && !in.isEmpty()) {
                paramMap.put("in", in);
            }
            if (out != null && !out.isEmpty()) {
                paramMap.put("out", out);
            }

            Optional<List<Object[]>> r = bookingTableService.showGuestBookingInfo(fullName, paramMap);
            r.ifPresent(objects -> model.addAttribute("info", objects));
            model.addAttribute("fullName", fullName);
            return "hotel/booking/show_guest_booking_info";
        }
        return "redirect:/hotel_california/error";
    }

    @PostMapping("/book/room")
    public String bookRoom(@ModelAttribute("guest") Guest guest,
                           @RequestParam(name = "in") String in,
                           @RequestParam(name = "out") String out,
                           @RequestParam(name = "roomNumber") String roomNumber) {

        Map<String, String> paramMap = new HashMap<>();
        {
            paramMap.put("roomNumber", roomNumber);
            paramMap.put("in", in);
            paramMap.put("out", out);
        }
        bookingTableService.bookRoom(guest, paramMap);
        return "redirect:/hotel_california/booking";
    }

    @GetMapping("/book/room")
    public String bookRoom(@RequestParam(name = "in") String in,
                           @RequestParam(name = "out") String out,
                           @RequestParam(name = "roomNumber") String roomNumber,
                           Model model) {

        model.addAttribute("guest", new Guest());
        model.addAttribute("in", in);
        model.addAttribute("out", out);
        model.addAttribute("roomNumber", roomNumber);
        return "hotel/booking/book_room";
    }

    @GetMapping("/booking/{id}")
    public String showBooking(@PathVariable("id") int id, Model model) {
        Object[] object = bookingTableService.showBookingById(id);
        model.addAttribute("object", object);
        return "hotel/booking/show_booking_by_id";
    }

    @DeleteMapping("/booking/{id}")
    public String deleteBooking(@PathVariable("id") int id) {
        bookingTableService.deleteRoom(id);
        return "redirect:/hotel_california/booking";
    }

    @PatchMapping("/booking/{id}")
    public String updateBooking(@PathVariable("id") int id,
                             @RequestParam(name = "guestId") String guestId,
                             @RequestParam(name = "roomId") String roomId,
                             @RequestParam(name = "in") String in,
                             @RequestParam(name = "out") String out,
                             Model model) {

        if (guestId == null || guestId.isEmpty() || roomId == null || roomId.isEmpty() ||
                in == null || in.isEmpty() || out == null || out.isEmpty()) {
            return "/hotel/general/error";
        }

        Map<String,String> paramMap = new HashMap<>();

        {
            paramMap.put("guestId", guestId);
            paramMap.put("roomId", roomId);
            paramMap.put("in", in);
            paramMap.put("out", out);
        }

        bookingTableService.updateBooking(id, paramMap);
        model.addAttribute("object", bookingTableService.showBookingById(id));
        model.addAttribute("id", id);
        return "hotel/booking/show_booking_by_id";
    }


}

