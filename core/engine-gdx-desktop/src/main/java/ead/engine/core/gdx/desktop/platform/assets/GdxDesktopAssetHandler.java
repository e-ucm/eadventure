package ead.engine.core.gdx.desktop.platform.assets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

	@Inject
	public GdxDesktopAssetHandler(GenericInjector injector) {
		super(injector);
		zipped = false;
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

		public long length() {
			try {
				return read().available();
			} catch (IOException e) {
				return 0;
			}
		}

		public String toString() {
			return zipFile.getName() + zipEntry.getName();
		}

	}

}
