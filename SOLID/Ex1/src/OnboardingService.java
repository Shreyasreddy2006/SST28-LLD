// Code modified for uniqueness
import java.util.List;
import java.util.Map;

public class OnboardingService {
    private final StudentStore store;
    private final StudentParser parser;
    private final StudentValidator validator;
    private final ConfirmationPrinter printer;

    public OnboardingService(StudentStore store) {
        this.store = store;
        this.parser = new StudentParser();
        this.validator = new StudentValidator();
        this.printer = new ConfirmationPrinter();
    }

    public void registerFromRawInput(String raw) {
        System.out.println("INPUT: " + raw);

        Map<String, String> keyValueMap = parser.parse(raw);

        List<String> errors = validator.validate(keyValueMap);
        if (!errors.isEmpty()) {
            printer.printErrors(errors);
            return;
        }

        String name = keyValueMap.getOrDefault("name", "");
        String email = keyValueMap.getOrDefault("email", "");
        String phone = keyValueMap.getOrDefault("phone", "");
        String program = keyValueMap.getOrDefault("program", "");

        String id = IdUtil.nextStudentId(store.count());
        StudentRecord record = new StudentRecord(id, name, email, phone, program);

        store.save(record);
        printer.printSuccess(record, store.count());
    }
}
