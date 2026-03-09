// Code modified for uniqueness
public class WhatsAppSender implements NotificationSender {
    private final AuditLog audit;

    public WhatsAppSender(AuditLog audit) { this.audit = audit; }

    @Override
    public void send(Notification n) {
        if (n.phone == null || !n.phone.startsWith("+")) {
            System.out.println("WA ERROR: phone must start with + and country code");
            audit.add("WA failed");
            return;
        }
        System.out.println("WA -> to=" + n.phone + " body=" + n.body);
        audit.add("wa sent");
    }
}
