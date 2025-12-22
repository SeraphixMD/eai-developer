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

import be.nabu.eai.developer.api.KeybindAction;
import be.nabu.jfx.control.ace.AceEditor;
import javafx.scene.input.KeyCombination;

/**
 * Helper class to configure AceEditor instances with keybinds from the
 * KeybindRegistry.
 */
public class AceEditorKeybindHelper {

    /**
     * Configures an AceEditor with the current keybinds from the registry.
     * Call this after creating a new AceEditor instance.
     */
    public static void configureKeybinds(AceEditor editor) {
        KeybindRegistry registry = KeybindRegistry.getInstance();

        // Editor-specific keybinds
        KeyCombination copy = registry.getKeybind(KeybindAction.EDITOR_COPY);
        if (copy != null) {
            editor.setKeyCombination(AceEditor.COPY, copy);
        }

        KeyCombination paste = registry.getKeybind(KeybindAction.EDITOR_PASTE);
        if (paste != null) {
            editor.setKeyCombination(AceEditor.PASTE, paste);
        }

        KeyCombination save = registry.getKeybind(KeybindAction.EDITOR_SAVE);
        if (save != null) {
            editor.setKeyCombination(AceEditor.SAVE, save);
        }

        KeyCombination close = registry.getKeybind(KeybindAction.CLOSE);
        if (close != null) {
            editor.setKeyCombination(AceEditor.CLOSE, close);
        }

        KeyCombination closeAll = registry.getKeybind(KeybindAction.CLOSE_ALL);
        if (closeAll != null) {
            editor.setKeyCombination(AceEditor.CLOSE_ALL, closeAll);
        }

        KeyCombination fullScreen = registry.getKeybind(KeybindAction.EDITOR_FULL_SCREEN);
        if (fullScreen != null) {
            editor.setKeyCombination(AceEditor.FULL_SCREEN, fullScreen);
        }
    }

    /**
     * Creates a new AceEditor and configures it with keybinds from the registry.
     */
    public static AceEditor createConfiguredEditor() {
        AceEditor editor = new AceEditor();
        configureKeybinds(editor);
        return editor;
    }
}
