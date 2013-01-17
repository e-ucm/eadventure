/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.editor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import com.google.inject.Singleton;

import ead.common.params.text.EAdString;
import ead.tools.StringHandler;

/**
 * A simple StringHandler, capable of managing language resolution.
 */
@Singleton
public class EditorStringHandler implements StringHandler {

	/**
	 * Map with all strings.
	 * The first key is the language ("" stands for "default language").
	 */
	private TreeMap<String, Map<EAdString, String>> strings;

	/**
	 * Set with all keys (kept synchronized with strings);
	 */
	private HashSet<EAdString> usedKeys = new HashSet<EAdString>();

	/**
	 * Default language. Character-code of the default language.
	 * "" for none-specified, "es" for Spanish, "es_ES" for
	 * Spanish-from-Spain, and so on. This language will be used
	 * for all non-language-specific searches. Note that this is different
	 * from the "fallback" language of "" (empty-string).
	 */
	private String defaultLanguage;

	public EditorStringHandler() {
		strings = new TreeMap<String, Map<EAdString, String>>();
		defaultLanguage = "";

		// add root language
		strings.put("", new HashMap<EAdString, String>());
	}

	public String getString(EAdString string, String lang) {
		// either this language or any other parent language will do
		while (!lang.isEmpty() && !strings.containsKey(lang)) {
			int pos = Math.max(lang.lastIndexOf('_') - 1, 0);
			lang = lang.substring(0, pos);
		}
		String value = strings.get(lang).get(string);
		return value == null ? "" : value;
	}

	public void setString(EAdString eAdString, String string, String lang) {
		while (true) {
			// if language does not exist, create it
			if (!strings.containsKey(lang)) {
				strings.put(lang, new HashMap<EAdString, String>());
			}
			strings.get(lang).put(eAdString, string);

			if (!lang.isEmpty()) {
				int pos = Math.max(lang.lastIndexOf('_') - 1, 0);
				lang = lang.substring(0, pos);
			} else {
				break;
			}
		}
	}

	public void setStrings(Map<EAdString, String> stringsToSet, String lang) {
		strings.clear();
		addStrings(stringsToSet, lang);
	}

	public void addStrings(Map<EAdString, String> stringsToSet, String lang) {
		for (Map.Entry<EAdString, String> e : stringsToSet.entrySet()) {
			usedKeys.add(e.getKey());
			setString(e.getKey(), e.getValue(), lang);
		}
	}

	public Map<EAdString, String> getStrings(String lang) {
		return strings.get(lang);
	}

	@Override
	public Map<EAdString, String> getStrings() {
		return getStrings(defaultLanguage);
	}

	@Override
	public String getString(EAdString string) {
		return getString(string, defaultLanguage);
	}

	@Override
	public void setString(EAdString eAdString, String string) {
		setString(eAdString, string, defaultLanguage);
	}

	@Override
	public void setStrings(Map<EAdString, String> strings) {
		setStrings(strings, defaultLanguage);
	}

	@Override
	public void addStrings(Map<EAdString, String> strings) {
		addStrings(strings, defaultLanguage);
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	@Override
	public EAdString generateNewString() {
		EAdString key = EAdString.newRandomEAdString("generatedString");
		while (usedKeys.contains(key)) {
			key = EAdString.newRandomEAdString("generatedString");
		}
		return key;
	}

	@Override
	public void addLanguage(String language) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLanguage(String language) {
		// TODO Auto-generated method stub
		
	}
}
