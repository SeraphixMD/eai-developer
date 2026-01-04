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

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import be.nabu.eai.developer.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Panel for managing font settings in the developer application.
 * Supports global zoom level and individual font settings per UI component.
 */
public class FontManagerPanel {

	private ObservableList<String> allFonts;
	private boolean updating = false;

	/**
	 * Shows the font manager panel in a new tab.
	 */
	public static void show() {
		Tab tab = MainController.getInstance().newTab("Font Settings");
		FontManagerPanel panel = new FontManagerPanel();
		AnchorPane pane = new AnchorPane();
		VBox content = panel.buildPanel();
		pane.getChildren().add(content);
		AnchorPane.setTopAnchor(content, 0d);
		AnchorPane.setBottomAnchor(content, 0d);
		AnchorPane.setLeftAnchor(content, 0d);
		AnchorPane.setRightAnchor(content, 0d);
		tab.setContent(pane);
	}

	/**
	 * Builds the font settings panel UI.
	 */
	public VBox buildPanel() {
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(15));

		// Load all available fonts
		allFonts = FXCollections.observableArrayList(FontManager.getInstance().getAvailableFonts());

		// Header
		Label titleLabel = new Label("Font Settings");
		titleLabel.getStyleClass().add("h2");

		Label subtitleLabel = new Label("Configure zoom level and individual font settings for each UI component");
		subtitleLabel.setStyle("-fx-text-fill: #666666;");

		// Reset all button
		Button resetAllButton = new Button("Reset All to Defaults");
		resetAllButton.setOnAction(e -> {
			FontManager.getInstance().resetAllToDefaults();
			// Refresh the panel
			Tab tab = MainController.getInstance().getTab("Font Settings");
			if (tab != null) {
				MainController.getInstance().close("Font Settings");
				show();
			}
		});

		HBox headerBox = new HBox(15);
		headerBox.setAlignment(Pos.CENTER_LEFT);
		VBox titleBox = new VBox(5);
		titleBox.getChildren().addAll(titleLabel, subtitleLabel);
		HBox.setHgrow(titleBox, Priority.ALWAYS);
		headerBox.getChildren().addAll(titleBox, resetAllButton);
		headerBox.setPadding(new Insets(0, 0, 10, 0));
		root.getChildren().add(headerBox);

		// Global Settings section
		TitledPane globalPane = createGlobalSettingsSection();
		globalPane.setExpanded(true);
		root.getChildren().add(globalPane);

		// Component sections
		root.getChildren().add(createComponentSection("Editor", "Code editors (scripts, Glue, etc.)",
			() -> FontManager.getInstance().getSettings().getEditorFontFamily(),
			() -> FontManager.getInstance().getSettings().getEditorFontSize(),
			(family, size) -> FontManager.getInstance().setEditorFont(family, size),
			true)); // monospace default

		root.getChildren().add(createComponentSection("Tree", "Repository navigator tree",
			() -> FontManager.getInstance().getSettings().getTreeFontFamily(),
			() -> FontManager.getInstance().getSettings().getTreeFontSize(),
			(family, size) -> FontManager.getInstance().setTreeFont(family, size),
			false));

		root.getChildren().add(createComponentSection("Tabs", "Tab labels",
			() -> FontManager.getInstance().getSettings().getTabFontFamily(),
			() -> FontManager.getInstance().getSettings().getTabFontSize(),
			(family, size) -> FontManager.getInstance().setTabFont(family, size),
			false));

		root.getChildren().add(createComponentSection("Menus", "Menu bar and context menus",
			() -> FontManager.getInstance().getSettings().getMenuFontFamily(),
			() -> FontManager.getInstance().getSettings().getMenuFontSize(),
			(family, size) -> FontManager.getInstance().setMenuFont(family, size),
			false));

		root.getChildren().add(createComponentSection("Buttons", "All buttons",
			() -> FontManager.getInstance().getSettings().getButtonFontFamily(),
			() -> FontManager.getInstance().getSettings().getButtonFontSize(),
			(family, size) -> FontManager.getInstance().setButtonFont(family, size),
			false));

		root.getChildren().add(createComponentSection("Labels", "General labels and text",
			() -> FontManager.getInstance().getSettings().getLabelFontFamily(),
			() -> FontManager.getInstance().getSettings().getLabelFontSize(),
			(family, size) -> FontManager.getInstance().setLabelFont(family, size),
			false));

		root.getChildren().add(createComponentSection("Tables", "Table cells",
			() -> FontManager.getInstance().getSettings().getTableFontFamily(),
			() -> FontManager.getInstance().getSettings().getTableFontSize(),
			(family, size) -> FontManager.getInstance().setTableFont(family, size),
			false));

		root.getChildren().add(createComponentSection("Lists", "List cells",
			() -> FontManager.getInstance().getSettings().getListFontFamily(),
			() -> FontManager.getInstance().getSettings().getListFontSize(),
			(family, size) -> FontManager.getInstance().setListFont(family, size),
			false));

		root.getChildren().add(createComponentSection("Console", "Console and log output",
			() -> FontManager.getInstance().getSettings().getConsoleFontFamily(),
			() -> FontManager.getInstance().getSettings().getConsoleFontSize(),
			(family, size) -> FontManager.getInstance().setConsoleFont(family, size),
			true)); // monospace default

		root.getChildren().add(createComponentSection("Tooltips", "Hover tooltips",
			() -> FontManager.getInstance().getSettings().getTooltipFontFamily(),
			() -> FontManager.getInstance().getSettings().getTooltipFontSize(),
			(family, size) -> FontManager.getInstance().setTooltipFont(family, size),
			false));

		root.getChildren().add(createComponentSection("Headers", "h1, h2, h3 styled elements",
			() -> FontManager.getInstance().getSettings().getHeaderFontFamily(),
			() -> FontManager.getInstance().getSettings().getHeaderFontSize(),
			(family, size) -> FontManager.getInstance().setHeaderFont(family, size),
			false));

		root.getChildren().add(createComponentSection("Input Fields", "Text fields and text areas",
			() -> FontManager.getInstance().getSettings().getInputFontFamily(),
			() -> FontManager.getInstance().getSettings().getInputFontSize(),
			(family, size) -> FontManager.getInstance().setInputFont(family, size),
			false));

		root.getChildren().add(createComponentSection("Dropdowns", "ComboBox and ChoiceBox",
			() -> FontManager.getInstance().getSettings().getDropdownFontFamily(),
			() -> FontManager.getInstance().getSettings().getDropdownFontSize(),
			(family, size) -> FontManager.getInstance().setDropdownFont(family, size),
			false));

		// Wrap in scroll pane
		ScrollPane scrollPane = new ScrollPane(root);
		scrollPane.setFitToWidth(true);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		VBox wrapper = new VBox(scrollPane);
		VBox.setVgrow(scrollPane, Priority.ALWAYS);
		return wrapper;
	}

	/**
	 * Creates the global settings section with zoom level and default font.
	 */
	private TitledPane createGlobalSettingsSection() {
		GridPane grid = new GridPane();
		grid.setHgap(15);
		grid.setVgap(15);
		grid.setPadding(new Insets(15));

		// Zoom level dropdown
		Label zoomLabel = new Label("Zoom Level:");
		ComboBox<String> zoomCombo = new ComboBox<>();
		zoomCombo.getItems().addAll("50%", "75%", "100%", "125%", "150%", "175%", "200%");
		zoomCombo.setValue(FontManager.getInstance().getZoomLevel() + "%");
		zoomCombo.setOnAction(e -> {
			String selected = zoomCombo.getValue();
			if (selected != null && !updating) {
				int zoom = Integer.parseInt(selected.replace("%", ""));
				FontManager.getInstance().setZoomLevel(zoom);
			}
		});

		grid.add(zoomLabel, 0, 0);
		grid.add(zoomCombo, 1, 0);

		// Default font family
		Label defaultFontLabel = new Label("Default Font:");
		ComboBox<String> defaultFontCombo = createFontComboBox();
		defaultFontCombo.setValue(FontManager.getInstance().getDefaultFontFamily());
		defaultFontCombo.setOnAction(e -> {
			String selected = defaultFontCombo.getValue();
			if (selected != null && !selected.isEmpty() && !updating) {
				FontManager.getInstance().setDefaultFontFamily(selected);
			}
		});

		grid.add(defaultFontLabel, 0, 1);
		grid.add(defaultFontCombo, 1, 1);

		// Info label
		Label infoLabel = new Label("Zoom level scales all font sizes. Component sizes are base values before zoom is applied.");
		infoLabel.setStyle("-fx-text-fill: #666666; -fx-font-style: italic;");
		grid.add(infoLabel, 0, 2, 2, 1);

		TitledPane pane = new TitledPane("Global Settings", grid);
		pane.setCollapsible(true);
		return pane;
	}

	/**
	 * Creates a component-specific font settings section.
	 */
	private TitledPane createComponentSection(String name, String description,
			Supplier<String> familyGetter, Supplier<Integer> sizeGetter,
			BiConsumer<String, Integer> setter, boolean monoDefault) {

		GridPane grid = new GridPane();
		grid.setHgap(15);
		grid.setVgap(10);
		grid.setPadding(new Insets(10));

		// Description
		Label descLabel = new Label(description);
		descLabel.setStyle("-fx-text-fill: #666666;");
		grid.add(descLabel, 0, 0, 3, 1);

		// Font family
		Label familyLabel = new Label("Font:");
		ComboBox<String> familyCombo = createFontComboBox();

		// Add "Use Default" option at the start
		ObservableList<String> fontsWithDefault = FXCollections.observableArrayList();
		fontsWithDefault.add("(Use Default)");
		fontsWithDefault.addAll(allFonts);
		familyCombo.setItems(fontsWithDefault);

		String currentFamily = familyGetter.get();
		familyCombo.setValue(currentFamily != null ? currentFamily : "(Use Default)");

		// Font size
		Label sizeLabel = new Label("Size:");
		Spinner<Integer> sizeSpinner = new Spinner<>();
		SpinnerValueFactory.IntegerSpinnerValueFactory factory =
			new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 48, 14);
		sizeSpinner.setValueFactory(factory);
		sizeSpinner.setEditable(true);

		Integer currentSize = sizeGetter.get();
		if (currentSize != null) {
			factory.setValue(currentSize);
		} else {
			factory.setValue(FontManager.DEFAULT_BASE_FONT_SIZE);
		}

		// Reset to default base size
		Button clearSizeButton = new Button("Reset");
		clearSizeButton.setOnAction(e -> {
			if (!updating) {
				String family = familyCombo.getValue();
				if ("(Use Default)".equals(family)) {
					family = null;
				}
				setter.accept(family, null);
				factory.setValue(FontManager.DEFAULT_BASE_FONT_SIZE);
			}
		});

		// Listeners
		familyCombo.setOnAction(e -> {
			if (!updating) {
				String selected = familyCombo.getValue();
				if ("(Use Default)".equals(selected)) {
					selected = null;
				}
				Integer size = sizeGetter.get();
				setter.accept(selected, size);
			}
		});

		sizeSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (!updating && newVal != null) {
				String family = familyCombo.getValue();
				if ("(Use Default)".equals(family)) {
					family = null;
				}
				setter.accept(family, newVal);
			}
		});

		grid.add(familyLabel, 0, 1);
		grid.add(familyCombo, 1, 1);
		grid.add(sizeLabel, 2, 1);
		grid.add(sizeSpinner, 3, 1);
		grid.add(clearSizeButton, 4, 1);

		TitledPane pane = new TitledPane(name, grid);
		pane.setExpanded(false);
		pane.setCollapsible(true);
		return pane;
	}

	/**
	 * Creates a ComboBox with font preview and search/filter capability.
	 */
	private ComboBox<String> createFontComboBox() {
		ComboBox<String> combo = new ComboBox<>(allFonts);
		combo.setEditable(true);

		// Set custom cell factory for font preview
		combo.setCellFactory(listView -> new FontPreviewListCell());

		// Add filtering when user types
		combo.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.isEmpty()) {
				combo.setItems(allFonts);
			} else if (!allFonts.contains(newVal)) {
				String filter = newVal.toLowerCase();
				List<String> filtered = allFonts.stream()
					.filter(font -> font.toLowerCase().contains(filter))
					.collect(Collectors.toList());
				combo.setItems(FXCollections.observableArrayList(filtered));
			}
			if (!combo.isShowing() && combo.isFocused()) {
				combo.show();
			}
		});

		return combo;
	}
}
