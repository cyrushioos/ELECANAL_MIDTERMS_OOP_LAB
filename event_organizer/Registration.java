package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
import java.time.LocalDateTime;

public final class Registration {
    private final int attendeeId;
    private final int eventId;
    private final LocalDateTime createdAt;
    private boolean active;

    public Registration(int n, int n2, LocalDateTime localDateTime) {
        this.attendeeId = n;
        this.eventId = n2;
        this.createdAt = localDateTime;
        this.active = true;
    }

    public int getAttendeeId() {
        return this.attendeeId;
    }

    public int getEventId() {
        return this.eventId;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public boolean isActive() {
        return this.active;
    }

    public void cancel() {
        this.active = false;
    }

    public String label(Attendee attendee, Event event) {
        return attendee.getLastName() + "," + attendee.getFirstName() + "-" + event.getTitle();
    }
}

