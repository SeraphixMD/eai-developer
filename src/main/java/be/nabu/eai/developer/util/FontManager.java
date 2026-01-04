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

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.nabu.eai.developer.Main.Developer;
import be.nabu.eai.developer.MainController;
import be.nabu.jfx.control.ace.AceEditor;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.text.Font;

/**
 * Singleton manager for font settings in the developer application.
 * Supports a global zoom level plus individual font settings per UI component.
 */
public class FontManager {

	private static FontManager instance;

	// Default values
	public static final int DEFAULT_ZOOM_LEVEL = 100;
	public static final int DEFAULT_BASE_FONT_SIZE = 14;
	public static final String DEFAULT_UI_FONT_FAMILY = "Lato";
	public static final String DEFAULT_EDITOR_FONT_FAMILY = "monospace";

	// Track registered AceEditor instances with weak references
	private final List<WeakReference<AceEditor>> registeredEditors = new ArrayList<>();

	private FontManager() {
	}

	public static FontManager getInstance() {
		if (instance == null) {
			synchronized (FontManager.class) {
				if (instance == null) {
					instance = new FontManager();
				}
			}
		}
		return instance;
	}

	/**
	 * Returns all available font families on the system.
	 */
	public List<String> getAvailableFonts() {
		return Font.getFamilies();
	}

	/**
	 * Gets the font settings, creating if null.
	 */
	public FontSettings getSettings() {
		Developer config = MainController.getDeveloperConfiguration();
		if (config.getFontSettings() == null) {
			config.setFontSettings(new FontSettings());
		}
		return config.getFontSettings();
	}

	/**
	 * Returns the zoom level (percentage, e.g., 100 = 100%).
	 */
	public int getZoomLevel() {
		Integer zoom = getSettings().getZoomLevel();
		return zoom != null ? zoom : DEFAULT_ZOOM_LEVEL;
	}

	/**
	 * Sets the zoom level and applies changes.
	 */
	public void setZoomLevel(int zoomLevel) {
		getSettings().setZoomLevel(zoomLevel);
		applyAllSettings();
		saveSettings();
	}

	/**
	 * Returns the default font family.
	 */
	public String getDefaultFontFamily() {
		String family = getSettings().getDefaultFontFamily();
		return family != null ? family : DEFAULT_UI_FONT_FAMILY;
	}

	/**
	 * Sets the default font family and applies changes.
	 */
	public void setDefaultFontFamily(String family) {
		getSettings().setDefaultFontFamily(family);
		applyAllSettings();
		saveSettings();
	}

	/**
	 * Calculates the effective font size for a component.
	 * The zoom level is always applied as a multiplier.
	 * If the component has a custom base size, use it; otherwise use DEFAULT_BASE_FONT_SIZE.
	 */
	public int getEffectiveSize(Integer componentSize) {
		int baseSize = componentSize != null ? componentSize : DEFAULT_BASE_FONT_SIZE;
		return (int) Math.round(baseSize * getZoomLevel() / 100.0);
	}

	/**
	 * Returns the base font size scaled by zoom level.
	 */
	public int getScaledBaseSize() {
		return (int) Math.round(DEFAULT_BASE_FONT_SIZE * getZoomLevel() / 100.0);
	}

	/**
	 * Returns the effective font family for a component.
	 * If null, uses the default font family.
	 */
	public String getEffectiveFamily(String componentFamily) {
		return componentFamily != null ? componentFamily : getDefaultFontFamily();
	}

	// Component-specific getters

	public String getEditorFontFamily() {
		String family = getSettings().getEditorFontFamily();
		return family != null ? family : DEFAULT_EDITOR_FONT_FAMILY;
	}

	public int getEditorFontSize() {
		return getEffectiveSize(getSettings().getEditorFontSize());
	}

	public String getTreeFontFamily() {
		return getEffectiveFamily(getSettings().getTreeFontFamily());
	}

	public int getTreeFontSize() {
		return getEffectiveSize(getSettings().getTreeFontSize());
	}

	public String getTabFontFamily() {
		return getEffectiveFamily(getSettings().getTabFontFamily());
	}

	public int getTabFontSize() {
		return getEffectiveSize(getSettings().getTabFontSize());
	}

	public String getMenuFontFamily() {
		return getEffectiveFamily(getSettings().getMenuFontFamily());
	}

	public int getMenuFontSize() {
		return getEffectiveSize(getSettings().getMenuFontSize());
	}

	public String getButtonFontFamily() {
		return getEffectiveFamily(getSettings().getButtonFontFamily());
	}

	public int getButtonFontSize() {
		return getEffectiveSize(getSettings().getButtonFontSize());
	}

	public String getLabelFontFamily() {
		return getEffectiveFamily(getSettings().getLabelFontFamily());
	}

	public int getLabelFontSize() {
		return getEffectiveSize(getSettings().getLabelFontSize());
	}

	public String getTableFontFamily() {
		return getEffectiveFamily(getSettings().getTableFontFamily());
	}

	public int getTableFontSize() {
		return getEffectiveSize(getSettings().getTableFontSize());
	}

	public String getListFontFamily() {
		return getEffectiveFamily(getSettings().getListFontFamily());
	}

	public int getListFontSize() {
		return getEffectiveSize(getSettings().getListFontSize());
	}

	public String getConsoleFontFamily() {
		String family = getSettings().getConsoleFontFamily();
		return family != null ? family : DEFAULT_EDITOR_FONT_FAMILY;
	}

	public int getConsoleFontSize() {
		return getEffectiveSize(getSettings().getConsoleFontSize());
	}

	public String getTooltipFontFamily() {
		return getEffectiveFamily(getSettings().getTooltipFontFamily());
	}

	public int getTooltipFontSize() {
		Integer size = getSettings().getTooltipFontSize();
		// Tooltips are slightly smaller by default (85% of base)
		int baseSize = size != null ? size : (int) Math.round(DEFAULT_BASE_FONT_SIZE * 0.85);
		return (int) Math.round(baseSize * getZoomLevel() / 100.0);
	}

	public String getHeaderFontFamily() {
		return getEffectiveFamily(getSettings().getHeaderFontFamily());
	}

	public int getHeaderFontSize() {
		return getEffectiveSize(getSettings().getHeaderFontSize());
	}

	public String getInputFontFamily() {
		return getEffectiveFamily(getSettings().getInputFontFamily());
	}

	public int getInputFontSize() {
		return getEffectiveSize(getSettings().getInputFontSize());
	}

	public String getDropdownFontFamily() {
		return getEffectiveFamily(getSettings().getDropdownFontFamily());
	}

	public int getDropdownFontSize() {
		return getEffectiveSize(getSettings().getDropdownFontSize());
	}

	// Component-specific setters (apply changes and save)

	public void setEditorFont(String family, Integer size) {
		getSettings().setEditorFontFamily(family);
		getSettings().setEditorFontSize(size);
		updateAllEditors();
		saveSettings();
	}

	public void setTreeFont(String family, Integer size) {
		getSettings().setTreeFontFamily(family);
		getSettings().setTreeFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setTabFont(String family, Integer size) {
		getSettings().setTabFontFamily(family);
		getSettings().setTabFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setMenuFont(String family, Integer size) {
		getSettings().setMenuFontFamily(family);
		getSettings().setMenuFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setButtonFont(String family, Integer size) {
		getSettings().setButtonFontFamily(family);
		getSettings().setButtonFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setLabelFont(String family, Integer size) {
		getSettings().setLabelFontFamily(family);
		getSettings().setLabelFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setTableFont(String family, Integer size) {
		getSettings().setTableFontFamily(family);
		getSettings().setTableFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setListFont(String family, Integer size) {
		getSettings().setListFontFamily(family);
		getSettings().setListFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setConsoleFont(String family, Integer size) {
		getSettings().setConsoleFontFamily(family);
		getSettings().setConsoleFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setTooltipFont(String family, Integer size) {
		getSettings().setTooltipFontFamily(family);
		getSettings().setTooltipFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setHeaderFont(String family, Integer size) {
		getSettings().setHeaderFontFamily(family);
		getSettings().setHeaderFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setInputFont(String family, Integer size) {
		getSettings().setInputFontFamily(family);
		getSettings().setInputFontSize(size);
		applyUICss();
		saveSettings();
	}

	public void setDropdownFont(String family, Integer size) {
		getSettings().setDropdownFontFamily(family);
		getSettings().setDropdownFontSize(size);
		applyUICss();
		saveSettings();
	}

	/**
	 * Registers an AceEditor instance to receive font updates.
	 * Uses weak references to avoid memory leaks.
	 */
	public void registerEditor(AceEditor editor) {
		cleanupEditors();
		registeredEditors.add(new WeakReference<>(editor));
		applyFontToEditor(editor);
	}

	/**
	 * Applies the current font settings to a specific editor.
	 */
	private void applyFontToEditor(AceEditor editor) {
		if (editor != null) {
			editor.setFontFamily(getEditorFontFamily());
			editor.setFontSize(getEditorFontSize());
		}
	}

	/**
	 * Updates all registered editors with current font settings.
	 */
	private void updateAllEditors() {
		cleanupEditors();
		for (WeakReference<AceEditor> ref : registeredEditors) {
			AceEditor editor = ref.get();
			if (editor != null) {
				applyFontToEditor(editor);
			}
		}
	}

	/**
	 * Removes dead weak references from the list.
	 */
	private void cleanupEditors() {
		Iterator<WeakReference<AceEditor>> it = registeredEditors.iterator();
		while (it.hasNext()) {
			if (it.next().get() == null) {
				it.remove();
			}
		}
	}

	/**
	 * Applies all settings (CSS + editors).
	 */
	private void applyAllSettings() {
		applyUICss();
		updateAllEditors();
	}

	/**
	 * Generates and applies dynamic CSS for UI fonts.
	 */
	private void applyUICss() {
		Platform.runLater(() -> {
			try {
				Scene scene = MainController.getInstance().getStage().getScene();
				if (scene != null) {
					String css = generateDynamicCss();
					String encoded = URLEncoder.encode(css, "UTF-8").replace("+", "%20");
					String dataUri = "data:text/css," + encoded;

					// Remove any existing dynamic font stylesheet
					scene.getStylesheets().removeIf(s -> s.startsWith("data:text/css,") && s.contains("fx-font"));

					// Add new stylesheet at the end (highest priority)
					scene.getStylesheets().add(dataUri);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Generates CSS string for all font settings.
	 */
	private String generateDynamicCss() {
		StringBuilder css = new StringBuilder();
		int baseSize = getScaledBaseSize();
		double zoomFactor = getZoomLevel() / 100.0;

		// Scaled dimensions for various controls
		int basePadding = (int) Math.round(5 * zoomFactor);
		int buttonPadding = (int) Math.round(4 * zoomFactor);
		int inputMinWidth = (int) Math.round(60 * zoomFactor);
		int comboMinWidth = (int) Math.round(100 * zoomFactor);
		int spinnerMinWidth = (int) Math.round(70 * zoomFactor);

		// Root font settings
		css.append(String.format(".root { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getDefaultFontFamily(), baseSize));

		// Headers (scale based on header size or proportionally)
		int headerSize = getHeaderFontSize();
		int h1Size = (int) Math.round(headerSize * 1.5);
		int h2Size = (int) Math.round(headerSize * 1.15);
		int h3Size = headerSize;
		css.append(String.format(".h1 { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getHeaderFontFamily(), h1Size));
		css.append(String.format(".h2 { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getHeaderFontFamily(), h2Size));
		css.append(String.format(".h3 { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getHeaderFontFamily(), h3Size));

		// Tree cells
		css.append(String.format(".tree-cell { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getTreeFontFamily(), getTreeFontSize()));

		// Tab labels - need all position variants to override theme.css specificity
		int tabSize = getTabFontSize();
		String tabFamily = getTabFontFamily();
		css.append(String.format(".tab-pane:top .tab .tab-label { -fx-font-family: '%s'; -fx-font-size: %dpx; } ", tabFamily, tabSize));
		css.append(String.format(".tab-pane:bottom .tab .tab-label { -fx-font-family: '%s'; -fx-font-size: %dpx; } ", tabFamily, tabSize));
		css.append(String.format(".tab-pane:left .tab .tab-label { -fx-font-family: '%s'; -fx-font-size: %dpx; } ", tabFamily, tabSize));
		css.append(String.format(".tab-pane:right .tab .tab-label { -fx-font-family: '%s'; -fx-font-size: %dpx; } ", tabFamily, tabSize));

		// Menu bar and context menus
		css.append(String.format(".menu-bar .menu .label { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getMenuFontFamily(), getMenuFontSize()));
		css.append(String.format(".context-menu .menu-item .label { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getMenuFontFamily(), getMenuFontSize()));

		// Buttons with scaled padding
		css.append(String.format(".button { -fx-font-family: '%s'; -fx-font-size: %dpx; -fx-padding: %dpx %dpx; } ",
			getButtonFontFamily(), getButtonFontSize(), buttonPadding, buttonPadding * 2));
		int smallButtonSize = (int) Math.round(getButtonFontSize() * 0.7);
		css.append(String.format(".button.small, .small .button { -fx-font-size: %dpx; } ", smallButtonSize));

		// Labels
		css.append(String.format(".label { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getLabelFontFamily(), getLabelFontSize()));

		// Table cells
		css.append(String.format(".table-cell { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getTableFontFamily(), getTableFontSize()));

		// List cells
		css.append(String.format(".list-cell { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getListFontFamily(), getListFontSize()));

		// Tooltips
		css.append(String.format(".tooltip { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getTooltipFontFamily(), getTooltipFontSize()));

		// Input fields with scaled padding and min-width
		css.append(String.format(".text-field, .text-area { -fx-font-family: '%s'; -fx-font-size: %dpx; -fx-padding: %dpx; -fx-min-width: %dpx; } ",
			getInputFontFamily(), getInputFontSize(), basePadding, inputMinWidth));

		// Dropdowns with scaled dimensions
		css.append(String.format(".combo-box, .choice-box { -fx-font-family: '%s'; -fx-font-size: %dpx; -fx-min-width: %dpx; } ",
			getDropdownFontFamily(), getDropdownFontSize(), comboMinWidth));
		css.append(String.format(".combo-box .list-cell { -fx-padding: %dpx; } ", basePadding));

		// Check boxes and radio buttons (use label font)
		css.append(String.format(".check-box, .radio-button { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getLabelFontFamily(), getLabelFontSize()));

		// Titled pane
		css.append(String.format(".titled-pane > .title > .text { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getLabelFontFamily(), getLabelFontSize()));

		// Spinner with scaled dimensions
		css.append(String.format(".spinner { -fx-font-family: '%s'; -fx-font-size: %dpx; -fx-min-width: %dpx; } ",
			getInputFontFamily(), getInputFontSize(), spinnerMinWidth));
		css.append(String.format(".spinner .text-field { -fx-padding: %dpx; } ", basePadding));

		// Entry list items
		css.append(String.format(".entry-list-item { -fx-font-family: '%s'; -fx-font-size: %dpx; } ",
			getListFontFamily(), getListFontSize()));

		// Small elements
		int tinySize = (int) Math.round(baseSize * 0.5);
		css.append(String.format(".serviceInput, .small-button, .result-table { -fx-font-size: %dpx; } ", tinySize));

		return css.toString();
	}

	/**
	 * Applies the currently saved font settings.
	 * Called on application startup.
	 */
	public void applyCurrentSettings() {
		applyAllSettings();
	}

	/**
	 * Saves the current font settings to the configuration file.
	 */
	public void saveSettings() {
		MainController.saveConfiguration();
	}

	/**
	 * Resets all font settings to defaults.
	 */
	public void resetAllToDefaults() {
		FontSettings settings = getSettings();
		settings.setZoomLevel(null);
		settings.setDefaultFontFamily(null);
		settings.setEditorFontFamily(null);
		settings.setEditorFontSize(null);
		settings.setTreeFontFamily(null);
		settings.setTreeFontSize(null);
		settings.setTabFontFamily(null);
		settings.setTabFontSize(null);
		settings.setMenuFontFamily(null);
		settings.setMenuFontSize(null);
		settings.setButtonFontFamily(null);
		settings.setButtonFontSize(null);
		settings.setLabelFontFamily(null);
		settings.setLabelFontSize(null);
		settings.setTableFontFamily(null);
		settings.setTableFontSize(null);
		settings.setListFontFamily(null);
		settings.setListFontSize(null);
		settings.setConsoleFontFamily(null);
		settings.setConsoleFontSize(null);
		settings.setTooltipFontFamily(null);
		settings.setTooltipFontSize(null);
		settings.setHeaderFontFamily(null);
		settings.setHeaderFontSize(null);
		settings.setInputFontFamily(null);
		settings.setInputFontSize(null);
		settings.setDropdownFontFamily(null);
		settings.setDropdownFontSize(null);
		applyAllSettings();
		saveSettings();
	}
}
