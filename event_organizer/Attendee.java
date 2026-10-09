package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
import java.time.LocalDate;
import java.time.Period;

public final class Attendee
extends Account {
    private final String lastName;
    private final String firstName;
    private final LocalDate birthDate;

    public Attendee(int n, String string, String string2, LocalDate localDate, String string3) {
        super(n, Attendee.accountName(string, string2), string3);
        if (localDate == null || localDate.isAfter(LocalDate.now()) || localDate.isBefore(LocalDate.now().minusYears(120L))) {
            throw new IllegalArgumentException("Birth date must be valid and within the last 120 years.");
        }
        this.lastName = string.trim();
        this.firstName = string2.trim();
        this.birthDate = localDate;
    }

    private static String accountName(String string, String string2) {
        if (string == null || string2 == null || string.isBlank() || string2.isBlank() || string.contains(",") || string2.contains(",")) {
            throw new IllegalArgumentException("Enter first and last names without commas.");
        }
        return string.trim() + "," + string2.trim();
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public int ageOn(LocalDate localDate) {
        return Period.between(this.birthDate, localDate).getYears();
    }

    @Override
    public String getRoleName() {
        return "Attendee";
    }
}

