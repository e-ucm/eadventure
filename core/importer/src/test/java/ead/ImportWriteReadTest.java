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

package ead;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.EAdAdventureModel;
import ead.importer.EAdventureImporter;
import ead.importer.ImporterModule;
import ead.reader.AdventureReader;
import ead.tools.java.DataPrettifier;
import ead.tools.java.JavaToolsModule;
import ead.tools.java.reflection.JavaReflectionClassLoader;
import ead.tools.java.reflection.JavaReflectionProvider;
import ead.tools.java.xml.JavaXMLParser;
import ead.tools.reflection.ReflectionClassLoader;
import ead.utils.FileUtils;
import ead.utils.Log4jConfig;
import ead.writer.AdventureWriter;

public class ImportWriteReadTest {

	private static final Logger logger = LoggerFactory
			.getLogger("ImportWriteReadTest");

	private EAdventureImporter importer;
	private AdventureReader reader;
	private AdventureWriter writer;

	public ImportWriteReadTest() {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Info, null);
	}

	@Before
	public void setUp() {
		Injector i = Guice.createInjector(new ImporterModule(),
				new JavaToolsModule());

		importer = i.getInstance(EAdventureImporter.class);
		reader = new AdventureReader(new JavaXMLParser(),
				new JavaReflectionProvider());
		writer = new AdventureWriter(new JavaReflectionProvider(),
				new JavaXMLParser());
		ReflectionClassLoader.init(new JavaReflectionClassLoader());
	}

	public void timport() throws Exception {

		File testFile = new File(ClassLoader.getSystemResource(
				"ead/importer/test/test.ead").getPath());

		assertTrue(testFile.exists());
		boolean errors = false;
		File tmpDir = FileUtils.createTempDir("test", "import");
		try {

			EAdAdventureModel model = importer.importGame(testFile
					.getAbsolutePath(), tmpDir.getAbsolutePath(), "none");
			File modelAfterImport = new File(tmpDir, "data.xml");

			File modelFile = new File(tmpDir, "modelFile.xml");
			writer.write(model, modelFile.getAbsolutePath());
			File modelFile2 = new File(tmpDir, "modelFile2.xml");
			writer.write(model, modelFile2.getAbsolutePath());

			if (!FileUtils.isFileBinaryEqual(modelAfterImport, modelFile)) {
				errors = true;
				logger.error("after-import != model-file");
			}
			if (!FileUtils.isFileBinaryEqual(modelFile, modelFile2)) {
				errors = true;
				logger.error("model-file != model-file2");
				logger.error("kdiff3 " + tmpDir + "/modelFile.xml " + tmpDir
						+ "/modelFile2.xml");
			}

			model = reader.readXML(FileUtils.loadFileToString(modelFile));
			File modelFileAfterRead = new File(tmpDir, "afterRead.xml");
			writer.write(model, modelFileAfterRead.getAbsolutePath());
			if (!FileUtils.isFileBinaryEqual(modelFile, modelFileAfterRead)) {
				errors = true;
				logger.error("model-file != model-file-after-read");
				logger.error("kdiff3 " + tmpDir + "/pretty-modelFile.xml "
						+ tmpDir + "/pretty-afterRead.xml");
			}

			File modelFileAfterRead2 = new File(tmpDir, "afterRead-2.xml");
			model = reader.readXML(FileUtils.loadFileToString(modelFile));
			writer.write(model, modelFileAfterRead2.getAbsolutePath());
			if (!FileUtils.isFileBinaryEqual(modelFileAfterRead2,
					modelFileAfterRead)) {
				errors = true;
				logger
						.error("model-file-after-read != model-file-after-read-2");
				logger.error("kdiff3 " + tmpDir + "/pretty-afterRead-2.xml "
						+ tmpDir + "/pretty-afterRead.xml");
			}

		} finally {
			if (errors) {

				for (File f : tmpDir.listFiles(new EndsInXmlFilter())) {
					File dest = new File(f.getParentFile(), "pretty-"
							+ f.getName());
					DataPrettifier.prettify(f, dest);
				}
				fail("see " + tmpDir + " for details");
			} else {
				FileUtils.deleteRecursive(tmpDir);
			}
		}
	}

	private static class EndsInXmlFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".xml");
		}
	}
}
