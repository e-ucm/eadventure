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

package ead.common.test;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;

import es.eucm.ead.model.params.text.EAdString;
import ead.tools.StringHandler;

public class CommonTestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StringHandler.class).to(TestStringHandler.class);
	}

	public static class TestStringHandler implements StringHandler {

		@Override
		public String getString(EAdString string) {
			return string.toString();
		}

		@Override
		public void setString(EAdString eAdString, String string) {

		}

		@Override
		public void setStrings(Map<EAdString, String> strings) {

		}

		@Override
		public void addStrings(Map<EAdString, String> strings) {

		}

		@Override
		public Map<EAdString, String> getStrings() {
			return new HashMap<EAdString, String>();
		}

		@Override
		public EAdString generateNewString() {
			return null;
		}

		@Override
		public void setLanguage(String language) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addLanguage(String language) {
			// TODO Auto-generated method stub

		}

		@Override
		public void clear() {
			// TODO Auto-generated method stub

		}

	}

}
