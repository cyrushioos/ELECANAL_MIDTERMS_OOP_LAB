# Video presentation script (about 10 minutes)

**Before recording:** Replace `[your name]`, `[your section]`, and the marked personal reflection with your own details and honest experience. Read and practice the code first. Open the project in NetBeans and have `uml-class-diagram.png`, `Main.java`, `Account.java`, `Attendee.java`, `EventManager.java`, `StandardRegistrationPolicy.java`, `RegistrationService.java`, `SelfTest.java`, and `test-cases.csv` ready. Increase the NetBeans editor and output font so viewers can read them. Record your screen and your own spoken explanation; a script alone is not the required video.

The program keeps accounts and events in memory. Start a fresh run for the demonstration, and use the future event date below only if it is still in the future when you record. Do not show a real password; the sample password is `secret1`.

## 0:00–1:00 — Introduction

**Show:** Report cover, then the NetBeans project.

**Say:**

> Good day. I am [your name] from [your section]. My project is **School Organization Event Manager**, a Java console application. School organizations need a simple way to keep event details and registration counts organized. Event managers create, edit, cancel, and review their events. Attendees use accounts to browse events, register, and cancel registrations. I selected this problem because school events involve two clear user roles and rules that are useful to model with object-oriented programming: age limits, event capacity, duplicate prevention, and event status. The application is a student prototype. Its data exists only while the program is running; it does not use a database.

## 1:00–3:00 — Design and UML

**Show:** `uml-class-diagram.png`. Point to each class while speaking.

**Say:**

> The abstract `Account` class contains the shared account ID, username, and password-checking behavior. `Attendee` and `EventManager` extend it. This is an **is-a** relationship: an attendee is an account, and a manager is an account. The two subclasses add information specific to their roles. An attendee stores a first name, last name, and birth date. A manager stores a display name. Each overrides `getRoleName()`.
>
> `Event` stores its manager ID, title, description, date and time, location, leader, minimum age, capacity, and status. `Registration` connects an attendee to an event and records whether the registration is active. The service classes organize operations: `AccountStore` handles accounts and login, `EventService` handles event creation and changes, and `RegistrationService` handles registration, cancellation, and counts. The UML multiplicities show that one manager can own many events and one attendee can have many registrations.
>
> `RegistrationPolicy` is an interface. `StandardRegistrationPolicy` implements the current rules. Keeping the rules behind an interface means the registration service can use the policy without containing every eligibility condition itself. The diagram also shows private fields with minus signs and public methods with plus signs. This helps protect data from uncontrolled changes.

**Point out:** The account username is `Lastname,Firstname`. The `-Event Title` suffix belongs to a **registration label**, so one account can join multiple events.

## 3:00–6:00 — Code walkthrough

**Show:** The actual source files in NetBeans. Scroll to the named methods; do not read every line.

**Say:**

> In `Main.java`, `main()` creates a `Main` object and starts `run()`. The `Scanner` reads console input. The `while` loops keep the main, manager, and attendee menus active until the user chooses back, logout, or exit. `switch` conditions route each menu choice. The `required()` and `readInt()` methods check input and ask again when it is blank or outside the allowed range.
>
> The `Main` object creates the account store, event service, and registration service. Notice that the registration service receives a `StandardRegistrationPolicy` through its `RegistrationPolicy` interface. That is object creation and dependency passing through a constructor.
>
> In `Account.java`, the ID, username, and password are private. The constructor validates them, and other classes can use controlled methods such as `getUsername()` and `matchesPassword()`. They cannot directly change the fields. In `Attendee.java` and `EventManager.java`, the constructors call `super(...)` to initialize the shared account data. Both override `getRoleName()`. `AccountStore` can hold both subclasses as `Account` objects, and the correct role-name method runs for the actual object. That is polymorphism. The abstract `Account` class supplies the common structure while leaving role-specific behavior to subclasses.
>
> In `StandardRegistrationPolicy.java`, the conditions reject a canceled event, an event that has started, an attendee below the minimum age, a duplicate active registration, or a full event. The attendee's age is calculated on the **event date**, which matters near a birthday. `RegistrationService.register()` asks the policy for a rejection reason before adding a record. `countFor()` counts active registration records. Therefore the displayed count changes when a registration is canceled; it is not a manually edited number.
>
> In `Event.java`, event fields are private and `update()` validates the information. `EventService` also checks that a manager owns the event before editing it. This separates the event's data rules from who is authorized to change it.

## 6:00–9:00 — Live program demonstration

**Show:** Run `Main.java` in NetBeans with **Shift+F6**. Speak while entering the following sample values. Start with a fresh program run; IDs then begin at 1.

1. At the main menu, enter `9`. Show **Invalid menu choice** and the menu appearing again.
2. Enter `2` for Event Manager, then `1` to create an account. Leave the first **Username** prompt blank once; show **This field cannot be empty**. Then enter username `lead`, display name `Organization Lead`, and password `secret1`.
3. Enter `2` to log in as `lead` with `secret1`. In the manager menu choose `1` to create an event. Enter title `School Fair`, description `Student club activities`, start `2030-05-15 09:00`, location `School Hall`, leader `Organization Lead`, minimum age `18`, and capacity `1`. Show the created event ID.
4. Choose `4` for **My events**. Point to the event details and **Registered: 0/1**. Choose `0` to log out, then `0` to go back to the main menu.
5. Enter `1` for Attendee, then `1` to create an account: last name `Reyes`, first name `Ana`, birth date `2005-05-01`, password `secret1`. Show that the account username is `Reyes,Ana`.
6. Enter `2` to log in with username `Reyes,Ana` and password `secret1`. Choose `1` to browse. Choose `2`, enter event ID `1`, and show successful registration as `Reyes,Ana-School Fair`.
7. Choose `2` and event ID `1` **again**. Show the duplicate-registration error. Choose `3` and event ID `1` to cancel. Choose `4` to show no active registrations. Optionally choose `2` and ID `1` again to show that registration is allowed after cancellation.
8. If time permits, log out, return to the manager account, and choose `4` or `5` to show the active count or registrant list. If step 7 ended with a cancellation, the count is `0/1`; if you registered again, it is `1/1`.

**Say during the run:**

> The invalid menu choice and blank input show input handling. The manager creates the event and sees its details and count. The attendee uses a separate role menu. The first registration succeeds because this attendee meets the age rule and a place is available. The second attempt fails because it would duplicate an active registration. Cancellation changes which registrations are active, so the count reflects the records. One account can participate in multiple events because the event name appears in each registration label, not in the account username.

If the event date has passed when you record, use another future date. If the chosen date changes, ensure Ana is at least 18 **on that date**.

## 9:00–10:30 — Testing and reflection

**Show:** `test-cases.csv`, then run `SelfTest.java` with **Shift+F6** in NetBeans. Show `RESULT: 24 passed, 0 failed` and `BUILD SUCCESS`.

**Say:**

> The assignment requires at least 10 test cases and more than normal inputs. I documented 24 cases with input, expected result, actual result, and pass or fail. They cover valid account and event creation, capacity boundaries, invalid and empty input, a wrong menu choice, both account subclasses, age limits, duplicate registration, event cancellation, ownership, and repeated register-cancel-register behavior. `SelfTest` uses a fixed clock for repeatable age and time comparisons. The live menu checks and the automated class checks complement each other.
>
> One actual development problem was restoring the Java source after the earlier output folder was lost. The recovered `Main.java` had unreachable `break` statements, and `SelfTest.java` had an invalid try-with-resources statement. Compiler errors identified those lines. The source was repaired, compiled for Java 17, and the 24 tests passed in NetBeans. ChatGPT helped locate and explain the errors, but I checked the result by compiling and running the tests. **Do not present this as a bug you personally fixed unless you have reviewed these files and can explain the repair.**
>
> I also changed one proposed design: putting `-Event` in the attendee **account** name would give the same person different usernames for different events. I kept a stable account name and put the event suffix on each registration label. [Add one or two sentences here about what **you personally learned**. For example, explain how you now distinguish inheritance from a registration relationship, but use your own words.] Thank you.

## Final recording check

- Keep the final recording between 8 and 12 minutes; the times above total about 10 minutes 30 seconds.
- Show the real NetBeans project, source, UML, live run, and test result on screen.
- Speak in your own words. Be ready to answer questions about the code and modify it.
- Submit the recording separately together with the report, source, UML, tests, screenshots, README, and complete ChatGPT conversation.
