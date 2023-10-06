package components.services.pdf;

import com.aspose.pdf.Document;

import java.io.IOException;
import java.util.Map;

public interface PDFService {

    Document generatePdfReport(Map<String,String> paramMap) throws IOException;
}
