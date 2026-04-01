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

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

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

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter`, `ViewPanel` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

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

### Editing a contact's name feature

#### Implementation

The `contact edit` command allows users to rename an existing contact while preserving all associated games and aliases. It is implemented via `EditContactCommand`, parsed by `EditContactCommandParser`, and routed through `ContactCommandParser`.

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
3. A new `Person` is created with `newName` and the original person's `tags`, `games`, and `isUserProfile` flag.
4. If the new name already belongs to a different person, a `CommandException` with `MESSAGE_DUPLICATE_PERSON` is thrown.
5. `model.setPerson()` replaces the old entry, and the filtered list is reset to show all persons.
6. A `CommandResult` is returned, displaying the updated contact.

**Design considerations:**

* **Immutable `Person` model** — `Person` objects are immutable; editing creates a new `Person` rather than mutating the existing one. This keeps the model simple and consistent with the rest of the codebase.
* **Index and name-based lookup** — The command supports both index and name identification, consistent with `alias edit` and `view`. A single constructor `(Index, Name, Name, boolean)` is used with the unused field passed as `null`, matching the pattern used by `EditAliasCommand`.
* **Games and aliases preserved** — The new `Person` is constructed with the original person's `games` map, so all associated data is retained after a rename.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


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

| Priority | As a …​    | I want to …​                       | So that I can…​                                              |
|----------|------------|------------------------------------|--------------------------------------------------------------|
| `* * *`  | user       | add a new contact                  |                                                              |
| `* * *`  | user       | delete a contact                   | remove contacts that I no longer need                        |
| `* * *`  | user       | edit a contact's name              | modify a contact without removing associated alias and games |
| `* * *`  | user       | display all contacts               |                                                              |
| `* *`    | user       | add an alias to a contact          | keep track of alternate usernames used by the contact        |
| `* *`    | user       | delete an alias from a contact     | remove aliases that the contact is no longer using           |
| `* *`    | user       | add a game that the contact plays  | keep track of which games the contact plays                  |
| `* *`    | user       | delete a game that a contact plays | remove games that the contact no longer plays                |
| `*`      | new user   | see usage instructions             | refer to command syntax when I forget how to use the app     | \

*{More to be added}*

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
1.  User requests to delete a specific contact by name.
2.  System identifies the matching contact.
3.  System removes the contact and its associated aliases and games.
4.  System confirms deletion.

    Use case ends.

**Extensions**

* 2a. No contact is found with matching name.
  * 2a1. System informs user that no matching user is found.

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
2. System identifies the contact and specific alias.
3. System removes the alias from the record.
4. System confirms the removal.

   Use case ends.

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
2. System removes the game from the contact.
3. System displays the contact’s detail panel.

   Use case ends.

*{More to be added}*

### Non-Functional Requirements

1.   Initial startup should take no longer than 2s.
2.   A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

*{More to be added}*

### Glossary

* **Alias**: Alternate usernames used by the user

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

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Editing a contact's name

1. Renaming a contact while all persons are shown

   1. Prerequisites: List all persons using the `list` command. At least one contact in the list (e.g. `Alex Yeoh` at index 1).

   1. Test case: `contact edit n/Alex Yeoh e/Alex`<br>
      Expected: Contact is renamed. Success message `Contact updated: Alex Yeoh → Alex` shown.

   1. Test case: `contact edit 1 e/Alex`<br>
      Expected: First contact is renamed. Success message `Contact updated: [original name] → Alex` shown.

   1. Test case: `contact edit n/NonExistent e/NewName`<br>
      Expected: No contact is renamed. Error message `Error: Name not found` shown.

   1. Test case: `contact edit 999 e/NewName` (index out of bounds)<br>
      Expected: No contact is renamed. Invalid index error shown.

   1. Test case: `contact edit 1 n/Alex Yeoh e/NewName` (both index and name provided)<br>
      Expected: No contact is renamed. Error message `Please provide either an index OR a name, not both.` shown.

   1. Test case: `contact edit n/Alex Yeoh e/Bernice Yu` (where `Bernice Yu` already exists)<br>
      Expected: No contact is renamed. Error message `Error: A contact with that name already exists` shown.

   1. Test case: `contact edit n/Alex Yeoh` (missing `e/` prefix)<br>
      Expected: No contact is renamed. Invalid command format error shown.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
