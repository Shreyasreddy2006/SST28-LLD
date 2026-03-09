// Code modified for uniqueness
import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;

import java.util.List;

public class TryIt {

    public static void main(String[] args) {
        TicketService ticketService = new TicketService();

        IncidentTicket originalTicket = ticketService.createTicket("TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Created: " + originalTicket);

        IncidentTicket assignedTicket = ticketService.assign(originalTicket, "agent@example.com");
        IncidentTicket escalatedTicket = ticketService.escalateToCritical(assignedTicket);
        System.out.println("\nAfter assign + escalate (new ticket): " + escalatedTicket);
        System.out.println("Original ticket unchanged: " + originalTicket);

        List<String> ticketTags = escalatedTicket.getTags();
        try {
            ticketTags.add("HACKED_FROM_OUTSIDE");
            System.out.println("\nShould not reach here");
        } catch (UnsupportedOperationException e) {
            System.out.println("\nExternal tag mutation blocked: " + e.getClass().getSimpleName());
        }
        System.out.println("Tags still safe: " + escalatedTicket.getTags());
    }
}
