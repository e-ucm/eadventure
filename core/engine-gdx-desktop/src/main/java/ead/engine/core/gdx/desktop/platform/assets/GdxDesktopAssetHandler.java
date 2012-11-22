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

package ead.engine.core.gdx.desktop.platform.assets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.badlogic.gdx.files.FileHandle;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.util.EAdURI;
import ead.engine.core.gdx.assets.GdxAssetHandler;
import ead.tools.GenericInjector;

@Singleton
public class GdxDesktopAssetHandler extends GdxAssetHandler {

	private ZipFile zipFile;

	private boolean zipped;

	private Map<String, String> tempFiles;

	@Inject
	public GdxDesktopAssetHandler(GenericInjector injector) {
		super(injector);
		zipped = false;
		tempFiles = new HashMap<String, String>();
	}

	@Override
	public void setResourcesLocation(EAdURI uri) {
		super.setResourcesLocation(uri);
		if (uri != null && uri.getPath() != null) {
			try {
				zipFile = new ZipFile(new File(uri.getPath()));
				zipped = true;
			} catch (ZipException e) {
				zipped = false;
			} catch (IOException e) {
				zipped = false;
			}
		} else {
			zipped = false;
		}
	}

	public String getTempFilePath(String path) {
		if (!tempFiles.containsKey(path)) {
			FileHandle file = getFileHandle(path);
			if (file.exists()) {
				try {
					File f = File.createTempFile("video", null);
					f.deleteOnExit();
					FileHandle tempFile = new FileHandle(f);
					file.copyTo(tempFile);
					tempFiles.put(path, f.getAbsolutePath());
				} catch (IOException e) {

				}
			}
		}
		return tempFiles.get(path);
	}

	@Override
	public FileHandle getProjectFileHandle(String uri) {
		FileHandle fh = super.getProjectFileHandle(uri);
		if (!fh.exists() && zipped) {
			ZipEntry zipEntry = zipFile.getEntry(uri);
			fh = new ZipFileHandle(zipFile, zipEntry);
		}
		return fh;
	}

	public class ZipFileHandle extends FileHandle {

		private ZipFile zipFile;

		private ZipEntry zipEntry;

		public ZipFileHandle(ZipFile zipFile, ZipEntry zipEntry) {
			this.zipFile = zipFile;
			this.zipEntry = zipEntry;
		}

		@Override
		public InputStream read() {
			try {
				return zipFile.getInputStream(zipEntry);
			} catch (IOException e) {

			}
			return null;
		}

		@Override
		public boolean exists() {
			return zipEntry != null;
		}

		@Override
		public long length() {
			try {
				return read().available();
			} catch (IOException e) {
				return 0;
			}
		}

		@Override
		public String toString() {
			return zipFile.getName() + zipEntry.getName();
		}
	}
}
