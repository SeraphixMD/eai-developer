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
import java.util.List;
import java.util.Optional;

import be.nabu.eai.developer.api.KeybindAction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Modal dialog for capturing and editing keyboard shortcuts.
 */
public class KeybindEditor {

    private final KeybindAction action;
    private KeyCombination capturedCombination;
    private boolean confirmed = false;

    private Stage dialog;
    private Label captureLabel;
    private Label conflictLabel;

    /**
     * Creates a new keybind editor for the given action.
     */
    public KeybindEditor(KeybindAction action) {
        this.action = action;
    }

    /**
     * Shows the dialog and returns the new key combination if confirmed.
     * Returns empty if cancelled, or the new combination if confirmed.
     */
    public Optional<KeyCombination> showAndWait() {
        createDialog();
        dialog.showAndWait();

        if (confirmed) {
            return Optional.ofNullable(capturedCombination);
        }
        return Optional.empty();
    }

    /**
     * Creates the dialog UI.
     */
    private void createDialog() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Edit Keybind - " + action.getDescription());
        dialog.setResizable(false);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Action description
        Label actionLabel = new Label(action.getDescription());
        actionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Current binding
        KeyCombination current = KeybindRegistry.getInstance().getKeybind(action);
        Label currentLabel = new Label("Current: " + (current != null ? current.getDisplayText() : "Not set"));
        currentLabel.setStyle("-fx-text-fill: #666666;");

        // Default binding
        Label defaultLabel = new Label("Default: " + action.getDefaultKeyCombinationDisplayText());
        defaultLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px;");

        // Instructions
        Label instructionLabel = new Label("Press the key combination you want to use:");
        instructionLabel.setStyle("-fx-font-size: 12px;");

        // Capture area
        captureLabel = new Label("Press keys...");
        captureLabel.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: monospace; " +
                        "-fx-background-color: white; -fx-padding: 15 30 15 30; " +
                        "-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");
        captureLabel.setMinWidth(200);
        captureLabel.setAlignment(Pos.CENTER);

        // Conflict warning
        conflictLabel = new Label("");
        conflictLabel.setStyle("-fx-text-fill: #cc6600; -fx-font-size: 11px;");
        conflictLabel.setWrapText(true);
        conflictLabel.setMaxWidth(300);
        conflictLabel.setAlignment(Pos.CENTER);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button okButton = new Button("OK");
        okButton.setMinWidth(80);
        okButton.setDefaultButton(true);
        okButton.setOnAction(e -> {
            confirmed = true;
            dialog.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setMinWidth(80);
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(e -> {
            confirmed = false;
            dialog.close();
        });

        Button resetButton = new Button("Reset to Default");
        resetButton.setOnAction(e -> {
            capturedCombination = action.getDefaultKeyCombination();
            updateCaptureDisplay();
        });

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            capturedCombination = null;
            captureLabel.setText("Not set");
            conflictLabel.setText("");
        });

        buttonBox.getChildren().addAll(okButton, cancelButton, resetButton, clearButton);

        root.getChildren().addAll(
                actionLabel,
                currentLabel,
                defaultLabel,
                instructionLabel,
                captureLabel,
                conflictLabel,
                buttonBox);

        Scene scene = new Scene(root, 400, 280);

        // Capture key events
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);

        dialog.setScene(scene);
    }

    /**
     * Handles key press events for capturing the key combination.
     */
    private void handleKeyPressed(KeyEvent event) {
        // Ignore modifier-only presses
        if (isModifierKey(event.getCode())) {
            return;
        }

        // Build the key combination
        capturedCombination = buildKeyCombination(event);
        updateCaptureDisplay();

        event.consume();
    }

    /**
     * Handles key release events.
     */
    private void handleKeyReleased(KeyEvent event) {
        // No action needed
    }

    /**
     * Checks if a key code is a modifier key.
     */
    private boolean isModifierKey(KeyCode code) {
        return code == KeyCode.SHIFT ||
                code == KeyCode.CONTROL ||
                code == KeyCode.ALT ||
                code == KeyCode.META ||
                code == KeyCode.COMMAND ||
                code == KeyCode.SHORTCUT;
    }

    /**
     * Builds a KeyCombination from a KeyEvent.
     */
    private KeyCombination buildKeyCombination(KeyEvent event) {
        List<KeyCombination.Modifier> modifiers = new ArrayList<>();

        if (event.isControlDown()) {
            modifiers.add(KeyCombination.CONTROL_DOWN);
        }
        if (event.isShiftDown()) {
            modifiers.add(KeyCombination.SHIFT_DOWN);
        }
        if (event.isAltDown()) {
            modifiers.add(KeyCombination.ALT_DOWN);
        }
        if (event.isMetaDown()) {
            modifiers.add(KeyCombination.META_DOWN);
        }

        return new KeyCodeCombination(event.getCode(), modifiers.toArray(new KeyCombination.Modifier[0]));
    }

    /**
     * Updates the capture display with the current combination.
     */
    private void updateCaptureDisplay() {
        if (capturedCombination != null) {
            captureLabel.setText(capturedCombination.getDisplayText());
            checkConflicts();
        } else {
            captureLabel.setText("Not set");
            conflictLabel.setText("");
        }
    }

    /**
     * Checks for conflicts with other keybinds.
     */
    private void checkConflicts() {
        if (capturedCombination == null) {
            conflictLabel.setText("");
            return;
        }

        KeybindAction conflict = KeybindRegistry.getInstance().findActionByKeybind(capturedCombination);
        if (conflict != null && conflict != action) {
            conflictLabel.setText(
                    "Warning: This will unbind '" + conflict.getDescription() + "' (" + conflict.getCategory() + ")");
        } else if (capturedCombination.equals(action.getDefaultKeyCombination())) {
            conflictLabel.setText("(Default binding)");
            conflictLabel.setStyle("-fx-text-fill: #339933; -fx-font-size: 11px;");
        } else {
            conflictLabel.setText("");
        }
    }

    /**
     * Static helper to edit a keybind with a dialog.
     * Returns true if the keybind was changed.
     */
    public static boolean editKeybind(KeybindAction action) {
        KeybindEditor editor = new KeybindEditor(action);
        Optional<KeyCombination> result = editor.showAndWait();

        if (result.isPresent()) {
            KeyCombination newCombination = result.get();
            KeybindRegistry.getInstance().setKeybind(action, newCombination);
            return true;
        } else if (editor.confirmed && editor.capturedCombination == null) {
            // User confirmed with "clear"
            KeybindRegistry.getInstance().clearKeybind(action);
            return true;
        }

        return false;
    }
}
