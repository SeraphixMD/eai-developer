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

package be.nabu.eai.developer.util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import be.nabu.eai.developer.api.KeybindAction;
import javafx.scene.input.KeyCombination;

/**
 * Central registry for managing keyboard shortcuts.
 * Provides a single point of access for keybind lookups and modifications.
 */
public class KeybindRegistry {

    private static KeybindRegistry instance;

    private final Map<KeybindAction, KeyCombination> customKeybinds;
    private final List<KeybindChangeListener> listeners;
    private final KeybindConfiguration configuration;

    /**
     * Listener interface for keybind changes.
     */
    public interface KeybindChangeListener {
        /**
         * Called when a keybind is changed.
         *
         * @param action         The action whose keybind changed
         * @param oldCombination The previous key combination (may be null)
         * @param newCombination The new key combination (may be null for reset)
         */
        void onKeybindChanged(KeybindAction action, KeyCombination oldCombination, KeyCombination newCombination);
    }

    private KeybindRegistry() {
        this.customKeybinds = new EnumMap<>(KeybindAction.class);
        this.listeners = new ArrayList<>();
        this.configuration = new KeybindConfiguration();
        loadConfiguration();
    }

    /**
     * Gets the singleton instance of the registry.
     */
    public static synchronized KeybindRegistry getInstance() {
        if (instance == null) {
            instance = new KeybindRegistry();
        }
        return instance;
    }

    /**
     * Loads the keybind configuration from disk.
     */
    private void loadConfiguration() {
        Map<KeybindAction, KeyCombination> loaded = configuration.load();
        if (loaded != null) {
            customKeybinds.putAll(loaded);
        }
    }

    /**
     * Saves the current keybind configuration to disk.
     */
    private void saveConfiguration() {
        configuration.save(customKeybinds);
    }

    /**
     * Gets the current key combination for an action.
     * Returns the custom binding if set, otherwise the default.
     */
    public KeyCombination getKeybind(KeybindAction action) {
        if (action == null) {
            return null;
        }
        KeyCombination custom = customKeybinds.get(action);
        return custom != null ? custom : action.getDefaultKeyCombination();
    }

    /**
     * Checks if an action has a custom (non-default) keybind.
     */
    public boolean hasCustomKeybind(KeybindAction action) {
        return customKeybinds.containsKey(action);
    }

    /**
     * Sets a custom key combination for an action.
     * This will also clear any conflicting keybinds.
     */
    public void setKeybind(KeybindAction action, KeyCombination combination) {
        if (action == null) {
            return;
        }

        KeyCombination oldCombination = getKeybind(action);

        // Check for conflicts and clear them
        if (combination != null) {
            KeybindAction conflict = findActionByKeybind(combination);
            if (conflict != null && conflict != action) {
                // Clear the conflicting keybind
                clearKeybind(conflict);
            }
        }

        // Set the new keybind
        if (combination == null || combination.equals(action.getDefaultKeyCombination())) {
            // Remove custom binding to use default
            customKeybinds.remove(action);
        } else {
            customKeybinds.put(action, combination);
        }

        saveConfiguration();

        // Notify listeners
        KeyCombination newCombination = getKeybind(action);
        if (!keyCombinationsEqual(oldCombination, newCombination)) {
            notifyListeners(action, oldCombination, newCombination);
        }
    }

    /**
     * Clears a custom keybind, setting the action to have no keybind.
     */
    public void clearKeybind(KeybindAction action) {
        if (action == null) {
            return;
        }

        KeyCombination oldCombination = getKeybind(action);

        // Use a special marker to indicate "no keybind"
        customKeybinds.put(action, KeybindConfiguration.NO_KEYBIND_MARKER);

        saveConfiguration();
        notifyListeners(action, oldCombination, null);
    }

    /**
     * Resets an action to its default keybind.
     */
    public void resetToDefault(KeybindAction action) {
        if (action == null) {
            return;
        }

        KeyCombination oldCombination = getKeybind(action);
        customKeybinds.remove(action);

        saveConfiguration();

        KeyCombination newCombination = action.getDefaultKeyCombination();
        if (!keyCombinationsEqual(oldCombination, newCombination)) {
            notifyListeners(action, oldCombination, newCombination);
        }
    }

    /**
     * Resets all actions to their default keybinds.
     */
    public void resetAllToDefaults() {
        for (KeybindAction action : KeybindAction.values()) {
            KeyCombination oldCombination = getKeybind(action);
            KeyCombination newCombination = action.getDefaultKeyCombination();

            if (!keyCombinationsEqual(oldCombination, newCombination)) {
                notifyListeners(action, oldCombination, newCombination);
            }
        }

        customKeybinds.clear();
        saveConfiguration();
    }

    /**
     * Finds the action currently bound to a key combination.
     * Returns null if no action is bound to the combination.
     */
    public KeybindAction findActionByKeybind(KeyCombination combination) {
        if (combination == null) {
            return null;
        }

        for (KeybindAction action : KeybindAction.values()) {
            KeyCombination current = getKeybind(action);
            if (current != null && keyCombinationsEqual(current, combination)) {
                return action;
            }
        }
        return null;
    }

    /**
     * Finds all actions in a specific category.
     */
    public List<KeybindAction> getActionsByCategory(String category) {
        List<KeybindAction> actions = new ArrayList<>();
        for (KeybindAction action : KeybindAction.values()) {
            if (action.getCategory().equals(category)) {
                actions.add(action);
            }
        }
        return actions;
    }

    /**
     * Gets all unique categories.
     */
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        for (KeybindAction action : KeybindAction.values()) {
            if (!categories.contains(action.getCategory())) {
                categories.add(action.getCategory());
            }
        }
        return categories;
    }

    /**
     * Gets the display text for the current keybind of an action.
     */
    public String getKeybindDisplayText(KeybindAction action) {
        KeyCombination combination = getKeybind(action);
        return combination != null ? combination.getDisplayText() : "Not set";
    }

    /**
     * Adds a listener for keybind changes.
     */
    public void addListener(KeybindChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener for keybind changes.
     */
    public void removeListener(KeybindChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners of a keybind change.
     */
    private void notifyListeners(KeybindAction action, KeyCombination oldCombination, KeyCombination newCombination) {
        for (KeybindChangeListener listener : new ArrayList<>(listeners)) {
            try {
                listener.onKeybindChanged(action, oldCombination, newCombination);
            } catch (Exception e) {
                // Don't let listener errors affect other listeners
                e.printStackTrace();
            }
        }
    }

    /**
     * Compares two key combinations for equality.
     */
    private boolean keyCombinationsEqual(KeyCombination a, KeyCombination b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.getName().equals(b.getName());
    }

    /**
     * Checks if a key combination matches the keybind for an action.
     */
    public boolean matches(KeybindAction action, KeyCombination combination) {
        KeyCombination bound = getKeybind(action);
        return bound != null && combination != null && keyCombinationsEqual(bound, combination);
    }

    /**
     * Checks if a key event matches the keybind for an action.
     */
    public boolean matches(KeybindAction action, javafx.scene.input.KeyEvent event) {
        KeyCombination bound = getKeybind(action);
        return bound != null && bound.match(event);
    }
}
