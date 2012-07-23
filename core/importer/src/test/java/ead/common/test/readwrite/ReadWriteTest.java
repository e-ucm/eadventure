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

package ead.common.test.readwrite;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;

import ead.common.model.elements.EAdAdventureModel;
import ead.reader.java.EAdAdventureDOMModelReader;
import ead.reader.java.extra.ObjectFactory;
import ead.reader.java.visitors.NodeVisitor;
import ead.writer.DOMWriter;
import ead.writer.EAdAdventureModelWriter;

public class ReadWriteTest {
	protected File resultFile;

	protected EAdAdventureModelWriter writer;

	protected EAdAdventureDOMModelReader reader;

	@Before
	public void setUp() {
		try {
			resultFile = File.createTempFile("data", ".xml");
			resultFile.deleteOnExit();
			writer = new EAdAdventureModelWriter();
			reader = new EAdAdventureDOMModelReader();
		} catch (IOException e) {

		}
	}
	
	public EAdAdventureModel writeAndRead(EAdAdventureModel model) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(resultFile);
			assertTrue(writer.write(model, out));
			assertTrue("DOM Writer had errors.", !DOMWriter.getError());
		} catch (FileNotFoundException e) {
			fail("Temp file impossible to be created to write XML.");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

				}
			}
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(resultFile);
			EAdAdventureModel model2 = reader.read(in);
			assertTrue(model2 != null);
			assertTrue("Node visitor had errors.", !NodeVisitor.getError());
			assertTrue("Object Factory had errors.", !ObjectFactory.getError());
			return model2;
		} catch (FileNotFoundException e) {
			fail("Temp file impossible to be created to write XML.");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}
		return null;
	}
}
