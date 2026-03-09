// Code modified for uniqueness
public class ReportWriter implements ReportWritable {
    public String write(Submission s, int plag, int code) {
        return "report-" + s.roll + ".txt";
    }
}
