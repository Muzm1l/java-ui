# Java UI — what each part of the code does

This project is a practice screen you can copy into a larger Java 1.6 program.

| File | Job |
|---|---|
| `src/main/java/page/FunctionKeyBar.java` | The footer. Draws the ten buttons. Does not decide what they do. |
| `src/main/java/page/FunctionPage.java` | The window. Owns the one listener and the functions (`create()` and the rest). |
| `src/main/java/page/WeaponConfigurationPanel.java` | The black form on the right. Built from the pieces in `page/form/`. |
| `src/main/java/page/form/` | The reusable rows, sections, and option groups. |
| `pom.xml` | Tells Maven to compile this as Java 1.6 and which class to start. |
| `.gitignore` | Tells git which generated files to leave out of the repository. |

The footer is a separate component. The screen passes in the size and the listener. When you join this to an existing window, you keep `FunctionKeyBar` as it is and move the listener into that window.

```text
You press F1 or click the F1 button
        |
        v
FunctionKeyBar   (same listener for every button)
        |
        v
FunctionPage.actionPerformed
        |
        v
create()          (only F1 goes here)
```

---

## 1. `FunctionKeyBar.java` — the footer

### `package page;`

Every Java file starts with the package. It is the folder the class lives in: `src/main/java/page/`.

When you paste these files into the other project, change this one line so it matches that project’s package. Both files must use the same package name.

### The import lines

An import lets this file use a class that lives in another package. Nothing here is downloaded. These all come with Java.

| Import | What it is used for |
|---|---|
| `BorderLayout` | Puts the button row along the bottom of the footer. |
| `Color` | The blue background and the black button border. |
| `Dimension` | Width and height in pixels. |
| `FlowLayout` | Lays the ten buttons in one left-to-right row. |
| `ActionEvent` | The object Swing sends when a button or a key fires. |
| `ActionListener` | The type of the one listener the screen passes in. |
| `KeyEvent` | The codes for the real F1–F10 keys. |
| `AbstractAction` | A small action object used to connect a keyboard key to the listener. |
| `BorderFactory` | Builds the black line around each button. |
| `JButton` | One function button. |
| `JComponent` | Needed for the “key works anywhere in the window” setting. |
| `JPanel` | A plain rectangular panel. The footer is a panel. |
| `KeyStroke` | “The F1 key, with no Shift or Ctrl”. |

### `public class FunctionKeyBar extends JPanel`

`FunctionKeyBar` is a `JPanel`. A panel is an empty rectangle that can hold other components. Because the footer *is* a panel, the screen can drop it in with `add(footer, BorderLayout.SOUTH)` the same way it adds any other panel.

`public` means other classes, including `FunctionPage`, are allowed to create it.

### `serialVersionUID`

```java
private static final long serialVersionUID = 1L;
```

Swing components can be saved to disk. Java asks every such class for a version number. `1L` means “version 1”. You will see this line all over an old Swing codebase. You do not call it. Leave it as it is.

`private` — only this class sees it.
`static` — it belongs to the class, not to one footer.
`final` — it never changes.

### `KEYS` — the ten button labels

```java
public static final String[] KEYS = {
    "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F0"
};
```

This is the list of labels, left to right. There are always ten slots, even if a function is still empty.

The same string is used for three things:

1. The text painted on the button.
2. The action command the listener reads.
3. The name used when a keyboard key fires.

`public` so the screen can see the list. In this project the screen does not need to; the listener compares the strings `"F1"`, `"F2"`, and so on directly.

### `KEY_CODES` — the real keyboard keys

```java
private static final int[] KEY_CODES = {
    KeyEvent.VK_F1,
    ...
    KeyEvent.VK_F10
};
```

`VK_F1` means “the F1 key on the keyboard”. A keyboard has F1 through F12. It does not have a key named F0.

Slot 0 of `KEYS` is `"F1"` and slot 0 of `KEY_CODES` is `VK_F1`. They stay in pairs. The last pair is the label `"F0"` with the physical key `VK_F10`.

`private` because only the footer needs the key codes. The screen talks in labels (`"F1"`, `"F0"`), not in `VK_` numbers.

### `barHeight`

```java
private final int barHeight;
```

The screen tells the footer how tall it should be. `final` means the constructor sets it once and nothing changes it later. The three `get*Size` methods at the bottom of the class read this field so the layout manager honours that height.

### The constructor

```java
public FunctionKeyBar(int barHeight, int buttonWidth, int buttonHeight, ActionListener listener)
```

This runs when the screen writes `new FunctionKeyBar(64, 56, 40, functionListener)`.

| Argument | In the sample | Meaning |
|---|---|---|
| `barHeight` | `64` | Height of the whole footer strip, in pixels. |
| `buttonWidth` | `56` | Width of each button. |
| `buttonHeight` | `40` | Height of each button. |
| `listener` | `functionListener` | The one listener from the screen. Every button receives this same object. |

Those three numbers are how you fit the bar into another screen. Change the numbers at the `new FunctionKeyBar(...)` call. You do not edit the inside of the loop.

#### `this.barHeight = barHeight;`

The argument and the field have the same name. `this.barHeight` is the field on the object. `barHeight` alone is the argument. This line copies the argument into the field so the size methods can still see it after the constructor finishes.

#### `setLayout(new BorderLayout());`

A layout manager places children inside a panel. `BorderLayout` has five regions: north, south, east, west, and center. This footer uses only south, so the button row sits on the bottom edge. Any extra height (the footer is 64 pixels, each button is 40) stays as empty blue space above the buttons.

#### `setBackground` and `setOpaque(true)`

`new Color(168, 214, 245)` is the light blue from the sketch. The three numbers are red, green, and blue, each from 0 to 255.

`setOpaque(true)` tells Swing “paint my own background”. If a panel is not opaque, the parent’s colour shows through and your `setBackground` appears to do nothing.

#### The inner row

```java
JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
row.setOpaque(false);
```

A second panel holds the buttons in one horizontal line. `FlowLayout.LEFT` starts at the left. The two `4`s are the horizontal gap and the vertical gap, in pixels, between buttons and around the row.

`setOpaque(false)` makes this row transparent, so the footer’s blue shows through the gaps. The buttons themselves are still painted.

This row is the only child of the footer. The footer stays one component from the screen’s point of view: the screen adds `FunctionKeyBar`, and never sees `row`.

### The loop that builds the ten buttons

```java
for (int i = 0; i < KEYS.length; i++) {
```

`i` starts at 0 and stops before 10. `KEYS.length` is 10. Each pass builds one button.

```java
String key = KEYS[i];
JButton button = new JButton(key);
```

`KEYS[0]` is `"F1"`, so the first button’s text is F1. `new JButton(key)` creates the button and sets that text.

```java
button.setActionCommand(key);
```

This is the important line for the listener. The action command is a string stored on the button. When the button is clicked, Swing puts that string on the event. The listener reads it with `event.getActionCommand()`.

The button text and the action command are both `"F1"`. They can be different. The listener must use the action command, not the visible text. In a large old program the button might display a word such as “Save” while the command is still `"F1"`.

```java
button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
```

Asks the layout for a button of the width and height you passed in. `FlowLayout` respects preferred size, so the buttons stay small and the rest of the footer stays empty, as in the sketch.

```java
button.setBackground(new Color(186, 224, 248));
button.setOpaque(true);
button.setFocusPainted(false);
button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
```

Light-blue face, black border two pixels thick. `setFocusPainted(false)` turns off the extra rectangle Swing draws when a button has keyboard focus, so the black border stays the only outline.

```java
button.addActionListener(listener);
```

Registers the screen’s listener on this button. The same `listener` object is passed on every pass of the loop. That is the “one listener for all ten buttons” rule. There is no second listener hiding inside the footer.

```java
row.add(button);
bindKey(KEY_CODES[i], key, listener);
```

Puts the button into the row, then connects the matching physical key (F1, or F10 for the F0 button) to the same listener.

After the loop:

```java
add(row, BorderLayout.SOUTH);
```

Puts the finished row on the bottom of the footer.

### The three size methods

Layout managers ask a component “how big do you want to be?” before placing it.

```java
public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    size.height = barHeight;
    return size;
}
```

`super.getPreferredSize()` is the height the buttons would choose by themselves. The next line replaces that height with the `barHeight` you passed in. Width is left alone, so the bar can still stretch across the window.

`getMinimumSize` returns the same thing, so the layout is not allowed to squash the footer shorter than `barHeight`.

`getMaximumSize` keeps that height and sets the width to `Integer.MAX_VALUE` (the largest `int`). That tells a layout “you may make me as wide as the window, but do not make me taller than `barHeight`”.

`BorderLayout.SOUTH` uses the preferred height and gives the component the full width of the window. Together, these methods are what make the footer a strip of a height you control.

### `bindKey` — the keyboard

A click and a keypress must end at the same listener. `bindKey` does that for one key.

```java
private void bindKey(int keyCode, final String command, final ActionListener listener)
```

`final` on `command` and `listener` is required by Java 6. The anonymous class created a few lines later is allowed to read a local variable only when that variable is `final`. You will see `final` used this way throughout a Java 6 UI.

```java
String mapKey = "function-" + command;
```

For F1 this string is `"function-F1"`. It is only a name inside Swing’s maps. It is not the action command. The action command stays `"F1"`.

```java
getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
        KeyStroke.getKeyStroke(keyCode, 0), mapKey);
```

Two maps work as a pair.

* The **input map** says: “when this key is pressed, look up this name.”
* `WHEN_IN_FOCUSED_WINDOW` means the key works while this window is in front, even if the button itself does not have focus.
* `getKeyStroke(keyCode, 0)` means that key with no modifier. The `0` is “no Shift, no Ctrl, no Alt”.

```java
getActionMap().put(mapKey, new AbstractAction() { ... });
```

The **action map** says: “when that name is looked up, run this action.”

Inside the action:

```java
listener.actionPerformed(new ActionEvent(
        FunctionKeyBar.this,
        ActionEvent.ACTION_PERFORMED,
        command));
```

This calls the screen’s listener directly, as if a button had been clicked. The third argument, `command`, becomes `event.getActionCommand()` on the screen. For the F1 key that is `"F1"`. For the F10 key that is `"F0"`.

`FunctionKeyBar.this` is the footer object. A plain `this` inside the anonymous class would mean the action, not the footer. The screen’s listener does not look at the source. It looks at the command. Either way the call is safe.

`AbstractAction` is a ready-made class that already implements the action method Swing expects. Writing `new AbstractAction() { public void actionPerformed(...) { ... } }` is the Java 6 way to make a one-off action. Java 8 would use a lambda here. This project does not, so the same code compiles on Java 6.

---

## 2. `FunctionPage.java` — the screen

### `public class FunctionPage extends JFrame`

A `JFrame` is a window with a title bar and a close button. `FunctionPage` is that window.

### `resultLabel`

```java
private final JLabel resultLabel = new JLabel(" ");
```

A label is text on the screen. It starts as a single space so the label has a height before the first click. `create()` and the other methods change this text so you can see which function ran.

In the real project this label goes away. Those methods will call the real create, save, or search code instead of `setText`.

### The one listener

```java
private final ActionListener functionListener = new ActionListener() {
    public void actionPerformed(ActionEvent event) {
        String key = event.getActionCommand();
        if ("F1".equals(key)) {
            create();
        } else if ("F2".equals(key)) {
            onF2();
        }
        // ... F3 through F0
    }
};
```

`ActionListener` is an interface. It has one method, `actionPerformed`. Swing calls that method when a button with this listener is clicked, and `bindKey` calls it when the matching key is pressed.

This block is an anonymous class: a class with no name, created on the spot, that implements `ActionListener`. Java 6 uses this anywhere a listener is needed. In the other codebase, search for `new ActionListener()` and for `actionPerformed`.

`event.getActionCommand()` returns the string that was set with `setActionCommand`. For the F1 button and the F1 key, that string is `"F1"`.

The test is written `"F1".equals(key)` rather than `key.equals("F1")`. If `key` were ever null, `key.equals(...)` would throw a `NullPointerException`. The constant is on the left, so a null command simply makes the test false and the chain continues. Old Java code uses this order a lot.

Java 6 has no `switch` on a `String`. The `if` / `else if` chain is the normal way to turn one listener into ten functions. If you find a `switch (key)` in the other project, that file was compiled as Java 7 or newer.

Each branch calls one method. The listener itself does no work beyond choosing the method. That keeps “which button was it?” separate from “what does F1 do?”.

The listener is stored in a field, not created inside the constructor, so the constructor can hand the same object to `FunctionKeyBar`.

### The constructor — building the window

```java
public FunctionPage() {
    super("Java UI");
```

`super("Java UI")` calls the `JFrame` constructor and sets the title-bar text.

```java
JPanel page = new JPanel(new BorderLayout());
page.setBackground(new Color(168, 214, 245));
```

The page is one panel with a border layout and the same light blue as the footer.

```java
resultLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
resultLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
page.add(resultLabel, BorderLayout.NORTH);
```

`Dialog` is one of Java’s built-in font names (the others are `DialogInput`, `Monospaced`, `Serif`, and `SansSerif`). It exists on every machine, so the code does not depend on a font being installed.

`createEmptyBorder(16, 16, 16, 16)` adds empty space around the text: top, left, bottom, right. It is padding, not a visible line.

`BorderLayout.NORTH` puts the label at the top of the page.

```java
FunctionKeyBar footer = new FunctionKeyBar(64, 56, 40, functionListener);
page.add(footer, BorderLayout.SOUTH);
```

This is the line you copy into an existing screen.

* `64` — footer height.
* `56` — button width.
* `40` — button height.
* `functionListener` — the one listener declared above.
* `BorderLayout.SOUTH` — the footer stays at the bottom while the rest of the page occupies the center.

```java
setContentPane(page);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(820, 520);
setLocationRelativeTo(null);
```

| Call | Effect |
|---|---|
| `setContentPane(page)` | Makes `page` the inside of the window. |
| `EXIT_ON_CLOSE` | Closing the window ends the program. |
| `setSize(820, 520)` | Window width and height in pixels. |
| `setLocationRelativeTo(null)` | Centers the window on the screen. |

The constructor builds the window. It does not show it. `setVisible(true)` in `main` does that.

### `create()` and `onF2()` … `onF0()`

```java
private void create() {
    resultLabel.setText("F1 called create()");
}
```

F1 is the only key with a real name in this sample, because that is the function you asked for. The body is a stand-in. In the other project, replace the `setText` line with the call that already exists there, for example the method that opens the create form.

`onF2` through `onF0` are the same shape. They exist so each key already has a method to fill in. `private` means only this screen calls them. The listener is the only caller.

`onF0` matches the button labelled F0. The physical key is F10, because that is what `KEY_CODES` binds.

### `main`

```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            new FunctionPage().setVisible(true);
        }
    });
}
```

`main` is where Java starts. `static` means it runs without an existing `FunctionPage` object. It is the one place that creates the window.

Swing has a rule: create and change components on the event thread (the EDT). `invokeLater` puts the `run()` block onto that thread and returns. Java then runs `run()`, which builds the window and shows it.

`new Runnable() { public void run() { ... } }` is another anonymous class. `Runnable` is an interface with one method, `run`. You will see `invokeLater(new Runnable()` at the start of almost every Swing `main` in a Java 6 program.

Search the other project for `public static void main` and for `invokeLater` to find how that program opens its first window.

---

## 3. `pom.xml` — the build file

You do not copy `pom.xml` into the other project unless that project is also a Maven project. The explanation is here so the settings are not a mystery.

| Line | Meaning |
|---|---|
| `<artifactId>java-ui</artifactId>` | The project id. Maven names the jar `java-ui-1.0.jar`. |
| `<name>Java UI</name>` | The human-readable project name. |
| `<maven.compiler.source>1.6</maven.compiler.source>` | Accept only Java 6 language. A lambda, a `switch` on a String, or `try`-with-resources will fail to compile. |
| `<maven.compiler.target>1.6</maven.compiler.target>` | Write class files that a Java 6 runtime can load (class version 50). |
| `<mainClass>page.FunctionPage</mainClass>` | The class whose `main` runs when you start the jar. |

The compiler plugin is listed twice, once in `<properties>` and once inside the plugin. Both say 1.6. The plugin block is the one Maven actually uses for this build.

### `.gitignore` — files git should not keep

Git stores the history of a project. A `.gitignore` file, in the project root next to `pom.xml`, lists paths git should skip. Those files can still exist on your disk. They are just not committed.

This project ignores generated build output, editor settings, and operating-system clutter.

| Pattern | What it is | Why it is ignored |
|---|---|---|
| `target/` | The folder Maven creates when you run `mvn package`. It holds `.class` files and `java-ui-1.0.jar`. | Anyone with the source and `pom.xml` can rebuild it. Committing it makes every compile look like a change. |
| `*.class` | A compiled class that landed outside `target/`. | Same reason. Source is the copy that matters. |
| `*.log` | Log output. | It records one run on one machine. |
| `.classpath`, `.project`, `.settings/`, `bin/` | Eclipse’s private project files and its own output folder. | Another person may use a different Eclipse setup. `pom.xml` already describes the build. |
| `.idea/`, `*.iml`, `*.ipr`, `*.iws` | IntelliJ’s project files. | Same as Eclipse. They differ from one computer to the next. |
| `.vscode/` | VS Code and Cursor settings for this folder. | Editor preferences, not part of the program. |
| `nbproject/private/`, `nbbuild/`, `dist/`, `nbdist/` | NetBeans private settings and build folders. | Private or generated. |
| `.DS_Store`, `Thumbs.db` | Hidden files macOS and Windows add to folders. | Nothing to do with the Java code. |

A line starting with `#` is a comment. Git skips it. A name with a trailing `/`, such as `target/`, means a directory and everything inside it. A name starting with `*`, such as `*.class`, means “any file ending in `.class`”.

The files you do want in git are the ones you would copy by hand: `src/`, `pom.xml`, `.gitignore`, and this guide. After `mvn package`, `target/` appears on disk and git leaves it alone.

If this folder is not a git repository yet, the ignore file does nothing until you run `git init` here (or copy the project into a repository that already exists). The other project you are joining may already have a `.gitignore`. Add the `target/` line there if that project is built with Maven and `target/` is not already listed.

---

## 4. How a click travels through the files

Using F1 as the example.

1. `FunctionPage` creates one `ActionListener` and stores it in `functionListener`.
2. The constructor calls `new FunctionKeyBar(64, 56, 40, functionListener)`.
3. The loop reaches `i == 0`. `key` is `"F1"`.
4. The new button gets text `"F1"`, action command `"F1"`, and `addActionListener(functionListener)`.
5. `bindKey` maps the physical F1 key to an action that calls `functionListener.actionPerformed` with command `"F1"`.
6. You click the button, or you press F1.
7. Swing calls `actionPerformed` on that one listener.
8. `getActionCommand()` returns `"F1"`.
9. `"F1".equals(key)` is true, so the listener calls `create()`.
10. `create()` sets the label to `F1 called create()`.

F2 through F9 follow the same steps and land in `onF2` through `onF9`. The F0 button and the F10 key both arrive with command `"F0"` and land in `onF0`.

---

## 5. Putting this into the other project

1. Copy `FunctionKeyBar.java` into that project’s source tree.
2. Change `package page;` to that project’s package.
3. On the existing window class, add the `functionListener` field and the `create()` method. Point `create()` at the function that project already uses for “create”.
4. In that window’s constructor, after the other panels are added:

```java
FunctionKeyBar footer = new FunctionKeyBar(64, 56, 40, functionListener);
add(footer, BorderLayout.SOUTH);
```

Change `64`, `56`, and `40` until the strip matches the existing screen. If that window already uses a layout other than `BorderLayout`, look for where its current bottom panel is added, and add the footer in that same place.

5. If the window already has function buttons, do not add a second bar. Find its listener (search for `addActionListener` and `getActionCommand`) and compare it with `functionListener` in `FunctionPage`. The shape you want is one `actionPerformed` and a chain of `"F1".equals(key)` tests that call methods.

Words worth searching in that codebase:

* `addActionListener`
* `getActionCommand`
* `setActionCommand`
* `ActionListener`
* `invokeLater`
* `VK_F1`
* `BorderLayout.SOUTH`

---

## 6. Run this sample

From a terminal:

```bash
cd ~/java/java-ui
mvn -q package
$JAVA6_HOME/bin/java -jar target/java-ui-1.0.jar
```

Click **F1** or press the F1 key. The label on the blue page reads `F1 called create()`. The other buttons report their own names. F10 on the keyboard reports `F0`.

The black form on the right is the Weapon Configuration screen. **OK** writes the two selected choices into that same label. **Cancel** writes `Cancel`.

---

## 7. The form on the right

The form is not one long class full of layout code. It is a screen assembled from small pieces. The pieces live in `src/main/java/page/form/`. The screen that uses them is `WeaponConfigurationPanel.java`.

```text
FunctionPage
    |
    +-- blue page, left
    |
    +-- WeaponConfigurationPanel, right     BorderLayout.EAST
            |
            +-- FormStack          vertical pile, scrolls
                    |
                    +-- FormRow            label + one control
                    +-- SectionPanel       triangle title, opens and closes
                            |
                            +-- OptionGroup    radio buttons, one choice
```

To make a similar screen, copy `WeaponConfigurationPanel`, change the title, and add your own rows and sections. You do not copy layout code into `FunctionPage`.

### Where it is attached

In `FunctionPage` the window has two regions above the footer:

```java
JPanel body = new JPanel(new BorderLayout());
body.add(blue, BorderLayout.CENTER);
body.add(weapon, BorderLayout.EAST);
```

`CENTER` is the blue page. `EAST` is the form. The footer stays `SOUTH` of the whole window, so the function keys still run along the bottom.

`weapon` is `final` because the OK and Cancel listeners are anonymous classes, and Java 6 only lets an anonymous class read a local variable when that variable is `final`.

### `Mono` — the shared look

`page/form/Mono.java` holds the black background, the white foreground, the font, and factories for the controls.

| Method | Gives you |
|---|---|
| `paint(component)` | Black background, white text, Dialog 13. |
| `text(columns)` | A text field with a white border. |
| `area(rows, columns)` | A comment box. Words wrap. |
| `combo()` | A drop-down. Java 6 `JComboBox` is not generic, so this returns a raw `JComboBox`, not `JComboBox<String>`. |
| `button(text)` | A black button with a white border. |
| `check()` | A square check box. |
| `radio(text)` | A round choice. |

`MarkIcon` and `TriangleIcon` are drawn here. Ubuntu’s own look would paint the usual grey radio. These icons stay white, which matches the screen you are copying.

### `Block` — one row that does not stretch

`BoxLayout` makes each child as tall as that child’s maximum height. A default panel’s maximum height is huge, so one row would eat the whole form. `Block.getMaximumSize()` returns the preferred height and a very large width. The row stays one line tall and still grows sideways.

`FormRow`, `OptionGroup`, and `SectionPanel` all extend `Block`. A new kind of row should extend `Block` too.

### `FormStack` — the vertical pile

`FormStack` is a panel with a top-to-bottom `BoxLayout`. It also implements `Scrollable`.

* `getScrollableTracksViewportWidth` returns `true`, so the pile becomes as wide as the scroll pane. No sideways scrollbar.
* `getScrollableTracksViewportHeight` returns `false`, so a tall pile scrolls up and down.

`WeaponConfigurationPanel` puts the stack in a `JScrollPane` and forces its own width to 560 pixels. Change `PANEL_WIDTH` if the next screen needs to be wider or narrower.

### `FormRow` — label plus anything

```java
new FormRow("Target Selection", targetSelection)
```

The label is fixed at 150 pixels. The component on the right takes the rest of the line. That component can be a text field, a combo, or a small panel. The Track line is a small panel: a short text field and a check box side by side, passed in as the second argument.

### `OptionGroup` — one choice among several

```java
new OptionGroup(new String[] {"Offset Engm", "Real Engm"}, 0, 2)
```

| Argument | Meaning |
|---|---|
| the string array | The choices, left to right, then the next line. |
| `0` | Which choice starts selected. The first one. Pass `-1` for none, as Linked Launch and Tube Select do. |
| `2` | How many choices sit on one line. |

The buttons share one `ButtonGroup`, so selecting one clears the others. `getSelected()` returns the text of the selected button, or `""` when nothing is selected.

Tube Select passes six names and `2` columns. Grid layout fills across, so the array order is Tube 6, Tube 5, Tube 4, Tube 3, Tube 2, Tube 1. That puts 6, 4, 2 down the left and 5, 3, 1 down the right.

### `SectionPanel` — a block you can close

```java
SectionPanel section = new SectionPanel("Weapon Selection");
section.addContent(weapon);
```

The header is a triangle and a title. A mouse click calls `setOpen`. Open shows a down triangle and the body. Closed shows a sideways triangle and hides the body. `addContent` is how a row joins that body.

### `WeaponConfigurationPanel` — this particular screen

This class only chooses what the pieces say. The order in `build()` is the order on screen:

1. Title, “Weapon Configuration”.
2. `FormRow` for Target Selection, Track, and Comments.
3. Cancel and OK, aligned to the right.
4. Four sections: Engagement Configuration, Weapon Selection, Linked Launch, Tube Select. Each section’s body is an `OptionGroup`.
5. Weapon Configuration Selection: a combo, the words “Target Parameters”, then five pairs of short fields.

The pairs are Track/RB, TB/DP, TR/TCo, TSp/Type, Lng/Cat. Each name is stored in a map when the field is built:

```java
private final Map<String, JTextField> parameters = new LinkedHashMap<String, JTextField>();
```

`LinkedHashMap` is a Java 6 map that remembers insertion order. `getParameter("TB")` looks the field up and returns what was typed. A missing name returns `""` instead of throwing.

The constructor switches to the classic Metal look before it builds the controls, then puts the previous look back in a `finally` block. The `finally` runs even if building throws. Controls already created keep the black Metal look. The function-key bar is created after this constructor returns, so it keeps the normal look.

### A second screen, same pieces

Copy `WeaponConfigurationPanel.java`, rename the class, and edit `build()`. A smaller example of the shape:

```java
FormStack form = new FormStack();
form.add(new FormRow("Name", Mono.text(12)));

OptionGroup mode = new OptionGroup(new String[] {"A", "B"}, 0, 2);
SectionPanel section = new SectionPanel("Mode");
section.addContent(mode);
form.add(section);
```

Then add the new panel the same way the weapon panel is added: `body.add(yourPanel, BorderLayout.EAST)`, or `CENTER` if it should fill the page. One screen, one class. The pieces in `page.form` stay shared.
