package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
import java.time.LocalDateTime;
import java.util.List;

public interface RegistrationPolicy {
    public String rejectionReason(Attendee var1, Event var2, List<Registration> var3, LocalDateTime var4);
}

