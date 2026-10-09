package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
public final class EventManager
extends Account {
    private final String displayName;

    public EventManager(int n, String string, String string2, String string3) {
        super(n, string, string3);
        if (string2 == null || string2.isBlank()) {
            throw new IllegalArgumentException("Manager display name is required.");
        }
        this.displayName = string2.trim();
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getRoleName() {
        return "Event Manager";
    }
}

