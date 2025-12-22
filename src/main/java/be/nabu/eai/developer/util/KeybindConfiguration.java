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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import be.nabu.eai.developer.api.KeybindAction;
import javafx.scene.input.KeyCombination;

/**
 * Handles persistence of custom keybinds to ~/.nabu/keybinds.xml
 */
public class KeybindConfiguration {

    private static final String CONFIG_DIR = ".nabu";
    private static final String CONFIG_FILE = "keybinds.xml";
    private static final String ROOT_ELEMENT = "keybinds";
    private static final String KEYBIND_ELEMENT = "keybind";
    private static final String ACTION_ATTR = "action";
    private static final String COMBINATION_ATTR = "combination";
    private static final String NO_KEYBIND_VALUE = "NONE";

    /**
     * Marker for "no keybind" - action is explicitly unbound.
     */
    public static final KeyCombination NO_KEYBIND_MARKER = null;

    /**
     * Gets the configuration file location.
     */
    public File getConfigFile() {
        String userHome = System.getProperty("user.home");
        File configDir = new File(userHome, CONFIG_DIR);
        return new File(configDir, CONFIG_FILE);
    }

    /**
     * Loads custom keybinds from the configuration file.
     * Returns null if no configuration exists or on error.
     */
    public Map<KeybindAction, KeyCombination> load() {
        File configFile = getConfigFile();
        if (!configFile.exists()) {
            return null;
        }

        Map<KeybindAction, KeyCombination> keybinds = new EnumMap<>(KeybindAction.class);

        try (InputStream is = new FileInputStream(configFile)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);

            NodeList keybindNodes = doc.getElementsByTagName(KEYBIND_ELEMENT);
            for (int i = 0; i < keybindNodes.getLength(); i++) {
                Element element = (Element) keybindNodes.item(i);
                String actionName = element.getAttribute(ACTION_ATTR);
                String combinationStr = element.getAttribute(COMBINATION_ATTR);

                try {
                    KeybindAction action = KeybindAction.valueOf(actionName);

                    if (NO_KEYBIND_VALUE.equals(combinationStr)) {
                        // Action is explicitly unbound
                        keybinds.put(action, NO_KEYBIND_MARKER);
                    } else if (combinationStr != null && !combinationStr.isEmpty()) {
                        KeyCombination combination = KeyCombination.valueOf(combinationStr);
                        keybinds.put(action, combination);
                    }
                } catch (IllegalArgumentException e) {
                    // Unknown action or invalid combination - skip it
                    System.err.println("Skipping unknown keybind: " + actionName + " -> " + combinationStr);
                }
            }

            return keybinds;
        } catch (Exception e) {
            System.err.println("Error loading keybind configuration: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves custom keybinds to the configuration file.
     * Only stores non-default bindings.
     */
    public void save(Map<KeybindAction, KeyCombination> keybinds) {
        if (keybinds == null || keybinds.isEmpty()) {
            // Delete config file if no custom keybinds
            File configFile = getConfigFile();
            if (configFile.exists()) {
                configFile.delete();
            }
            return;
        }

        try {
            // Ensure config directory exists
            File configFile = getConfigFile();
            configFile.getParentFile().mkdirs();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement(ROOT_ELEMENT);
            doc.appendChild(root);

            for (Map.Entry<KeybindAction, KeyCombination> entry : keybinds.entrySet()) {
                KeybindAction action = entry.getKey();
                KeyCombination combination = entry.getValue();

                Element keybindElement = doc.createElement(KEYBIND_ELEMENT);
                keybindElement.setAttribute(ACTION_ATTR, action.name());

                if (combination == null) {
                    // Explicitly unbound
                    keybindElement.setAttribute(COMBINATION_ATTR, NO_KEYBIND_VALUE);
                } else {
                    keybindElement.setAttribute(COMBINATION_ATTR, combination.getName());
                }

                root.appendChild(keybindElement);
            }

            // Write to file with pretty formatting
            try (OutputStream os = new FileOutputStream(configFile)) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(os);
                transformer.transform(source, result);
            }

        } catch (Exception e) {
            System.err.println("Error saving keybind configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks if a custom keybind configuration exists.
     */
    public boolean configExists() {
        return getConfigFile().exists();
    }

    /**
     * Deletes the custom keybind configuration.
     */
    public boolean deleteConfig() {
        File configFile = getConfigFile();
        if (configFile.exists()) {
            return configFile.delete();
        }
        return true;
    }
}
