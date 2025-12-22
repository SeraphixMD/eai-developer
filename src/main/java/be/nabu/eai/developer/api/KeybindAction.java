/*
* Copyright (C) 2014 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.eai.developer.api;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Defines all remappable keyboard actions in the developer application.
 * Each action has a default key combination, description, and category.
 */
public enum KeybindAction {

    // File Operations
    SAVE(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), "Save", "File Operations"),
    SAVE_ALL(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN), "Save All",
            "File Operations"),
    CLOSE(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN), "Close", "File Operations"),
    CLOSE_ALL(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN), "Close All",
            "File Operations"),
    CLOSE_OTHER(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN,
            KeyCombination.ALT_DOWN), "Close Other", "File Operations"),
    DETACH_TAB(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN), "Detach/Reattach Tab",
            "File Operations"),

    // Navigation
    FIND_IN_TREE(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN), "Find In Tree", "Navigation"),
    FIND_IN_FILES(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
            "Find In Files", "Navigation"),
    LOCATE_IN_TREE(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN), "Locate In Tree", "Navigation"),

    // View
    TOGGLE_PROPERTIES(new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN),
            "Toggle Properties / Maximize", "View"),
    VIEW_SERVER_LOG(new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN), "View Server Log", "View"),
    VIEW_TODOS(new KeyCodeCombination(KeyCode.J, KeyCombination.CONTROL_DOWN), "View Todos", "View"),
    CLEAR_SERVER_LOG(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN), "Clear Server Log", "View"),
    FULL_SCREEN(new KeyCodeCombination(KeyCode.F11), "Full Screen / Set Changed", "View"),

    // Execution
    RUN_SERVICE(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN), "Run Service", "Execution"),
    REFRESH(new KeyCodeCombination(KeyCode.F5), "Refresh", "Execution"),
    HARD_REFRESH(new KeyCodeCombination(KeyCode.F5, KeyCombination.CONTROL_DOWN), "Hard Refresh (with file system)",
            "Execution"),
    VALIDATE(new KeyCodeCombination(KeyCode.F2), "Validate", "Execution"),

    // Tree Operations
    RENAME_ITEM(new KeyCodeCombination(KeyCode.F2), "Rename Item", "Tree Operations"),
    DELETE_ITEM(new KeyCodeCombination(KeyCode.DELETE), "Delete Item", "Tree Operations"),
    MOVE_ITEM_UP(new KeyCodeCombination(KeyCode.UP, KeyCombination.CONTROL_DOWN), "Move Item Up", "Tree Operations"),
    MOVE_ITEM_DOWN(new KeyCodeCombination(KeyCode.DOWN, KeyCombination.CONTROL_DOWN), "Move Item Down",
            "Tree Operations"),
    MOVE_ITEM_LEFT(new KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_DOWN), "Move Item to Parent",
            "Tree Operations"),
    MOVE_ITEM_RIGHT(new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.CONTROL_DOWN), "Move Item into Sibling",
            "Tree Operations"),
    COPY(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN), "Copy", "Tree Operations"),
    PASTE(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN), "Paste", "Tree Operations"),
    EXPAND_TREE(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN), "Expand Tree", "Tree Operations"),
    COLLAPSE_TREE(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
            "Collapse Tree", "Tree Operations"),

    // Service Editor
    ENABLE_DISABLE_STEP(new KeyCodeCombination(KeyCode.F1), "Enable/Disable Step", "Service Editor"),
    EDIT_STEP_COMMENT(new KeyCodeCombination(KeyCode.F2), "Edit Step Comment/Label", "Service Editor"),
    TOGGLE_INDEX_QUERY_LINE(new KeyCodeCombination(KeyCode.F8), "Toggle Index Query Line (Blue Line)",
            "Service Editor"),
    CLONE_STEP(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN), "Clone Step", "Service Editor"),
    DELETE_MAPPING(new KeyCodeCombination(KeyCode.DELETE), "Delete Mapping/Invoke", "Service Editor"),
    LOCATE_SERVICE(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN), "Locate Service in Tree (on invoke)",
            "Service Editor"),
    CREATE_LINK(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN), "Create Link (input to output)",
            "Service Editor"),
    CREATE_MASK_LINK(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN), "Create Mask Link (input to output)",
            "Service Editor"),
    PASTE_SERVICE(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN), "Paste Service (in service tree)",
            "Service Editor"),

    // Code Editor
    EDITOR_COPY(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN), "Copy", "Code Editor"),
    EDITOR_PASTE(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN), "Paste", "Code Editor"),
    EDITOR_SAVE(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), "Save", "Code Editor"),
    EDITOR_EXECUTE(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN), "Execute (in console)",
            "Code Editor"),
    EDITOR_FULL_SCREEN(new KeyCodeCombination(KeyCode.F11), "Full Screen", "Code Editor"),

    // Type/Structure Editor
    TOGGLE_OPTIONAL(new KeyCodeCombination(KeyCode.F3), "Toggle Optional/Mandatory", "Type Editor"),
    TOGGLE_LIST(new KeyCodeCombination(KeyCode.F4), "Toggle List/Single", "Type Editor"),
    CLONE_ELEMENT(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN), "Clone Element", "Type Editor"),
    SWITCH_TO_STRING(new KeyCodeCombination(KeyCode.F1, KeyCombination.META_DOWN), "Switch to String type",
            "Type Editor"),
    SWITCH_TO_UUID(new KeyCodeCombination(KeyCode.F2, KeyCombination.META_DOWN), "Switch to UUID type", "Type Editor"),
    SWITCH_TO_LONG(new KeyCodeCombination(KeyCode.F3, KeyCombination.META_DOWN), "Switch to Long type", "Type Editor"),

    // Miscellaneous
    TOGGLE_FIREBUG(new KeyCodeCombination(KeyCode.F12), "Toggle Firebug (Web Browser)", "Miscellaneous");

    private final KeyCombination defaultKeyCombination;
    private final String description;
    private final String category;

    KeybindAction(KeyCombination defaultKeyCombination, String description, String category) {
        this.defaultKeyCombination = defaultKeyCombination;
        this.description = description;
        this.category = category;
    }

    /**
     * Gets the default key combination for this action.
     */
    public KeyCombination getDefaultKeyCombination() {
        return defaultKeyCombination;
    }

    /**
     * Gets the human-readable description of this action.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the category this action belongs to.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the display string for the default key combination.
     */
    public String getDefaultKeyCombinationDisplayText() {
        return defaultKeyCombination.getDisplayText();
    }

    /**
     * Converts a KeyCombination to a string representation for storage.
     */
    public static String keyCombinationToString(KeyCombination combination) {
        if (combination == null) {
            return null;
        }
        return combination.getName();
    }

    /**
     * Parses a string representation back to a KeyCombination.
     */
    public static KeyCombination stringToKeyCombination(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return KeyCombination.valueOf(str);
    }
}
