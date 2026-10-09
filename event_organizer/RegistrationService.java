package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RegistrationService {
    private final List<Registration> registrations = new ArrayList<Registration>();
    private final RegistrationPolicy policy;
    private final Clock clock;

    public RegistrationService(RegistrationPolicy registrationPolicy, Clock clock) {
        this.policy = registrationPolicy;
        this.clock = clock;
    }

    public Registration register(Attendee attendee, Event event) {
        if (attendee == null || event == null) {
            throw new IllegalArgumentException("Attendee or event not found.");
        }
        LocalDateTime localDateTime = LocalDateTime.now(this.clock);
        String string = this.policy.rejectionReason(attendee, event, this.registrations, localDateTime);
        if (string != null) {
            throw new IllegalArgumentException(string);
        }
        Registration registration = new Registration(attendee.getId(), event.getId(), localDateTime);
        this.registrations.add(registration);
        return registration;
    }

    public void cancel(Attendee attendee, Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event not found.");
        }
        for (Registration registration : this.registrations) {
            if (!registration.isActive() || registration.getAttendeeId() != attendee.getId() || registration.getEventId() != event.getId()) continue;
            registration.cancel();
            return;
        }
        throw new IllegalArgumentException("No active registration for this event.");
    }

    public int countFor(int n) {
        int n2 = 0;
        for (Registration registration : this.registrations) {
            if (!registration.isActive() || registration.getEventId() != n) continue;
            ++n2;
        }
        return n2;
    }

    public List<Registration> forAttendee(Attendee attendee) {
        ArrayList<Registration> arrayList = new ArrayList<Registration>();
        for (Registration registration : this.registrations) {
            if (!registration.isActive() || registration.getAttendeeId() != attendee.getId()) continue;
            arrayList.add(registration);
        }
        return arrayList;
    }

    public List<Registration> forEvent(int n) {
        ArrayList<Registration> arrayList = new ArrayList<Registration>();
        for (Registration registration : this.registrations) {
            if (!registration.isActive() || registration.getEventId() != n) continue;
            arrayList.add(registration);
        }
        return Collections.unmodifiableList(arrayList);
    }
}

