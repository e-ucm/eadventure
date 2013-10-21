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

package es.eucm.ead.tests.writer;

import es.eucm.ead.model.elements.AdventureGame;
import es.eucm.ead.model.elements.Chapter;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.reader.AdventureReader;
import es.eucm.ead.tools.java.JavaTextFileWriter;
import es.eucm.ead.tools.java.reflection.JavaReflectionProvider;
import es.eucm.ead.tools.java.xml.SaxXMLParser;
import es.eucm.ead.writer.AdventureWriter;
import org.junit.Test;

public class ReaderWriterTest {

	@Test
	public void testReadWrite() {
		String path = "src/test/resources/tests";
		AdventureGame model = new AdventureGame();
		Chapter chapter = new Chapter();
		Chapter chapter2 = new Chapter();
		model.getChapters().add(chapter);
		model.getChapters().add(chapter2);
		Scene scene1 = new Scene();
		scene1.addSceneElement(new SceneElement());
		Scene scene2 = new Scene();
		scene2.addSceneElement(new SceneElement());
		chapter.addScene(scene1);
		chapter.addScene(scene2);
		model.setInitialChapter(chapter2);
		AdventureWriter writer = new AdventureWriter(
				new JavaReflectionProvider());
		writer.write(model, path, new JavaTextFileWriter());
		AdventureReader reader = new AdventureReader(
				new JavaReflectionProvider(), new SaxXMLParser(),
				new TestTextFileReader());
	}
}
