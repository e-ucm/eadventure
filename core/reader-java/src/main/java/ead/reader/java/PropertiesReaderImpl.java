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

package ead.reader.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ead.common.model.elements.EAdAdventureModel;
import ead.reader.PropertiesReader;

public class PropertiesReaderImpl implements PropertiesReader {

	@Override
	public void setProperties(EAdAdventureModel model, String eadPropertiesFile) {
		FileReader reader = null;
		BufferedReader br = null;

		try {
			reader = new FileReader(eadPropertiesFile);

			br = new BufferedReader(reader);
			String line = null;

			while ((line = br.readLine()) != null) {
				String[] strings = line.split("=");
				if (strings.length == 2) {
					String key = strings[0];
					String value = strings[1];
					model.setProperty(key, value);
				}
			}
		} catch (IOException e) {

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
