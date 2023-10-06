package components.conrtollers;



import components.entities.Room;
import components.services.room.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/hotel_california")
public class RoomController {

    private RoomService roomService;

    @GetMapping("/rooms")
    public String showAllRooms(Model model) {
        Optional<List<Room>> r = roomService.showAllRooms();
        r.ifPresent(objects -> model.addAttribute("rooms", objects));
        model.addAttribute("all", "All");
        return "hotel/room/show_rooms";
    }

    @GetMapping("room/{id}")
    public String showRoomById(@PathVariable("id") int id, Model model) {
        model.addAttribute("room", roomService.showRoomById(id));
        return "hotel/room/show_room_by_id";
    }

    @GetMapping("/room")
    public String showGuestsByParam(@RequestParam(name = "roomNumber", required = false) String roomNumber,
                                    @RequestParam(name = "classType", required = false) String classType,
                                    Model model) {


        Map<String, String> paramMap = new HashMap<>();

        if (roomNumber != null) {
            paramMap.put("roomNumber", roomNumber);
        } else if (classType != null) {
            paramMap.put("classType", classType);
        } else {
            return "redirect:/hotel_california/error";
        }

        Optional<List<Room>> r = roomService.showRoomsByParam(paramMap);
        r.ifPresent(objects -> model.addAttribute("rooms", objects));
        String modelValue = roomNumber != null ? roomNumber : classType;

        if (roomNumber != null) {
            model.addAttribute("roomNumber", roomNumber);
        } else {
            model.addAttribute("classType", classType);
        }
        return "hotel/room/show_rooms";

    }



    @GetMapping("/rooms/book")
    public String showFreeRooms(@RequestParam(name = "in", required = false) String in,
                                @RequestParam(name = "out", required = false) String out,
                                @RequestParam(name = "roomNumber", required = false) String roomNumber,
                                @RequestParam(name = "classType", required = false) String classType,
                                Model model) {

        if (in != null && out != null) {
            Map<String, String> paramMap = new HashMap<>();
            if (roomNumber != null) {
                paramMap.put("roomNumber", roomNumber);
            } else if (classType != null) {
                paramMap.put("classType", classType);
            }
            Optional<List<Room>> r = roomService.showFreeRooms(in, out, paramMap);
            r.ifPresent(objects -> model.addAttribute("rooms", objects));
            model.addAttribute("in", in);
            model.addAttribute("out", out);
            return "hotel/room/show_free_rooms";
        }
        return "redirect:/hotel_california/error";
    }


    @DeleteMapping("/room/{id}")
    public String deleteRoom(@PathVariable("id") int id) {
        roomService.deleteRoom(id);
        return "redirect:/hotel_california/rooms";
    }

    @PatchMapping("/room/{id}")
    public String updateRoom(@ModelAttribute("room") @Valid Room room,
                             BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "hotel/room/show_room_by_id";
        }
        roomService.updateRoom(id, room);
        return "hotel/room/show_room_by_id";
    }

    @PostMapping("/rooms")
    public String createRoom(@ModelAttribute("room") @Valid Room room,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "hotel/room/create_new_room";
        }
        roomService.saveNewRoom(room);
        return "redirect:/hotel_california/rooms";
    }

    @GetMapping("/room/new")
    public String newRoom(Model model) {
        model.addAttribute("room", new Room());
        return "hotel/room/create_new_room";
    }

    @GetMapping("/room/total_price")
    public String getTotal(@RequestParam(name = "fullName") String fullName,
                           @RequestParam(name = "roomNumber") String roomNumber,
                           @RequestParam(name = "in") String in,
                           @RequestParam(name = "out") String out,
                           Model model) {

        double totalPrice = roomService.getTotalPrice(in, out, roomNumber);

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("fullName", fullName);
        model.addAttribute("roomNumber", roomNumber);
        model.addAttribute("in", in);
        model.addAttribute("out", out);

        return "redirect:/hotel_california/generate_receipt";
    }


}
