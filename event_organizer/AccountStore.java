package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class AccountStore {
    private final List<Account> accounts = new ArrayList<Account>();
    private int nextId = 1;

    public Attendee addAttendee(String string, String string2, LocalDate localDate, String string3) {
        Attendee attendee = new Attendee(this.nextId, string, string2, localDate, string3);
        this.ensureUnique(attendee.getUsername(), Attendee.class);
        this.accounts.add(attendee);
        ++this.nextId;
        return attendee;
    }

    public EventManager addManager(String string, String string2, String string3) {
        EventManager eventManager = new EventManager(this.nextId, string, string2, string3);
        this.ensureUnique(eventManager.getUsername(), EventManager.class);
        this.accounts.add(eventManager);
        ++this.nextId;
        return eventManager;
    }

    private void ensureUnique(String string, Class<? extends Account> clazz) {
        for (Account account : this.accounts) {
            if (!clazz.isInstance(account) || !account.getUsername().equalsIgnoreCase(string)) continue;
            throw new IllegalArgumentException("That username already exists for this role.");
        }
    }

    public Account login(String string, String string2, Class<? extends Account> clazz) {
        for (Account account : this.accounts) {
            if (!clazz.isInstance(account) || !account.getUsername().equalsIgnoreCase(string) || !account.matchesPassword(string2)) continue;
            return account;
        }
        return null;
    }

    public Attendee findAttendee(int n) {
        for (Account account : this.accounts) {
            if (!(account instanceof Attendee) || account.getId() != n) continue;
            return (Attendee)account;
        }
        return null;
    }
}

