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

package ead.common.importer.auxiliar;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import es.eucm.eadventure.common.loader.InputStreamCreator;

public class ZipInputStreamCreator implements InputStreamCreator {

	private ZipFile zipFile;

	public ZipInputStreamCreator(String file) {
		try {
			zipFile = new ZipFile(file);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public InputStream buildInputStream(String filePath) {
		ZipEntry zipEntry = zipFile.getEntry(filePath);
		try {
			if (zipEntry != null)
				return zipFile.getInputStream(zipEntry);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String[] listNames(String filePath) {

		if (!filePath.contains(".")) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			HashMap<String, String> files = new HashMap<String, String>();
			while (entries.hasMoreElements()) {
				ZipEntry ze = entries.nextElement();
				if (ze.getName().startsWith(filePath + "/")) {
					if (ze.getName().contains("/"))
						files.put(
								ze.getName().substring(0,
										ze.getName().indexOf("/")), null);
					else
						files.put(ze.getName(), null);
				}
			}
			return files.keySet().toArray(new String[files.size()]);
		} else
			return new String[0];
	}

	@Override
	public URL buildURL(String path) {
		ZipEntry zipEntry = zipFile.getEntry(path);

		try {
			return new URL(zipEntry.getName());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
