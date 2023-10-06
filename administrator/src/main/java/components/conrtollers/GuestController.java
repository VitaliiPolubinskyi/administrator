package components.conrtollers;

import components.entities.Guest;
import components.services.guest.GuestService;
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
public class GuestController {

    private GuestService guestService;

    @GetMapping("/guests")
    public String showAllGuests(Model model) {
        Optional<List<Guest>> g = guestService.showAllGuests();
        g.ifPresent(objects -> model.addAttribute("guests", objects));
        return "hotel/guest/show_guests";
    }

    @GetMapping("/guest/{id}")
    public String showGuestById(@PathVariable("id") int id, Model model) {
        model.addAttribute("guest", guestService.showGuestById(id));
        return "hotel/guest/show_guest_by_id";
    }

    @GetMapping("/guests/{str}")
    public String showCurrentGuests(@PathVariable("str") String str, Model model) {
        if(str.equals("current") || str.equals("past") || str.equals("future")){
            Optional<List<Guest>> g = guestService.showCurrentGuests(str);
            g.ifPresent(objects -> model.addAttribute("guests", objects));
            model.addAttribute("str", str);
            return "hotel/guest/show_guests";
        }
        return "/hotel/general/error";
    }

    @GetMapping("/guest")
    public String showGuestsByParam(@RequestParam(name = "fullName", required = false) String fullName,
                                      @RequestParam(name = "passport", required = false) String passport,
                                      @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
                                      @RequestParam(name = "email", required = false) String email,
                                      @RequestParam(name = "status", required = false) String status,
                                      @RequestParam(name = "notes", required = false) String notes,
                                      Model model) {

        Map<String, String> paramMap = new HashMap<>();

        if (fullName != null) { paramMap.put("fullName", fullName);}
        else if (passport != null) { paramMap.put("passport", passport);}
        else if (phoneNumber != null) { paramMap.put("phoneNumber", phoneNumber);}
        else if (email != null) { paramMap.put("email", email);}
        else if (status != null) { paramMap.put("status", status);}
        else if (notes != null) { paramMap.put("notes", notes);}

        Optional<List<Guest>> g = guestService.showGuestsByParam(paramMap);
        g.ifPresent(objects -> model.addAttribute("guests", objects));
        model.addAttribute("str", "found");
        return "hotel/guest/show_guests";
    }

    @DeleteMapping("/guest/{id}")
    public String deleteGuest(@PathVariable("id") int id) {
        guestService.deleteGuest(id);
        return "redirect:/hotel_california/guests";
    }

    @PatchMapping("/guest/{id}")
    public String updateGuest(@ModelAttribute("guest") Guest guest, @PathVariable("id") int id) {
        guestService.updateGuest(id, guest);
        return "hotel/guest/show_guest_by_id";
    }

}
