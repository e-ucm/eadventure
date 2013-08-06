package es.eucm.ead.engine.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ResourcesGenerator {
	private static final String PACKAGE = "es/eucm/ead/engine/resources";
	private static final String PREFIX = "core/engine-resources/src/main/";

	public static void main(String args[]) {
		File f = new File(PREFIX + "java/" + PACKAGE + "/R.java");
		BufferedWriter writer = null;
		try {
			f.createNewFile();
			writer = new BufferedWriter(new FileWriter(f));
			writer.write("package " + PACKAGE.replace('/', '.') + ";");
			writer.newLine();
			writer.newLine();
			writer.write("public class R {");
			writer.newLine();
			writer
					.write("public static final String[] RESOURCES = new String[]{");
			writer.newLine();

			ArrayList<File> resources = getResources();
			for (int i = 0; i < resources.size(); i++) {
				File file = resources.get(i);
				String name = file.getAbsolutePath().substring(
						file.getAbsolutePath().indexOf(PACKAGE));
				writer.write("  \"" + name + "\""
						+ (i == resources.size() - 1 ? "" : ","));
				writer.newLine();
			}
			writer.write("};");
			writer.newLine();
			writer.write("}");
		} catch (IOException e) {
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

	public static ArrayList<File> getResources() {
		File folder = new File(PREFIX + "resources/" + PACKAGE);
		ArrayList<File> resources = new ArrayList<File>();
		addFiles(folder, resources);
		return resources;
	}

	public static void addFiles(File f, ArrayList<File> resources) {
		for (File file : f.listFiles()) {
			if (file.isDirectory()) {
				addFiles(file, resources);
			} else {
				resources.add(file);
			}
		}
	}
}
