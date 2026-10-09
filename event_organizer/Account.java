package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
public abstract class Account {
    private final int id;
    private final String username;
    private final String password;

    protected Account(int n, String string, String string2) {
        if (n <= 0 || string == null || string.isBlank() || string2 == null || string2.length() < 6) {
            throw new IllegalArgumentException("Account requires an ID, username, and password of at least 6 characters.");
        }
        this.id = n;
        this.username = string.trim();
        this.password = string2;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean matchesPassword(String string) {
        return this.password.equals(string);
    }

    public abstract String getRoleName();
}

