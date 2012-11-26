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

package ead.utils.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

//TODO This class should be called for any project that has
//     resources upon building
/**
 * Automatically creates and updates R.java (for resources) and Messages.java 
 * (for internationalized strings) files used for internationalization (I18N).
 * The R classes contain generated maps of all available resources in the
 * classpath, and should be regenerated as part of the release process. This
 * method is much quicker than scanning jar-files at execution time. Messages.java
 * files have a similar structure and workflow.
 *
 * R.java files imitate a similar mechanism developed for Android applications;
 * you can read more about it at
 * http://developer.android.com/guide/topics/resources/accessing-resources.html
 */
public class ResourceCreator {

	private static String eol = System.getProperty("line.separator");

	/**
	 * Generate the R.java file with the 'R' class for the given project and package
	 *
	 * @param args projecteURL: the location of the project for which the R file must be generated
	 * packageName: the name of the main package in the project
	 */
	public static void main(String[] args) throws IOException {

		String regenName = ResourceCreator.class.getCanonicalName();
		if (args.length < 3 || args.length > 4
				|| (args.length > 0 && args[0].equals("-h"))) {
			System.err
					.println("Syntax: java -cp <classpath> "
							+ regenName
							+ " <project-location> <package-name> <license-file> [<source-location>]\n"
							+ "Where \n"
							+ "   classpath - "
							+ "the classpath you are using now\n"
							+ "   project-location - "
							+ "location of the project whose resources you want to index\n"
							+ "   license-file - "
							+ "name of the file with the license you want to pre-pend\n"
							+ "   package-name - "
							+ "name of the package where R.java / Messages.java files should be generated\n"
							+ "   source-location - "
							+ "location for resulting R.java / Messages.java files; if absent, stdout is used\n");
			System.exit(-1);
		}

		String projectURL = args[0]; // .../eadventure.editor-core
		String packageName = args[1]; // ead.editor
		String licenseFileName = args[2]; // etc/LICENSE.txt
		PrintStream out = (args.length == 3) ? System.out : new PrintStream(
				args[3]);

		String importName = ResourceCreator.class.getCanonicalName().replace(
				ResourceCreator.class.getSimpleName(), "I18N");

		// Expect a project-location/src/main/resources folder
		File resources = new File(projectURL + File.separator + "src"
				+ File.separator + "main" + File.separator + "resources");

		// build a paramString to include in class comment
		StringBuilder sb = new StringBuilder();
		for (String s : args) {
			sb.append(" \"").append(s).append("\"");
		}
		String parameterString = sb.toString();

		// write single R file
		System.err.println("\tProcessing resources (from " + resources + ")");
		printLicense(licenseFileName, out);
		out.println(resourceFileContents(packageName, importName, regenName,
				parameterString, resources));
		if (out != System.out) {
			out.close();
		}

		// find each Messages.properties file, and generate a mirror Messages.java file
		System.err.println("\tProcessing messages...");
		for (File propsFile : new FileFinder(resources, "Messages.properties")) {
			String p = propsFile.getPath();
			File outputFile = new File(p.replace(
					"main" + File.separator + "resources",
					"main" + File.separator + "java").replace(".properties",
					".java"));
			String startOfPackage = "main" + File.separator + "resources";
			String truePackage = p.substring(
					p.indexOf(startOfPackage) + startOfPackage.length() + 1,
					p.indexOf(propsFile.getName()) - 1).replace(File.separator,
					".");
			System.err
					.println("\t" + truePackage + " (from " + propsFile + ")");
			out = (args.length == 3) ? System.out : new PrintStream(outputFile);

			// write this Messages file
			printLicense(licenseFileName, out);
			out.println(messageFileContents(truePackage, importName, regenName,
					parameterString, propsFile));
			if (out != System.out) {
				out.close();
			}
		}
	}

	private static class FileFinder implements Iterable<File> {
		private ArrayList<File> found = new ArrayList<File>();

		public FileFinder(File dir, String name) {
			find(dir, name);
		}

		private void find(File dir, String name) {
			for (File f : dir.listFiles()) {
				if (f.isFile() && f.getName().equals(name)) {
					found.add(f);
				} else if (f.isDirectory()) {
					find(f, name);
				}
			}
		}

		@Override
		public Iterator<File> iterator() {
			return found.iterator();
		}
	}

	private static String resourceFileContents(String packageName,
			String importName, String regenName, String parameterString,
			File resources) {
		return "package " + packageName + ";" + eol + eol
				+ "import java.util.Set;" + eol + "import java.util.TreeSet;"
				+ eol + eol + "import " + importName + ";" + eol + eol + "/**"
				+ eol
				+ " * Resource index for this package (statically compiled)."
				+ eol + " *" + eol
				+ " * This is an AUTOMATICALLY-GENERATED file - " + eol
				+ " * Run class " + regenName + " with parameters: " + eol
				+ " *   " + parameterString + eol
				+ " * to re-create or update this class" + eol + " */" + eol
				+ "@edu.umd.cs.findbugs.annotations.SuppressFBWarnings" + eol
				+ "public class R {" + eol + eol
				+ createResourceContents(resources, "Drawable") + "}" + eol;
	}

	private static String messageFileContents(String packageName,
			String importName, String regenName, String parameterString,
			File resource) {
		return "package "
				+ packageName
				+ ";"
				+ eol
				+ eol
				+ "import "
				+ importName
				+ ";"
				+ eol
				+ eol
				+ "/**"
				+ eol
				+ " * Message index for this class (bound at run-time according to user preferences)"
				+ eol + " *" + eol
				+ " * This is an AUTOMATICALLY-GENERATED file - " + eol
				+ " * Run class " + regenName + " with parameters: " + eol
				+ " *   " + parameterString + eol
				+ " * to re-create or update this class" + eol + " */" + eol
				+ "@edu.umd.cs.findbugs.annotations.SuppressFBWarnings" + eol
				+ "public class Messages {" + eol + eol
				+ createMessageContents(resource) + "}" + eol;
	}

	/**
	 * Writes license-file contents into a PrintStream
	 */
	private static void printLicense(String fileName, PrintStream out) {
		File f = new File(fileName);
		BufferedReader br = null;
		try {
			out.println("/**");
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), "UTF-8"));
			while (br.ready()) {
				out.println(" * " + br.readLine());
			}
			out.println(" */");
		} catch (IOException e) {
			System.err.println("Error adding license from '"
					+ f.getAbsolutePath() + "'");
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Write resource-list into R class.
	 * Notice that "qualifiers" (-something extensions in the leading directories) 
	 * are ignored, to allow for internationalization. Examples:
	 * drawable/EditorIcon16x16_bw.png becomes 
	 *		EditorIcon16x16_bw_png
	 * drawable/SplashScreenLogo.png becomes 
	 *		SplashScreenLogo.png
	 * drawable-es_ES/SplashScreenLogo.png becomes 
	 *		es_ES/SplashScreenLogo_png
	 * drawable/conditions/vars.png becomes 
	 *		conditions__vars_png
	 * drawable-es_ES/SplashScreenLogo.png becomes 
	 *		es_ES/conditions__vars_png
	 *
	 * @param location the location of the resources
	 * @param className the name of the new class
	 * @return A string with the full definition of the sub-class
	 */
	private static String createResourceContents(File location,
			final String className) {
		StringBuilder classContent = new StringBuilder("\tpublic static class "
				+ className + " {" + eol);

		FileFilter ff = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().startsWith(className.toLowerCase());
			}
		};

		Set<String> files = new TreeSet<String>();
		for (File resources : location.getAbsoluteFile().listFiles(ff)) {
			String localeString = resources.getName().contains("-") ? resources
					.getName().replaceAll(".*[-]", "")
					+ "//" : "";
			for (File file : resources.listFiles()) {
				if (file.getName().startsWith(".")) {
					// ignore . and ..
				} else if (file.isDirectory())
					recursive(files, file.getName() + File.separator, file);
				else {
					files.add(localeString + file.getName());
				}
			}
		}

		Set<String> res = new TreeSet<String>();
		for (String resource : files) {
			// removes locales
			resource = resource.replaceAll(".*[/][/]", "");

			if (!resource.matches("^[a-zA-Z0-9_/]+[.][a-zA-Z0-9_]+$")) {
				System.err
						.println("Sorry, '"
								+ resource
								+ "' has an invalid name. \n"
								+ "\tPlease avoid spaces and any non-alphanumeric characters, such as '-+' or ':'; '_' is ok, though");
			} else if (resource.matches(".*[_][_].*")) {
				System.err
						.println("Sorry, '"
								+ resource
								+ "' has an invalid name. \n"
								+ "\tPlease avoid two '__' in a row; we use it for '/'-substitution...");
			} else {
				resource = resource.replaceAll("/", "__").replace(".", "_");
				if (!res.contains(resource)) {
					classContent.append("\t\tpublic static String ").append(
							resource).append(";").append(eol);
					res.add(resource);
				}
			}
		}

		classContent.append(eol).append("\t\tstatic {").append(eol).append(
				"\t\t\tSet<String> files = new TreeSet<String>();").append(eol)
				.append(eol);

		for (String file : files) {
			classContent.append("\t\t\tfiles.add(\"").append(
					file.replaceAll("[/][/]", "/")).append("\");").append(eol);
		}

		classContent.append(eol).append(
				"\t\t\tI18N.initializeResources(Drawable.class.getName(),"
						+ " Drawable.class, files);").append(eol).append(
				"\t\t}").append(eol).append("\t}").append(eol);

		return classContent.toString();
	}

	/**
	 * Write message-list into Messages class.
	 * Only messages that exist in the default language are included. 
	 *
	 * @param location the location of the source .properties file
	 * @param className the name of the new class
	 * @return A string with the full definition of the sub-class
	 */
	private static String createMessageContents(File location) {

		Properties properties = new Properties();
		try {
			properties.load(new FileReader(location));
		} catch (Exception e) {
			System.err.println("Sorry, '" + location
					+ "' is not a valid properties file: \n" + "\t"
					+ e.getMessage() + "\n");
			e.printStackTrace();
			return "ERROR GENERATING FROM " + location;
		}

		ArrayList<String> keys = new ArrayList<String>();
		for (Object o : properties.keySet()) {
			keys.add(o.toString());
		}
		Collections.sort(keys);

		StringBuilder classContent = new StringBuilder();
		for (String key : keys) {
			classContent.append("\tpublic static String ").append(key).append(
					";").append(eol);
		}

		classContent.append(eol).append("\tstatic {").append(eol).append(
				"\t\tI18N.initializeMessages(Messages.class.getName(),"
						+ " Messages.class);").append(eol).append("\t}")
				.append(eol);

		return classContent.toString();
	}

	/**
	 * Recursive method to visit all the sub-folders in the resource structure.
	 *
	 * @param files
	 * @param currentPath
	 * @param currentDir
	 */
	private static void recursive(Set<String> files, String currentPath,
			File currentDir) {
		for (File file : currentDir.listFiles()) {
			if (file.isDirectory())
				recursive(files, currentPath + File.separator, file);
			else {
				files.add(currentPath + file.getName());
			}
		}
	}
}
