package com.mycompany.event_organizer;

/*
 * Decompiled with CFR 0.152.
 */
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class SelfTest {
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);
    private static final LocalDateTime FUTURE = LocalDateTime.of(2027, 1, 1, 9, 0);
    private static int passed;
    private static int failed;

    public static void main(String[] stringArray) {
        SelfTest.test("01 attendee account and login", () -> {
            AccountStore accountStore = new AccountStore();
            Attendee attendee = accountStore.addAttendee("Reyes", "Ana", LocalDate.of(2005, 5, 1), "secret1");
            SelfTest.check(attendee.getUsername().equals("Reyes,Ana"));
            SelfTest.check(accountStore.login("reyes,ana", "secret1", Attendee.class) == attendee);
        });
        SelfTest.test("02 manager account and role polymorphism", () -> {
            Attendee attendee = new Attendee(1, "Reyes", "Ana", LocalDate.of(2005, 5, 1), "secret1");
            EventManager eventManager = new EventManager(2, "manager", "Club Lead", "secret2");
            SelfTest.check(((Account)attendee).getRoleName().equals("Attendee") && ((Account)eventManager).getRoleName().equals("Event Manager"));
        });
        SelfTest.test("03 reject duplicate username", () -> {
            AccountStore accountStore = new AccountStore();
            accountStore.addAttendee("Reyes", "Ana", LocalDate.of(2005, 5, 1), "secret1");
            SelfTest.expectError("already exists", () -> accountStore.addAttendee("reyes", "ana", LocalDate.of(2004, 1, 1), "secret2"));
        });
        SelfTest.test("04 reject bad password", () -> {
            AccountStore accountStore = new AccountStore();
            accountStore.addManager("lead", "Lead", "secret1");
            SelfTest.check(accountStore.login("lead", "wrong", EventManager.class) == null);
        });
        SelfTest.test("05 create event with minimum capacity", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(0, 1);
            SelfTest.check(event.getCapacity() == 1 && event.getMinimumAge() == 0 && event.getStatus() == EventStatus.SCHEDULED);
        });
        SelfTest.test("06 reject empty event title", () -> {
            Fixture fixture = new Fixture();
            SelfTest.expectError("details", () -> fixture.events.create(fixture.manager, " ", "Description", FUTURE, "Hall", "Lead", 0, 5));
        });
        SelfTest.test("07 reject invalid capacity", () -> {
            Fixture fixture = new Fixture();
            SelfTest.expectError("details", () -> fixture.events.create(fixture.manager, "Fair", "Description", FUTURE, "Hall", "Lead", 0, 0));
        });
        SelfTest.test("08 reject event in the past", () -> {
            Fixture fixture = new Fixture();
            SelfTest.expectError("future", () -> fixture.events.create(fixture.manager, "Fair", "Description", LocalDateTime.of(2025, 1, 1, 9, 0), "Hall", "Lead", 0, 5));
        });
        SelfTest.test("09 accept attendee at minimum age", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(18, 5);
            Attendee attendee = fixture.attendee("Reyes", "Ana", LocalDate.of(2009, 1, 1));
            fixture.registrations.register(attendee, event);
            SelfTest.check(fixture.registrations.countFor(event.getId()) == 1);
        });
        SelfTest.test("10 reject attendee below minimum age", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(18, 5);
            Attendee attendee = fixture.attendee("Reyes", "Ana", LocalDate.of(2009, 1, 2));
            SelfTest.expectError("minimum age", () -> fixture.registrations.register(attendee, event));
            SelfTest.check(fixture.registrations.countFor(event.getId()) == 0);
        });
        SelfTest.test("11 prevent duplicate registration", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(0, 5);
            Attendee attendee = fixture.attendee("Reyes", "Ana", LocalDate.of(2005, 1, 1));
            fixture.registrations.register(attendee, event);
            SelfTest.expectError("Already registered", () -> fixture.registrations.register(attendee, event));
            SelfTest.check(fixture.registrations.countFor(event.getId()) == 1);
        });
        SelfTest.test("12 enforce event capacity", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(0, 1);
            fixture.registrations.register(fixture.attendee("Reyes", "Ana", LocalDate.of(2005, 1, 1)), event);
            SelfTest.expectError("full", () -> fixture.registrations.register(fixture.attendee("Cruz", "Ben", LocalDate.of(2005, 1, 1)), event));
        });
        SelfTest.test("13 cancel and register again", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(0, 1);
            Attendee attendee = fixture.attendee("Reyes", "Ana", LocalDate.of(2005, 1, 1));
            fixture.registrations.register(attendee, event);
            fixture.registrations.cancel(attendee, event);
            SelfTest.check(fixture.registrations.countFor(event.getId()) == 0);
            fixture.registrations.register(attendee, event);
            SelfTest.check(fixture.registrations.countFor(event.getId()) == 1);
        });
        SelfTest.test("14 registration label is event specific", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(0, 2);
            Attendee attendee = fixture.attendee("Reyes", "Ana", LocalDate.of(2005, 1, 1));
            SelfTest.check(fixture.registrations.register(attendee, event).label(attendee, event).equals("Reyes,Ana-School Fair"));
        });
        SelfTest.test("15 reject canceled event registration", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(0, 2);
            fixture.events.cancel(fixture.manager, event.getId());
            SelfTest.expectError("canceled", () -> fixture.registrations.register(fixture.attendee("Reyes", "Ana", LocalDate.of(2005, 1, 1)), event));
        });
        SelfTest.test("16 reject already started event registration", () -> {
            Attendee attendee = new Attendee(1, "Reyes", "Ana", LocalDate.of(2005, 1, 1), "secret1");
            Event event = new Event(1, 2, "Fair", "Description", LocalDateTime.of(2025, 1, 1, 9, 0), "Hall", "Lead", 0, 2);
            RegistrationService registrationService = new RegistrationService(new StandardRegistrationPolicy(), CLOCK);
            SelfTest.expectError("started", () -> registrationService.register(attendee, event));
        });
        SelfTest.test("17 reject manager editing another manager's event", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(0, 2);
            EventManager eventManager = fixture.accounts.addManager("other", "Other", "secret2");
            SelfTest.expectError("your account", () -> fixture.events.edit(eventManager, event.getId(), "New", "Description", FUTURE, "Hall", "Lead", 0, 2, 0));
        });
        SelfTest.test("18 keep capacity above active count", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(0, 2);
            fixture.registrations.register(fixture.attendee("Reyes", "Ana", LocalDate.of(2005, 1, 1)), event);
            fixture.registrations.register(fixture.attendee("Cruz", "Ben", LocalDate.of(2005, 1, 1)), event);
            SelfTest.expectError("Capacity", () -> fixture.events.edit(fixture.manager, event.getId(), "Fair", "Description", FUTURE, "Hall", "Lead", 0, 1, fixture.registrations.countFor(event.getId())));
        });
        SelfTest.test("19 lock eligibility rules after registration", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(0, 2);
            fixture.registrations.register(fixture.attendee("Reyes", "Ana", LocalDate.of(2005, 1, 1)), event);
            SelfTest.expectError("locked", () -> fixture.events.edit(fixture.manager, event.getId(), "Fair", "Description", FUTURE.plusDays(1L), "Hall", "Lead", 18, 2, 1));
        });
        SelfTest.test("20 one account can join two events", () -> {
            Fixture fixture = new Fixture();
            Attendee attendee = fixture.attendee("Reyes", "Ana", LocalDate.of(2005, 1, 1));
            Event event = fixture.event(0, 2);
            Event event2 = fixture.events.create(fixture.manager, "Science Day", "Projects", FUTURE.plusDays(1L), "Lab", "Lead", 0, 2);
            fixture.registrations.register(attendee, event);
            fixture.registrations.register(attendee, event2);
            SelfTest.check(fixture.registrations.forAttendee(attendee).size() == 2);
            SelfTest.check(attendee.getUsername().equals("Reyes,Ana"));
        });
        SelfTest.test("21 reject incorrect main menu selection", () -> {
            String string = SelfTest.runConsole("9\n0\n");
            SelfTest.check(string.contains("Invalid menu choice.") && string.contains("Goodbye."));
        });
        SelfTest.test("22 retry empty required input", () -> {
            String string = SelfTest.runConsole("2\n1\n\nlead\nClub Lead\nsecret1\n0\n0\n");
            SelfTest.check(string.contains("This field cannot be empty."));
            SelfTest.check(string.contains("Created Event Manager account: lead"));
        });
        SelfTest.test("23 accept upper age and reasonable capacity boundary", () -> {
            Fixture fixture = new Fixture();
            Event event = fixture.event(120, 1000);
            SelfTest.check(event.getMinimumAge() == 120 && event.getCapacity() == 1000);
        });
        SelfTest.test("24 reject age rule above maximum", () -> {
            Fixture fixture = new Fixture();
            SelfTest.expectError("details", () -> fixture.event(121, 1000));
        });
        System.out.println("RESULT: " + passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void test(String string, Runnable runnable) {
        try {
            runnable.run();
            ++passed;
            System.out.println("PASS " + string);
        }
        catch (Throwable throwable) {
            ++failed;
            System.out.println("FAIL " + string + " - " + String.valueOf(throwable));
        }
    }

    private static void check(boolean bl) {
        if (!bl) {
            throw new AssertionError((Object)"Condition was false");
        }
    }

    private static void expectError(String string, Runnable runnable) {
        try {
            runnable.run();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            if (illegalArgumentException.getMessage().contains(string)) {
                return;
            }
            throw new AssertionError((Object)("Wrong error: " + illegalArgumentException.getMessage()));
        }
        throw new AssertionError((Object)("Expected error containing: " + string));
    }

    private static String runConsole(String string) {
        try {
            String string2 = Path.of(System.getProperty("java.home"), "bin", "java").toString();
            String classDirectory = Path.of(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
            Process process = new ProcessBuilder(string2, "-cp", classDirectory, Main.class.getName()).redirectErrorStream(true).start();
            try (OutputStream stream = process.getOutputStream()) {
                stream.write(string.getBytes(StandardCharsets.UTF_8));
            }
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            if (process.waitFor() != 0) {
                throw new AssertionError("Main process failed: " + output);
            }
            return output;
        }
        catch (Exception exception) {
            throw new AssertionError("Could not run console test", exception);
        }
    }

    private static final class Fixture {
        final AccountStore accounts = new AccountStore();
        final EventManager manager = this.accounts.addManager("lead", "Club Lead", "secret1");
        final EventService events = new EventService(CLOCK);
        final RegistrationService registrations = new RegistrationService(new StandardRegistrationPolicy(), CLOCK);

        private Fixture() {
        }

        Event event(int n, int n2) {
            return this.events.create(this.manager, "School Fair", "Club exhibits", FUTURE, "Main Hall", "Club Lead", n, n2);
        }

        Attendee attendee(String string, String string2, LocalDate localDate) {
            return this.accounts.addAttendee(string, string2, localDate, "secret1");
        }
    }
}
