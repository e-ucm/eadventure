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

package ead.common.test.writer.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdAdventureModelImpl;
import ead.common.model.elements.EAdChapterImpl;
import ead.common.reader.EAdAdventureDOMModelReader;
import ead.common.writer.EAdAdventureModelWriter;

public class EAdWriterTest extends TestCase {

	private File file;
	private EAdAdventureModelImpl model;
	private EAdAdventureModelWriter writer;
	private EAdAdventureDOMModelReader reader;
	private File file2;

	@Before
	public void setUp() {
		Injector injector = Guice.createInjector(new ConfigurationModule());
		reader = injector.getInstance(EAdAdventureDOMModelReader.class);

		file = new File("src/test/resources/result.xml");
		file2 = new File("src/test/resources/result2.xml");
		model = new EAdAdventureModelImpl();
		EAdChapterImpl chapter = new EAdChapterImpl();
		chapter.setId("chapter1");

		model.getChapters().add(chapter);
		model.getChapters().add(chapter);
		
//		chapter.getScenes().add(new SpeakAndMoveScene());

		writer = new EAdAdventureModelWriter();

	}

	public class ConfigurationModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(EAdAdventureModel.class).to(EAdAdventureModelImpl.class);
		}
	}

	@Test
	public void testWriteOS() {
		FileOutputStream os;
		try {
			os = new FileOutputStream(file);
			writer.write(model, os);

			os.close();

			InputStream is = new FileInputStream(file);
			EAdAdventureModel modelRead = reader.read(is);

			is.close();

			os = new FileOutputStream(file2);
			writer.write(modelRead, os);

			os.close();

			// assertNotNull(modelRead);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

	}

}
