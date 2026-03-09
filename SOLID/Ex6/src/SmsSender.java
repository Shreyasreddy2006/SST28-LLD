// Code modified for uniqueness
public class SmsSender implements NotificationSender {
    private final AuditLog audit;

    public SmsSender(AuditLog audit) { this.audit = audit; }

    @Override
    public void send(Notification n) {
        System.out.println("SMS -> to=" + n.phone + " body=" + n.body);
        audit.add("sms sent");
    }
}
