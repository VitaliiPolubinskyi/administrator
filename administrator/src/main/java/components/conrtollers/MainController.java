package components.conrtollers;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;




@Controller
@RequestMapping("/hotel_california")
public class MainController {

    @GetMapping()
    public String homePage () {
        return "hotel/general/homepage";
    }

    @GetMapping("/error")
    public String errorPage () {
        return "hotel/general/error";
    }

}
