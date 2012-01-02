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
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Singleton;

import ead.common.params.text.EAdString;
import ead.common.resources.StringHandler;

/**
 * TODO EditorStringHandler should have support for translation and other i18n tasks
 * 
 */
@Singleton
public class EditorStringHandler implements StringHandler {

	private Map<EAdString, String> strings;

	public EditorStringHandler() {
		strings = new HashMap<EAdString, String>();
	}

	@Override
	public String getString(EAdString string) {
		String value = strings.get(string);
		return value == null ? "" : value;
	}

	@Override
	public void setString(EAdString eAdString, String string) {
		strings.put(eAdString, string);
	}

	@Override
	public void setStrings(Map<EAdString, String> strings) {
		this.strings = strings;

	}

	@Override
	public void addStrings(Map<EAdString, String> strings) {
		for (Entry<EAdString, String> entry : strings.entrySet()) {
			this.strings.put(entry.getKey(), entry.getValue());
		}

	}

	@Override
	public Map<EAdString, String> getStrings() {
		return strings;
	}

}
