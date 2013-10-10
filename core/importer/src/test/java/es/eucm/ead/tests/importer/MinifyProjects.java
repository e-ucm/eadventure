package es.eucm.ead.tests.importer;

import es.eucm.ead.tools.java.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Substitutes all the assets in all the projects in src/test/resources for an empty file (placeholder.bin), to
 * keep the repository small.
 * <p/>
 * When you want to add a new game to the test, you need to run this
 */
public class MinifyProjects {

	public static final String FOLDER = "src/test/resources/";

	public static final File PLACEHOLDER = new File(FOLDER + "placeholder.bin");

	public static void main(String args[]) throws Exception {
		if (!PLACEHOLDER.exists()) {
			throw new Exception(
					"You're not executing the minifier in the right workspace.");
		}

		File root = new File(FOLDER);
		// To avoid minify the placeholder
		for (File child : root.listFiles()) {
			if (child.isDirectory()) {
				minify(child);
			}
		}
	}

	public static void minify(File folder) {
		for (File child : folder.listFiles()) {
			if (child.isDirectory()) {
				minify(child);
			} else {
				if (!(child.getName().endsWith(".xml")
						|| child.getName().endsWith(".dtd") || child.getName()
						.endsWith(".eaa"))) {
					try {
						FileUtils.copy(PLACEHOLDER, child);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
