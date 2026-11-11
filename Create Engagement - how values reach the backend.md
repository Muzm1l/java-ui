# Create Engagement — how the values reach the backend

There is no separate server in this sample. The backend is a normal Java method. The form does not send itself. OK reads the fields into one object, and that object is the only thing the backend method receives.

```text
You type in the form
        |
        v
You press the bottom OK
        |
        v
WeaponConfigurationPanel.readForm()     copies every field
        |
        v
Engagement                              a plain object, no buttons inside it
        |
        v
EngagementService.create(engagement)    this is the backend
```

The three files are:

| File | Role |
|---|---|
| `src/main/java/page/WeaponConfigurationPanel.java` | The form. `readForm()` copies the screen. |
| `src/main/java/page/model/Engagement.java` | The values, after they have left the screen. |
| `src/main/java/page/backend/EngagementService.java` | The method that receives them. |

The call sits in `FunctionPage`, on the bottom OK button:

```java
Engagement engagement = form.readForm();
backend.create(engagement);
```

`readForm()` runs first. `create()` runs second. `create()` never asks a text field what it contains.

---

## 1. What “backend” means here

The form is Swing. Swing components (`JTextField`, `JComboBox`, `JRadioButton`) live on the screen. A backend method should not be given those components. If it calls `getText()` itself, it is tied to this window and it can only do that on Swing’s event thread.

The split used here:

1. The screen reads its own components.
2. It stores the answers in an `Engagement`.
3. It hands that `Engagement` to `create()`.

In the other project, keep this split. Find the method that already saves an engagement and call it from the OK listener, passing the object you just filled. Do not pass the panel.

The small **OK** next to Field 3 does not do this. Only the **OK** at the bottom of the form calls `readForm()` and then `create()`.

---

## 2. `readForm()` — the copy

`readForm()` is on the panel because only the panel can see the private fields. It creates a new `Engagement` and sets one property per control.

```java
public Engagement readForm() {
    Engagement engagement = new Engagement();
    engagement.setField1(field1.getValue());
    engagement.setField2(field2.getValue());
    engagement.setGroup1(group1.getSelected());
    // ... the rest of the fields ...
    engagement.setField6(field6.getValue());
    return engagement;
}
```

The labels on the form are abstract (`Field 1`, `Group 1`, `Choice A`, `P1`). The same words are the Java names. The real names are not in this sample.

Each setter matches one control:

| What you see | How it is read | Where it lands |
|---|---|---|
| Field 1 | `field1.getValue()` | `setField1` |
| Field 2 | `field2.getValue()` | `setField2` |
| Choice A / Choice B | `group1.getSelected()` | `setGroup1` |
| Choice C, Choice D | `group2.getSelected()` | `setGroup2` |
| Choice E / Choice F | `group3.getSelected()` | `setGroup3` |
| Field 3 | `field3.getText()` | `setField3` |
| Choice 1 … Choice 6 | `group4.getSelected()` | `setGroup4` |
| Group 5 combo | `selected(group5)` | `setGroup5` |
| P1, P2, P3, P4, P5, P6, P7 | `getParameter("P1")` and the same for the other names | `setP1`, `setP2`, `setP3`, `setP4`, `setP5`, `setP6`, `setP7` |
| Field 4 combo | `selected(field4)` | `setField4` |
| P8, P9 | `getParameter("P8")`, `getParameter("P9")` | `setP8`, `setP9` |
| Field 5 combo | `selected(field5)` | `setField5` |
| Field 6 | `field6.getValue()` | `setField6` |

`getSelected()` on a radio group returns the label of the selected button, for example `"Choice A"` or `"Choice 1"`. If nothing is selected it returns `""`.

`group2.getSelected()` joins the ticked boxes. None ticked is `""`. Both ticked is `"Choice C, Choice D"`.

`selected(box)` is the combo version. `getSelectedItem()` returns an `Object`. The helper turns it into a `String`, or `""` when the combo has no item.

`getParameter("P1")` looks up the text field that was stored under the name `"P1"` when the row was built. The name must match the label used in `parameterPair`. `"P1"` finds the field. `"p1"` does not.

---

## 3. Hint text is not a value

Field 1, Field 2, and Field 6 start with grey hint text: “Enter field 1”, “Enter field 2”, “Enter field 6”.

Those fields are `HintField` and `HintArea`. While the hint is showing, `getValue()` returns `""`. It does not return the hint sentence.

`getText()` would return the hint, which is why `readForm()` uses `getValue()` for those three. Field 3 and the parameter boxes are ordinary fields, so `getText()` is the right call for them.

If the user types a number and then deletes it, the hint comes back and `getValue()` is `""` again.

---

## 4. `Engagement` — the object the backend sees

`page.model.Engagement` is a JavaBean. Every value is a private `String` with a getter and a setter. It has no `JTextField`, no `JButton`, and no import from `javax.swing`.

After `readForm()` returns, the backend reads with getters:

```java
String number = engagement.getField1();
String state = engagement.getGroup1();
String choice = engagement.getGroup4();
String p1 = engagement.getP1();
```

The strings are exactly what was on the screen. `getP1()` might be `"305.00"`. It is still a `String`. This sample does not parse it into a number. If the other project needs a `double`, parse it inside the backend method, after the object has arrived.

A new `Engagement` is created on every OK click. Later edits to the form do not change an object that was already passed in.

---

## 5. `EngagementService.create` — where you receive them

```java
public void create(Engagement engagement) {
    lastCreated = engagement;
}
```

This is the receiving method. The argument `engagement` is the whole form. Inside `create` you use the getters from section 4.

`lastCreated` only proves the object arrived. `getLastCreated()` returns it. A real project replaces the body of `create` with the call that already exists there, for example a database save or a method on an existing service. The signature stays `create(Engagement engagement)` or whatever that project already names the method. The important part is that the parameter is the data object, not the panel.

OK is pressed on Swing’s event thread, so `readForm()` is allowed to touch the components. Do the reading there. If `create()` does slow work, pass it the `Engagement` and do that work after the copy. Do not keep the panel and read the fields later from another thread.

---

## 6. The OK listener, line by line

From `FunctionPage`:

```java
final EngagementService backend = new EngagementService();
form.getOkButton().addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent event) {
        Engagement engagement = form.readForm();
        backend.create(engagement);
        resultLabel.setText("Saved " + engagement.getField1()
                + " / " + engagement.getGroup1());
    }
});
```

1. `backend` is created once, with the window. `final` is required because the anonymous listener uses it. Java 6 only allows that when the local variable is `final`.
2. `actionPerformed` runs when the bottom OK is clicked.
3. `readForm()` copies the screen into a new `Engagement`.
4. `create(engagement)` is the backend call. This is the line to point at the other project’s save method.
5. The label on the blue page is only a check that the copy worked. It is not the backend. Delete that line when you connect a real save.

Cancel does not call `readForm()` and does not call `create()`.

---

## 7. Adding the form to an existing screen

Do this on the window that is already in the other project. Do not copy `FunctionPage`. That class is only the blue practice window in this sample. The form you add is `WeaponConfigurationPanel`.

### Files to copy

Copy these into that project’s source tree, keeping the same folders under the new package:

| Copy this | Why |
|---|---|
| `page/WeaponConfigurationPanel.java` | The Create Engagement form. |
| `page/form/` (every file in that folder) | The rows, cards, colours, hints. The panel does not compile without them. |
| `page/model/Engagement.java` | The object `readForm()` fills. Skip it if that project already has its own engagement class. |
| `page/backend/EngagementService.java` | Only if you want a place to receive the object. If a save method already exists, call that method and do not copy this class. |

Leave `FunctionPage.java` and `FunctionKeyBar.java` behind unless that screen also needs the F1–F12 footer. The footer is a separate component. Adding the form does not require it.

### Change the package line

Every copied file starts with `package page...;`. Change that line to the package of the screen you are joining. The form files must all use the same package, or you must fix the `import page.form...` lines to match the folders you put them in.

A file in `com.example.screen` starts with:

```java
package com.example.screen;
```

### Find the existing screen

In that project, search for:

* `extends JFrame` or `extends JDialog` — the window
* `extends JPanel` — a screen that is already inside a window
* `setVisible(true)` — where that screen is shown
* `addActionListener` on the button that should open Create Engagement

Open the class that draws the screen you want the form on. The work goes in that class, usually in its constructor, after the other panels are created.

### Put the form on the screen

The form is a `JPanel`. Add it the same way that screen adds any other panel.

If the screen uses `BorderLayout` and the form should sit on the right, as it does in this sample:

```java
WeaponConfigurationPanel form = new WeaponConfigurationPanel();
add(form, BorderLayout.EAST);
```

If the form should fill the screen:

```java
WeaponConfigurationPanel form = new WeaponConfigurationPanel();
add(form, BorderLayout.CENTER);
```

If the screen uses another layout, find the `add(...)` call that places the panel you want to sit beside, and add the form in that same place. Do not wrap it in `FunctionPage`.

`form` must be `final` if an anonymous OK listener below uses it. Java 6 only lets that listener read a local variable when the variable is `final`.

```java
final WeaponConfigurationPanel form = new WeaponConfigurationPanel();
```

### Connect the bottom OK to the existing save

After the form is added, hang one listener on its OK button. This is the whole hand-off:

```java
final YourExistingService service = new YourExistingService();
form.getOkButton().addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent event) {
        Engagement engagement = form.readForm();
        service.create(engagement);
    }
});
```

Replace `YourExistingService` and `create` with the class and method that project already uses to store an engagement. If that method expects the project’s own object instead of this `Engagement`, fill their object inside `readForm()` and pass that.

If the form should open only when a button or F1 is pressed, still construct it the same way, then show it from that button:

```java
createButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent event) {
        form.setVisible(true);
    }
});
```

The **×** on the form calls `setVisible(false)`. Wire Cancel the same way if that screen should hide the form instead of saving:

```java
form.getCancelButton().addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent event) {
        form.setVisible(false);
    }
});
```

### What you should see in their code

Search the existing screen for these words when you are looking for the spot to join:

| Search | You are looking for |
|---|---|
| `extends JFrame` | The window class. |
| `new BorderLayout` | A screen you can add the form to with `EAST` or `CENTER`. |
| `addActionListener` | The button that should open the form, or their current OK. |
| `getText()` or `getSelectedItem()` | Their current way of reading fields. Yours is `readForm()` instead. |
| `actionPerformed` | The method that runs on a click. Your OK listener is one of these. |

Their OK method is the backend entry. Your listener should end by calling it. Do not copy their screen into `FunctionPage`, and do not give their save method the `WeaponConfigurationPanel`. Give it the `Engagement` from `readForm()`.
