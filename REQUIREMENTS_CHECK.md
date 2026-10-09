# Requirement check

In this NetBeans project, Java source references such as `src/Main.java` below refer to files in `src/main/java/com/mycompany/event_organizer/`.

The project meets the implementable program and documentation requirements. It is **not a complete personal submission** until the student adds the items at the end of this file.

| Assignment requirement | Evidence in this package | Status |
|---|---|---|
| Specific real-world problem and users | `report.pdf`, page 2: school organization events, managers, and attendees | Done |
| Problem statement, objectives, scope, limitations | `report.pdf`, page 2 | Done as a draft for student review |
| OOP analysis before implementation | `report.pdf`, page 3 class responsibilities; page 4 UML; this chat's earlier design discussion | Documented |
| Java console input, conditions, loops | `src/Main.java`: Scanner input and repeated role menus; `src/StandardRegistrationPolicy.java` conditions | Done |
| Classes, objects, fields, constructors, methods | `src/` contains 13 Java classes/interfaces/enum; `src/Main.java` creates the services and accounts | Done |
| Encapsulation and controlled access | Private fields in `Account`, `Event`, and `Registration`; validation in their methods and services | Done |
| Inheritance, `extends`, `super`, overriding, polymorphism | `Attendee` and `EventManager` extend abstract `Account`, call `super`, and override `getRoleName()`; `Account` references are used in `Main` and `AccountStore` | Done |
| Abstraction and interface | Abstract `Account` and `RegistrationPolicy` interface, implemented by `StandardRegistrationPolicy` | Done |
| UML with visibility, relationships, multiplicity | `uml-class-diagram.mmd` and `uml-class-diagram.png`; report page 4 | Done |
| Event management and registration rules | `EventService`, `RegistrationService`, `StandardRegistrationPolicy` | Done |
| At least 10 tests, with expected/actual/results | `test-cases.csv` and report pages 7-8 contain 24 cases | Done |
| Invalid, empty, menu, repeated, subclass, and boundary tests | `src/SelfTest.java` cases 2, 5-13, 21-24 | Done |
| Run and screenshot in NetBeans | `evidence/netbeans-24-tests-passed.png`: 24 passed, 0 failed, BUILD SUCCESS; report pages 9-11 | Done |
| PDF report with requested sections | `report.pdf` | Done as a draft for student review |
| README compile/run instructions | `README.md` and `pom.xml` | Done |
| 8–12 minute video presentation plan | `VIDEO_PRESENTATION_SCRIPT.md` covers all five requested sections with live NetBeans steps | Script done; student recording required |

## Student work still required

1. Add your name and section to the report. Rewrite its marked personal reflection in your own words.
2. Review the Java source and practice explaining each class and the rules. The individual defense requires your own understanding.
3. Submit the **complete** ChatGPT conversation or share link, including any earlier brainstorming chat. A report summary or selected screenshots are not a substitute.
4. Record the required 8–12 minute demonstration yourself using `VIDEO_PRESENTATION_SCRIPT.md`. The script is a guide, not a recording or evidence of your personal understanding.
5. Confirm that the initial brainstorming chronology in your exported conversation shows your own choice of problem and evaluation of at least three AI suggestions. That history cannot be recreated retroactively by a report.

The restored source compiles with `javac --release 17`; its 24-case suite passed from the command line and in NetBeans. The source was recovered from compiled class files after the earlier output directory disappeared, then repaired and checked. Review that recovery before submission.
