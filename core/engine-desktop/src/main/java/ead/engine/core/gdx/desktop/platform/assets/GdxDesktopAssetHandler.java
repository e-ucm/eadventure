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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.badlogic.gdx.files.FileHandle;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.assets.multimedia.EAdVideo;
import ead.engine.core.assets.AssetHandlerImpl;
import ead.engine.core.assets.SpecialAssetRenderer;
import es.eucm.ead.tools.GenericInjector;
import ead.engine.core.utils.SceneGraph;
import ead.tools.java.JavaInjector;

@Singleton
public class GdxDesktopAssetHandler extends AssetHandlerImpl {

	private ZipFile zipFile;

	private boolean zipped;

	private Map<String, String> tempFiles;

	private Integer preloading;

	@Inject
	public GdxDesktopAssetHandler(GenericInjector injector,
			SceneGraph sceneGraph) {
		super(injector, sceneGraph);
		zipped = false;
		tempFiles = new HashMap<String, String>();
		preloading = 0;
	}

	@Override
	public void setResourcesLocation(String uri) {
		super.setResourcesLocation(uri);
		if (uri != null && uri != null) {
			try {
				zipFile = new ZipFile(new File(uri));
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
					File f = File.createTempFile("video", ".avi");
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

	@Override
	public void getTextfileAsync(String path, TextHandler textHandler) {
		String result = null;
		InputStream is = ClassLoader.getSystemResourceAsStream(path);
		if (is == null) {
			File f = new File(path);
			if (f.exists()) {
				try {
					is = new FileInputStream(f);
				} catch (FileNotFoundException e) {

				}
			}
		}

		if (is != null) {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));

			try {
				String line = null;
				result = "";
				while ((line = reader.readLine()) != null) {
					result += line + "\n";
				}
			} catch (IOException e) {
				logger.error("Error reading file", e);
				result = null;
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						logger.error("Error closing reader", e);
					}
				}
			}
		} else {
			logger.warn("Unable to find resource {}", path);
		}
		textHandler.handle(result);
	}

	@Override
	public <T extends AssetDescriptor> SpecialAssetRenderer<T, ?> getSpecialAssetRenderer(
			T specialAsset) {
		synchronized (preloading) {
			return super.getSpecialAssetRenderer(specialAsset);
		}
	}

	public boolean isPreloadingVideos() {
		synchronized (preloading) {
			return preloading > 0;
		}
	}

	public boolean preloadVideos() {
		synchronized (preloading) {
			preloading = 0;
			Injector i = ((JavaInjector) injector).getInjector();
			for (EAdVideo video : this.videos) {
				SpecialAssetRenderer<EAdVideo, ?> renderer = i
						.getInstance(Key
								.get(new TypeLiteral<SpecialAssetRenderer<EAdVideo, ?>>() {
								}));
				new Thread(new LoadVideo(video, renderer)).start();
				preloading++;
			}
			return true;
		}
	}

	public class LoadVideo implements Runnable {

		private EAdVideo video;

		private SpecialAssetRenderer<EAdVideo, ?> renderer;

		public LoadVideo(EAdVideo video,
				SpecialAssetRenderer<EAdVideo, ?> renderer) {
			this.video = video;
			this.renderer = renderer;
		}

		@Override
		public void run() {
			renderer.getComponent(video);
			synchronized (preloading) {
				preloading--;
				specialRenderers.put(video, renderer);
			}
		}

	}

}
