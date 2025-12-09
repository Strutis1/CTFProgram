# JavaFX CTF – Everything Happens for a Reason

This is a small single-player CTF (Capture the Flag) game built with JavaFX.

The application presents **10 tasks**, each on its own tab. Your goal is to discover and submit a valid flag for every task.

All flags follow the general format:

```text
[FL4G]:something_here
```

---

## What this is

* A self-contained desktop CTF app.
* 10 tasks with gradually increasing difficulty.
* Puzzles that may involve:

    * the graphical interface,
    * exported files,
    * and the way the program behaves at runtime.
* A built-in “terminal” area with simple commands to track and manage progress.

You get the full source code. Exploring it is allowed and sometimes helpful.

---

## Requirements

* Java **21**
* JavaFX **21** (SDK/runtime)
* Any modern OS
  (developed on Linux; also runnable on Windows with JavaFX configured)

---

## Building

The project uses **Maven**.

From the project root:

```bash
mvn clean package
```

This produces:

```text
target/CTFProgram-1.0-SNAPSHOT.jar
```

The same JAR can be used on Linux and Windows.

---

## Running from IntelliJ IDEA (or another IDE)

### IntelliJ IDEA

1. **Open the project**

    * `File → New → Project from Existing Sources…`
    * Select the project folder (the one containing `pom.xml`).
    * Choose **“Import project from external model → Maven”**.

2. **Select SDK**

    * Make sure the project SDK is set to **Java 21**
      (`File → Project Structure → Project`).

3. **Run configuration**

    * In the Project view, locate the class
      `com.kiber.ctfprogram.Launcher`
      (or `CTFApplication` if that’s your entry point).
    * Right-click the class → **Run 'Launcher'**
      (or create a run configuration with that main class).

   IntelliJ uses the Maven dependencies (including JavaFX) on the classpath, so in most setups this will just work.
   If you ever need VM options manually, you can add:

   ```text
   --add-modules javafx.controls,javafx.fxml
   ```

   in the run configuration’s **VM options** field.

4. **Alternative: run via Maven inside IntelliJ**

    * Open the **Maven** tool window.
    * Run the goal: `javafx:run`.

### Other IDEs

* Import the project as a **Maven** project.
* Set JDK to **21**.
* Create a run configuration with the main class:

  ```text
  com.kiber.ctfprogram.Launcher
  ```

  (or your actual entry class), and ensure JavaFX modules are available on the classpath/module-path.

---

## Running the JAR (Linux & Windows)

You can also run the built JAR directly.

### Linux

Assuming JavaFX SDK is unpacked at `/opt/javafx-sdk-21.0.6`:

```bash
java \
  --module-path /opt/javafx-sdk-21.0.6/lib \
  --add-modules javafx.controls,javafx.fxml \
  -jar target/CTFProgram-1.0-SNAPSHOT.jar
```

### Windows

Assuming JavaFX SDK is at `C:\javafx-sdk-21.0.6` and the jar is in the current directory:

```bat
java ^
  --module-path "C:\\javafx-sdk-21.0.6\\lib" ^
  --add-modules javafx.controls,javafx.fxml ^
  -jar CTFProgram-1.0-SNAPSHOT.jar
```

Adjust paths as needed.

---

## How to Play

* Each tab in the main window is **one task**.
* Every task has:

    * a title,
    * an input field for a flag,
    * a **Submit** button,
    * a **Show hint** button (short nudge, not a full solution).
* Some tasks provide an **Export file** button. That exported file is part of the puzzle and is meant to be inspected with external tools of your choice.
* The terminal at the bottom supports commands such as:

    * `/help` – list available commands
    * `/completed` – list tasks you have already solved
    * `/reset` – reset all task progress
    * `/finalize` – check if you’re done

You can close the app and come back later; task completion state is stored locally.

---

## Flags

* All flags start with:

  ```text
  [FL4G]:
  ```

* The rest of the flag depends on the task.

* Enter the full flag (including `[FL4G]:`) into the task’s input and press **Submit**.

---

## External Tools

You are free to use whatever tools you like.
Depending on how you approach the challenges, you might find it helpful to use:

* a text editor,
* command-line utilities,
* viewers/parsers for exported files,
* and other common CTF-style tools.

No specific tool is strictly required; multiple approaches may work.

---


## Modifying / Extending

If you add or change tasks:

* Update titles, hints, and any per-task text in the controller.
* Adjust the validation logic as needed.
* Add or modify resources under `src/main/resources/com/kiber/ctfprogram/` (images, task files, etc.).

---

Enjoy, poke at everything, and assume that nothing in the app is purely accidental.
