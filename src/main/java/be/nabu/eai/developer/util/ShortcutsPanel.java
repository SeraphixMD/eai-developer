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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.api.KeybindAction;
import be.nabu.eai.developer.util.KeybindRegistry.KeybindChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Panel that displays all keyboard shortcuts in the developer application,
 * organized by category. Supports interactive editing of keybinds.
 */
public class ShortcutsPanel implements KeybindChangeListener {

    private static final String CUSTOM_KEYBIND_STYLE = "-fx-font-weight: bold; -fx-font-family: monospace; " +
            "-fx-background-color: #d4edda; -fx-padding: 3 8 3 8; " +
            "-fx-background-radius: 3; -fx-border-color: #28a745; -fx-border-radius: 3;";

    private static final String DEFAULT_KEYBIND_STYLE = "-fx-font-weight: bold; -fx-font-family: monospace; " +
            "-fx-background-color: #e8e8e8; -fx-padding: 3 8 3 8; " +
            "-fx-background-radius: 3;";

    private static final String UNSET_KEYBIND_STYLE = "-fx-font-style: italic; -fx-font-family: monospace; " +
            "-fx-background-color: #f8d7da; -fx-padding: 3 8 3 8; " +
            "-fx-background-radius: 3; -fx-text-fill: #721c24;";

    private final Map<KeybindAction, Label> keybindLabels = new HashMap<>();
    private VBox root;

    /**
     * Creates and displays the shortcuts panel in a tab in the central panel.
     */
    public static void show() {
        Tab tab = MainController.getInstance().newTab("Keyboard Shortcuts");
        ShortcutsPanel panel = new ShortcutsPanel();
        AnchorPane pane = new AnchorPane();
        VBox content = panel.buildPanel();
        pane.getChildren().add(content);
        AnchorPane.setTopAnchor(content, 0d);
        AnchorPane.setBottomAnchor(content, 0d);
        AnchorPane.setLeftAnchor(content, 0d);
        AnchorPane.setRightAnchor(content, 0d);
        tab.setContent(pane);

        // Register for keybind changes
        KeybindRegistry.getInstance().addListener(panel);

        // Unregister when tab closes
        tab.setOnClosed(e -> {
            KeybindRegistry.getInstance().removeListener(panel);
        });
    }

    /**
     * Builds the shortcuts panel UI.
     */
    public VBox buildPanel() {
        root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(15));
        root.getStyleClass().add("shortcuts-panel");

        // Header with title and reset button
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Keyboard Shortcuts");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        Label subtitleLabel = new Label("Click on a shortcut to customize it");
        subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");

        Button resetAllButton = new Button("Reset All to Defaults");
        resetAllButton.setOnAction(e -> {
            KeybindRegistry.getInstance().resetAllToDefaults();
        });

        header.getChildren().addAll(titleLabel, subtitleLabel, resetAllButton);
        header.setPadding(new Insets(0, 0, 10, 0));
        root.getChildren().add(header);

        // Legend
        HBox legend = createLegend();
        root.getChildren().add(legend);

        VBox categoriesBox = new VBox();
        categoriesBox.setSpacing(5);

        KeybindRegistry registry = KeybindRegistry.getInstance();
        List<String> categories = registry.getCategories();

        for (String category : categories) {
            List<KeybindAction> actions = registry.getActionsByCategory(category);
            TitledPane categoryPane = createCategoryPane(category, actions);
            categoriesBox.getChildren().add(categoryPane);
        }

        ScrollPane scrollPane = new ScrollPane(categoriesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.getChildren().add(scrollPane);

        return root;
    }

    /**
     * Creates the legend showing what the colors mean.
     */
    private HBox createLegend() {
        HBox legend = new HBox(20);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setPadding(new Insets(5, 0, 10, 0));

        HBox defaultLegend = new HBox(5);
        defaultLegend.setAlignment(Pos.CENTER_LEFT);
        Label defaultSample = new Label("Ctrl+S");
        defaultSample.setStyle(DEFAULT_KEYBIND_STYLE);
        Label defaultText = new Label("Default");
        defaultText.setStyle("-fx-font-size: 11px;");
        defaultLegend.getChildren().addAll(defaultSample, defaultText);

        HBox customLegend = new HBox(5);
        customLegend.setAlignment(Pos.CENTER_LEFT);
        Label customSample = new Label("Ctrl+X");
        customSample.setStyle(CUSTOM_KEYBIND_STYLE);
        Label customText = new Label("Custom");
        customText.setStyle("-fx-font-size: 11px;");
        customLegend.getChildren().addAll(customSample, customText);

        HBox unsetLegend = new HBox(5);
        unsetLegend.setAlignment(Pos.CENTER_LEFT);
        Label unsetSample = new Label("Not set");
        unsetSample.setStyle(UNSET_KEYBIND_STYLE);
        Label unsetText = new Label("Unbound");
        unsetText.setStyle("-fx-font-size: 11px;");
        unsetLegend.getChildren().addAll(unsetSample, unsetText);

        legend.getChildren().addAll(defaultLegend, customLegend, unsetLegend);

        return legend;
    }

    /**
     * Creates a TitledPane for a category of shortcuts.
     */
    private TitledPane createCategoryPane(String categoryName, List<KeybindAction> actions) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(8);
        grid.setPadding(new Insets(10));

        int row = 0;
        for (KeybindAction action : actions) {
            Label keyLabel = createKeybindLabel(action);
            keybindLabels.put(action, keyLabel);

            Label descLabel = new Label(action.getDescription());
            descLabel.setStyle("-fx-text-fill: #333333;");

            grid.add(keyLabel, 0, row);
            grid.add(descLabel, 1, row);
            row++;
        }

        TitledPane pane = new TitledPane(categoryName, grid);
        pane.setExpanded(true);
        pane.setCollapsible(true);

        return pane;
    }

    /**
     * Creates an interactive keybind label for an action.
     */
    private Label createKeybindLabel(KeybindAction action) {
        KeybindRegistry registry = KeybindRegistry.getInstance();
        KeyCombination combination = registry.getKeybind(action);

        Label keyLabel = new Label();
        keyLabel.setMinWidth(150);
        keyLabel.setAlignment(Pos.CENTER_LEFT);
        keyLabel.setCursor(Cursor.HAND);

        updateKeybindLabel(keyLabel, action, combination);

        // Make clickable for editing
        keyLabel.setOnMouseClicked(e -> {
            KeybindEditor.editKeybind(action);
        });

        // Add tooltip with additional info
        updateTooltip(keyLabel, action);

        return keyLabel;
    }

    /**
     * Updates a keybind label with current state.
     */
    private void updateKeybindLabel(Label label, KeybindAction action, KeyCombination combination) {
        KeybindRegistry registry = KeybindRegistry.getInstance();

        if (combination == null) {
            label.setText("Not set");
            label.setStyle(UNSET_KEYBIND_STYLE);
        } else if (registry.hasCustomKeybind(action)) {
            label.setText(combination.getDisplayText());
            label.setStyle(CUSTOM_KEYBIND_STYLE);
        } else {
            label.setText(combination.getDisplayText());
            label.setStyle(DEFAULT_KEYBIND_STYLE);
        }
    }

    /**
     * Updates the tooltip for a keybind label.
     */
    private void updateTooltip(Label label, KeybindAction action) {
        KeybindRegistry registry = KeybindRegistry.getInstance();
        StringBuilder tooltipText = new StringBuilder();
        tooltipText.append("Click to edit\n");
        tooltipText.append("Default: ").append(action.getDefaultKeyCombinationDisplayText());

        if (registry.hasCustomKeybind(action)) {
            tooltipText.append("\n(Currently using custom binding)");
        }

        label.setTooltip(new Tooltip(tooltipText.toString()));
    }

    /**
     * Called when a keybind changes - updates the display.
     */
    @Override
    public void onKeybindChanged(KeybindAction action, KeyCombination oldCombination, KeyCombination newCombination) {
        Label label = keybindLabels.get(action);
        if (label != null) {
            javafx.application.Platform.runLater(() -> {
                updateKeybindLabel(label, action, newCombination);
                updateTooltip(label, action);
            });
        }
    }
}
