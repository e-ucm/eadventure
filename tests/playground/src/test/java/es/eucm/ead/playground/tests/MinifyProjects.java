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

package es.eucm.ead.playground.tests;

import es.eucm.ead.tools.java.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
			if (child.getName().endsWith(".ead")) {
				minifyZip(child);
			}
		}
	}

	public static void minifyZip(File eadZip) {
		try {
			File temp = new File(eadZip.getParentFile() + "/temp");
			File tempZip = new File(eadZip.getAbsolutePath() + ".2");
			FileUtils.unzip(eadZip, temp);
			minify(temp);
			FileUtils.zip(tempZip, temp);
			FileUtils.deleteRecursive(temp);
			FileUtils.copy(tempZip, eadZip);
			FileUtils.deleteRecursive(tempZip);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void minify(File folder) {
		for (File child : folder.listFiles()) {
			if (child.isDirectory()) {
				minify(child);
			} else {
				if (child.getName().endsWith(".avi")
						|| child.getName().endsWith(".mp3")
						|| child.getName().endsWith(".wav")
						|| child.getName().endsWith(".html")) {
					try {
						FileUtils.copy(PLACEHOLDER, child);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (child.getName().endsWith(".png")
						|| child.getName().endsWith(".jpg")) {
					try {
						BufferedImage im = ImageIO.read(child);

						BufferedImage i = new BufferedImage(im.getWidth(), im
								.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
						im.flush();
						String extension = child.getName().substring(
								child.getName().length() - 3,
								child.getName().length());
						ImageIO.write(i, extension, child);
						i.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else if (child.getName().endsWith(".db")) {
					try {
						FileUtils.deleteRecursive(child);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
