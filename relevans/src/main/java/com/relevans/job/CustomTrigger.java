package com.relevans.job;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.time.Instant;
import java.util.Date;

public class CustomTrigger implements Trigger {

    private final Date eventDate;

    public CustomTrigger(Date eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public Instant nextExecution(TriggerContext triggerContext) {
        return eventDate.toInstant();
    }
}
