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

package es.eucm.eadventure.utils.i18n;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

//TODO This class should be called for any project that has
//     resources upon building
/**
 * This class allow the automatic creation of the R.java files that define
 * the class which is used to internationalize resources in the application.
 * This class is generated with a map of all available resources in the
 * classpath for efficiency purposes and should thus be included in the
 * release generation process to include the latest changes to the
 * resource file structure.
 */
public class ResourceCreator {

	private static String eol = System.getProperty("line.separator");
	
	/**
	 * Generate the R.java file with the R class for the given project and package
	 * 
	 * @param args projecteURL: the location of the project for which the R file must be generated
	 * packageName: the name of the main package in the project
	 */
	public static void main(String[] args) {
		String projectURL = args[0]; // /Users/..../eadventure.editor-core-impl
		String packageName = args[1]; // es.eucm.eadventure.editor
		
		File resources = 
			new File(projectURL + File.separator + "src" + File.separator
					+ "main" + File.separator + "resources");

		String classContent = "package " + packageName + ";" + eol;
		classContent += "import es.eucm.eadventure.utils.i18n.I18N;" + eol;
		classContent += "import java.util.HashSet;" + eol;
		classContent += "import java.util.Set;" + eol;
		
		classContent += "/*" + eol;
		classContent += " * This is an automatically generated resources class" + eol;
		classContent += " * Run class es.eucm.eadventure.utils with " + eol;
		classContent += " * paramenters: \"" + projectURL + " " + packageName + "\"" + eol;
		classContent += " * where \"" + projectURL + "\" is the project location" + eol;
		classContent += " * and \"" + packageName + "\" is the name of the package" + eol;
		classContent += " * to recrate or update this class" + eol;
		classContent += " */" + eol;
		classContent += "public class R {" + eol + eol;
		
		classContent += createClass(resources, "Drawable") + eol;
		
		classContent += "}" + eol;
		
		System.out.println(classContent);
		//TODO class contents should be written to R.
	}
	
	/**
	 * Create a sub-class, which contains the actual resources (e.g. "Drawable").
	 * 
	 * @param location the location of the resources
	 * @param className the name of the new class
	 * @return A string with the full definition of the sub-class
	 */
	private static String createClass(File location, String className) {
		String classContent = "public static class " + className + " {" + eol;
		
		File resources = 
			new File(location.getAbsolutePath() + File.separator + className.toLowerCase());

		
		Set<String> res = new HashSet<String>();
		Set<String> files = new HashSet<String>();
		
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
			String tmp[] = resource.split("\\.");
			String constant = tmp[0];
			for (int i = 1; i < tmp.length - 1; i++) {
				constant += "." + tmp[i];
			}
			constant += "_" + tmp[tmp.length - 1];
			classContent += "public static String " + constant + ";" + eol;
		}
		
		classContent += "static {" + eol;
		classContent += "Set<String> files = new HashSet<String>();" + eol;

		for (String file : files) 
			classContent += "files.add(\"" + file + "\");" + eol;
		classContent += "I18N.initializeResources(Drawable.class.getName(), Drawable.class, files);" + eol;
		
		classContent += "}" + eol;
		classContent += "}" + eol;
		
		return classContent;
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
