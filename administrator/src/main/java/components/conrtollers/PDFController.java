package components.conrtollers;

import com.aspose.pdf.Document;
import components.services.pdf.PDFService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/hotel_california")
public class PDFController {

    private PDFService pdfService;

    @GetMapping("/generate_receipt")
    public void generateReport(@RequestParam(name = "totalPrice") String totalPrice,
                               @RequestParam(name = "fullName") String fullName,
                               @RequestParam(name = "roomNumber") String roomNumber,
                               @RequestParam(name = "in") String in,
                               @RequestParam(name = "out") String out,
                               HttpServletResponse response) throws IOException {


        Map<String, String> paramMap = new HashMap<>();
        {
            paramMap.put("totalPrice", totalPrice);
            paramMap.put("fullName", fullName);
            paramMap.put("roomNumber", roomNumber);
            paramMap.put("in", in);
            paramMap.put("out", out);
        }

        Document document = pdfService.generatePdfReport(paramMap);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=example.pdf");
        document.save(response.getOutputStream());
    }
}