package ead.common.importer.auxiliar.inputstreamcreators;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.loader.InputStreamCreator;

@Singleton
public class ImporterInputStreamCreator implements InputStreamCreator {

	private InputStreamCreator currentStreamCreator;

	private static final Logger logger = Logger.getLogger("ImporterInputStreamCreator");

	public void setFile(String projectFile) {
		if (new File(projectFile).isDirectory()) {
			currentStreamCreator = new EAPInputStreamCreator();
			((EAPInputStreamCreator) currentStreamCreator).setFile(projectFile);
			logger.log(Level.INFO, "Folder project reader");
		} else if (projectFile.endsWith(".eap")) {
			projectFile = projectFile.substring(0, projectFile.length() - 4);
			currentStreamCreator = new EAPInputStreamCreator();
			((EAPInputStreamCreator) currentStreamCreator).setFile(projectFile);
			currentStreamCreator = new EAPInputStreamCreator();
			logger.log(Level.INFO, "Eap project reader");
		} else if (projectFile.endsWith(".zip") || projectFile.endsWith(".ead")) {
			currentStreamCreator = new ZipInputStreamCreator();
			((ZipInputStreamCreator) currentStreamCreator).setZipFile(projectFile);
			logger.log(Level.INFO, "Zip/ead project reader");
		} else {
			logger.log(Level.SEVERE, "No project reader available for: " + projectFile);
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
