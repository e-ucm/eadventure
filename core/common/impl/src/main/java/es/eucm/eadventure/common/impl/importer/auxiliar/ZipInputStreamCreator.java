package es.eucm.eadventure.common.impl.importer.auxiliar;

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
