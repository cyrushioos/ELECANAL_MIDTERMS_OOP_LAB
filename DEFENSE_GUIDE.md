# Short code defense guide

Use these as study prompts. Explain the code in your own words during any individual defense.

- **Why is `Account` abstract?** Both account types share an ID, username, and password check. Their role names differ, so each subclass supplies `getRoleName()`.
- **Where is inheritance?** `Attendee extends Account` and `EventManager extends Account`. Their constructors use `super(...)` to initialize the shared account fields.
- **Where is polymorphism?** `AccountStore` stores both kinds as `Account` objects. Calling `getRoleName()` on an `Account` reference runs the matching subclass method.
- **Why use an interface?** `RegistrationService` asks a `RegistrationPolicy` whether a registration is allowed. `StandardRegistrationPolicy` contains the current school-event rules. The service can accept a different policy later.
- **Why are fields private?** Other classes should not change an event's capacity or status by changing a field directly. Methods validate those changes.
- **How is age checked?** `Attendee.ageOn(event date)` calculates age on the event day; the policy compares it to the event's minimum age.
- **How is capacity enforced?** The policy counts active registrations for the event and rejects a new one when the count reaches capacity. `RegistrationService.countFor` derives the displayed count from active registrations.
- **Why does one attendee have several registration labels?** The account username is `Lastname,Firstname`. Each registration label adds the event title, so the account remains stable across events.
- **What happens after event cancellation?** The event status changes to CANCELED and the policy rejects new registrations. Existing records remain visible for review.
- **Which tests show error handling?** Cases 3, 4, 6-8, 10-12, 15-19, 21, 22, and 24 cover invalid or rejected operations.
- **What design issue was fixed?** A single account name containing an event name would be inconsistent if one person joined two events. The event suffix was moved to the registration label.

To demonstrate the program, run `Main`, create a manager and an event, then create an attendee, browse and register, attempt a duplicate registration, cancel it, and show the manager's count. Run `SelfTest` to show all 24 cases pass.
