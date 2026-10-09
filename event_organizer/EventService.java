package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EventService {
    private final List<Event> events = new ArrayList<Event>();
    private final Clock clock;
    private int nextId = 1;

    public EventService(Clock clock) {
        this.clock = clock;
    }

    public Event create(EventManager eventManager, String string, String string2, LocalDateTime localDateTime, String string3, String string4, int n, int n2) {
        this.requireFuture(localDateTime);
        Event event = new Event(this.nextId, eventManager.getId(), string, string2, localDateTime, string3, string4, n, n2);
        this.events.add(event);
        ++this.nextId;
        return event;
    }

    public void edit(EventManager eventManager, int n, String string, String string2, LocalDateTime localDateTime, String string3, String string4, int n2, int n3, int n4) {
        Event event = this.ownedEvent(eventManager, n);
        if (event.getStatus() == EventStatus.CANCELED) {
            throw new IllegalArgumentException("Canceled events cannot be edited.");
        }
        if (!event.getStart().isAfter(LocalDateTime.now(this.clock))) {
            throw new IllegalArgumentException("Started events cannot be edited.");
        }
        this.requireFuture(localDateTime);
        if (n3 < n4) {
            throw new IllegalArgumentException("Capacity cannot be less than active registrations.");
        }
        if (!(n4 <= 0 || localDateTime.equals(event.getStart()) && n2 == event.getMinimumAge())) {
            throw new IllegalArgumentException("Start time and minimum age are locked after registration begins.");
        }
        event.update(string, string2, localDateTime, string3, string4, n2, n3);
    }

    public void cancel(EventManager eventManager, int n) {
        Event event = this.ownedEvent(eventManager, n);
        if (event.getStatus() == EventStatus.CANCELED) {
            throw new IllegalArgumentException("Event is already canceled.");
        }
        if (!event.getStart().isAfter(LocalDateTime.now(this.clock))) {
            throw new IllegalArgumentException("Started events cannot be canceled.");
        }
        event.cancel();
    }

    private void requireFuture(LocalDateTime localDateTime) {
        if (localDateTime == null || !localDateTime.isAfter(LocalDateTime.now(this.clock))) {
            throw new IllegalArgumentException("Event start must be in the future.");
        }
    }

    private Event ownedEvent(EventManager eventManager, int n) {
        Event event = this.find(n);
        if (event == null || event.getManagerId() != eventManager.getId()) {
            throw new IllegalArgumentException("Event not found in your account.");
        }
        return event;
    }

    public Event find(int n) {
        for (Event event : this.events) {
            if (event.getId() != n) continue;
            return event;
        }
        return null;
    }

    public List<Event> all() {
        return Collections.unmodifiableList(this.events);
    }

    public List<Event> byManager(EventManager eventManager) {
        ArrayList<Event> arrayList = new ArrayList<Event>();
        for (Event event : this.events) {
            if (event.getManagerId() != eventManager.getId()) continue;
            arrayList.add(event);
        }
        return arrayList;
    }
}

