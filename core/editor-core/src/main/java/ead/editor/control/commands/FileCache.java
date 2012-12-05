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

package ead.editor.control.commands;

import ead.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A cache intended to allow undo/redo for file operations.
 *
 * @author mfreire
 */
public class FileCache {

	private static final Logger logger = LoggerFactory.getLogger("FileCache");

	private File base;

	public FileCache(File base) {
		this.base = base;
		if (!base.isDirectory() || base.mkdir()) {
			throw new IllegalArgumentException(
					"Could not create file cache directory");
		}
	}

	private String hashFile(File f) throws IOException {
		FileChannel fc = null;
		try {
			MessageDigest sha1Sun = MessageDigest.getInstance("SHA-1");
			ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
			if (f != null) {
				fc = new RandomAccessFile(f, "r").getChannel();
				while (fc.position() < fc.size()) {
					fc.read(buffer);
					buffer.flip();
					sha1Sun.update(buffer.array(), 0, buffer.limit());
					buffer.clear();
				}
			}

			byte[] sun = sha1Sun.digest();
			sha1Sun.reset();
			buffer.clear();
			if (fc != null) {
				fc.close();
				fc = null;
			}
			return showBytes(sun);
		} catch (Throwable e) {
			throw new IOException("Error hashing" + f + "; see log", e);
		} finally {
			try {
				if (fc != null) {
					fc.close();
				}
			} catch (Exception e) {
				logger.error("Error closing file-channel after hashing {}", f,
						e);
			}
		}
	}

	private String showBytes(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			sb.append(String.format("%02x", b[i]));
		}
		return sb.toString();
	}

	/**
	 * Saves the file for later retrieval.
	 * @param f file to save (can be null; an empty file is then saved)
	 * @return the hash-key from which to retrieve it later. Lose the key, lose
	 * the file.
	 * @throws IOException 
	 */
	public String saveFile(File f) throws IOException {
		String hash = hashFile(f);
		File dest = new File(base, hash);
		if (!dest.exists()) {
			if (f == null) {
				dest.createNewFile();
			} else {
				FileUtils.copyRecursive(f, null, dest);
			}
		}
		return hash;
	}

	/**
	 * Restores a saved file.
	 * @param hash returned when saving the file
	 * @param dest File where the recovered bytes should be placed.
	 * @throws IOException 
	 */
	public void restoreFile(String hash, File dest) throws IOException {
		File source = new File(base, hash);
		FileUtils.copyRecursive(source, null, dest);
	}
}
