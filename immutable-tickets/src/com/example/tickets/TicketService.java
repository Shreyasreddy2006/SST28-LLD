// Code modified for uniqueness
package com.example.tickets;

import java.util.ArrayList;
import java.util.List;

public class TicketService {

    public IncidentTicket createTicket(String id, String reporterEmail, String title) {
        List<String> initialTags = new ArrayList<>();
        initialTags.add("NEW");

        return new IncidentTicket.Builder(id, reporterEmail, title)
                .priority("MEDIUM")
                .source("CLI")
                .customerVisible(false)
                .tags(initialTags)
                .build();
    }

    public IncidentTicket escalateToCritical(IncidentTicket t) {
        List<String> escalatedTags = new ArrayList<>(t.getTags());
        escalatedTags.add("ESCALATED");

        return t.toBuilder()
                .priority("CRITICAL")
                .tags(escalatedTags)
                .build();
    }

    public IncidentTicket assign(IncidentTicket t, String assigneeEmail) {
        return t.toBuilder()
                .assigneeEmail(assigneeEmail)
                .build();
    }
}
