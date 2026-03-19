---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# Harmony User Guide

Harmony is a **desktop app for managing contacts and their gaming aliases, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Harmony can help you keep track of your friends' gaming identities faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103T-W09-1/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for Harmony.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar harmony.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe` : Adds a contact named `John Doe` to Harmony.

   * `game add 1 g/Valorant` : Adds the game `Valorant` to the 1st contact shown.

   * `alias add 1 g/Valorant al/JohnnyV` : Adds the alias `JohnnyV` to the 1st contact's `Valorant` game.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME t/TAG`, `t/TAG n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to Harmony.

Format: `add n/NAME [t/TAG]…​`

<box type="tip" seamless>

**Tip:** A person can have any number of tags (including 0)
</box>

Examples:
* `add n/John Doe`
* `add n/Betsy Crowe t/friend t/classmate`

### Listing all persons : `list`

Shows a list of all persons in Harmony.

Format: `list`

### Editing a person : `edit`

Edits an existing person in Harmony.

Format: `edit INDEX [n/NAME] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e. adding of tags is not cumulative.
* You can remove all the person's tags by typing `t/` without specifying any tags after it.

Examples:
* `edit 1 n/John Smith` Edits the name of the 1st person to be `John Smith`.
* `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons: `find`

Finds persons by name, game, or alias.

**Find by name:**

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`

**Find by game:**

Format: `find g/GAME_NAME`

* Returns all persons who have the specified game.
* The search is case-insensitive.

Examples:
* `find g/Valorant` returns all persons who have `Valorant` in their game list.

**Find by alias:**

Format: `find al/ALIAS`

* Returns all persons who have the specified alias (across all games).
* The search is case-insensitive.

Examples:
* `find al/Benjumpin` returns all persons with the alias `Benjumpin`.

### Deleting a person : `delete`

Deletes the specified person from Harmony.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in Harmony.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Clearing all entries : `clear`

Clears all entries from Harmony.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

--------------------------------------------------------------------------------------------------------------------

## Game Management

### Adding a game to a contact : `game add`

Adds a game to an existing contact.

Format (by index): `game add INDEX g/GAME_NAME`

Format (by name): `game add n/CONTACT_NAME g/GAME_NAME`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).
* The game cannot already exist for that contact.

Examples:
* `game add 1 g/Minecraft`
* `game add n/John Doe g/Valorant`

### Deleting a game from a contact : `game delete`

Removes a game from an existing contact.

Format (by index): `game delete INDEX g/GAME_NAME`

Format (by name): `game delete n/CONTACT_NAME g/GAME_NAME`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).
* The game must exist for that contact.

Examples:
* `game delete 1 g/Minecraft`
* `game delete n/John Doe g/Valorant`

### Listing games of a contact : `game list`

Lists all games of an existing contact.

Format (by index): `game list INDEX`

Format (by name): `game list n/CONTACT_NAME`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).

Examples:
* `game list 1`
* `game list n/John Doe`

--------------------------------------------------------------------------------------------------------------------

## Alias Management

### Adding an alias to a game : `alias add`

Adds an alias to a game of an existing contact.

Format (by index): `alias add INDEX g/GAME_NAME al/ALIAS`

Format (by name): `alias add n/CONTACT_NAME g/GAME_NAME al/ALIAS`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).
* The contact must already have the specified game.
* The alias cannot already exist for that game.

Examples:
* `alias add 1 g/Valorant al/Benjumpin`
* `alias add n/Benjamin g/Valorant al/Benjumpin`

### Deleting an alias from a game : `alias delete`

Removes an alias from a game of an existing contact.

Format (by index): `alias delete INDEX g/GAME_NAME al/ALIAS`

Format (by name): `alias delete n/CONTACT_NAME g/GAME_NAME al/ALIAS`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).
* The contact must already have the specified game.
* The alias must exist for that game.

Examples:
* `alias delete 1 g/Valorant al/Benjumpin`
* `alias delete n/Benjamin g/Valorant al/Benjumpin`

--------------------------------------------------------------------------------------------------------------------

## Data Management

### Saving the data

Harmony data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

Harmony data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, Harmony will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause Harmony to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install Harmony on the other computer and overwrite the empty data file it creates with the file that contains the data of your previous Harmony home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action              | Format, Examples
--------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**             | `add n/NAME [t/TAG]…​` <br> e.g., `add n/James Ho t/friend t/colleague`
**Clear**           | `clear`
**Delete**          | `delete INDEX`<br> e.g., `delete 3`
**Edit**            | `edit INDEX [n/NAME] [t/TAG]…​`<br> e.g., `edit 2 n/James Lee t/friend`
**Find (name)**     | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Find (game)**     | `find g/GAME_NAME`<br> e.g., `find g/Valorant`
**Find (alias)**    | `find al/ALIAS`<br> e.g., `find al/Benjumpin`
**List**            | `list`
**Help**            | `help`
**Game Add**        | `game add INDEX g/GAME_NAME` or `game add n/CONTACT_NAME g/GAME_NAME`<br> e.g., `game add 1 g/Minecraft`
**Game Delete**     | `game delete INDEX g/GAME_NAME` or `game delete n/CONTACT_NAME g/GAME_NAME`<br> e.g., `game delete 1 g/Minecraft`
**Game List**       | `game list INDEX` or `game list n/CONTACT_NAME`<br> e.g., `game list 1`
**Alias Add**       | `alias add INDEX g/GAME_NAME al/ALIAS` or `alias add n/CONTACT_NAME g/GAME_NAME al/ALIAS`<br> e.g., `alias add 1 g/Valorant al/Benjumpin`
**Alias Delete**    | `alias delete INDEX g/GAME_NAME al/ALIAS` or `alias delete n/CONTACT_NAME g/GAME_NAME al/ALIAS`<br> e.g., `alias delete 1 g/Valorant al/Benjumpin`
