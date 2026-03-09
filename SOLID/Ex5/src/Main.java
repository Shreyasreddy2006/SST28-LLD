// Code modified for uniqueness
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Export Demo ===");

        ExportRequest req = new ExportRequest("Weekly Report", SampleData.longBody());
        Exporter pdf = new PdfExporter();
        Exporter csv = new CsvExporter();
        Exporter json = new JsonExporter();

        System.out.println("PDF: " + describe(pdf.export(req)));
        System.out.println("CSV: " + describe(csv.export(req)));
        System.out.println("JSON: " + describe(json.export(req)));
    }

    private static String describe(ExportResult r) {
        if (r.hasError()) return "ERROR: " + r.error;
        return "OK bytes=" + r.bytes.length;
    }
}
