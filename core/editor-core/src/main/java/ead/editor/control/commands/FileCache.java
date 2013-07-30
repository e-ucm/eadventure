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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.utils.FileUtils;

/**
 * A cache intended to allow undo/redo for file operations. The cache is backed
 * by a 'base' folder, into which file hashes (with the corresponding contents)
 * are saved. Key management is entirely up to the user. Out-of-application
 * changes can be checked for using key.sameAsFor(f,deep).
 *
 * @author mfreire
 */
public class FileCache {

	private static final Logger logger = LoggerFactory.getLogger("FileCache");

	/**
	 * Base directory for file access; allows safe relative filenames.
	 */
	private File base;

	public FileCache(File base) {
		this.base = base;
		if (!base.isDirectory() || base.mkdir()) {
			throw new IllegalArgumentException(
					"Could not create file cache directory");
		}
	}

	/**
	 * Shows bytes, as series of two-digit hex chars.
	 * @param b
	 * @return the bytes, encoded in hex.
	 */
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
	 * @return the key from which to retrieve file-contents later.
	 * Lose the key, lose the file.
	 * @throws IOException
	 */
	public Key saveFile(File f) throws IOException {
		Key k = new Key();
		k.addAttributes(f);
		k.addContents(f);
		File dest = new File(base, showBytes(k.hash));
		if (!dest.exists()) {
			if (f == null) {
				dest.createNewFile();
			} else {
				FileUtils.copy(f, dest);
			}
		}
		return k;
	}

	/**
	 * Restores a saved file.
	 * @param key returned when saving the file
	 * @param dest File where the recovered bytes should be placed.
	 * @throws IOException
	 */
	public void restoreFile(Key key, File dest) throws IOException {
		File source = new File(base, showBytes(key.hash));
		FileUtils.copy(source, dest);
	}

	/**
	 * Used to find files in the cache.
	 */
	public static class Key {

		private static MessageDigest digester = null;

		private String[] attributes;
		private byte[] hash;

		public void addAttributes(File f) throws IOException {
			if (attributes == null) {
				attributes = computeAttributes(f);
			}
		}

		public void addContents(File f) throws IOException {
			if (hash == null) {
				hash = computeContentsHash(f);
			}
		}

		public boolean sameAsFor(File f, boolean deep) throws IOException {
			Key k = new Key();
			k.addAttributes(f);
			if (Arrays.deepEquals(attributes, k.attributes)) {
				k.addContents(f);
				if (!deep || Arrays.equals(hash, k.hash)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Returns a hash for a file's meta-data. Reads file size,
		 * creation & modification time (but not file-contents) into a string. 
		 * Different strings implies
		 * "different" files (different attributes; but contents *may* be the same)
		 */
		private static String[] computeAttributes(File f) throws IOException {
			return new String[] { f.getPath(), "" + f.length(),
					"" + f.lastModified() };
		}

		/**
		 * Returns a hash for a file's contents. Reads file contents into a hashing
		 * function.
		 * @param f a file
		 * @return the file's hash
		 * @throws IOException
		 */
		private static byte[] computeContentsHash(File f) throws IOException {
			FileChannel fc = null;
			try {
				if (digester == null) {
					digester = MessageDigest.getInstance("SHA-1");
				}
				ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
				if (f != null && f.exists() && f.canRead()) {
					fc = new RandomAccessFile(f, "r").getChannel();
					while (fc.position() < fc.size()) {
						fc.read(buffer);
						buffer.flip();
						digester.update(buffer.array(), 0, buffer.limit());
						buffer.clear();
					}
				} else {
					logger.info("no such file {} - using empty contents", f);
				}

				byte[] output = digester.digest();
				digester.reset();
				buffer.clear();
				if (fc != null) {
					fc.close();
					fc = null;
				}
				return output;
			} catch (Throwable e) {
				throw new IOException("Error hashing" + f + "; see log", e);
			} finally {
				try {
					if (fc != null) {
						fc.close();
					}
				} catch (Exception e) {
					logger.error("Error closing file-channel after hashing {}",
							f, e);
				}
			}
		}
	}
}
