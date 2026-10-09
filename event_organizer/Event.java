package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
import java.time.LocalDateTime;

public final class Event {
    private final int id;
    private final int managerId;
    private String title;
    private String description;
    private LocalDateTime start;
    private String location;
    private String leader;
    private int minimumAge;
    private int capacity;
    private EventStatus status;

    public Event(int n, int n2, String string, String string2, LocalDateTime localDateTime, String string3, String string4, int n3, int n4) {
        if (n <= 0 || n2 <= 0) {
            throw new IllegalArgumentException("Invalid event or manager ID.");
        }
        this.id = n;
        this.managerId = n2;
        this.status = EventStatus.SCHEDULED;
        this.update(string, string2, localDateTime, string3, string4, n3, n4);
    }

    public void update(String string, String string2, LocalDateTime localDateTime, String string3, String string4, int n, int n2) {
        if (string == null || string.isBlank() || string2 == null || string2.isBlank() || localDateTime == null || string3 == null || string3.isBlank() || string4 == null || string4.isBlank() || n < 0 || n > 120 || n2 < 1) {
            throw new IllegalArgumentException("All event details are required; age must be 0-120 and capacity at least 1.");
        }
        this.title = string.trim();
        this.description = string2.trim();
        this.start = localDateTime;
        this.location = string3.trim();
        this.leader = string4.trim();
        this.minimumAge = n;
        this.capacity = n2;
    }

    public void cancel() {
        this.status = EventStatus.CANCELED;
    }

    public int getId() {
        return this.id;
    }

    public int getManagerId() {
        return this.managerId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public String getLocation() {
        return this.location;
    }

    public String getLeader() {
        return this.leader;
    }

    public int getMinimumAge() {
        return this.minimumAge;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public EventStatus getStatus() {
        return this.status;
    }
}

