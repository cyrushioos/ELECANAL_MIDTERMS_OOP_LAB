# School Organization Event Manager

This is the **current working project folder**. Open this folder as a Maven project in Apache NetBeans. The Java console application uses Java 17 or newer and has no external code libraries.

## Run in NetBeans

1. Open `EVENT_ORGANIZER` in NetBeans.
2. Choose **Clean and Build** if NetBeans still shows an older build result.
3. Choose **Run Project** to start the main menu. Maven's main class is `com.mycompany.event_organizer.Main`.
4. To run the 24-case self-test, open `src/main/java/com/mycompany/event_organizer/SelfTest.java` and choose **Run File** (Shift+F6), or run the Maven command below.

## Run from PowerShell

From this folder, with Maven available:

```powershell
mvn clean compile
mvn exec:java
mvn '-Dexec.mainClass=com.mycompany.event_organizer.SelfTest' exec:java
```

The self-test should finish with `RESULT: 24 passed, 0 failed` and `BUILD SUCCESS`.

## Included files

- `src/main/java/com/mycompany/event_organizer/`: 13 Java source files in the `com.mycompany.event_organizer` package.
- `pom.xml`: Maven build and run settings.
- `report.pdf`: documentation and NetBeans screenshots.
- `uml-class-diagram.png` and `.mmd`: class diagram.
- `test-cases.csv`: 24 documented test cases.
- `evidence/`: console and NetBeans screenshots.
- `VIDEO_PRESENTATION_SCRIPT.md`: 8–12 minute recording guide.
- `REQUIREMENTS_CHECK.md` and `DEFENSE_GUIDE.md`: checklist and study notes.

Accounts and events exist only during one application run. Use sample passwords, not real passwords. An attendee account uses `Lastname,Firstname`; each registration label adds `-Event Title`.

Before submission, add your name and section, write the personal reflection in your own words, record your demonstration, and include the complete ChatGPT conversation. Review and understand the restored source code before presenting it.
