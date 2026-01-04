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

import javafx.scene.control.ListCell;
import javafx.scene.text.Font;

/**
 * Custom ListCell that renders each font name in its own typeface,
 * providing a preview of what the font looks like.
 */
public class FontPreviewListCell extends ListCell<String> {

	private final double previewSize;

	public FontPreviewListCell() {
		this(14);
	}

	public FontPreviewListCell(double previewSize) {
		this.previewSize = previewSize;
	}

	@Override
	protected void updateItem(String fontName, boolean empty) {
		super.updateItem(fontName, empty);
		if (empty || fontName == null) {
			setText(null);
			setFont(Font.getDefault());
		} else {
			setText(fontName);
			setFont(Font.font(fontName, previewSize));
		}
	}
}
