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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.loader.InputStreamCreator;

@Singleton
public class ZipInputStreamCreator implements InputStreamCreator {

	private ZipFile zipFile;

	private String zipPath;

	public void setZipFile(String file) {
		try {
			this.zipPath = file;
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
			Map<String, String> files = new LinkedHashMap<String, String>();
			while (entries.hasMoreElements()) {
				ZipEntry ze = entries.nextElement();
				if (ze.getName().startsWith(filePath + "/")) {
					if (ze.getName().contains("/"))
						files.put(ze.getName().substring(0,
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
		try {
			return createAssetURL(zipFile, zipPath, path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;

	}

	// Old classes

	public static URL createAssetURL(ZipFile zipFile, String zipPath,
			String assetPath) throws MalformedURLException {

		URL url = null;

		// File parentFile = new File(Controller.getInstance().getZipFile());
		File parentFile = new File(zipPath);
		File file = new File(parentFile, assetPath);

		url = file.toURI().toURL();
		url = new URL(url.getProtocol(), url.getHost(), url.getPort(), url
				.getFile(),
				new ZipURLStreamHandler(zipFile, zipPath, assetPath));

		return url;
	}

	public static class ZipURLStreamHandler extends URLStreamHandler {

		private String assetPath;

		private String zipPath;

		private ZipFile zipFile;

		public ZipURLStreamHandler(ZipFile zipFile, String zipPath,
				String assetPath) {
			this.zipFile = zipFile;
			this.assetPath = assetPath;
			this.zipPath = zipPath;
		}

		public ZipURLStreamHandler(String assetPath) {

			this.assetPath = assetPath;
			zipPath = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.net.URLStreamHandler#openConnection(java.net.URL)
		 */
		@Override
		protected URLConnection openConnection(URL u) throws IOException {

			if (zipPath != null)
				return new ZipURLConnection(u, zipPath, assetPath, zipFile);
			return new ZipURLConnection(u, assetPath, zipFile);
		}

	}

	public static class ZipURLConnection extends URLConnection {

		private String assetPath;

		private ZipFile zipFile;

		/**
		 * @param url
		 * @throws MalformedURLException
		 */
		public ZipURLConnection(URL assetURL, String zipPath, String assetPath,
				ZipFile zipFile) {

			super(assetURL);
			this.assetPath = assetPath;
			this.zipFile = zipFile;
		}

		/**
		 * @param url
		 * @throws MalformedURLException
		 */
		public ZipURLConnection(URL assetURL, String assetPath, ZipFile zipFile) {

			super(assetURL);
			this.assetPath = assetPath;
			this.zipFile = zipFile;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.net.URLConnection#connect()
		 */
		@Override
		public void connect() throws IOException {

		}

		@Override
		public InputStream getInputStream() {

			if (assetPath != null) {
				return buildInputStream();
			} else {
				try {
					return url.openStream();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				// return AssetsController.getInputStream(assetPath);
			}
		}

		private InputStream buildInputStream() {

			try {
				return zipFile.getInputStream(zipFile.getEntry(this.assetPath));

			} catch (FileNotFoundException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}
