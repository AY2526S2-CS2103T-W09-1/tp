---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Harmony Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the [AddressBook-Level3 (AB3)](https://se-education.org/addressbook-level3/) project created by the [SE-EDU initiative](https://se-education.org). The overall architecture, component structure (UI, Logic, Model, Storage), and base documentation were adapted from AB3.
* The undo feature design was inspired by the proposed undo/redo implementation described in the [AB3 Developer Guide](https://se-education.org/addressbook-level3/DeveloperGuide.html). Our implementation differs in that it uses per-command state capture rather than the VersionedAddressBook (full snapshot) approach.
* The `CopyCommand` uses the Java AWT `Clipboard` API (`java.awt.Toolkit`) to interface with the operating system clipboard.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S2-CS2103T-W09-1/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S2-CS2103T-W09-1/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `contact delete 1`.
<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103T-W09-1/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter`, `ViewPanel` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI. `ViewPanel` is a custom addition that displays the full profile (games and aliases) of a selected contact.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103T-W09-1/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103T-W09-1/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103T-W09-1/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("contact delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `contact delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteContactCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteContactCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteContactCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve. 
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`. 
5. If the command implements the `UndoableCommand` interface, `LogicManager` pushes it onto the `commandHistory` stack so it can be reversed by a subsequent `undo` command.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `ContactCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddContactCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `ContactCommandParser`, `DeleteContactCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g., during testing.
### Model component
**API** : [`Model.java`](https://github.com/AY2526S2-CS2103T-W09-1/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)



### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-W09-1/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Copy Command Feature
The `copy` command allows users to quickly serialize a specific contact's complex profile (including their name, games, and aliases) into a perfectly formatted, executable CLI string (e.g., `contact add n/John Doe g/Valorant...`) and saves it directly to their operating system's clipboard.

#### Architecture and Execution
To avoid cluttering the main logic architecture diagram, the structural relationship of the `CopyCommand` is shown in the feature-specific class diagram below.

![CopyCommandClassDiagram](images/CopyCommandClassDiagram.png)

The command execution heavily relies on the `Logic` and `Model` components to retrieve the target contact, format the string, and then interfaces with the external OS `Clipboard`.

Step-by-step execution:
1. The user launches the application and inputs `copy 1` into the `CommandBox`.
2. `LogicManager` passes the input to `AddressBookParser`, which maps the command word to the `CopyCommandParser`.
3. `CopyCommandParser` parses the index, creates a `CopyCommand` object, and returns it.
4. `LogicManager` calls `CopyCommand#execute(model)`.
5. The command fetches the target `Person` from the `Model` using the provided index.
6. The command formats the person's attributes into a valid CLI string.
7. The command fetches the system `Clipboard` and sets its content to the formatted string.
8. A `CommandResult` is returned to indicate success.

The following sequence diagram illustrates this interaction:

![CopySequenceDiagram](images/CopySequenceDiagram.png)

#### Design Considerations:
* **Alternative 1 (Current):** Interface directly with the `Clipboard` within the `CopyCommand` execution logic, but use a mock/stub clipboard for testing.
    * **Pros:** Keeps the command highly cohesive and straightforward.
    * **Cons:** Requires careful testing setup to ensure CI/CD pipelines (which run in headless Linux environments without physical clipboards) do not crash with `IllegalStateException`.
* **Alternative 2:** Have the `CopyCommand` return the generated string inside the `CommandResult`, and force the `MainWindow` (UI) to push it to the clipboard.
    * **Pros:** Avoids headless testing issues entirely since the Logic component never touches the OS.
    * **Cons:** Violates the separation of concerns. The UI should merely display results, not execute system-level operations meant to be triggered by a specific command.

---

### Command History Navigation (Up/Down Arrows)
To improve the Quality of Life (QoL) of the CLI interface, users can navigate their session's command history using the Up and Down arrow keys.

#### Architecture and Execution
The command history state is managed directly within the `CommandBox` UI component. Because the history is only relevant to the current user session and involves direct manipulation of the JavaFX text field and caret position, a lightweight, self-contained implementation was chosen to minimize unnecessary dependencies between the UI and Logic components.

The state is tracked using two internal variables:
* `commandHistory`: An `ArrayList` storing the string values of previously submitted commands.
* `historyPointer`: An integer tracking the user's current index during navigation.

<box type="info" seamless>

Commands are added to `commandHistory` **before** execution is attempted. This means commands that fail (e.g. due to invalid input) are still stored and accessible via the Up arrow. This is intentional — it allows users to quickly retrieve and correct a malformed command without retyping it.

</box>

Step-by-step execution for navigating backwards:
1. The user presses the Up arrow key while focused on the text field.
2. The `CommandBox` captures the event via its `handleKeyPress(KeyEvent)` listener.
3. The component checks if the `historyPointer` is greater than 0. If so, it decrements the pointer.
4. The `setCommandTextFromHistory()` method retrieves the string from the `commandHistory` list at the new pointer index.
5. The text field is updated, and the cursor (`caret`) is forcefully positioned at the end of the loaded string so the user can immediately begin typing.
6. The `KeyEvent` is consumed to prevent the default JavaFX behavior (which would move the caret back to the beginning of the text).

![CommandHistorySequenceDiagram](images/CommandHistorySequenceDiagram.png)

#### Design Considerations
* **Integrated UI State vs. Logic Component:** While moving history tracking to the `Logic` package would decouple the data, keeping it inside `CommandBox` drastically simplifies the architecture. The history list and pointer are inherently tied to UI-specific actions (JavaFX `KeyEvent` handling, text property listeners, and caret manipulation). Furthermore, since the history does not need to persist across app launches (it is session-only), implementing a simple `ArrayList` within the UI layer avoids over-engineering the application's core Logic API.

---

### Editing a contact's name feature

The `contact edit` command allows users to rename an existing contact while preserving all associated games and aliases. It is implemented via `EditContactCommand`, parsed by `EditContactCommandParser`, and routed through `ContactCommandParser`.

#### Architecture and Execution

**Parsing flow:**
1. `AddressBookParser` receives `"contact edit 1 e/Jan"` (or `"contact edit n/Janelle e/Jan"`) and dispatches to `ContactCommandParser`.
2. `ContactCommandParser` splits on the first token (`"edit"`) and delegates the remaining args to `EditContactCommandParser`.
3. `EditContactCommandParser` tokenizes using `PREFIX_NAME` (`n/`) and `PREFIX_NEW_NAME` (`e/`). It then determines the target via `ParserUtil.verifyIndexOrNamePresent` — either an index from the preamble, a name from `n/`, or `0` for the user profile — and returns an `EditContactCommand(targetIndex, targetName, newName, useUserProfile)`.

**Execution flow:**
1. `EditContactCommand#execute()` resolves the target `Person`:
   * If `useUserProfile` is true, retrieves the user profile via `model.getUserProfile()`.
   * If `targetIndex` is set, retrieves the person at that index from the filtered list.
   * If `targetName` is set, searches `model.getFilteredPersonList()` for a case-insensitive name match.
2. If not found, a `CommandException` with `MESSAGE_PERSON_NOT_FOUND` is thrown.
3. A new `Person` is created with `newName` and the original person's `games`, and `isUserProfile` flag.
4. If the new name already belongs to a different person, a `CommandException` with `MESSAGE_DUPLICATE_PERSON` is thrown.
5. `model.setPerson()` replaces the old entry, and the filtered list is reset to show all persons.
6. A `CommandResult` is returned, displaying the updated contact.

#### Design Considerations

* **Immutable `Person` model** — `Person` objects are immutable; editing creates a new `Person` rather than mutating the existing one. This keeps the model simple and consistent with the rest of the codebase.
* **Index and name-based lookup** — The command supports both index and name identification, consistent with `alias edit` and `view`. A single constructor `(Index, Name, Name, boolean)` is used with the unused field passed as `null`, matching the pattern used by `EditAliasCommand`.
* **Games and aliases preserved** — The new `Person` is constructed with the original person's `games` map, so all associated data is retained after a rename.

---

### Delete Confirmation Feature
The `contact delete`, `game delete`, and `alias delete` commands require a y/n confirmation from the user before deletion is applied, to prevent accidental data loss.

#### Architecture and Execution
The structural relationship of the delete commands is shown in the class diagram below.

<puml src="diagrams/DeleteCommandClassDiagram.puml" alt="Delete Command Class Diagram" />

All three delete commands implement `ConfirmableDeleteCommand`, which declares `performDeletion()` and `getCancelMessage()`. This gives `LogicManager` a single unified code path for all confirmation handling.

The following sequence diagram illustrates the two-step flow for `contact delete n/Alice` followed by `y`:

<puml src="diagrams/DeleteConfirmSequenceDiagram.puml" alt="Delete Confirmation Sequence Diagram" />

Step-by-step execution:
1. The user inputs `contact delete n/Alice`.
2. `LogicManager` passes the input to `AddressBookParser`, which creates a `DeleteContactCommand`.
3. `LogicManager` calls `DeleteContactCommand#execute(model)`, which finds the target but does **not** delete yet — returns a `CommandResult` with `isAwaitingConfirmation = true`.
4. `LogicManager` stores the command in `pendingConfirmableCommand` and shows the confirmation prompt.
5. The user inputs `y` — `LogicManager#handleDeleteConfirmation()` calls `performDeletion()` on the command, conditionally pushes it to `commandHistory` if it implements `UndoableCommand`, and saves the address book.
6. If the user inputs `n` or any other input, `getCancelMessage()` is returned and the model is unchanged.

#### Design Considerations:
* **`ConfirmableDeleteCommand` is decoupled from `UndoableCommand`** — allows future commands to require confirmation without needing to support undo. All three current delete commands implement both interfaces explicitly.
* **Alternative:** Have `LogicManager` perform the deletion directly (e.g. `model.deletePerson()`) after confirmation, without a `performDeletion()` method on the command.
    * **Pros:** Simpler — no extra interface or method needed.
    * **Cons:** `LogicManager` must know the internals of each delete command (e.g. which game or alias to remove), violating separation of concerns and requiring `instanceof` checks for each command type.

<box type="info" seamless>

**Note:** `ClearCommand` also requires a y/n confirmation before proceeding, but it does **not** implement `ConfirmableDeleteCommand`. Instead, it uses its own `executeConfirmed(Model model)` method, triggered by a separate `awaitingClearConfirmation` flag in `CommandResult` that `LogicManager` checks. This is because `ConfirmableDeleteCommand` is designed around commands that identify a specific target before asking for confirmation. `ClearCommand` has no such target resolution step — it unconditionally clears all contacts — so it does not fit the interface's contract.

</box>

---

### Undo feature

The undo mechanism is implemented using a **command history stack** managed by `LogicManager`, together with an `UndoableCommand` interface that undoable commands implement.

#### Architecture and Execution

**Key classes involved:**

* `UndoableCommand` (interface) — declares `void undo(Model model)`, which each undoable command implements to reverse its own effect.
* `UndoCommand` — pops the most recent command from the history stack and calls its `undo()` method.
* `LogicManager` — owns the history stack (`Deque<UndoableCommand> commandHistory`) and pushes commands onto it after successful execution.

**How the history stack is maintained:**

After a command executes successfully, `LogicManager` checks whether it implements `UndoableCommand`. If it does, the command is pushed onto the `commandHistory` deque (used as a LIFO stack). Read-only commands (e.g. `list`, `find`) do not implement `UndoableCommand` and are never added to history.

<box type="info" seamless>

**Note:** `UndoCommand` itself is not pushed to history — it is created directly in `LogicManager` when the keyword `undo` is detected, bypassing the parser.

</box>

**How each command reverses itself (per-command state capture):**

Rather than saving a full snapshot of the address book after each command, each undoable command captures only what it needs:

| Command | State captured | Undo action |
|---|---|---|
| `AddContactCommand` | The `Person` added | `model.deletePerson(toAdd)` |
| `DeleteContactCommand` | The `Person` deleted (set after confirmation) | `model.addPerson(deletedPerson)` |
| `ClearCommand` | Full `AddressBook` snapshot (before clear) | `model.setAddressBook(previousAddressBook)` |
| `AddGameCommand` | `personBeforeEdit`, `personAfterEdit` | `model.setPerson(personAfterEdit, personBeforeEdit)` |
| `DeleteGameCommand` | `personBeforeEdit`, `personAfterEdit` | `model.setPerson(personAfterEdit, personBeforeEdit)` |
| `AddAliasCommand` | `personBeforeEdit`, `personAfterEdit` | `model.setPerson(personAfterEdit, personBeforeEdit)` |
| `DeleteAliasCommand` | `personBeforeEdit`, `personAfterEdit` | `model.setPerson(personAfterEdit, personBeforeEdit)` |
| `EditContactCommand` | `personBeforeEdit`, `personAfterEdit` | `model.setPerson(personAfterEdit, personBeforeEdit)` |
| `EditAliasCommand` | `personBeforeEdit`, `personAfterEdit` | `model.setPerson(personAfterEdit, personBeforeEdit)` |

**Special case — delete confirmation:**

As described in the previous section, confirmable commands (`DeleteContactCommand`, `DeleteGameCommand`, `DeleteAliasCommand`) are only pushed to `commandHistory` after the user confirms with `y`/`yes`. Cancelling the deletion means nothing is added to history.

Given below is an example usage scenario showing how the undo mechanism behaves.

Step 1. The user executes `contact add n/David` to add a new contact. `AddContactCommand` executes, stores the added `Person`, and is pushed onto `commandHistory`.

Step 2. The user executes `game add n/David g/Chess` to add a game. `AddGameCommand` executes, stores `personBeforeEdit` and `personAfterEdit`, and is pushed onto `commandHistory`.

Step 3. The user executes `undo`. `LogicManager` creates `UndoCommand(commandHistory)`. It pops `AddGameCommand` from the stack and calls its `undo()`, which calls `model.setPerson(personAfterEdit, personBeforeEdit)`, restoring David's original state.

Step 4. The user executes `undo` again. `UndoCommand` pops `AddContactCommand` and calls its `undo()`, which calls `model.deletePerson(toAdd)`, removing David entirely.

Step 5. The user executes `undo` again. The history stack is empty, so `UndoCommand` throws a `CommandException` with the message `"Error: Nothing to undo."`

The following sequence diagram shows how an undo operation flows through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram.puml" alt="UndoSequenceDiagram" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/UndoActivityDiagram.puml" width="250" />

#### Design Considerations

**Aspect: How undo executes:**

* **Current choice:** Per-command state capture — each command stores only the data it needs to reverse itself.
  * Pros: Low memory overhead; no full address book snapshots needed for most commands.
  * Cons: Every new undoable command must correctly implement `undo()`.

* **Alternative:** Save a full address book snapshot after every command (VersionedAddressBook approach).
  * Pros: Simpler to implement per command — no per-command state logic needed.
  * Cons: Higher memory usage, especially with large contact lists.

**Aspect: No redo support:**

Redo is not implemented. Once a command is undone it is removed from the history stack permanently.

---

### Theme Switching Feature

The `theme` command allows users to switch the application's visual appearance between `light`, `dark`, and `rainbow` themes at runtime.

#### Architecture and Execution

Theme switching is notable because it involves **UI-side effects triggered from the Logic layer** — `ThemeCommand` does not modify the `Model`, but instead signals `MainWindow` to swap CSS stylesheets by embedding the chosen theme string inside the `CommandResult`.

The following sequence diagram illustrates the flow for `theme light`:

<puml src="diagrams/ThemeSequenceDiagram.puml" alt="Theme Sequence Diagram" />

Step-by-step execution:
1. The user inputs `theme light` (or `theme dark` / `theme rainbow`).
2. `AddressBookParser` routes the command to `ThemeCommandParser`, which creates a `ThemeCommand("light")`.
3. `ThemeCommand#execute()` validates the parameter against the accepted values (`light`, `dark`, `rainbow`) and returns a `CommandResult` carrying the theme string via `CommandResult(msg, "light")`.
4. Back in `MainWindow#executeCommand()`, after receiving the result, it checks `commandResult.getThemeToSwitch()`.
5. If non-null, `MainWindow#handleThemeSwitch(String theme)` is called, which swaps the active CSS stylesheets on the scene dynamically.

#### Design Considerations:
* **Theme string carried in `CommandResult`** — `ThemeCommand` places the theme value inside `CommandResult` rather than calling the UI directly. This keeps `ThemeCommand` within the Logic layer and prevents a downward dependency from Logic onto UI.
* **Alternative:** Have `ThemeCommand` interact with the UI directly (e.g. via a callback or static reference to `MainWindow`).
    * **Pros:** No changes needed to `CommandResult`.
    * **Cons:** Creates an illegal dependency from Logic to UI, violating the architecture's layered design.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* plays games on with a friends online

**Value proposition**: It allows the user to have quick access to the different aliases that their contacts may be using.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​    | I want to …​                                          | So that I can…​                                                    |
|----------|------------|-------------------------------------------------------|--------------------------------------------------------------------|
| `* * *`  | user       | add a new contact                                     | keep track of a new gaming contact                                 |
| `* * *`  | user       | delete a contact                                      | remove contacts that I no longer need                              |
| `* * *`  | user       | edit a contact's name                                 | modify a contact without removing associated aliases and games     |
| `* * *`  | user       | display all contacts                                  | see everyone in my contact list at a glance                        |
| `* * *`  | user       | clear all contacts at once                            | quickly reset the contact list                                     |
| `* *`    | user       | add an alias to a contact                             | keep track of alternate usernames used by the contact              |
| `* *`    | user       | delete an alias from a contact                        | remove aliases that the contact is no longer using                 |
| `* *`    | user       | edit an alias of a contact                            | update an alias without deleting and re-adding it                  |
| `* *`    | user       | add a game that the contact plays                     | keep track of which games the contact plays                        |
| `* *`    | user       | delete a game that a contact plays                    | remove games that the contact no longer plays                      |
| `* *`    | user       | undo my last action                                   | recover from accidental changes                                    |
| `* *`    | user       | copy a contact's details to my clipboard              | quickly reuse their information to add them to another device      |
| `* *`    | user       | search for contacts by name                           | find a specific contact quickly without scrolling through the list |
| `* *`    | user       | view the full details of a contact                    | see all their games and aliases at once                            |
| `*`      | user       | list all games a contact plays                        | get a quick overview of their games                                |
| `*`      | user       | switch between light, dark, and rainbow themes        | use the app comfortably in different environments                  |
| `*`      | user       | navigate previous commands using the Up/Down arrow keys | avoid retyping recently used commands                            |
| `*`      | new user   | see usage instructions                                | refer to command syntax when I forget how to use the app           |

### Use cases

(For all use cases below, the **System** is `Harmony` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC1 - Add a contact**

**MSS**

1.  User requests to add a contact.
2.  System adds contact to User Contact list.
3.  System displays updated Contact list.

    Use case ends.

**Extensions**

* 1a. Contact already exists.

    * 1a1. System displays error message indicating duplicate Contact name.

      Use case ends.


**Use Case: UC2 - Delete contact**

**Precondition**
* The list is not empty.

**MSS**
1. User requests to delete a specific contact by name or index.
2. System identifies the matching contact and prompts for confirmation.
3. User confirms with `y`.
4. System removes the contact and its associated aliases and games.
5. System confirms deletion.

   Use case ends.

**Extensions**

* 2a. No contact is found with the given name or index.
  * 2a1. System informs user that no matching contact is found.

    Use case ends.

* 3a. User cancels with `n`.
  * 3a1. System aborts the deletion and informs the user.

    Use case ends.

**Use case: UC3 -  Add an alias**

**MSS**
1. User request to add an alias to a contact by specifying the name and new alias to be added.
2. System identifies the matching contact.
3. System add new alias to the contact.
4. System confirms changes made.

   Use case ends.

**Extensions**

* 1a. System detects errors in the entered field..
  * 1a1. System notify user of the errors in the invalid entry.
  * 1a2. User re-enter the field.

     Steps 1a1-1a2 are repeated until the data entered are correct.

     Use case resumes from step 2.


* 1b. Contact not in database.
  * 1b1. System notify user of the invalid contact.

    Use case ends.

**Use Case: UC4 - Edit contact’s name**

**Precondition**
* Contact exists in user’s address book.

**MSS**
1. User requests to edit contact name.
2. System shows the requested contact’s detail.
3. User enters new name.
4. System outputs contact updated message.

   Use case ends.

**Extensions**
* 1a. User tries to edit a non-existent contact.
  * 1a1. System outputs: Contact does not exist.

    Use case ends.

* 3a. User inputs name with violations .
  * 3a1. System outputs: invalid names message.

    Use case ends.

**Use Case: UC5 - Listing all User’s contacts**

**MSS**
1. User requests to list all contacts.
2. System displays all contacts.

   Use case ends.

**Extensions**
* 1a. User does not have any Contacts in list.
  * 1a1. “No contacts in address book” messages pop up.

    Use case ends.

**Use Case: UC6 - Delete an alias**

**Precondition**
* The contact and alias exist.

**MSS**
1. User requests to remove a specific alias from a contact.
2. System identifies the contact and specific alias and prompts for confirmation.
3. User confirms with `y`.
4. System removes the alias from the record.
5. System confirms the removal.

   Use case ends.

**Extensions**
* 3a. User cancels with `n`.
  * 3a1. System aborts the deletion and informs the user.

    Use case ends.

<box type="info" seamless>

**Note:** The confirmation flow follows the same pattern as UC8 (Delete a game from Contact).

</box>

**Use Case: UC7 - Add a game to Contact**

**Precondition**
* Harmony is running and specified contact must already exist in the database.

**MSS**
1. User requests to add a game that a specific contact plays.
2. System adds the game to the contact’s profile.
3. System displays the contact’s detail panel to with the games added.

   Use case ends.

**Extensions**
* 2a. The game is a duplicate (already exists for that specific contact)  .
  * 2a1. System informs user that the game already exists for that contact.

    Use case ends.

* 2b. The game name is missing or exceeds 200 characters.
  * 2b1. System informs the user of the invalid game field.

    Use case ends.

* *a. At any time, the user chooses to cancel the operation
  * *a1. System stops the process

    Use case ends


**Use Case: UC8 - Delete a game from Contact**

**Precondition**
* The game to be deleted exists in the User’s contact list.

**MSS**
1. User requests to remove a specific game from a contact.
2. System displays confirmation message for deletion
3. User enters "y"
4. System removes the game from the contact.
5. System displays the contact’s detail panel.

   Use case ends.

**Extensions**
* 3a. User enters "n"
    * 3a1. System aborts the deletion
    * 3a2. System displays message that the deletion was aborted

      Use case ends.


**Use Case: UC9 - Clear all contacts**

**Precondition**
* Harmony is running.

**MSS**
1. User requests to clear all contacts.
2. System prompts for confirmation.
3. User confirms with `y`.
4. System removes all contacts, preserving the user profile.
5. System confirms that the contact list has been cleared.

   Use case ends.

**Extensions**
* 3a. User cancels with `n`.
  * 3a1. System aborts the operation and informs the user.

    Use case ends.


**Use Case: UC10 - Undo last action**

**MSS**
1. User requests to undo the last action.
2. System reverses the most recent undoable command.
3. System displays a message confirming what was undone.

   Use case ends.

**Extensions**
* 1a. There are no undoable commands in history.
  * 1a1. System informs the user that there is nothing to undo.

    Use case ends.

* 1b. The last action was a delete command that required confirmation.
  * The undo reverses the deletion only if the user had confirmed it. Cancelled deletions are not in history and cannot be undone.

    Use case resumes from step 2.


### Non-Functional Requirements

1. Initial startup should take no longer than 2 seconds.
2. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
3. The app is intended for a **single user** — it does not support concurrent access or multiple user accounts.
4. All contact data should **persist between sessions** — data is saved automatically after every command and restored on the next launch.
5. The app should work on **Windows, macOS, and Linux** with Java 17 or above installed.
6. The app should function **fully offline** — no internet connection is required for any feature.

### Glossary

* **Alias**: An alternate in-game username used by a contact for a specific game
* **Contact**: A person stored in Harmony's contact list, identified by a unique name
* **Game**: A game title associated with a contact, under which aliases are grouped
* **User Profile**: A special contact representing the user themselves, accessible via the `me` keyword

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file Expected: Shows the GUI with a placeholder User Profile. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding, editing, and deleting a contact

1. Adding a new contact

   1. Test case: `contact add n/John Doe`<br>
      Expected: Contact `John Doe` added with no games. Success message shown.

   2. Test case: `contact add n/John Doe g/Valorant al/JohnV`<br>
      Expected: Contact `John Doe` added with game `Valorant` and alias `JohnV`. Success message shown.

   3. Test case: `contact add n/Alex Yeoh` (where `Alex Yeoh` already exists)<br>
      Expected: No contact added. Error message `Error: Contact already exists in the contact list` shown.

   4. Test case: `contact add` (missing `n/` prefix)<br>
      Expected: No contact added. Invalid command format error shown.

2. Editing a contact's name

   1. Prerequisites: List all persons using the `list` command. At least one contact in the list (e.g. `Alex Yeoh` at index 1).

   2. Test case: `contact edit n/Alex Yeoh e/Alex`<br>
      Expected: Contact is renamed. Success message `Contact updated: Alex Yeoh → Alex` shown.

   3. Test case: `contact edit 1 e/Alex`<br>
      Expected: First contact is renamed. Success message `Contact updated: [original name] → Alex` shown.

   4. Test case: `contact edit n/NonExistent e/NewName`<br>
      Expected: No contact is renamed. Error message `Error: Contact not found in the current list. Use 'list' to show all contacts.` shown.

   5. Test case: `contact edit 999 e/NewName` (index out of bounds)<br>
      Expected: No contact is renamed. Invalid index error shown.

   6. Test case: `contact edit 1 n/Alex Yeoh e/NewName` (both index and name provided)<br>
      Expected: No contact is renamed. Error message `Please provide either an index OR a name, not both.` shown.

   7. Test case: `contact edit n/Alex Yeoh e/Bernice Yu` (where `Bernice Yu` already exists)<br>
      Expected: No contact is renamed. Error message `Error: A contact with that name already exists` shown.

   8. Test case: `contact edit n/Alex Yeoh` (missing `e/` prefix)<br>
      Expected: No contact is renamed. Invalid command format error shown.

3. Deleting a contact

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   2. Test case: `contact delete 1`<br>
      Expected: Confirmation prompt shown. Enter `y` — first contact deleted, success message shown. Enter `n` — deletion aborted.

   3. Test case: `contact delete me`<br>
      Expected: Confirmation prompt shown. Enter `y` — user profile reset to placeholder. Enter `n` — operation aborted.

   4. Other incorrect delete commands to try: `contact delete`, `contact delete x` (where x is larger than the list size)<br>
      Expected: No contact deleted. Error details shown in the status message.

### Adding and deleting a game

1. Adding a game to a contact

   1. Prerequisites: At least one contact in the list (e.g. `Alex Yeoh` at index 1).

   2. Test case: `game add 1 g/Minecraft`<br>
      Expected: Game `Minecraft` added to the first contact. Updated contact details shown.

   3. Test case: `game add n/Alex Yeoh g/Minecraft` (where `Minecraft` already exists for that contact)<br>
      Expected: No game added. Error message `Error: Game already exists for the contact.` shown.

   4. Test case: `game add me g/Valorant`<br>
      Expected: Game `Valorant` added to the user profile. Updated profile shown.

2. Deleting a game from a contact

   1. Prerequisites: Contact at index 1 has at least one game (e.g. `Minecraft`).

   2. Test case: `game delete 1 g/Minecraft`<br>
      Expected: Confirmation prompt shown. Enter `y` to confirm — game removed, success message shown. Enter `n` — deletion aborted.

   3. Test case: `game delete 1 g/NonExistentGame`<br>
      Expected: No confirmation prompt. Error message `Error: This contact does not have this game.` shown.

### Adding, editing, and deleting an alias

1. Adding an alias

   1. Prerequisites: Contact at index 1 has game `Valorant`.

   2. Test case: `alias add 1 g/Valorant al/ProPlayer`<br>
      Expected: Alias `ProPlayer` added to `Valorant` for the first contact. Success message shown.

   3. Test case: `alias add 1 g/Valorant al/ProPlayer` (where `ProPlayer` already exists)<br>
      Expected: No alias added. Error message `Error: This alias already exists for this game.` shown.

2. Editing an alias

   1. Prerequisites: Contact at index 1 has game `Valorant` with alias `ProPlayer`.

   2. Test case: `alias edit 1 g/Valorant al/ProPlayer na/ElitePlayer`<br>
      Expected: Alias renamed from `ProPlayer` to `ElitePlayer`. Success message shown.

   3. Test case: `alias edit 1 g/Valorant al/NonExistent na/NewAlias`<br>
      Expected: No alias edited. Error message `Error: Alias not found` shown.

3. Deleting an alias

   1. Prerequisites: Contact at index 1 has game `Valorant` with alias `ElitePlayer`.

   2. Test case: `alias delete 1 g/Valorant al/ElitePlayer`<br>
      Expected: Confirmation prompt shown. Enter `y` — alias removed, success message shown. Enter `n` — deletion aborted.

### Finding contacts

1. Prerequisites: Contact list contains at least `Alex Yeoh` who plays `Valorant` with alias `Ace`.

2. Test case: `find n/Alex`<br>
   Expected: Contacts whose names contain `Alex` are shown.

3. Test case: `find g/Valorant`<br>
   Expected: Contacts who have `Valorant` as a game are shown.

4. Test case: `find al/Ace`<br>
   Expected: Contacts who have alias `Ace` (in any game) are shown.

5. Test case: `find n/Alex g/Valorant`<br>
   Expected: Only contacts matching both criteria (name contains `Alex` AND has game `Valorant`) are shown.

6. Test case: `find` (no parameters)<br>
   Expected: Error message `Invalid command format!` shown. At least one search parameter required.

### Viewing a contact

1. Prerequisites: At least one contact in the list.

2. Test case: `view 1`<br>
   Expected: Full details (games and aliases) of the first contact displayed in the detail panel.

3. Test case: `view n/Alex Yeoh`<br>
   Expected: Full details of `Alex Yeoh` displayed.

4. Test case: `view me`<br>
   Expected: User profile details displayed.

5. Test case: `view 999` (index out of bounds)<br>
   Expected: Error message `The person index provided is invalid` shown.

### Listing games

1. Prerequisites: Contact at index 1 has at least one game.

2. Test case: `game list 1`<br>
   Expected: All games of the first contact listed in the result display.

3. Test case: `game list me`<br>
   Expected: All games of the user profile listed.

4. Test case: `game list 1` (where contact has no games)<br>
   Expected: Message indicating the contact has no games shown.

### Copying a contact

1. Prerequisites: At least one contact in the list.

2. Test case: `copy 1`<br>
   Expected: A formatted `contact add` command string for the first contact is copied to the system clipboard. Paste it into any text field to verify.

3. Test case: `copy 999` (index out of bounds)<br>
   Expected: Error message `The person index provided is invalid` shown. Clipboard unchanged.

### Undoing a command

1. Prerequisites: At least one undoable command has been executed (e.g. `contact add n/Temp`).

2. Test case: `undo`<br>
   Expected: The last undoable command is reversed. Success message shown indicating what was undone.

3. Test case: `undo` (with empty history)<br>
   Expected: Error message `Error: Nothing to undo.` shown.

4. Test case: Execute `contact delete 1`, cancel with `n`, then `undo`<br>
   Expected: Nothing to undo — cancelled deletions are not added to history.

### Clearing all contacts

1. Test case: `clear`<br>
   Expected: Confirmation prompt shown. Enter `y` — all contacts removed (user profile preserved). Enter `n` — operation aborted.

2. Test case: `clear` on an already empty list, confirm with `y`<br>
   Expected: Success message shown. Contact list remains empty.

### Switching themes

1. Test case: `theme light`<br>
   Expected: Application switches to the light theme.

2. Test case: `theme dark`<br>
   Expected: Application switches to the dark theme.

3. Test case: `theme rainbow`<br>
   Expected: Application switches to the rainbow theme.

4. Test case: `theme invalid`<br>
   Expected: Error message `Invalid theme! Please specify either 'light' or 'dark'.` shown. Theme unchanged.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

Team size: 5

1. **Make the command result window scale with text.** The current command result window is too small to fully view the result of the command. This might prevent users from viewing the full result without breaking from "full CLI". We plan to make the result display area dynamically resize based on the length of the output text.

2. **Make the Contact List panel scrollable without mouse input.** The current Contact List panel can only be scrolled with a mouse (barring the `find` command to reduce the list size), breaking from "full CLI". We plan to add keyboard shortcuts (e.g. `Ctrl+Up`/`Ctrl+Down`) to scroll through the contact list without a mouse.

3. **Improve component scaling when the application window is resized.** Currently, resizing the application window only widens the command result area while the bottom view panels do not scale proportionally, which does not translate well on larger screens. We plan to update the layout so all panels scale proportionally with the window size.

4. **Accept special characters in contact names.** The current name validation only accepts alphanumeric characters and spaces, rejecting real-world names such as `Mary-Jane`, `O'Brien`, and `Kumar s/o Ravi`. We plan to extend the name validation regex to also permit hyphens (`-`), apostrophes (`'`), and forward slashes (`/`), so that names like `contact add n/Kumar s/o Ravi` are accepted.

5. **Restate the contact name in the deletion confirmation prompt.** Currently, after triggering a deletion (e.g. `contact delete 1`), if the user scrolls away from the confirmation message, there is no reminder of which contact is about to be deleted when they type `y`. We plan to repeat the contact's name in the confirmation prompt so it is always visible, e.g. `Confirm deletion of Alex Yeoh? (y/n)`.

6. **Make the UI reflect the state before and after an undo.** Currently the `undo` command updates the model but the UI only refreshes to show the new state without visually indicating what changed. We plan to highlight the affected contact in the list after an undo so the user can clearly see what was reversed.

7. **Add the ability to edit a game name.** Currently, games can only be added, deleted, and listed. If a game name is entered incorrectly, the user must delete and re-add it, losing all associated aliases in the process. We plan to add a `game edit` command (e.g. `game edit 1 g/OldGame e/NewGame`) that renames the game while preserving its aliases.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Effort**

**Difficulty level:** Significantly higher than AB3.

**Effort relative to AB3:** We estimate the overall effort at approximately 2× that of AB3, based on the increased complexity of the data model, the number of new features, and the non-trivial cross-feature interactions introduced.

**Key challenges and achievements:**

* **Multiple interconnected entity types.** AB3 manages a single entity type (`Person`) with flat fields (name, phone, email, address, tags). Harmony manages a three-level hierarchy: `Person` → `Game` → `Alias`. Every command that touches games or aliases must navigate this nesting, handle the immutable `Person` model by reconstructing the full object, and propagate changes upward through the hierarchy. This significantly increased the complexity of every game and alias command compared to an equivalent AB3 command.

* **Custom two-tier command syntax.** AB3 uses simple single-word commands (e.g. `add`, `delete`). Harmony introduces a namespaced command structure (`contact add`, `game delete`, `alias edit`) requiring a two-level parser architecture (`AddressBookParser` → `ContactCommandParser`/`GameCommandParser`/`AliasCommandParser`). Designing and testing this routing layer was a non-trivial addition.

* **Undo mechanism.** AB3 does not implement undo. We built a per-command state capture approach where each undoable command stores only the minimal state needed to reverse itself (e.g. `personBeforeEdit`/`personAfterEdit`). This required careful design of the `UndoableCommand` interface and integration with `LogicManager`'s history stack across all modifying commands.

* **Delete confirmation flow.** AB3 deletes immediately on command. We added a two-step confirmation flow (`ConfirmableDeleteCommand` interface + `pendingConfirmableCommand` in `LogicManager`) that required coordinating state across command executions without breaking the existing architecture.

* **Clipboard integration and headless testing.** The `CopyCommand` interfaces directly with the OS clipboard via `java.awt.Toolkit`, which required solving a non-trivial CI/CD problem: headless Linux environments used in GitHub Actions do not expose a physical clipboard, causing `IllegalStateException`. This required a mock/stub clipboard setup for tests.

* **Theme switching.** Implementing runtime CSS theme switching (light/dark/rainbow) required a clean signal path from the Logic layer to the UI without violating the architecture's layer boundaries. This was solved by embedding the theme string in `CommandResult` and having `MainWindow` act on it.

**Effort saved through reuse:**

The overall component structure (UI, Logic, Model, Storage), the JSON storage mechanism, and the JavaFX layout scaffolding were adapted from AB3 with modifications, saving significant boilerplate work. The base parser infrastructure (`ArgumentTokenizer`, `ArgumentMultimap`, `CliSyntax`) was reused with extensions for new prefixes (`g/`, `al/`, `e/`).
