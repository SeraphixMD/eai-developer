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

import javax.xml.bind.annotation.XmlType;

/**
 * Font settings for the developer application.
 * Supports a global zoom level plus individual font settings per UI component.
 *
 * When a component's font family is null, it uses the default font family.
 * When a component's font size is null, it uses the zoom-scaled default size.
 */
@XmlType(propOrder = {
	"zoomLevel",
	"defaultFontFamily",
	"editorFontFamily", "editorFontSize",
	"treeFontFamily", "treeFontSize",
	"tabFontFamily", "tabFontSize",
	"menuFontFamily", "menuFontSize",
	"buttonFontFamily", "buttonFontSize",
	"labelFontFamily", "labelFontSize",
	"tableFontFamily", "tableFontSize",
	"listFontFamily", "listFontSize",
	"consoleFontFamily", "consoleFontSize",
	"tooltipFontFamily", "tooltipFontSize",
	"headerFontFamily", "headerFontSize",
	"inputFontFamily", "inputFontSize",
	"dropdownFontFamily", "dropdownFontSize"
})
public class FontSettings {

	// Global zoom level as percentage (100 = 100%, 125 = 125%, etc.)
	private Integer zoomLevel;

	// Default font family for UI (used when component-specific is null)
	private String defaultFontFamily;

	// Editor (AceEditor - code/scripts) - typically monospace
	private String editorFontFamily;
	private Integer editorFontSize;

	// Tree (repository navigator)
	private String treeFontFamily;
	private Integer treeFontSize;

	// Tab labels
	private String tabFontFamily;
	private Integer tabFontSize;

	// Menu bar and context menus
	private String menuFontFamily;
	private Integer menuFontSize;

	// Buttons
	private String buttonFontFamily;
	private Integer buttonFontSize;

	// General labels
	private String labelFontFamily;
	private Integer labelFontSize;

	// Table cells
	private String tableFontFamily;
	private Integer tableFontSize;

	// List cells
	private String listFontFamily;
	private Integer listFontSize;

	// Console/log output
	private String consoleFontFamily;
	private Integer consoleFontSize;

	// Tooltips
	private String tooltipFontFamily;
	private Integer tooltipFontSize;

	// Headers (h1, h2, h3)
	private String headerFontFamily;
	private Integer headerFontSize;

	// Input fields (text fields, text areas)
	private String inputFontFamily;
	private Integer inputFontSize;

	// Dropdowns (ComboBox, ChoiceBox)
	private String dropdownFontFamily;
	private Integer dropdownFontSize;

	// Getters and setters

	public Integer getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(Integer zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public String getDefaultFontFamily() {
		return defaultFontFamily;
	}

	public void setDefaultFontFamily(String defaultFontFamily) {
		this.defaultFontFamily = defaultFontFamily;
	}

	public String getEditorFontFamily() {
		return editorFontFamily;
	}

	public void setEditorFontFamily(String editorFontFamily) {
		this.editorFontFamily = editorFontFamily;
	}

	public Integer getEditorFontSize() {
		return editorFontSize;
	}

	public void setEditorFontSize(Integer editorFontSize) {
		this.editorFontSize = editorFontSize;
	}

	public String getTreeFontFamily() {
		return treeFontFamily;
	}

	public void setTreeFontFamily(String treeFontFamily) {
		this.treeFontFamily = treeFontFamily;
	}

	public Integer getTreeFontSize() {
		return treeFontSize;
	}

	public void setTreeFontSize(Integer treeFontSize) {
		this.treeFontSize = treeFontSize;
	}

	public String getTabFontFamily() {
		return tabFontFamily;
	}

	public void setTabFontFamily(String tabFontFamily) {
		this.tabFontFamily = tabFontFamily;
	}

	public Integer getTabFontSize() {
		return tabFontSize;
	}

	public void setTabFontSize(Integer tabFontSize) {
		this.tabFontSize = tabFontSize;
	}

	public String getMenuFontFamily() {
		return menuFontFamily;
	}

	public void setMenuFontFamily(String menuFontFamily) {
		this.menuFontFamily = menuFontFamily;
	}

	public Integer getMenuFontSize() {
		return menuFontSize;
	}

	public void setMenuFontSize(Integer menuFontSize) {
		this.menuFontSize = menuFontSize;
	}

	public String getButtonFontFamily() {
		return buttonFontFamily;
	}

	public void setButtonFontFamily(String buttonFontFamily) {
		this.buttonFontFamily = buttonFontFamily;
	}

	public Integer getButtonFontSize() {
		return buttonFontSize;
	}

	public void setButtonFontSize(Integer buttonFontSize) {
		this.buttonFontSize = buttonFontSize;
	}

	public String getLabelFontFamily() {
		return labelFontFamily;
	}

	public void setLabelFontFamily(String labelFontFamily) {
		this.labelFontFamily = labelFontFamily;
	}

	public Integer getLabelFontSize() {
		return labelFontSize;
	}

	public void setLabelFontSize(Integer labelFontSize) {
		this.labelFontSize = labelFontSize;
	}

	public String getTableFontFamily() {
		return tableFontFamily;
	}

	public void setTableFontFamily(String tableFontFamily) {
		this.tableFontFamily = tableFontFamily;
	}

	public Integer getTableFontSize() {
		return tableFontSize;
	}

	public void setTableFontSize(Integer tableFontSize) {
		this.tableFontSize = tableFontSize;
	}

	public String getListFontFamily() {
		return listFontFamily;
	}

	public void setListFontFamily(String listFontFamily) {
		this.listFontFamily = listFontFamily;
	}

	public Integer getListFontSize() {
		return listFontSize;
	}

	public void setListFontSize(Integer listFontSize) {
		this.listFontSize = listFontSize;
	}

	public String getConsoleFontFamily() {
		return consoleFontFamily;
	}

	public void setConsoleFontFamily(String consoleFontFamily) {
		this.consoleFontFamily = consoleFontFamily;
	}

	public Integer getConsoleFontSize() {
		return consoleFontSize;
	}

	public void setConsoleFontSize(Integer consoleFontSize) {
		this.consoleFontSize = consoleFontSize;
	}

	public String getTooltipFontFamily() {
		return tooltipFontFamily;
	}

	public void setTooltipFontFamily(String tooltipFontFamily) {
		this.tooltipFontFamily = tooltipFontFamily;
	}

	public Integer getTooltipFontSize() {
		return tooltipFontSize;
	}

	public void setTooltipFontSize(Integer tooltipFontSize) {
		this.tooltipFontSize = tooltipFontSize;
	}

	public String getHeaderFontFamily() {
		return headerFontFamily;
	}

	public void setHeaderFontFamily(String headerFontFamily) {
		this.headerFontFamily = headerFontFamily;
	}

	public Integer getHeaderFontSize() {
		return headerFontSize;
	}

	public void setHeaderFontSize(Integer headerFontSize) {
		this.headerFontSize = headerFontSize;
	}

	public String getInputFontFamily() {
		return inputFontFamily;
	}

	public void setInputFontFamily(String inputFontFamily) {
		this.inputFontFamily = inputFontFamily;
	}

	public Integer getInputFontSize() {
		return inputFontSize;
	}

	public void setInputFontSize(Integer inputFontSize) {
		this.inputFontSize = inputFontSize;
	}

	public String getDropdownFontFamily() {
		return dropdownFontFamily;
	}

	public void setDropdownFontFamily(String dropdownFontFamily) {
		this.dropdownFontFamily = dropdownFontFamily;
	}

	public Integer getDropdownFontSize() {
		return dropdownFontSize;
	}

	public void setDropdownFontSize(Integer dropdownFontSize) {
		this.dropdownFontSize = dropdownFontSize;
	}
}
