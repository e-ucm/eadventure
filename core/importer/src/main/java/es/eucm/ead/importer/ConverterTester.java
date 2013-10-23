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

package es.eucm.ead.importer;

import com.google.inject.Singleton;
import es.eucm.ead.tools.TextFileWriter;

import java.util.ArrayList;

@Singleton
public class ConverterTester {

	private ArrayList<String> instructions;

	public ConverterTester() {
		this.instructions = new ArrayList<String>();
	}

	public void check(String command, String result) {
		check(0, command, result);
	}

	public void command(String command) {
		check(0, command, "");
	}

	public void command(int time, String command) {
		check(time, command, "");
	}

	public void check(int time, String command, String result) {
		instructions.add(time + ";" + command + ";" + result);
	}

	public void write(String file, TextFileWriter writer) {
		String text = "";
		for (String s : instructions) {
			text += s + System.lineSeparator();
		}
		writer.write(text, file);
	}
}
