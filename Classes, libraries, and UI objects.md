# Classes, libraries, and UI objects

This file explains every class in this project, every library those classes use, and every UI object (a `JPanel`, a `JButton`, a layout, a colour). For each one it says what it is, how it works, what this screen uses it for, and how you use it.

Two other files sit beside this one:

* `Java UI - code guide.md` walks the footer and the window line by line.
* `Create Engagement - how values reach the backend.md` walks the path from the form to `create()`.

This file is the reference for the pieces themselves.

Nothing visual is downloaded. The widgets come with Java. Maven only compiles the program. The classes under `src/main/java/page/` are the ones this project wrote on top of those widgets.

---

## 1. The two layers

```text
Java itself
    java.awt          sizes, colours, fonts, layouts, drawing
    java.awt.event    clicks, keys, focus, mouse
    javax.swing       the widgets: window, panel, button, field
    java.util         the map that remembers parameter fields

This project
    page.FunctionPage                  the window
    page.FunctionKeyBar                the F1–F12 footer
    page.WeaponConfigurationPanel      the Create Engagement form
    page.model.Engagement              the values after they leave the form
    page.backend.EngagementService     the method that receives those values
    page.form.*                        the rows, cards, colours, and hints
```

A **library** here is a package of classes that ship with the JDK. You import them. You do not copy them into the project.

A **class** is the written description. `JPanel` is a class. `WeaponConfigurationPanel` is a class.

An **object** is one living instance. `new JPanel()` creates one panel object. `new WeaponConfigurationPanel()` creates one form object. Two `new JButton("OK")` calls create two button objects that share the `JButton` class and hold different text.

A **UI object** is an object the user can see or that decides where visible objects sit. `JPanel`, `JButton`, `JLabel`, `BorderLayout`, and `Color` are UI objects. `Engagement` is a plain object. It has no pixels.

---

## 2. Words that the code keeps using

| Word | Meaning in this project |
|---|---|
| Package | The folder a class lives in, written as the first line of the file. `package page.form;` means the file belongs in `src/main/java/page/form/`. |
| Import | Permission to use a class from another package by its short name. `import javax.swing.JPanel;` lets the file say `JPanel` instead of `javax.swing.JPanel`. |
| Field | A variable that belongs to the object and stays for as long as the object stays. `field1` on the form is a field. |
| Method | A named block of code on a class. `readForm()` is a method. |
| Constructor | The method that runs on `new`. It has the class name and no return type. `public FunctionPage()` is a constructor. |
| `extends` | This class is a kind of that class. `FunctionPage extends JFrame` means the page is a window. It can do everything a `JFrame` can do, plus its own methods. |
| `implements` | This class promises to provide the methods of an interface. `FormStack implements Scrollable` promises the five scroll methods. |
| Interface | A list of methods with no body. `ActionListener` has one method, `actionPerformed`. `Icon` has three. |
| `public` | Any class may use it. |
| `private` | Only the class that wrote it may use it. |
| `static` | Belongs to the class, not to one object. `Mono.paint(...)` is static. You call it on `Mono`, not on a `new Mono()`. |
| `final` on a class | No other class may extend it. `Mono` is final. |
| `final` on a field | The variable is assigned once. |
| `final` on a local | Required in Java 6 when an anonymous listener uses that local. `final WeaponConfigurationPanel form` is final for that reason. |
| Anonymous class | A class written inline at the `new`, with no name of its own. The OK listener is one. It can read fields of the outer class, and locals that are `final`. |

`java.lang` is imported for you. `String`, `Object`, `Exception`, `Math`, and `Runnable` need no import line.

Java 6 is the language level in `pom.xml`. The code avoids later syntax on purpose. `JComboBox` is written as the raw type, because `JComboBox<String>` arrived in Java 7. Joined strings in `CheckGroup` are built with `StringBuffer`, which has been in Java since the beginning.

---

## 3. How a Swing screen works

Swing draws a tree of components. A component is a rectangle. A container is a component that holds other components. The window is the root.

```text
FunctionPage                         a JFrame, the operating-system window
└─ page                              a JPanel, BorderLayout, light blue
   ├─ CENTER  body                   a JPanel, BorderLayout, transparent
   │  ├─ CENTER  blue                a JPanel, the empty blue area
   │  │  └─ NORTH  resultLabel       a JLabel, "Saved …" after OK
   │  └─ EAST  WeaponConfigurationPanel
   │     ├─ NORTH  title bar         "Create Engagement" and the ×
   │     ├─ CENTER JScrollPane
   │     │  └─ FormStack             the rows, headings, and cards
   │     └─ SOUTH  button bar        Cancel and the cyan OK
   └─ SOUTH  FunctionKeyBar          F1 through F12
      └─ SOUTH  row                  twelve JButtons in a FlowLayout
```

`BorderLayout.EAST` is the right-hand region. `SOUTH` is the bottom. `CENTER` takes whatever space is left.

### The event thread

Swing has one thread that may touch components. It is called the event dispatch thread.

`main` does not run on that thread. `SwingUtilities.invokeLater` posts a `Runnable` onto it. The `run` method creates the window and shows it. Every click listener also runs on that thread. `readForm()` is allowed to call `getText()` because the OK click already put it on the event thread.

Read the components in the listener. Hand the plain `Engagement` to any later work. A background thread that calls `getText()` on a `JTextField` is outside the rules Swing is built on.

### Sizes

A layout manager places children by asking three questions:

| Method | What the component answers |
|---|---|
| `getPreferredSize()` | The size it would like. |
| `getMinimumSize()` | The smallest size it will accept. |
| `getMaximumSize()` | The largest size it will accept. |

`Dimension` holds a width and a height in pixels.

`JComponent` answers the maximum question with a very large number (32767 by 32767) unless a subclass overrides it. `BoxLayout` then stretches that child to fill leftover space. `Block` overrides the maximum so a row stays only as tall as its field, while its width may still grow to the width of the form. That one override is why the form rows do not become huge empty bands.

### Paint

Swing calls `paint` when a rectangle needs drawing. This project rarely paints pixels itself. It sets a background, a foreground, a font, and a border, and Swing paints them.

Two small pictures are painted by hand: `Mono.MarkIcon` (the radio dot and the check box) and `Mono.TriangleIcon` (the section triangle). Swing calls `paintIcon` on them while it paints the button or the label. They are not components. They have no `add`, no layout, and no listener.

### Opaque

`setOpaque(true)` means this component paints its own background. The parent does not show through.

`setOpaque(false)` means the parent shows through. The blue body panel is transparent so the page colour behind it is the colour you see. The footer is opaque and paints its own blue.

### Events

An event is an object Swing creates when something happens. The listener is your object, with a method Swing calls.

| What happened | Event class | Listener |
|---|---|---|
| Button click, or a key bound as a click | `ActionEvent` | `ActionListener.actionPerformed` |
| Keyboard focus entered or left a field | `FocusEvent` | `FocusAdapter.focusGained`, `focusLost` |
| Mouse click on the section title | `MouseEvent` | `MouseAdapter.mouseClicked` |
| A function key while the window is active | turned into an `ActionEvent` by `FunctionKeyBar.bindKey` | the same `ActionListener` as the button |

`ActionEvent.getActionCommand()` is the string the button was given, such as `"F1"` or the button text. `getSource()` is the object that fired. The footer listener uses the command, so a mouse click and a real F-key share one path.

---

## 4. The libraries

### `java.awt` — drawing and layout

AWT is the older window toolkit. Swing is built on top of it. This project uses AWT for colour, font, size, layout, and the drawing surface. It does not use AWT buttons or AWT text fields. Those would be `java.awt.Button` and `java.awt.TextField`, and they are a different set of widgets. Every control you see here is Swing.

| Class | What it is | How this screen uses it |
|---|---|---|
| `Component` | The base of every visible thing. Position, size, visibility, font, colour, mouse listeners. | `MarkIcon.paintIcon` receives the component it is drawing on. `Block.row` accepts any `Component`. |
| `Container` | A `Component` that holds children. `add`, `remove`, `setLayout`. | `JPanel`, `JFrame`, and `JScrollPane` are containers. |
| `Color` | A colour as red, green, and blue, each from 0 to 255. | `new Color(168, 214, 245)` is the page blue. `Color.WHITE` and `Color.BLACK` are constants. |
| `Font` | A typeface, a style, and a size in points. | `new Font("Dialog", Font.PLAIN, 13)` is the form font. `"Dialog"` is a logical font. Java maps it to a real font on the machine. `Font.BOLD` is the title and the cyan OK. |
| `Dimension` | A width and a height in pixels. | Preferred, minimum, and maximum sizes. `new Dimension(620, 700)` is the form’s preferred size. |
| `Rectangle` | An x, a y, a width, and a height. | `FormStack` receives the visible rectangle when Swing asks how far to scroll. |
| `Insets` | Padding on four sides: top, left, bottom, right. | `GroupLabel` uses it as the gap between the lines and the centred title. |
| `Graphics` | The pen Swing hands you during painting. | `paintIcon` receives one. |
| `Graphics2D` | The same pen with smoother drawing. | The icons cast `Graphics` to `Graphics2D`, turn on antialiasing, draw the shape, then `dispose()` the copy. `dispose` releases that copy. It does not destroy the screen. |
| `RenderingHints` | Drawing options. | `KEY_ANTIALIASING` / `VALUE_ANTIALIAS_ON` smooths the circle, the square, and the triangle. |

`Graphics.create()` returns a copy of the pen. Drawing on the copy cannot leak colour or transform into the next component. The icon disposes the copy when it is finished.

### `java.awt` layouts

A layout is an object you give to a container with `setLayout`. When the container is shown or resized, the layout sets the bounds of each child. You choose the layout. You do not set `setBounds` yourself anywhere in this project.

**`BorderLayout`** divides the container into five regions: `NORTH`, `SOUTH`, `EAST`, `WEST`, `CENTER`. One child per region. A second `add` to the same region replaces the first.

* `NORTH` and `SOUTH` are as tall as their preferred height, and as wide as the container.
* `EAST` and `WEST` are as wide as their preferred width, and as tall as the space between north and south.
* `CENTER` receives the remainder.

The window uses it for “form on the right, footer on the bottom, blue area in the middle.” The form uses it for “title on top, buttons on the bottom, scrolling body in the middle.” `FormRow` uses it for “label on the left, field in the center.”

`new BorderLayout(8, 0)` sets a horizontal gap of 8 pixels and a vertical gap of 0. `namedField` uses that so the short name (`P1`, `P8`) sits 8 pixels left of the text field.

**`FlowLayout`** places children left to right in one row, at their preferred size. When the row is full, it wraps. It does not stretch children.

`new FlowLayout(FlowLayout.LEFT, 4, 4)` aligns left, with 4 pixels between buttons and 4 pixels above and below. That is the F-key row. `FlowLayout.RIGHT` is the Cancel / OK bar. `FlowLayout.CENTER` is the Field 3 line.

**`GridLayout`** makes a grid of equal cells. Every radio button in a group is the same size, which is what makes Choice 1 line up under Choice 5.

```java
new GridLayout(rows, columns, 12, 4)
```

`rows` and `columns` are the grid. `12` is the horizontal gap. `4` is the vertical gap. `OptionGroup` computes the row count from the number of choices:

```text
rows = ((how many choices - 1) / columns) + 1
```

Six choices and 2 columns produce 3 rows. The order is row by row: Choice 6, Choice 5, then Choice 4, Choice 3, then Choice 2, Choice 1.

**`BoxLayout`** stacks children in one direction. `BoxLayout.Y_AXIS` is top to bottom. It respects maximum size and `setAlignmentX`. `FormStack`, `FormCard`, and the body of `SectionPanel` use it.

`JComponent.LEFT_ALIGNMENT` is `0.0f`, the left edge. `FormCard.addRow` sets that on each row so a short row stays left. The default alignment is centered.

**`GridBagLayout`** places children with a constraint object per child. `GroupLabel` uses it for “line, title, line” in one row. The lines have `weightx = 1` and `fill = HORIZONTAL`, so they grow and the title stays in the middle at its own width. `GridBagConstraints.RELATIVE` (the default gridx) means “the next cell.”

**`Box`** is a container, and it also has static factories for invisible spacers. `Box.createVerticalStrut(8)` is an invisible component whose height is 8 pixels. The form inserts one between Field 1 and Field 2. The strut is not a field and it is not copied into `Engagement`.

### `java.awt.event` — what the user did

| Class | Role |
|---|---|
| `ActionListener` | One method, `actionPerformed(ActionEvent)`. Implement it to react to a button. |
| `ActionEvent` | Describes one activation. `getActionCommand()` is the command string. `ACTION_PERFORMED` is the id constant used when the footer builds a key event by hand. |
| `KeyEvent` | Keyboard constants. `VK_F1` through `VK_F12` are the key codes. The footer never reads a typed character. It only uses these constants. |
| `FocusAdapter` | Empty implementations of the focus methods, so a subclass writes only `focusGained` and `focusLost`. |
| `FocusEvent` | Swing passes this when focus moves. The hint fields use the callback. They do not read the event’s fields. |
| `MouseAdapter` | Empty implementations of the mouse methods. `SectionPanel` writes `mouseClicked`. |
| `MouseEvent` | The click on a section header. The method ignores the coordinates and toggles open or closed. |

`addActionListener` can be called more than once on the same button. Each listener runs. The form’s OK button starts with no listener. `FunctionPage` adds the one that calls `readForm()`.

### `javax.swing` — the widgets

Swing components are lightweight. Java draws them inside the native window. The native window is the `JFrame`. Everything inside it is Swing.

`JFrame` does not extend `JComponent`. It extends the AWT `Frame`. You put Swing children in it by setting the content pane, or by calling `add`, which forwards to that pane. This project uses `setContentPane(page)`.

| Class | What the user sees | What you call |
|---|---|---|
| `JFrame` | The framed window, with a title bar supplied by the operating system. | `setTitle` via `super("Java UI")`, `setContentPane`, `setSize`, `setLocationRelativeTo`, `setDefaultCloseOperation`, `setVisible(true)`. |
| `JPanel` | A blank rectangle. On its own it has no text and no click behaviour. | `new JPanel(layout)`, `add`, `setBackground`, `setOpaque`. |
| `JLabel` | Text, or a small icon, or both. It does not take typing. | `new JLabel("Create Engagement")`, `setFont`, `setForeground`, `setIcon`. |
| `JButton` | A clickable labelled control. | `new JButton("OK")`, `addActionListener`, `setActionCommand`, `setEnabled`. |
| `JTextField` | One line of typing. | `getText`, `setText`, `setCaretColor`. The column count is the preferred width in character columns, not a limit on how many characters the user may type. |
| `JTextArea` | Several lines of typing. | `setLineWrap(true)` wraps long lines. `setWrapStyleWord(true)` breaks on words. Rows and columns set the preferred size, not a character limit. |
| `JComboBox` | A closed field that opens a list. | Construct with a `String[]`. `getSelectedItem()` returns the chosen object, or `null` when the list is empty. `setRenderer` replaces the way each row is painted. |
| `JCheckBox` | A box that toggles on its own. | `isSelected()`, `setSelected`, `getText`. Several may be on at once. |
| `JRadioButton` | A round choice. | Same selected methods as a check box. Exclusivity comes from `ButtonGroup`, not from the button class. |
| `ButtonGroup` | Invisible. It is not added to a panel and it has no size. | `group.add(button)` on each radio. The group clears the others when one becomes selected. |
| `JScrollPane` | Scroll bars around one child. | `new JScrollPane(form)`. `getViewport().setBackground(...)` colours the area behind the form. `setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER)` refuses a sideways bar. `getVerticalScrollBar().setUnitIncrement(16)` is 16 pixels per arrow click. |
| `JSeparator` | A thin line. | `GroupLabel` puts one on each side of the centred title. |
| `JComponent` | The base of the Swing widgets above. Borders, opacity, input maps, alignment. | `setBorder`, `setOpaque`, `setAlignmentX`, `getInputMap`, `getActionMap`, `revalidate`, `repaint`. |
| `JList` | A list widget. This screen never shows a standalone list. | It appears only as the argument type of a combo-box cell renderer. |
| `AbstractAction` | An `ActionListener` that is also an action entry. | The footer stores one per function key in the action map. |
| `KeyStroke` | A description of a key, plus modifiers. | `KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)`. The `0` means no Shift, Ctrl, or Alt. |
| `BorderFactory` | Builds border objects. You do not `new` a border yourself. | Empty padding, a line, a matte edge, a compound border. |
| `UIManager` | Holds the current look and feel for the whole program. | The form installs Metal while it constructs its controls, then puts the previous look back. |
| `LookAndFeel` | The object that decides how a widget is painted. | Saved and restored around `build()`. |
| `SwingUtilities` | Small Swing helpers. | `invokeLater` is the one this program calls. |
| `Icon` | A picture with a width, a height, and `paintIcon`. | `MarkIcon` and `TriangleIcon`. |
| `DefaultListCellRenderer` | The default combo and list cell, which is a `JLabel`. | `Mono.combo` replaces the colours so the drop-down stays white with dark text. |
| `Scrollable` | An interface, not a visible class. | `FormStack` implements it so the scroll pane knows to match the viewport width and to scroll vertically. |

**`JPanel` in practice.** A panel is the board you pin other components to. It becomes useful when you give it a layout and `add` children. `FunctionKeyBar` is a panel, so the window can `add` it like any other panel. `WeaponConfigurationPanel` is a panel for the same reason. Prefer a subclass of `JPanel` when the rectangle has behaviour of its own (`readForm`, a key binding, a preferred size). Use a plain `new JPanel(layout)` when it only groups children, as the title bar and the button bar do.

**`JFrame` in practice.**

```java
public class FunctionPage extends JFrame {
    public FunctionPage() {
        super("Java UI");                 // the title in the window bar
        setContentPane(page);             // the tree inside the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 760);               // outer width and height, in pixels
        setLocationRelativeTo(null);      // centre on the screen
    }
}
```

`EXIT_ON_CLOSE` ends the program when this window closes. That fits a sample whose only window is this frame. A form placed inside an existing program should leave the frame’s close operation alone. Hiding the form is `setVisible(false)` on the panel, which is what the × button does.

`setSize` forces 1180 by 760. `pack()` would size the frame from the preferred sizes of the tree instead. This sample uses `setSize`.

**`JButton` in practice.** The button shows a string. A click calls every `ActionListener`. `setActionCommand("F1")` stores the string the listener will read. If you never call it, the command is the button text. The footer sets both to the same `"F1"`.

`setFocusPainted(false)` turns off the extra rectangle some looks draw when the button has keyboard focus. `setContentAreaFilled(false)` on the × asks the button not to paint a filled rectangle, so the title bar shows around the glyph.

**`JTextField` and `JTextArea` in practice.** `getText()` returns the characters currently in the control, including a hint sentence if the program put that sentence there as the text. `setText` replaces them. `setCaretColor` is the colour of the insertion cursor. On a white field the cursor is the dark `FIELD_TEXT` colour, so it stays visible.

**`JComboBox` in practice.** The constructor `new JComboBox(items)` copies the array in as the list and selects the first item. This form’s combos each have one item, so the closed field shows that item. `Mono.combo` also installs a renderer: one `JLabel` is restamped for every visible row, with a white background and dark text. The renderer is how the drop-down matches the white parameter fields.

**`ButtonGroup` in practice.** Creating radio buttons and adding them to a panel does not make them exclusive. You also `group.add(button)` each one. The group is a logical object. `OptionGroup` keeps the group as a local in the constructor. After that, the buttons themselves remember the selection, and `getSelected()` walks them.

**`JScrollPane` in practice.** The scroll pane has a viewport. The viewport shows a window onto the child. The child (`FormStack`) may be taller than the viewport. The vertical bar moves the view. `FormStack.getScrollableTracksViewportWidth()` returns `true`, so the form is forced to the viewport width and the horizontal bar stays off. `getScrollableTracksViewportHeight()` returns `false`, so a tall form keeps its own height and scrolls. The unit increment of 16 matches `getScrollableUnitIncrement`. A page scroll uses the viewport height minus 16, from `getScrollableBlockIncrement`.

**Input map and action map.** `JComponent` has two maps that turn a key into a method call.

1. The input map binds a `KeyStroke` to a name. The footer uses the name `"function-F1"`.
2. The action map binds that name to an `Action`.

`getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)` means the binding works when this window is the active window, including while focus is in a text field. Function keys are not characters a text field types, so F1 still reaches this map while the user is in Field 1. The `AbstractAction` then builds an `ActionEvent` whose command is `"F1"` and hands it to the same listener the button uses.

There are three map conditions. `WHEN_FOCUSED` works only when that component itself has focus. `WHEN_IN_FOCUSED_WINDOW` is the one the footer needs. `WHEN_ANCESTOR_OF_FOCUSED_COMPONENT` works when focus is inside a child.

**`revalidate` and `repaint`.** `revalidate()` tells Swing the tree’s sizes have changed and the layout should run again. `repaint()` asks for a new paint. `SectionPanel.setOpen` does both, because hiding the body changes the height and the triangle picture.

### Borders — `javax.swing.border`

A border is painted inside the component’s edges and insets the children. `BorderFactory` builds them.

| Call | Effect |
|---|---|
| `createEmptyBorder(top, left, bottom, right)` | Invisible padding. The title bar uses `(8, 12, 4, 8)`. |
| `createLineBorder(color)` | A one-pixel line. The F-key buttons use black, 2 pixels, via `createLineBorder(Color.BLACK, 2)`. |
| `createLineBorder(color, thickness)` | The same, with a chosen thickness. |
| `createMatteBorder(1, 0, 0, 0, color)` | A line on the sides you name. The button bar has a one-pixel top edge and no line on the other three sides. |
| `createCompoundBorder(outer, inner)` | One border outside another. Form cards use a line outside and empty padding inside. Cancel uses a line outside and empty padding inside so the label does not touch the line. |

`Mono` keeps two shared borders: `LINE` in the rule colour, and `FIELD_LINE` in a light grey for white inputs.

### Look and feel — `javax.swing.plaf`

A look and feel is the code that paints a `JButton` as a Metal button, a GTK button, or a Windows button. `UIManager.getLookAndFeel()` returns the one installed now. On Ubuntu that is often the system GTK look.

`WeaponConfigurationPanel` does this around construction:

1. Remember the current look.
2. `MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme())`.
3. `UIManager.setLookAndFeel(new MetalLookAndFeel())`.
4. Create every control and call `build()`.
5. In `finally`, put the remembered look back.

A control picks up its painter when it is created. Putting the old look back does not repaint controls that already exist, because this code never calls `updateComponentTreeUI`. The form stays on the Metal painters it was born with. The blue page was created before the switch, and the footer is created after the restore, so those stay on the look the program started with.

`DefaultMetalTheme` is the classic Metal palette. From Java 5 onward the default Metal theme is Ocean. Setting the theme before `new MetalLookAndFeel()` is what selects the classic one.

If `setLookAndFeel` throws, the empty `catch` keeps the look that is already installed and construction continues. The `finally` still restores whatever was saved.

`MarkIcon` exists so the check mark and the radio dot are drawn by this project. The comment in `Mono` states why: the mark stays white on Ubuntu’s own look and feel. The icon ignores the look’s built-in radio gif.

### `java.util` — the parameter map

| Type | Role |
|---|---|
| `Map<String, JTextField>` | The field type of `parameters`. A map looks up a value by a key. |
| `LinkedHashMap<String, JTextField>` | The concrete map. `get` and `put` are the methods used. Keys are the labels `"P1"`, `"P2"`, `"P3"`, `"P4"`, `"P5"`, `"P6"`, `"P7"`, `"P8"`, `"P9"`. |

`parameters.put(name, field)` runs inside `namedField`, at the moment the row is built. `getParameter("P1")` later does `parameters.get("P1")` and returns that field’s `getText()`. The key must match the string passed to `namedField`. `"P1"` finds the field. `"p1"` misses, `get` returns `null`, and `getParameter` returns `""`.

`LinkedHashMap` remembers insertion order when someone iterates the keys. `readForm()` does not iterate. It asks for each name. The map is the link between a label on the screen and the `JTextField` object that holds what the user typed.

---

## 5. Maven, and what is not a library

`pom.xml` is the Maven build file. Maven is not imported by any Java file. It is not on screen. It compiles `.java` files into `.class` files and can pack them into a jar.

| Piece | Value | What it decides |
|---|---|---|
| `groupId` | `local.page` | A name for the family of builds. It does not appear in the window. |
| `artifactId` | `java-ui` | The build name. The jar is `java-ui-1.0.jar`. |
| `version` | `1.0` | The version in that jar name. |
| `maven.compiler.source` and `target` | `1.6` | The source language and the class-file version are Java 6. |
| `project.build.sourceEncoding` | `UTF-8` | Source files are read as UTF-8. The degree sign and the × are Unicode escapes anyway (`\u00B0`, `\u00D7`), so they survive even if an editor saves the file as another encoding. |
| `maven-compiler-plugin` 3.8.1 | | Runs `javac` at source 1.6 and target 1.6. |
| `maven-jar-plugin` 3.2.2 | | Writes the jar. `mainClass` is `page.FunctionPage`, so `java -jar` starts `FunctionPage.main`. |

There is no `<dependencies>` block. No third-party jar is added to the program.

---

## 6. Every class in this project

### `page.FunctionPage` — the window

`public class FunctionPage extends JFrame`

One object of this class is the sample window. It owns the blue page, the form, the footer, the one F-key listener, and the OK listener that talks to the backend.

| Member | What it does |
|---|---|
| `resultLabel` | The `JLabel` in the blue area. It starts as a single space so the label has a line height. OK sets it to `Saved `, the engagement number, ` / `, and the engagement state. Cancel sets it to `Cancel`. F1 sets it to `F1 called create()`. The other first-set keys write their own name. F4 writes which set is showing. |
| `functionListener` | One `ActionListener` for every footer button. It reads `getActionCommand()`. `"F4"` calls `onF4`. `"F1"` calls `create`. `"B1"` calls `onB1`. The rest follow the same pattern. |
| `footer` | The `FunctionKeyBar`. A field, so `onF4` can call `setLabels` after the window exists. |
| `secondSet` | False on the first set (F1–F12). True after F4, until F4 is pressed again. |
| `SECOND_KEYS` | The twelve captions of the second set: B1, B2, B3, F4, B5 through B12. |
| `FunctionPage()` | Builds the tree, wires OK and Cancel, sizes the window, centres it. |
| `create()` | F1. In this sample it only changes the label. In the other project this is where that key’s real work goes. |
| `onF4()` | Swaps the footer to the second set, or back to the first set. |
| `onF2()` … `onF12()` | First-set operations, except F4. Each one writes its key name on the label. |
| `onB1()` … `onB12()` | Second-set operations. There is no `onB4`. Slot 4 is F4 in both sets. |
| `main` | Sends window creation to the event thread and shows the frame. |

The OK listener is separate from `functionListener`. F1 does not press the form’s OK. The form’s OK does not call `create()`. They are two different buttons.

How you use it in this sample: run `page.FunctionPage`. How you use the idea on an existing window: do not copy this class onto that window. Copy the listener pattern. Their window constructs a `WeaponConfigurationPanel`, `add`s it, and hangs a listener on `getOkButton()`.

`serialVersionUID = 1L` is on every Swing subclass here. Swing components implement `Serializable`. Java asks a serializable class for a version number when it writes the object to a byte stream. This program never saves a window to disk. The field is there so the class has an explicit version. Leave it as `1L`.

### `page.FunctionKeyBar` — the footer

`public class FunctionKeyBar extends JPanel`

It draws twelve buttons and binds F1–F12. It does not decide what the keys mean. The screen passes in one `ActionListener`.

| Member | What it does |
|---|---|
| `KEYS` | The first twelve labels, `"F1"` through `"F12"`. Public. `onF4` passes this array back when it restores the first set. |
| `KEY_CODES` | `KeyEvent.VK_F1` through `VK_F12`, in the same order as `KEYS`. Private. The physical keys never change. |
| `FIRST_FACE` | Light blue `(186, 224, 248)`, the first set. |
| `SECOND_FACE` | Light orange `(255, 214, 153)`, the second set. |
| `buttons` | The twelve `JButton` objects. `setLabels` changes these. It does not create a new row. |
| `barHeight` | The height, in pixels, passed to the constructor. Stored so `getPreferredSize` can force it. |
| Constructor `(barHeight, buttonWidth, buttonHeight, listener)` | Builds the row, then `setLabels(KEYS, FIRST_FACE)`. This sample calls `new FunctionKeyBar(64, 56, 40, functionListener)`. |
| `setLabels(labels, face)` | Writes a new caption, command, and face colour on each button, and rebinds that slot’s physical key. `labels` must have twelve strings. |
| `getPreferredSize` | The superclass width, with the height replaced by `barHeight`. |
| `getMinimumSize` | The same as the preferred size, so a layout cannot squash the bar below that height. |
| `getMaximumSize` | The preferred height, and a width of `Integer.MAX_VALUE`, so a vertical layout may stretch the bar across the window. |
| `bindKey` | Connects one physical key to the shared listener. The map name is the key code, so a later call replaces the previous action. |

Inside the constructor the bar is a `BorderLayout`, opaque, colour `(168, 214, 245)`. A transparent `FlowLayout` row is added at `SOUTH`, so the buttons sit on the bottom of the 64-pixel strip and the spare blue is above them.

Each button is created empty, then `setLabels` fills it:

1. `setText` shows the caption, `F1` or `B1`.
2. `setActionCommand` stores that same string for the listener.
3. `setPreferredSize(new Dimension(buttonWidth, buttonHeight))` makes every key the same 56 by 40. That line is in the constructor, before `setLabels`.
4. The face colour comes from `setLabels`. The first call uses `FIRST_FACE`. Opaque, focus rectangle off, black 2-pixel border.
5. `addActionListener(listener)` — the single listener from the page, not a new listener per button. That line is also in the constructor.
6. `bindKey` for the matching `VK_F*` code.

`bindKey` puts `KeyStroke.getKeyStroke(keyCode, 0)` into the window input map under the name `"function-"` plus the key code, and puts an `AbstractAction` in the action map. When the key fires, that action calls `listener.actionPerformed` with a new `ActionEvent`. The source is the bar (`FunctionKeyBar.this`). The command is the caption on that slot. The page never looks at the source. Calling `bindKey` again for the same key code replaces the action, so F1 sends `"B1"` after the swap and `"F1"` after the swap back.

How you use it:

```java
FunctionKeyBar footer = new FunctionKeyBar(64, 56, 40, functionListener);
page.add(footer, BorderLayout.SOUTH);
```

Change `64`, `56`, and `40` to match the other screen. The listener branches on the caption (`"F1"`, `"B1"`, `"F4"`). To add behaviour you edit the listener, not the bar. To swap the captions:

```java
footer.setLabels(secondLabels, FunctionKeyBar.SECOND_FACE);
footer.setLabels(FunctionKeyBar.KEYS, FunctionKeyBar.FIRST_FACE);
```

Keep a `"F4"` in the same slot of both arrays when that key is what switches back.

### `page.WeaponConfigurationPanel` — the form

`public class WeaponConfigurationPanel extends JPanel`

This is the dark Create Engagement panel. It builds every control, holds them in fields, and copies them out through `readForm()`.

`PANEL_WIDTH` is 620. Preferred size is 620 by 700. Minimum size is 620 by 240. Maximum width stays 620 and maximum height is unlimited, so `BorderLayout.EAST` can stretch it to the full height of the window and keep the width at 620.

The constructor:

1. `super(new BorderLayout())` so the panel itself has north / center / south.
2. Switches the look to Metal, as described above.
3. Creates every control.
4. Calls `build()` inside `try` / `finally`, and restores the look in `finally`.

| Field | Control | Starts as |
|---|---|---|
| `field1` | `HintField`, 18 columns | Hint “Enter field 1”. |
| `field2` | `HintField`, 18 columns | Hint “Enter field 2”. |
| `group1` | `OptionGroup` | `"Choice A"` selected (`selectedIndex` 0). Choices are Choice A, Choice B. 2 columns. |
| `group2` | `CheckGroup` | Neither box ticked. Choice C, Choice D. 2 columns. |
| `group3` | `OptionGroup` | `"Choice F"` selected (`selectedIndex` 1). |
| `field3` | ordinary `JTextField` | Text `"0"`. |
| `group4` | `OptionGroup` | `"Choice 1"` selected. That choice is index 5 in the array, which is the last cell of the 3 by 2 grid. |
| `group5` | `JComboBox` | `"Choice G"`. |
| `field4` | `JComboBox` | `"Choice H"`. |
| `field5` | `JComboBox` | `"Choice I"`. |
| `field6` | `HintArea`, 4 rows, 20 columns | Hint “Enter field 6”. |
| `cancelButton` | `Mono.button("Cancel")` | No listener until the page adds one. |
| `okButton` | `Mono.accentButton("OK")` | No listener until the page adds one. |
| `closeButton` | a `JButton` whose text is × (`\u00D7`) | Its listener is added in `titleBar` and calls `setVisible(false)` on this panel. |
| `parameters` | `LinkedHashMap` | Filled by `namedField` for P1, P2, P3, P4, P5, P6, P7, P8, P9. |

`build()` paints the panel, then adds three children: `titleBar()` at north, `scroller(form())` at center, `buttonBar()` at south.

`form()` returns a `FormStack` with 12 pixels of padding on the left and right and on the bottom, and 4 on the top. Top to bottom the stack is:

1. `FormRow` “Field 1 *” + `field1`.
2. An 8-pixel vertical strut.
3. `FormRow` “Field 2 *” + `field2`.
4. Heading “Section 1”, then `section1Card()`.
5. Heading “Section 2”, then `section2Card()`.
6. Heading “Section 3”, then `section3Card()`.
7. Heading “Section 4”, then `section4Card()`.

`section1Card()` is a `FormCard`. Inside it, a `GroupLabel` introduces each cluster, then the cluster itself: Group 1, Group 2, Group 3, the Field 3 line, Group 4, the Group 5 combo.

The Field 3 line is a centred `FlowLayout` panel: the words “Field 3”, the text field, and a small OK from `Mono.button("OK")`. That small OK has no listener. Pressing it does not call `readForm()`.

`section2Card()` is five pair-rows. Three of them are two named fields. The fourth is P7 on the left and the Field 4 combo on the right. The fifth pair is P8 and P9. The sample values are plain numbers such as `"0.00"`.

`section3Card()` is one `FormRow`: “Field 5 *” and the Field 5 combo. `section4Card()` is the hint area inside a `Block` with 4 pixels of padding.

`buttonBar()` is a right-aligned flow with a one-pixel top rule. Cancel is added first, then the cyan OK, so OK is the rightmost control.

**Reading the screen.** `getField1`, `getField2`, `getGroup1`, `getGroup2`, `getGroup3`, `getGroup4`, and `getField6` each return one string. `getParameter(name)` returns one parameter field’s text, or `""` when the name is absent. `readForm()` is the method the OK path uses. It builds a new `Engagement` and sets every property. `selected(box)` is the combo helper: `getSelectedItem()`, then `toString()`, or `""` when the item is null.

How you use the panel:

```java
final WeaponConfigurationPanel form = new WeaponConfigurationPanel();
add(form, BorderLayout.EAST);

form.getOkButton().addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent event) {
        Engagement engagement = form.readForm();
        service.create(engagement);
    }
});
```

`getCancelButton()` is there so the screen can hide the form or clear a status line. The panel does not save on Cancel.

To add a field that should reach the backend: create the control, put it on a row in `build`’s helpers, store it in a field if it is a single control, add a property on `Engagement`, and copy it inside `readForm()`. A parameter-style field can go through `namedField`, which both shows it and registers the name in the map.

### `page.model.Engagement` — the values

`public class Engagement`

A JavaBean of strings. No Swing import. The backend can use it without a window.

Each property is a private `String` that starts as `""`, with `getX()` and `setX(String)`. The names:

| Property | Screen source |
|---|---|
| `field1` | Field 1, via `getValue()` |
| `field2` | Field 2, via `getValue()` |
| `group1` | Choice A or Choice B |
| `group2` | Ticked choice names, joined by `", "` |
| `group3` | Choice E or Choice F |
| `field3` | The Field 3 text, including the starting `"0"` |
| `group4` | Choice 1 … Choice 6 |
| `group5` | The Group 5 combo |
| `p1`, `p2`, `p3`, `p4`, `p5`, `p6`, `p7` | Those parameter fields |
| `field4` | The Field 4 combo |
| `p8`, `p9` | P8 and P9 |
| `field5` | The Field 5 combo |
| `field6` | The Section 4 area, via `getValue()` |

The strings are the characters from the screen. `"305.00"` stays a string. Parsing to `double` belongs in the method that receives the object, after it has arrived.

A new `Engagement` is created on every `readForm()` call. Editing the form later changes the controls. It does not change an `Engagement` that was already returned.

How you use it: call the setters from `readForm()`, call the getters in `create()`. If the other project already has its own engagement class, fill that class instead and do not keep two.

### `page.backend.EngagementService` — the receiving method

`public class EngagementService`

| Member | What it does |
|---|---|
| `lastCreated` | The last `Engagement` passed to `create`, or `null` before the first OK. |
| `create(Engagement)` | Stores that reference. This is the stand-in for a real save. |
| `getLastCreated()` | Returns it. |

`create` does not read a `JTextField`. It does not know which button was pressed. The page has already copied the screen.

How you use it: keep the shape of the call, `service.create(engagement)`. Replace the body with the save that already exists in the other project, or replace `EngagementService` with that project’s class and call its method.

### `page.form.Mono` — colours, fonts, and factory methods

`public final class Mono`

A final class with a private constructor. Nothing does `new Mono()`. Every method and colour is `static`.

| Constant | Value | Where it shows |
|---|---|---|
| `BACKGROUND` | RGB `(11, 22, 34)` | The form, cards, labels, dark buttons. A near-black navy. |
| `FOREGROUND` | `Color.WHITE` | Label text, radio text, the ×. |
| `ACCENT` | RGB `(0, 214, 214)` | Headings, the filled OK, the selected radio dot and check mark. |
| `FIELD` | `Color.WHITE` | Text fields, combos, the comment area. |
| `FIELD_TEXT` | RGB `(20, 28, 38)` | Typed text and the caret. |
| `MUTED` | RGB `(130, 140, 150)` | Hint text while the hint is showing. |
| `RULE` | RGB `(70, 90, 110)` | Card outlines, the button-bar top edge, separator lines, the dark button border. |
| `FONT` | Dialog, plain, 13 | Body text and fields. |
| `TITLE_FONT` | Dialog, bold, 18 | “Create Engagement” and the ×. |
| `HEADING_FONT` | Dialog, bold, 15 | Cyan section headings. |

| Method | What it returns or changes |
|---|---|
| `paint(component)` | Opaque, `BACKGROUND`, `FOREGROUND`, `FONT`. Call it on any dark region or label. |
| `field(columns)` | A white `JTextField` with dark text and a light border. |
| `area(rows, columns)` | A white wrapping `JTextArea`. The current form uses `HintArea` instead of this method. The method is there for a comment box that has no hint. |
| `styleField(component)` | White, opaque, body font, light border. If the component is a `JTextField`, the caret becomes `FIELD_TEXT`. `HintField` and `HintArea` both start here. |
| `combo(items)` | A `JComboBox` on the white field colours, with a renderer that paints every drop-down row the same way. |
| `button(text)` | A dark button, white text, rule-coloured line, padding 4, 16, 4, 16. Cancel and the small Field 3 OK. |
| `accentButton(text)` | Cyan fill, dark text, bold 13, padding 6, 22, 6, 22. The bottom OK. |
| `check(text)` | A check box with `MarkIcon` for off and on. |
| `radio(text)` | A radio button with the round `MarkIcon` for off and on. |

`paint` on a `JButton` sets the button opaque and dark. `accentButton` does not call `paint`. It sets the cyan background itself. Calling `paint` on that button would turn it navy.

**`MarkIcon`.** `implements Icon`. The constructor takes `radio` (circle when true, square when false) and `selected`. Size is 14 by 14. Unselected outline is RGB `(190, 198, 206)`. Selected outline and the inner fill are `ACCENT`. The inner shape is inset by 3 pixels. `JCheckBox` and `JRadioButton` swap icons through `setIcon` (off) and `setSelectedIcon` (on). Swing chooses which icon to paint from `isSelected()`.

**`TriangleIcon`.** `implements Icon`. Size is 10 by 10. White. `open == true` draws a downward triangle (a wide top and a point at the bottom). `open == false` draws a rightward triangle. `SectionPanel` replaces the icon by constructing a new `TriangleIcon` when the section toggles. Icons are not told to animate. The label receives a new icon and repaints.

How you use `Mono`: call `Mono.field`, `Mono.combo`, `Mono.button`, or `Mono.paint` when you add a control, so the new control matches this form. Change a colour once, in `Mono`, when the whole form should change.

### `page.form.Block` — one row that refuses to stretch tall

`public class Block extends JPanel`

`BoxLayout` on the parent gives extra height to children whose maximum height is larger than their preferred height. A plain `JPanel` reports a huge maximum height, so a single row would eat the card.

`Block` answers:

* maximum width = `Integer.MAX_VALUE`, maximum height = preferred height
* minimum width = 0, minimum height = preferred height

The row can grow sideways to the card width. It stays as tall as its label, field, or buttons. The constructor calls `Mono.paint`, so a block is dark unless a child covers it.

`FormRow`, `FormCard`, `Heading`, `GroupLabel`, `OptionGroup`, `CheckGroup`, and `SectionPanel` all extend `Block`. Extend it again for a new kind of row that will be added to a `FormStack` or a `FormCard`.

### `page.form.FormStack` — the scrolling body

`public class FormStack extends JPanel implements Scrollable`

Vertical `BoxLayout`, painted dark. It is the child of the `JScrollPane`.

| `Scrollable` method | Return value | Effect |
|---|---|---|
| `getPreferredScrollableViewportSize` | `getPreferredSize()` | The size the scroll pane would like for the view. |
| `getScrollableUnitIncrement` | `16` | Arrow and wheel step, in pixels. The arguments (visible rectangle, orientation, direction) are ignored. The step is always 16. |
| `getScrollableBlockIncrement` | viewport height minus 16, and at least 16 | Page up and page down. |
| `getScrollableTracksViewportWidth` | `true` | Width follows the scroll pane. Rows stretch. No horizontal scrollbar is required. |
| `getScrollableTracksViewportHeight` | `false` | Height stays the height of the rows. A short window scrolls. |

How you use it: `FormStack form = new FormStack(); form.add(row);` then put the stack in a `JScrollPane`. Add `Block` rows, headings, cards, or a `Box` strut. Set `alignmentX` to the left on anything that is not a `Block` and might sit narrower than the stack.

### `page.form.FormCard` — a bordered group

`public class FormCard extends Block`

Vertical `BoxLayout`. The border is a compound: a one-pixel `RULE` line outside, empty padding `(6, 8, 8, 8)` inside.

`body` is assigned `this`. `addRow` sets the child’s left alignment and adds it to the card. There is no extra inner panel.

How you use it:

```java
FormCard card = new FormCard();
card.addRow(new GroupLabel("Group 4"));
card.addRow(group4);
form.add(card);
```

One card is one boxed group. Section 1, Section 2, Section 3, and Section 4 are four cards.

### `page.form.FormRow` — label on the left, control on the right

`public class FormRow extends Block`

`BorderLayout` with an 8-pixel horizontal gap. The label’s preferred size is 168 by 24, so Field 1, Field 2, and Field 5 share a label column. The field goes in `CENTER` and receives the rest of the width.

The field argument is a `JComponent`, so it can be a `HintField`, a `JComboBox`, or a small panel.

How you use it: `new FormRow("Field 1 *", field1)`. The star is part of the label string. The class does not treat `*` as special.

### `page.form.Heading` — a cyan section title

`public class Heading extends Block`

Empty padding `(12, 2, 6, 2)`, so there is space above the title. A `JLabel` in `WEST`, foreground `ACCENT`, font `HEADING_FONT`. The text is whatever you pass: “Section 1”, “Section 2”, “Section 3”, “Section 4”.

How you use it: `form.add(new Heading("Section 2"));` immediately before the card it names.

### `page.form.GroupLabel` — a centred title between two lines

`public class GroupLabel extends Block`

`GridBagLayout`. A `JSeparator` grows on the left, the label sits in the middle, a `JSeparator` grows on the right. Padding around the whole row is `(8, 4, 4, 4)`. Each separator’s foreground is `RULE` and its preferred size is 40 by 2. The weight on the lines is what lets them grow past 40 pixels.

The label is painted with `Mono.paint`, so the words are white, not cyan. Cyan is reserved for `Heading`.

How you use it: `card.addRow(new GroupLabel("Group 2"));` as the line above a group, inside a card.

### `page.form.OptionGroup` — one choice among radios

`public class OptionGroup extends Block`

```java
new OptionGroup(choices, selectedIndex, columns)
```

| Argument | Meaning |
|---|---|
| `choices` | The labels, in order. One `JRadioButton` per string. |
| `selectedIndex` | Which index starts selected. `-1` leaves all of them clear. |
| `columns` | How many sit side by side. Values below 1 are treated as 1. |

A `ButtonGroup` makes the set exclusive. `getSelected()` returns the selected button’s text, or `""` when none is selected. The text is the label you passed in, such as `"Choice 1"`.

How you use it:

```java
OptionGroup state = new OptionGroup(new String[] {"Choice A", "Choice B"}, 0, 2);
String chosen = state.getSelected();
```

Index `0` is the first label. The current form uses index `0` for Choice A, index `1` for Choice F, and index `5` for Choice 1.

### `page.form.CheckGroup` — ticks that do not exclude each other

`public class CheckGroup extends Block`

```java
new CheckGroup(labels, columns)
```

The grid math matches `OptionGroup`. There is no `ButtonGroup`. Each box toggles alone. Nothing starts selected, because the constructor never calls `setSelected(true)`.

`getSelected()` walks the boxes. For each selected box it appends `getText()`. The separator between two labels is `", "`. No selection returns `""`. Both Group 2 boxes return `"Choice C, Choice D"`. The buffer is a `StringBuffer`.

How you use it:

```java
CheckGroup group2 = new CheckGroup(new String[] {"Choice C", "Choice D"}, 2);
String ticked = group2.getSelected();
```

### `page.form.HintField` — one line with grey hint text

`public class HintField extends JTextField`

Java 6 does not provide placeholder text on a text field. `HintField` stores the hint as the field’s real text and remembers that fact in `showingHint`.

Construction: `super(columns)`, remember the hint, `Mono.styleField` (white background), `showHint()`, then a focus listener.

`showHint` sets `showingHint` to true, `setText(hint)`, and the foreground to `MUTED`.

On `focusGained`, if the hint is showing, the field clears, the foreground becomes `FIELD_TEXT`, and `showingHint` becomes false. The user then types real text.

On `focusLost`, if `getText().trim()` has length 0, the hint comes back. Spaces alone count as empty because of `trim()`.

`getValue()` returns `""` while the hint is showing, and `getText()` otherwise. `readForm` uses `getValue()` for Field 1, Field 2, and Field 6. Calling `getText()` while the hint is up would hand the sentence “Enter field 1” to the backend.

The columns argument is still the `JTextField` preferred width. 18 does not cap the length.

How you use it:

```java
HintField field1 = new HintField("Enter field 1", 18);
String number = field1.getValue();
```

### `page.form.HintArea` — the same hint, on several lines

`public class HintArea extends JTextArea`

Same `showingHint` rule as `HintField`. The constructor also turns on line wrap and word wrap. `getValue()` is the method to read. The Section 4 card uses `new HintArea("Enter field 6", 4, 20)`.

Focus behaviour matches the field: click in, the hint clears; leave it empty, the hint returns; `getValue()` is `""` whenever the hint is visible.

### `page.form.SectionPanel` — a block that opens and closes

`public class SectionPanel extends Block`

This class is in the project. The current Create Engagement form does not add one. Headings on that form are `Heading` plus a `FormCard`, and they stay open.

The panel is a `BorderLayout`. North is a header: a `TriangleIcon` and a title, in a left `FlowLayout`, painted dark. Center is a body with a vertical `BoxLayout` and padding `(2, 22, 8, 4)`, so content sits to the right of the triangle.

A mouse click on the header calls `setOpen(!open)`. `setOpen`:

1. Stores the flag.
2. Replaces the triangle icon (down when open, right when closed).
3. `body.setVisible(open)`.
4. `revalidate()` and `repaint()` so the form height changes and the new triangle is drawn.

The header listener is on the header panel, so a click on the title or the triangle both toggle it. `isOpen()` returns the flag. The constructor starts open.

How you use it:

```java
SectionPanel section = new SectionPanel("Section 1");
section.addContent(section1Card());
form.add(section);
section.setOpen(false);
```

`addContent` sets left alignment and adds to the body. Put rows in with `addContent`. Put the section itself on the stack with `add`.

---

## 7. Objects that are not classes of their own

These are objects the screen creates from JDK classes, without a new `.java` file.

| Object | Created in | Job |
|---|---|---|
| The page panel | `FunctionPage` | Light blue `JPanel`, `BorderLayout`, holds the body and the footer. |
| The blue panel | `FunctionPage` | Holds `resultLabel` at `NORTH`. Same blue, `(168, 214, 245)`. |
| The body panel | `FunctionPage` | Transparent. Blue area in `CENTER`, form in `EAST`. |
| `resultLabel` | `FunctionPage` | Dialog 16 plain. Empty border 16 pixels. Shows the last action. |
| The F-key row | `FunctionKeyBar` | Transparent flow of twelve buttons. |
| Each F-key `JButton` | `FunctionKeyBar` | 56 by 40, black border. Pale blue on the first set, light orange on the second set. |
| Each key `AbstractAction` | `FunctionKeyBar.bindKey` | Turns that key into an `ActionEvent` for the shared listener. |
| The title bar | `WeaponConfigurationPanel` | Dark bar, title on the west, × on the east. |
| The title `JLabel` | title bar | White, `TITLE_FONT`. |
| The × button | `closeControl` | Character ×, title font, empty border, content area not filled. Hides the form. |
| The button bar | `buttonBar` | Dark, top rule, Cancel then OK, aligned right. |
| The small Field 3 OK | `field3Row` | A dark `Mono.button`. No listener. |
| Each parameter line | `namedField` | A 44-by-24 label plus a white field, registered in the map. |
| The Field 4 line | `field4Row` | The words “Field 4” plus the Field 4 combo. |
| Vertical strut | `form()` | 8 pixels between the two name rows. |
| `ButtonGroup` inside each `OptionGroup` | `OptionGroup` | Keeps that group’s radios exclusive. One group per `OptionGroup` object, so Group 4 does not clear Group 3. |
| Metal look and `DefaultMetalTheme` | form constructor | Installed only while the form’s controls are created. |
| `previous` look | form constructor | The look that was current, restored in `finally`. |

The page blue and the footer blue are the same `new Color(168, 214, 245)`. The F-key face is a lighter `new Color(186, 224, 248)`. The form does not use that blue. The form uses `Mono.BACKGROUND`.

---

## 8. How the pieces call each other

```text
main
  invokeLater
    new FunctionPage()
      new WeaponConfigurationPanel()
        Metal look on
        new HintField, OptionGroup, CheckGroup, JComboBox, HintArea, JButton
        build()
          FormStack, Heading, FormCard, FormRow, GroupLabel
        Metal look restored
      OK listener: readForm() then EngagementService.create()
      new FunctionKeyBar(..., functionListener)
      setVisible(true)

User clicks the bottom OK
  OK listener
    readForm() fills a new Engagement from the controls
    create(engagement) stores it
    resultLabel shows the number and the state

User presses F1
  input map on the bar, or the F1 button
    functionListener
      create() on FunctionPage
        resultLabel shows "F1 called create()"

User presses F4
  onF4()
    setLabels writes B1…B12 and the orange face
    the F1 key now sends "B1"
  F4 again
    setLabels writes F1…F12 and the blue face
```

`OptionGroup.getSelected` reads `JRadioButton.isSelected` and `getText`. `CheckGroup.getSelected` does the same with `JCheckBox`. `HintField.getValue` reads the `showingHint` flag before it trusts `getText`. `getParameter` reads the map, then `JTextField.getText`. `selected` reads `JComboBox.getSelectedItem`. `readForm` is the only method that performs all of those reads in one place.

---

## 9. How to use them

**Show this sample.** The main class is `page.FunctionPage`. The window opens on the event thread from `main`.

**Put the form on a window you already have.**

```java
final WeaponConfigurationPanel form = new WeaponConfigurationPanel();
add(form, BorderLayout.EAST);
```

Use `CENTER` when the form should fill the space. Keep the variable `final` when an anonymous listener uses it.

**Save from the bottom OK.**

```java
form.getOkButton().addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent event) {
        Engagement engagement = form.readForm();
        service.create(engagement);
    }
});
```

`service.create` stands for the method that already stores an engagement. Inside that method, read `engagement.getField1()`, `getGroup4()`, `getP1()`, and the other getters.

**Hide the form.**

```java
form.setVisible(false);
```

The × button does that already. Wire `getCancelButton()` the same way when Cancel should close without saving.

**Add the footer to a window.**

```java
FunctionKeyBar footer = new FunctionKeyBar(64, 56, 40, listener);
add(footer, BorderLayout.SOUTH);
```

The listener reads `event.getActionCommand()` and compares it to `"F1"`, `"B1"`, `"F4"`, and the rest. F4 calls `setLabels` to swap the twelve captions, and the next F4 swaps them back.

**Build a matching control.**

```java
JTextField field3 = Mono.field(10);
JComboBox box = Mono.combo(new String[] {"Choice H"});
JButton cancel = Mono.button("Cancel");
JButton ok = Mono.accentButton("OK");
JRadioButton radio = Mono.radio("Yes");
JCheckBox check = Mono.check("Choice C");
```

**Build a matching group.**

```java
OptionGroup group4 = new OptionGroup(
        new String[] {"Choice 1", "Choice 2"}, -1, 2);
CheckGroup group2 = new CheckGroup(
        new String[] {"Choice C", "Choice D"}, 2);
```

`-1` starts the radios with nothing selected. `getSelected()` is then `""` until the user clicks one.

**Add a labelled line and a section.**

```java
FormStack stack = new FormStack();
stack.add(new FormRow("Field 1 *", new HintField("Enter field 1", 18)));
stack.add(new Heading("Section 1"));
FormCard card = new FormCard();
card.addRow(new GroupLabel("Group 4"));
card.addRow(group4);
stack.add(card);
```

**Read a hint field.** Call `getValue()`. An empty return means the user left the hint in place, or cleared the field and left it.

**Read a parameter you registered with `namedField`.** Call `getParameter("P1")` with the same spelling and the same capitals.

**Open and close a section.** `new SectionPanel("Title")`, `addContent` for each row, `setOpen(false)` to start closed.

**Change the shared colours.** Edit the constants on `Mono`. `paint`, `field`, `combo`, `button`, `check`, and `radio` all read those constants.

**Change what F1 does.** Edit `FunctionPage.create()`. The bar already delivers the click. **Change what the second set does.** Edit `onB1` and the other `onB` methods. F4 already swaps the captions. Keep `"F4"` in slot 4 of both arrays.

**Change what OK saves.** Edit the listener so it passes `readForm()` to the real save method. Edit `EngagementService.create` when this sample is the place that should keep the object.
