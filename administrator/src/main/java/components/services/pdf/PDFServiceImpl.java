package components.services.pdf;

import com.aspose.pdf.*;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class PDFServiceImpl implements PDFService {

    @Override
    public Document generatePdfReport(Map<String, String> paramMap) {

        Document document = new Document();
        Page page = document.getPages().add();

        String str = String.format("Dear %s! Total price for your living from %s till %s in room " +
                "%s is", paramMap.get("fullName"), paramMap.get("in"), paramMap.get("out"),
                paramMap.get("roomNumber"));

        TextFragment title = new TextFragment("Hotel California");
        title.getTextState().setFont(FontRepository.findFont("Arial"));
        title.getTextState().setFontSize(20);
        title.getTextState().setFontStyle(FontStyles.Bold);
        title.getTextState().setUnderline(true);

        TextFragment totalPrice = new TextFragment(paramMap.get("totalPrice"));
        totalPrice.getTextState().setFont(FontRepository.findFont("Arial"));
        totalPrice.getTextState().setFontSize(12);
        totalPrice.getTextState().setFontStyle(FontStyles.Bold);

        TextFragment text = new TextFragment(str);
        text.getTextState().setFont(FontRepository.findFont("Arial"));
        text.getTextState().setFontSize(12);
        text.getTextState().setFontStyle(FontStyles.Regular);

        TextFragment farewell = new TextFragment("Thank you for choosing our hotel!");
        farewell.getTextState().setFont(FontRepository.findFont("Arial"));
        farewell.getTextState().setFontSize(12);
        farewell.getTextState().setFontStyle(FontStyles.Regular);

        page.getParagraphs().add(title);
        page.getParagraphs().add(text);
        page.getParagraphs().add(totalPrice);
        page.getParagraphs().add(farewell);

        return document;


    }


}
