package com.mycompany.event_organizer;


import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public final class Main {
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
    private final Scanner scanner = new Scanner(System.in);
    private final AccountStore accounts = new AccountStore();
    private final Clock clock = Clock.systemDefaultZone();
    private final EventService events = new EventService(this.clock);
    private final RegistrationService registrations = new RegistrationService(new StandardRegistrationPolicy(), this.clock);

    public static void main(String[] stringArray) {
        new Main().run();
    }

    private void run() {
        System.out.println("SCHOOL ORGANIZATION EVENT MANAGER");
        block10: while (true) {
            String string;
            System.out.println("\nMain menu: 1 Attendee  2 Event Manager  0 Exit");
            switch (string = this.read("Choose: ")) {
                case "1": {
                    this.roleMenu(Attendee.class);
                    continue block10;
                }
                case "2": {
                    this.roleMenu(EventManager.class);
                    continue block10;
                }
                case "0": {
                    System.out.println("Goodbye.");
                    return;
                }
            }
            System.out.println("Invalid menu choice.");
        }
    }

    private void roleMenu(Class<? extends Account> clazz) {
        block10: while (true) {
            System.out.println("\n" + (clazz == Attendee.class ? "Attendee" : "Event Manager") + ": 1 Create account  2 Log in  0 Back");
            switch (this.read("Choose: ")) {
                case "1": {
                    this.createAccount(clazz);
                    continue block10;
                }
                case "2": {
                    this.login(clazz);
                    continue block10;
                }
                case "0": {
                    return;
                }
            }
            System.out.println("Invalid menu choice.");
        }
    }

    private void createAccount(Class<? extends Account> clazz) {
        try {
            Account account;
            if (clazz == Attendee.class) {
                String string = this.required("Last name: ");
                String string2 = this.required("First name: ");
                LocalDate localDate = this.readDate("Birth date (yyyy-MM-dd): ");
                String string3 = this.required("Password (at least 6 characters): ");
                account = this.accounts.addAttendee(string, string2, localDate, string3);
            } else {
                String string = this.required("Username: ");
                String string4 = this.required("Display name: ");
                String string5 = this.required("Password (at least 6 characters): ");
                account = this.accounts.addManager(string, string4, string5);
            }
            System.out.println("Created " + ((Account)account).getRoleName() + " account: " + account.getUsername());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Error: " + illegalArgumentException.getMessage());
        }
    }

    private void login(Class<? extends Account> clazz) {
        String string;
        String string2 = this.required("Username: ");
        Account account = this.accounts.login(string2, string = this.required("Password: "), clazz);
        if (account == null) {
            System.out.println("Login failed.");
            return;
        }
        System.out.println("Welcome, " + account.getUsername() + " (" + account.getRoleName() + ").");
        if (account instanceof Attendee) {
            Attendee attendee = (Attendee)account;
            this.attendeeMenu(attendee);
        } else if (account instanceof EventManager) {
            EventManager eventManager = (EventManager)account;
            this.managerMenu(eventManager);
        }
    }

    private void managerMenu(EventManager eventManager) {
        block18: while (true) {
            System.out.println("\nManager: 1 Create event  2 Edit event  3 Cancel event  4 My events  5 Registrants  0 Log out");
            try {
                switch (this.read("Choose: ")) {
                    case "1": {
                        EventInput eventInput = this.eventInput();
                        Object object = this.events.create(eventManager, eventInput.title, eventInput.description, eventInput.start, eventInput.location, eventInput.leader, eventInput.minimumAge, eventInput.capacity);
                        System.out.println("Created event #" + ((Event)object).getId() + ".");
                        continue block18;
                    }
                    case "2": {
                        int n = this.readInt("Event ID: ", 1, Integer.MAX_VALUE);
                        Object object = this.eventInput();
                        this.events.edit(eventManager, n, ((EventInput)object).title, ((EventInput)object).description, ((EventInput)object).start, ((EventInput)object).location, ((EventInput)object).leader, ((EventInput)object).minimumAge, ((EventInput)object).capacity, this.registrations.countFor(n));
                        System.out.println("Event updated.");
                        continue block18;
                    }
                    case "3": {
                        int n = this.readInt("Event ID: ", 1, Integer.MAX_VALUE);
                        this.events.cancel(eventManager, n);
                        System.out.println("Event canceled.");
                        continue block18;
                    }
                    case "4": {
                        this.showEvents(this.events.byManager(eventManager));
                        continue block18;
                    }
                    case "5": {
                        this.showRegistrants(eventManager);
                        continue block18;
                    }
                    case "0": {
                        return;
                    }
                }
                System.out.println("Invalid menu choice.");
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Error: " + illegalArgumentException.getMessage());
                continue;
            }
        }
    }

    private void attendeeMenu(Attendee attendee) {
        block16: while (true) {
            System.out.println("\nAttendee: 1 Browse events  2 Register  3 Cancel registration  4 My registrations  0 Log out");
            try {
                switch (this.read("Choose: ")) {
                    case "1": {
                        this.showEvents(this.events.all());
                        continue block16;
                    }
                    case "2": {
                        int n = this.readInt("Event ID: ", 1, Integer.MAX_VALUE);
                        Event event = this.events.find(n);
                        if (event == null) {
                            throw new IllegalArgumentException("Event not found.");
                        }
                        Registration registration = this.registrations.register(attendee, event);
                        System.out.println("Registered as " + registration.label(attendee, event) + ".");
                        continue block16;
                    }
                    case "3": {
                        int n = this.readInt("Event ID: ", 1, Integer.MAX_VALUE);
                        this.registrations.cancel(attendee, this.events.find(n));
                        System.out.println("Registration canceled.");
                        continue block16;
                    }
                    case "4": {
                        this.showMyRegistrations(attendee);
                        continue block16;
                    }
                    case "0": {
                        return;
                    }
                }
                System.out.println("Invalid menu choice.");
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Error: " + illegalArgumentException.getMessage());
                continue;
            }
        }
    }

    private void showEvents(List<Event> list) {
        if (list.isEmpty()) {
            System.out.println("No events found.");
            return;
        }
        for (Event event : list) {
            System.out.println("\n#" + event.getId() + " " + event.getTitle() + " [" + String.valueOf((Object)event.getStatus()) + "]");
            System.out.println("  " + event.getDescription());
            System.out.println("  When: " + event.getStart().format(DATE_TIME) + " | Where: " + event.getLocation());
            System.out.println("  Leader: " + event.getLeader() + " | Minimum age: " + event.getMinimumAge());
            System.out.println("  Registered: " + this.registrations.countFor(event.getId()) + "/" + event.getCapacity());
        }
    }

    private void showRegistrants(EventManager eventManager) {
        int n = this.readInt("Event ID: ", 1, Integer.MAX_VALUE);
        Event event = this.events.find(n);
        if (event == null || event.getManagerId() != eventManager.getId()) {
            throw new IllegalArgumentException("Event not found in your account.");
        }
        List<Registration> list = this.registrations.forEvent(n);
        System.out.println("Registrants for " + event.getTitle() + " (" + list.size() + "):");
        for (Registration registration : list) {
            Attendee attendee = this.accounts.findAttendee(registration.getAttendeeId());
            System.out.println("  " + registration.label(attendee, event));
        }
    }

    private void showMyRegistrations(Attendee attendee) {
        List<Registration> list = this.registrations.forAttendee(attendee);
        if (list.isEmpty()) {
            System.out.println("No active registrations.");
            return;
        }
        for (Registration registration : list) {
            Event event = this.events.find(registration.getEventId());
            System.out.println("  #" + event.getId() + " " + registration.label(attendee, event) + " [" + String.valueOf((Object)event.getStatus()) + "]");
        }
    }

    private EventInput eventInput() {
        String string = this.required("Title: ");
        String string2 = this.required("Description: ");
        LocalDateTime localDateTime = this.readDateTime("Start (yyyy-MM-dd HH:mm): ");
        String string3 = this.required("Location: ");
        String string4 = this.required("Leader: ");
        int n = this.readInt("Minimum age (0-120): ", 0, 120);
        int n2 = this.readInt("Capacity (1 or more): ", 1, Integer.MAX_VALUE);
        return new EventInput(string, string2, localDateTime, string3, string4, n, n2);
    }

    private String read(String string) {
        System.out.print(string);
        if (!this.scanner.hasNextLine()) {
            System.out.println("\nInput closed. Goodbye.");
            System.exit(0);
        }
        return this.scanner.nextLine().trim();
    }

    private String required(String string) {
        String string2;
        while ((string2 = this.read(string)).isBlank()) {
            System.out.println("This field cannot be empty.");
        }
        return string2;
    }

    private int readInt(String string, int n, int n2) {
        while (true) {
            try {
                int n3 = Integer.parseInt(this.read(string));
                if (n3 >= n && n3 <= n2) {
                    return n3;
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            System.out.println("Enter a whole number from " + n + " to " + n2 + ".");
        }
    }

    private LocalDate readDate(String string) {
        while (true) {
            try {
                return LocalDate.parse(this.read(string));
            }
            catch (DateTimeParseException dateTimeParseException) {
                System.out.println("Enter a valid date in yyyy-MM-dd format.");
                continue;
            }
        }
    }

    private LocalDateTime readDateTime(String string) {
        while (true) {
            try {
                return LocalDateTime.parse(this.read(string), DATE_TIME);
            }
            catch (DateTimeParseException dateTimeParseException) {
                System.out.println("Enter a valid date and time in yyyy-MM-dd HH:mm format.");
                continue;
            }
        }
    }

    private record EventInput(String title, String description, LocalDateTime start, String location, String leader, int minimumAge, int capacity) {
    }
}
