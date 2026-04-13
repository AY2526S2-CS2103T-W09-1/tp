---
layout: default.md
title: "User Guide"
pageNav: 3
---

# Harmony User Guide

Harmony is a **desktop app for managing contacts and their gaming aliases, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Harmony can help you keep track of your friends' gaming identities faster than traditional GUI apps.

<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Contents

**Getting Started**
* [Quick start](#quick-start)

**General**
* [Viewing help: `help`](#viewing-help-help)
* [Listing all contacts: `list`](#listing-all-contacts-list)
* [Undoing the last command: `undo`](#undoing-the-last-command-undo)
* [Clearing all entries: `clear`](#clearing-all-entries-clear)
* [Changing the UI theme: `theme`](#changing-the-ui-theme-theme)
* [Exiting the program: `exit`](#exiting-the-program-exit)

**Contact Management**
* [Adding a contact: `contact add`](#adding-a-contact-contact-add)
* [Viewing a contact's profile: `view`](#viewing-a-contacts-profile-view)
* [Copying a contact: `copy`](#copying-a-contact-copy)
* [Editing a contact's name: `contact edit`](#editing-a-contacts-name-contact-edit)
* [Locating contacts: `find`](#locating-contacts-find)
* [Deleting a contact: `contact delete`](#deleting-a-contact-contact-delete)

**Game Management**
* [Adding a game: `game add`](#adding-a-game-to-a-contact-game-add)
* [Deleting a game: `game delete`](#deleting-a-game-from-a-contact-game-delete)
* [Listing games: `game list`](#listing-games-of-a-contact-game-list)

**Alias Management**
* [Adding an alias: `alias add`](#adding-an-alias-to-a-game-alias-add)
* [Editing an alias: `alias edit`](#editing-an-alias-of-a-game-alias-edit)
* [Deleting an alias: `alias delete`](#deleting-an-alias-from-a-game-alias-delete)

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103T-W09-1/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for Harmony.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar harmony.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * `list` : Lists all contacts.

    * `contact add n/John Doe` : Adds a contact named `John Doe` to Harmony.

    * `game add 1 g/Valorant` : Adds the game `Valorant` to the 1st contact shown.

    * `alias add 1 g/Valorant al/JohnnyV` : Adds the alias `JohnnyV` to the 1st contact's `Valorant` game.

    * `contact edit me e/Benny` : Edits your User Profile to display `Benny` as the name.

    * `view me` : Opens the profile view panel to display your own user profile.

    * `copy 1` : Copies the exact command needed to recreate the 1st contact to your system clipboard.

    * `theme light` : Switches the application's visual interface to Light Mode.

    * `contact delete n/John Doe` : Deletes the contact named `John Doe` (with confirmation prompt).

    * `clear` : Deletes all contacts.

    * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Commands follow the format `CATEGORY ACTION`, where `CATEGORY` is `contact`, `game`, or `alias`, followed by an action such as `add`, `delete`, or `edit`.<br>
  e.g. `contact add`, `game delete`, `alias add`.

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `contact add n/NAME`, `NAME` is a parameter which can be used as `contact add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g. `find KEYWORD [MORE_KEYWORDS]` can be used as `find John Doe` or as `find John`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. in `contact add n/NAME [g/GAME [al/ALIAS]…​]…​`, `[al/ALIAS]…​` means you can specify multiple aliases: `contact add n/Alice g/Valorant al/Ace al/ProGamer`.

* For `game`, `alias`, and target-specific commands, a contact can be targeted by their index (`INDEX`), by name (`n/CONTACT_NAME`), or by using the `me` keyword to target your own User Profile.<br>
  e.g. `game add 1 g/Valorant`, `game add n/John Doe g/Valorant`, and `game add me g/Valorant` are all valid.

* `INDEX` must be a positive integer and must appear before any prefixed parameters.<br>
  e.g. `game add 1 g/Valorant`, not `game add g/Valorant 1`.

* Prefixed parameters (those using `n/`, `g/`, `al/`, etc.) can be in any order.<br>
  e.g. `alias add n/John Doe g/Valorant al/JohnV` and `alias add g/Valorant n/John Doe al/JohnV` are both acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `undo`, `clear` and `exit`) will be ignored.<br>
  e.g. `help 123` will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
  </box>

## General

### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Listing all contacts: `list`

Shows a list of all contacts in Harmony.

Format: `list`


### Undoing the last command: `undo`

Reverses the most recently executed undoable command.

Format: `undo`

* Undoable commands include: `contact add`, `contact delete`, `contact edit`, `game add`, `game delete`, `alias add`, `alias delete`, and `clear`.
* Multiple consecutive `undo` calls will reverse commands in reverse order of execution.
* If there are no commands left to undo, an error message is shown.


### Clearing all entries: `clear`

Clears all entries from Harmony.

Format: `clear`


### Changing the UI theme: `theme`

Changes the visual appearance of the Harmony application.

Format: `theme THEME_NAME`

* Currently supported themes are `dark`, `light`, and `rainbow`.
* The `dark` theme is the default appearance.
* The application will immediately update its interface to the specified theme.

Examples:
* `theme light`
* `theme rainbow`


### Exiting the program: `exit`

Exits the program.

Format: `exit`

--------------------------------------------------------------------------------------------------------------------

## Contact Management

### Adding a contact: `contact add`

Adds a contact to Harmony.

Format: `contact add n/NAME [g/GAME [al/ALIAS]…​]…​`

<box type="tip" seamless>

**Tip:** A contact can have any number of games and aliases (including 0). Aliases must be declared after the game they belong to.
</box>

Examples:
* `contact add n/John Doe`
* `contact add n/Alice g/Valorant al/AliceV g/Minecraft`


### Viewing a contact's profile: `view`

Displays the full details, including all games and aliases, of a specific contact or your own user profile in the side panel.

Format:
* By index: `view INDEX`
* By name: `view n/NAME`
* User Profile: `view me`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `NAME` must match the contact's full name exactly (case-insensitive).
* Use the exact keyword `me` to view your own user profile.
* `contact view` also works as an alternative form.

Examples:
* `view 1`
* `view n/John Doe`
* `view me`


### Editing a contact's name: `contact edit`

Renames an existing contact or your own User Profile in Harmony.

Format:
* By index: `contact edit INDEX e/NEW_NAME`
* By name: `contact edit n/NAME e/NEW_NAME`
* User Profile: `contact edit me e/NEW_NAME`

* `INDEX` refers to the index number shown in the displayed contact list. Must be a positive integer.
* Providing both `INDEX` and `n/NAME` at the same time is not allowed.
* The contact's games and aliases are preserved after the rename.
* `NEW_NAME` must not already belong to another contact.

Examples:
* `contact edit 1 e/John Smith` Renames the 1st contact to `John Smith`.
* `contact edit n/John Doe e/John Smith` Renames `John Doe` to `John Smith`.
* `contact edit me e/ProGamer99` Renames your own user profile to `ProGamer99`.


### Locating contacts: `find`

Finds contacts by name, game, or alias.

**Find by name:**

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only full words will be matched e.g. `Han` will not match `Hans`
* All keywords must be present in the name (i.e. `AND` search).
  e.g. `find Hans Bo` will only return contacts whose name contains both `Hans` and `Bo`

Examples:
* `find John` returns `john` and `John Doe`
* `find John Doe` returns `John Doe` but not `John Smith` or `Jane Doe`
* `find alex david` returns `Alex David` but not `Alex Yeoh` or `David Li`

**Find by game:**

Format: `find g/GAME_NAME`

* Returns all contacts who have the specified game.
* The search is case-insensitive.

Examples:
* `find g/Valorant` returns all contacts who have `Valorant` in their game list.

**Find by alias:**

Format: `find al/ALIAS`

* Returns all contacts who have the specified alias (across all games).
* The search is case-insensitive.

Examples:
* `find al/Benjumpin` returns all contacts with the alias `Benjumpin`.

**Combined search:**

Format: `find [KEYWORD]…​ [g/GAME_NAME] [al/ALIAS]`

* All specified constraints must be satisfied (i.e. `AND` search).
* At least one constraint must be provided.

Examples:
* `find Alice g/Valorant` returns contacts named `Alice` who have `Valorant` in their game list.
* `find g/Valorant al/Ace` returns contacts who play `Valorant` with the alias `Ace`.
* `find Alice g/Valorant al/Ace` returns contacts named `Alice` who play `Valorant` with the alias `Ace`.


### Deleting a contact: `contact delete`

Deletes the specified contact from Harmony.

Format: `contact delete INDEX` or `contact delete n/NAME`

* Deletes the contact at the specified `INDEX` in the displayed list, or whose name matches `NAME` (case-insensitive).
* `INDEX` must be a positive integer (e.g., 1, 2, 3…​). Note: You can also use `0` to target your own user profile.
* A confirmation prompt will appear. Type `y` or `yes` to confirm, or `n` or `no` to cancel.
* Any other input cancels the deletion.

Examples:
* `contact delete 1` prompts for confirmation, then deletes the 1st contact in the list.
* `contact delete n/John Doe` prompts for confirmation, then deletes the contact named `John Doe`.


### Copying a contact: `copy`

Copies the exact `contact add` command for the specified contact (or your own profile) to your system clipboard. This makes it easy to share contact details with other Harmony users!

Format:
* By index: `copy INDEX`
* By name: `copy n/NAME`
* User Profile: `copy me`

The copied output will look like:
```
contact add n/John Doe g/Valorant al/JohnnyV g/Minecraft
```

Your friend can paste this command directly into their own Harmony command box to add you as a contact, complete with all your games and aliases.

Examples:
* `copy 1`
* `copy n/John Doe`
* `copy me`

--------------------------------------------------------------------------------------------------------------------

## Game Management

### Adding a game to a contact: `game add`

Adds a game to an existing contact or your user profile.

Format (by index): `game add INDEX g/GAME_NAME`
Format (by name): `game add n/CONTACT_NAME g/GAME_NAME`
Format (User Profile): `game add me g/GAME_NAME`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).
* The game cannot already exist for that contact.

Examples:
* `game add 1 g/Minecraft`
* `game add n/John Doe g/Valorant`
* `game add me g/Overwatch`

### Deleting a game from a contact: `game delete`

Removes a game from an existing contact or your user profile.

Format (by index): `game delete INDEX g/GAME_NAME`
Format (by name): `game delete n/CONTACT_NAME g/GAME_NAME`
Format (User Profile): `game delete me g/GAME_NAME`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).
* The game must exist for that contact.
* A confirmation prompt will appear. Type `y` or `yes` to confirm, or `n` or `no` to cancel.
* Any other input cancels the deletion.

Examples:
* `game delete 1 g/Minecraft` prompts for confirmation, then removes Minecraft from the 1st contact.
* `game delete me g/Valorant` prompts for confirmation, then removes Valorant from your profile.

### Listing games of a contact: `game list`

Lists all games of an existing contact or your user profile.

Format (by index): `game list INDEX`
Format (by name): `game list n/CONTACT_NAME`
Format (User Profile): `game list me`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).

Examples:
* `game list 1`
* `game list me`

--------------------------------------------------------------------------------------------------------------------

## Alias Management

<box type="info" seamless>

**Note:** Alias commands require the contact to already have the specified game. Use `game add` first if needed.

</box>

### Adding an alias to a game: `alias add`

Adds an alias to a game of an existing contact or your user profile.

Format (by index): `alias add INDEX g/GAME_NAME al/ALIAS`
Format (by name): `alias add n/CONTACT_NAME g/GAME_NAME al/ALIAS`
Format (User Profile): `alias add me g/GAME_NAME al/ALIAS`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).
* The contact must already have the specified game.
* The alias cannot already exist for that game.

Examples:
* `alias add 1 g/Valorant al/Benjumpin`
* `alias add me g/Valorant al/Benjumpin`

### Editing an alias of a game: `alias edit`

Updates an existing alias of a game for a contact or your user profile.

Format (by index): `alias edit INDEX g/GAME_NAME al/OLD_ALIAS na/NEW_ALIAS`
Format (by name): `alias edit n/CONTACT_NAME g/GAME_NAME al/OLD_ALIAS na/NEW_ALIAS`
Format (User Profile): `alias edit me g/GAME_NAME al/OLD_ALIAS na/NEW_ALIAS`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).
* The contact must already have the specified game.
* `OLD_ALIAS` must already exist for that game.
* `NEW_ALIAS` must not already exist for that game.

Examples:
* `alias edit 1 g/Valorant al/JohnnyV na/JohnnyValorant`
* `alias edit n/John Doe g/Valorant al/JohnnyV na/JohnnyValorant`
* `alias edit me g/Valorant al/JohnnyV na/JohnnyValorant`

### Deleting an alias from a game: `alias delete`

Removes an alias from a game of an existing contact or your user profile.

Format (by index): `alias delete INDEX g/GAME_NAME al/ALIAS`
Format (by name): `alias delete n/CONTACT_NAME g/GAME_NAME al/ALIAS`
Format (User Profile): `alias delete me g/GAME_NAME al/ALIAS`

* `INDEX` must be a positive integer 1, 2, 3, …​
* `CONTACT_NAME` must match the contact's full name exactly (case-insensitive).
* The contact must already have the specified game.
* The alias must exist for that game.
* A confirmation prompt will appear. Type `y` or `yes` to confirm, or `n` or `no` to cancel.
* Any other input cancels the deletion.

Examples:
* `alias delete 1 g/Valorant al/Benjumpin` prompts for confirmation, then removes the alias from the 1st contact.
* `alias delete me g/Valorant al/Benjumpin` prompts for confirmation, then removes the alias from your profile.

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

| Action              | Format, Examples |
|---------------------|-----------------|
| **List** | `list` |
| **Help** | `help` |
| **Undo** | `undo` |
| **Theme** | `theme THEME_NAME`<br> e.g., `theme light` |
| **Contact Add** | `contact add n/NAME [g/GAME [al/ALIAS]…​]…​` <br> e.g., `contact add n/James Ho` |
| **View** | `view INDEX` or `view n/NAME` or `view me`<br> e.g., `view me` (also: `contact view me`) |
| **Contact Delete** | `contact delete INDEX` or `contact delete n/NAME`<br> e.g., `contact delete 1` or `contact delete n/James Ho` |
| **Contact Edit** | `contact edit INDEX e/NEW_NAME` or `contact edit n/NAME e/NEW_NAME` or `contact edit me e/NEW_NAME`<br> e.g., `contact edit me e/James Lee` |
| **Copy** | `copy INDEX` or `copy n/NAME` or `copy me`<br> e.g., `copy 1` |
| **Clear** | `clear` |
| **Alias Add** | `alias add INDEX g/GAME_NAME al/ALIAS` or `alias add n/CONTACT_NAME g/GAME_NAME al/ALIAS` or `alias add me g/GAME_NAME al/ALIAS`<br> e.g., `alias add 1 g/Valorant al/Benjumpin` |
| **Alias Edit** | `alias edit INDEX g/GAME_NAME al/OLD_ALIAS na/NEW_ALIAS` or `alias edit n/CONTACT_NAME g/GAME_NAME al/OLD_ALIAS na/NEW_ALIAS` or `alias edit me g/GAME_NAME al/OLD_ALIAS na/NEW_ALIAS`<br> e.g., `alias edit 1 g/Valorant al/JohnnyV na/JohnnyValorant` |
| **Alias Delete** | `alias delete INDEX g/GAME_NAME al/ALIAS` or `alias delete n/CONTACT_NAME g/GAME_NAME al/ALIAS` or `alias delete me g/GAME_NAME al/ALIAS`<br> e.g., `alias delete 1 g/Valorant al/Benjumpin` |
| **Game Add** | `game add INDEX g/GAME_NAME` or `game add n/CONTACT_NAME g/GAME_NAME` or `game add me g/GAME_NAME`<br> e.g., `game add 1 g/Minecraft` |
| **Game Delete** | `game delete INDEX g/GAME_NAME` or `game delete n/CONTACT_NAME g/GAME_NAME` or `game delete me g/GAME_NAME`<br> e.g., `game delete 1 g/Minecraft` |
| **Game List** | `game list INDEX` or `game list n/CONTACT_NAME` or `game list me`<br> e.g., `game list 1` |
| **Find (name)** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake` |
| **Find (game)** | `find g/GAME_NAME`<br> e.g., `find g/Valorant` |
| **Find (alias)** | `find al/ALIAS`<br> e.g., `find al/Benjumpin` |
