package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
import java.time.LocalDateTime;
import java.util.List;

public final class StandardRegistrationPolicy
implements RegistrationPolicy {
    @Override
    public String rejectionReason(Attendee attendee, Event event, List<Registration> list, LocalDateTime localDateTime) {
        if (event.getStatus() == EventStatus.CANCELED) {
            return "Event is canceled.";
        }
        if (!event.getStart().isAfter(localDateTime)) {
            return "Event has already started.";
        }
        if (attendee.ageOn(event.getStart().toLocalDate()) < event.getMinimumAge()) {
            return "Attendee does not meet the event's minimum age.";
        }
        long l = 0L;
        for (Registration registration : list) {
            if (!registration.isActive() || registration.getEventId() != event.getId()) continue;
            if (registration.getAttendeeId() == attendee.getId()) {
                return "Already registered for this event.";
            }
            ++l;
        }
        if (l >= (long)event.getCapacity()) {
            return "Event is full.";
        }
        return null;
    }
}

