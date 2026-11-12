# Run on Linux

The program is already built. The other Linux machine does not need Maven, and it does not need to compile. It needs Java, and a desktop so the window can open.

The file to run is `run/java-ui.jar`. It is a jar, not a Windows `.exe`. A `.exe` would not start on Linux. This jar starts with the `java` command that is already installed there.

The class files inside the jar are Java 6. Java 6 and every newer Java can run them.

---

## Start it

Open a terminal on the Linux machine, go to this project, and run:

```bash
cd /path/to/java-ui
java -jar run/java-ui.jar
```

Replace `/path/to/java-ui` with the folder you cloned or copied. The Engagement Parameters window opens. The function keys are along the bottom.

Check that Java is the one the terminal will use:

```bash
java -version
```

If that prints a version, the command above is the right one. If the shell says `java: command not found`, Java is installed but not on the path. Use the full path, for example:

```bash
/usr/bin/java -jar run/java-ui.jar
```

Find it with:

```bash
which java
```

---

## What has to be true on that machine

* Java is installed. `java -version` prints a version. Java 6 or newer is enough.
* You are at a graphical login. The program is a window. A machine with no screen, or an SSH session that has no display, cannot open it. The error looks like `HeadlessException` or `Can't connect to X11 window server`.
* The jar is in the git checkout. It lives at `run/java-ui.jar`. `target/` is not in git. Do not look there on the other machine.

Nothing else is required. There is no database, no extra jar, and no install step.

---

## Putting the jar in git

`run/java-ui.jar` is the copy that travels with the source. After you change the Java and rebuild on a machine that has Maven, copy the new jar over the old one before you commit:

```bash
mvn -q package
cp -f target/java-ui-1.0.jar run/java-ui.jar
```

Then commit `run/java-ui.jar` with the source. The other machine gets the new program on the next pull, and runs the same `java -jar` command.
