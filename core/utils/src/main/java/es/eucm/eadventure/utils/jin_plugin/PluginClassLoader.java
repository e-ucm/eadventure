package es.eucm.eadventure.utils.jin_plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {

	public PluginClassLoader() {
		super(new URL[] {});
	}

	private void addFile(File file) throws MalformedURLException {
		addURL(file.toURI().toURL());
	}

	public void loadPlugin(File pluginDir) {
		try {
			File classesDir = new File(pluginDir, "classes");
			if (classesDir.exists())
				addFile(classesDir);
			File libDir = new File(pluginDir, "lib");
			File[] jars = libDir.listFiles();
			if (jars != null)
				for (File jar : jars)
					addFile(jar);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
