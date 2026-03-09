// Code modified for uniqueness
public class PlagiarismChecker implements PlagiarismCheckable {
    public int check(Submission s) {
        return (s.code.contains("class") ? 12 : 40);
    }
}
