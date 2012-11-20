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

package ead.importer.auxiliar.inputstreamcreators;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.loader.InputStreamCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ImporterInputStreamCreator implements InputStreamCreator {

	private InputStreamCreator currentStreamCreator;
	private static final Logger logger = LoggerFactory
			.getLogger("ImporterInputStreamCreator");

	public void setFile(String projectFile) {
		if (new File(projectFile).isDirectory()) {
			currentStreamCreator = new EAPInputStreamCreator();
			((EAPInputStreamCreator) currentStreamCreator).setFile(projectFile);
			logger.info("Folder project reader");
		} else if (projectFile.endsWith(".eap")) {
			projectFile = projectFile.substring(0, projectFile.length() - 4);
			currentStreamCreator = new EAPInputStreamCreator();
			((EAPInputStreamCreator) currentStreamCreator).setFile(projectFile);
			logger.info("Eap project reader");
		} else if (projectFile.endsWith(".zip") || projectFile.endsWith(".ead")) {
			currentStreamCreator = new ZipInputStreamCreator();
			((ZipInputStreamCreator) currentStreamCreator)
					.setZipFile(projectFile);
			logger.info("Zip/ead project reader");
		} else {
			logger.info("No project reader available for: " + projectFile);
		}
	}

	@Override
	public InputStream buildInputStream(String file) {
		return currentStreamCreator.buildInputStream(file);
	}

	@Override
	public URL buildURL(String file) {
		return currentStreamCreator.buildURL(file);
	}

	@Override
	public String[] listNames(String file) {
		return currentStreamCreator.listNames(file);
	}

}
