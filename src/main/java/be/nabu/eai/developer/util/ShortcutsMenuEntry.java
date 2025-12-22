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

import be.nabu.eai.developer.api.MainMenuEntry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Adds a "Keyboard Shortcuts" menu item to the Help menu.
 */
public class ShortcutsMenuEntry implements MainMenuEntry {

    @Override
    public void populate(MenuBar menuBar) {
        Menu helpMenu = findOrCreateHelpMenu(menuBar);

        MenuItem shortcutsItem = new MenuItem("Keyboard Shortcuts");
        shortcutsItem.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ShortcutsPanel.show();
            }
        });

        helpMenu.getItems().add(shortcutsItem);
    }

    /**
     * Finds or creates the Help menu in the menu bar.
     */
    private Menu findOrCreateHelpMenu(MenuBar menuBar) {
        for (Menu menu : menuBar.getMenus()) {
            if ("Help".equals(menu.getText())) {
                return menu;
            }
        }
        // Create if not found
        Menu helpMenu = new Menu("Help");
        menuBar.getMenus().add(helpMenu);
        return helpMenu;
    }
}
