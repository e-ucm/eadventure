package es.eucm.ead.engine.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ResourcesGenerator {
	private static final String PACKAGE = "es/eucm/ead/engine/resources";
	private static final String PREFIX_FROM_MODULE = "src/main/";
	private static final String PREFIX_FROM_ROOT = "core/engine-resources/src/main/";

	public static void main(String args[]) throws IOException {
		File fromModule = new File(PREFIX_FROM_MODULE);
		File fromRoot = new File(PREFIX_FROM_ROOT);
		File from = null;
		if (fromModule.isDirectory()) {
			from = fromModule;
		} else if (fromRoot.isDirectory()) {
			from = fromRoot;
		} else {
			throw new IOException("Cannot build R-file: bad relative paths");
		}
		File f = new File(from, "java/" + PACKAGE + "/R.java");

		System.err.println("Writing R file to '" + f.getAbsolutePath() + "'");
		BufferedWriter writer = null;
		try {
			f.createNewFile();
			writer = new BufferedWriter(new FileWriter(f));
			writer.write("package " + PACKAGE.replace('/', '.') + ";");
			writer.newLine();
			writer.newLine();
			writer.write("public class R {");
			writer.newLine();
			writer.write("public static final String[]"
					+ " RESOURCES = new String[]{");
			writer.newLine();

			ArrayList<File> resources = getResources(from);
			for (int i = 0; i < resources.size(); i++) {
				File file = resources.get(i);
				String name = file.getAbsolutePath().replace('\\', '/');
				//                                System.err.println("Mentioning " + name);
				name = name.substring(name.indexOf(PACKAGE));
				writer.write("  \"" + name + "\""
						+ (i == resources.size() - 1 ? "" : ","));
				writer.newLine();
			}
			writer.write("};");
			writer.newLine();
			writer.write("}");
		} catch (IOException e) {
			System.err.println("Error creating R-file: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	public static ArrayList<File> getResources(File from) {
		File folder = new File(from, "resources/" + PACKAGE);
		ArrayList<File> resources = new ArrayList<File>();
		addFiles(folder, resources);
		return resources;
	}

	public static void addFiles(File f, ArrayList<File> resources) {
		//                System.err.println(" ... adding files '" + f.getAbsolutePath() + "'");

		for (File file : f.listFiles()) {
			if (file.isDirectory()) {
				addFiles(file, resources);
			} else {
				resources.add(file);
			}
		}
	}
}
