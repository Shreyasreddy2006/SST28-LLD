// Code modified for uniqueness
import java.nio.charset.StandardCharsets;

public class CsvExporter implements Exporter {
    @Override
    public ExportResult export(ExportRequest req) {
        String body = req.body == null ? "" : req.body;
        String quotedBody = "\"" + body.replace("\"", "\"\"") + "\"";
        String csv = "title,body\n" + req.title + "," + quotedBody + "\n";
        return new ExportResult("text/csv", csv.getBytes(StandardCharsets.UTF_8));
    }
}
