// Code modified for uniqueness
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Student Onboarding ===");
        FakeDb database = new FakeDb();
        OnboardingService svc = new OnboardingService(database);

        String raw = "name=Riya;email=riya@sst.edu;phone=9876543210;program=CSE";
        svc.registerFromRawInput(raw);

        System.out.println();
        System.out.println("-- DB DUMP --");
        System.out.print(TextTable.render3(database));
    }
}
