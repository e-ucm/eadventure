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

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.String;
import java.util.Set;
import java.util.TreeSet;

//TODO This class should be called for any project that has
//     resources upon building
/**
 * This class allows the automatic creation of the R.java files that define
 * the class which is used to internationalize resources in the application.
 * The R classes contain generated maps of all available resources in the
 * classpath, and should be regenerated as part of the release process. This
 * method is much quicker than scanning jar-files at execution time.
 *
 * R.java files imitate a similar mechanism developed for Android applications;
 * you can read more about it at
 * http://developer.android.com/guide/topics/resources/accessing-resources.html
 */
public class ResourceCreator {

	private static String eol = System.getProperty("line.separator");

	/**
	 * Generate the R.java file with the R class for the given project and package
	 *
	 * @param args projecteURL: the location of the project for which the R file must be generated
	 * packageName: the name of the main package in the project
	 */
	public static void main(String[] args) throws IOException {

        String regenName = ResourceCreator.class.getCanonicalName();
        if (args.length < 2 || (args.length > 0 && args[0].equals("-h"))) {
            System.err.println("Syntax: java -cp <classpath> "
                + regenName
                + " <project-location> <package-name> [<source-location>]\n"
                + "Where \n"
                + "   classpath - "
                    + "the classpath you are using now\n"
                + "   project-location - "
                    + "location of the project whose resources you want to index\n"
                + "   package-name - "
                    + "name of the package you want to create the R.java file in\n"
                + "   source-location - "
                    + "location for output R.java file; if absent, stdout is used\n");
            System.exit(-1);
        }

		String projectURL = args[0];  // /Users/..../eadventure.editor-core-impl
		String packageName = args[1]; // ead.editor
        PrintStream out = (args.length == 2) ?
                System.out : new PrintStream(args[2]);

        String importName = ResourceCreator.class.getCanonicalName()
                .replace(ResourceCreator.class.getSimpleName(), "I18N");

        // Expect a project-location/src/main/resources folder
		File resources =
			new File(projectURL + File.separator
                + "src" + File.separator
				+ "main" + File.separator
                + "resources");

		String classContent = ""
            + "package " + packageName + ";" + eol
            + eol
            + "import " + importName + ";" + eol
            + "import java.util.Set;" + eol
            + "import java.util.TreeSet;" + eol
            + eol
            + "/**" + eol
            + " * Resource index for this package (statically compiled)." + eol
            + " *" + eol
            + " * This is an AUTOMATICALLY-GENERATED file - " + eol
            + " * Run class " + regenName + " with " + eol
            + " * paramenters: \"" + projectURL + " " + packageName + "\"" + eol
            + " * where \"" + projectURL + "\" is the project location" + eol
            + " * and \"" + packageName + "\" is the name of the package" + eol
            + " * to re-create or update this class" + eol
            + " */" + eol
            + "public class R {" + eol
            + eol
            + createClass(resources, "Drawable")
            + "}" + eol;

		out.println(classContent);
	}

	/**
	 * Create a sub-class, which contains the actual resources (e.g. "Drawable").
	 *
	 * @param location the location of the resources
	 * @param className the name of the new class
	 * @return A string with the full definition of the sub-class
	 */
	private static String createClass(File location, String className) {
		StringBuilder classContent = new StringBuilder(
                "\tpublic static class " + className + " {" + eol);

		File resources =
			new File(location.getAbsolutePath() + File.separator + className.toLowerCase());


		Set<String> res = new TreeSet<String>();
		Set<String> files = new TreeSet<String>();

		for (File file : resources.listFiles()) {
			if (file.getName().startsWith(".")) {
			} else if (file.isDirectory())
				recursive(files, file.getName() + File.separator, file);
			else {
				res.add(file.getName());
				files.add(file.getName());
			}
		}

		for (String resource : res) {
			String constant = resource.replace('.', '_');
            classContent.append("\t\tpublic static String ")
                    .append(constant).append(";")
                    .append(eol);
		}

        classContent.append(eol)
                .append("\t\tstatic {").append(eol)
                .append("\t\t\tSet<String> files = new TreeSet<String>();").append(eol)
                .append(eol);

		for (String file : files) {
			classContent.append("\t\t\tfiles.add(\"").append(file).append("\");")
                    .append(eol);
        }

        classContent.append(eol)
            .append("\t\t\tI18N.initializeResources(Drawable.class.getName(),"
                + " Drawable.class, files);").append(eol)
            .append("\t\t}").append(eol)
            .append("\t}").append(eol);

		return classContent.toString();
	}

	/**
	 * Recursive method to visit all the sub-folders in the resource structure
	 *
	 * @param files
	 * @param currentPath
	 * @param currentDir
	 */
	private static void recursive(Set<String> files, String currentPath, File currentDir) {
		for (File file : currentDir.listFiles()) {
			if (file.isDirectory())
				recursive(files, currentPath + file.getName() + File.separator, file);
			else {
				files.add(currentPath + file.getName());
			}
		}
	}
}
