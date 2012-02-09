package ead.common.importer.auxiliar.inputstreamcreators;

import java.io.InputStream;
import java.net.URL;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.loader.InputStreamCreator;

@Singleton
public class ImporterInputStreamCreator implements InputStreamCreator {

	private EAPInputStreamCreator eapCreator;

	private ZipInputStreamCreator zipCreator;

	private InputStreamCreator currentStreamCreator;

	public ImporterInputStreamCreator() {
		eapCreator = new EAPInputStreamCreator();
		zipCreator = new ZipInputStreamCreator();
	}

	public void setFile(String projectFile) {
		if (projectFile.endsWith(".eap")) {
			projectFile = projectFile.substring(0, projectFile.length() - 4);
			eapCreator.setFile(projectFile);
			currentStreamCreator = eapCreator;
		} else if (projectFile.endsWith(".zip") || projectFile.endsWith(".ead")) {
			zipCreator.setZipFile(projectFile);
			currentStreamCreator = zipCreator;
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
