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

package es.eucm.ead.tools.java.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Misc file utilities for use in EAdventure
 * 
 * @author mfreire
 */
public class FileUtils {

	private static final Logger logger = LoggerFactory.getLogger("FileUtils");
	/** All zip files start with these bytes */
	public static int[] zipMagic = new int[] { 0x50, 0x4b };
	/** Size of buffer for stream operations */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * Returns the inputStream for a single zip-file entry.
	 * 
	 * @param zipFile
	 * @param entryName
	 * @return the requested input stream
	 * @throws IOException
	 *             on error
	 */
	public static InputStream readEntryFromZip(ZipFile zip, String entryName)
			throws IOException {
		return zip.getInputStream(zip.getEntry(entryName));
	}

	/**
	 * Checks if a zipfile contains a given entry. Matches with regexp, so you
	 * have to escape any special chars.
	 */
	public static boolean zipContainsEntry(File zipFile, String entryNameRegexp)
			throws IOException {
		if (!FileUtils.startMatches(zipFile, zipMagic)) {
			throw new IOException("File is not a zip archive");
		}

		ZipFile zf = new ZipFile(zipFile);
		boolean matched = false;
		try {
			Enumeration<?> entries = zf.entries();
			while (entries.hasMoreElements()) {
				ZipEntry e = (ZipEntry) entries.nextElement();
				String name = FileUtils.toCanonicalPath(e.getName());
				if (name.matches(entryNameRegexp)) {
					logger.debug("{} MATCHES {}", new Object[] { name,
							entryNameRegexp });
					matched = true;
					break;
				} else {
					logger.debug("{} does not match {}", new Object[] { name,
							entryNameRegexp });
				}
			}
			return matched;
		} finally {
			zf.close();
		}
	}

	/**
	 * Checks if folder contains a given entry (as a top-level file-or-folder).
	 * Matches name with regexp, so you have to escape any special chars.
	 */
	public static boolean folderContainsEntry(File folder, String nameRegexp)
			throws IOException {
		if (!folder.isDirectory()) {
			throw new IOException("Folder cannot be read");
		}
		for (File f : folder.listFiles()) {
			if (f.getName().matches(nameRegexp)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Writes an entry into a ZipFile. Can be called with any type of
	 * InputStream or entry name. Will overwrite entry if it already exists.
	 * 
	 * @param zipFile
	 * @param entryName
	 * @param is
	 * @throws IOException
	 */
	public static void appendEntryToZip(File zipFile, String entryName,
			InputStream is) throws IOException {
		boolean errors = false;
		ZipOutputStream out = null;
		File tempFile = File.createTempFile("ead-copy-zip", null);
		ZipFile source = new ZipFile(zipFile);
		try {
			out = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(tempFile)));

			// copy all old stuff
			Enumeration<? extends ZipEntry> entries = source.entries();
			while (entries.hasMoreElements()) {
				ZipEntry ze = entries.nextElement();
				if (ze.getName().equals(entryName)) {
					// avoid duplicating - will be written later
					continue;
				}
				out.putNextEntry(ze);
				if (!ze.isDirectory()) {
					copy(source.getInputStream(ze), out, false);
				}
			}

			// now, append the file
			ZipEntry ze = new ZipEntry(entryName);
			out.putNextEntry(ze);
			copy(is, out, false);
			out.closeEntry();
		} catch (Exception e) {
			logger.error("Error outputting zip to {}", zipFile, e);
			errors = true;
		} finally {
			if (source != null) {
				source.close();
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe) {
					logger.error("Could not close zip file writing to '{}'",
							zipFile, ioe);
					errors = true;
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException ioe) {
					logger.error("Could not close input stream for '{}'",
							entryName, ioe);
					errors = true;
				}
			}
		}

		if (!errors) {
			// try to switch original source with temp target
			zipFile.delete();
			if (!tempFile.renameTo(zipFile)) {
				logger.error("Could not replace input stream");
				errors = true;
			}
		}

		if (errors) {
			throw new IOException("Could not write '" + entryName + "' into '"
					+ zipFile + "'; see log for details");
		}
	}

	/**
	 * Pump from input to output. Closes input, and optionally output, when
	 * finished
	 * 
	 * @param input
	 * @param output
	 * @param closeOutput
	 *            if true, output will be closed when finished
	 * @throws IOException
	 */
	public static void copy(InputStream input, OutputStream output,
			boolean closeOutput) throws IOException {
		int len;
		byte[] b = new byte[BUFFER_SIZE];
		try {
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
		} finally {
			input.close();
			if (closeOutput) {
				output.close();
			}
		}
	}

	/**
	 * Pump from input to output. Closes both when finished
	 * 
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public static void copy(InputStream input, OutputStream output)
			throws IOException {
		copy(input, output, true);
	}

	/**
	 * Copy one file to another. If the files are actually directories, will
	 * copy them over recursively. If you do not wish to copy recursively, then 
	 * check your arguments first with isDirectory().
	 * @param source
	 * @param target
	 * @throws IOException
	 */
	public static void copy(File source, File target) throws IOException {
		copyRecursive(source, null, target);
	}

	/**
	 * Uncompresses a zip file into a directory
	 */
	public static void expand(File source, File destDir) throws IOException {

		if (!FileUtils.startMatches(source, zipMagic)) {
			throw new IOException("File is not a zip archive");
		}

		ZipFile zf = new ZipFile(source);
		byte[] b = new byte[BUFFER_SIZE];

		try {
			logger.debug("Extracting zip: " + source.getName());
			Enumeration<?> entries = zf.entries();
			while (entries.hasMoreElements()) {
				ZipEntry e = (ZipEntry) entries.nextElement();

				// backslash-protection: zip format expects only 'fw' slashes
				String name = FileUtils.toCanonicalPath(e.getName());

				if (e.isDirectory()) {
					logger.debug("\tExtracting directory '{}'", e.getName());
					File dir = new File(destDir, name);
					dir.mkdirs();
					continue;
				}

				logger.debug("\tExtracting file '{}'", name);
				File outFile = new File(destDir, name);
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(outFile);

				InputStream zis = zf.getInputStream(e);
				int len = 0;
				while ((len = zis.read(b)) != -1) {
					fos.write(b, 0, len);
				}
				fos.close();
				zis.close();
			}
		} finally {
			zf.close();
		}
	}

	/**
	 * Canonicalizes a path, transforming windows '\' to unix '/', stripping off
	 * any './' or '../' occurrences, and trimming start and end whitespace
	 */
	public static String toCanonicalPath(String name) {
		return name.replaceAll("\\\\", "/").replaceAll("(\\.)+/", "").trim();
	}

	/**
	 * Check the magic in a file
	 */
	public static boolean startMatches(File f, int[] magic) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			return startMatches(in, magic);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * Check the first few bytes in a stream against a pattern.
	 * 
	 * @param is
	 *            source to check
	 * @param magic
	 *            to check it against
	 * @return true if first bytes match magic
	 * @throws IOException
	 */
	public static boolean startMatches(InputStream is, int[] magic)
			throws IOException {
		for (int i = 0; i < magic.length; i++) {
			int r = is.read();
			if (r == -1 || r != magic[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Deletes a directory recursively. Use wisely
	 */
	public static void deleteRecursive(File f) throws IOException {
		if (f.isDirectory()) {
			for (File file : f.listFiles()) {
				deleteRecursive(file);
			}
		}
		f.delete();
	}

	/**
	 * Copies a directory recursively. Use wisely
	 * 
	 * @param src
	 *            source file or directory to copy
	 * @param dstDir
	 *            target directory to place it into. If dstName is null, must
	 *            exist, and dstDir/src must NOT exist.
	 * @param dstName
	 *            if not null, then src will be copied into dstName (and
	 *            dstDir/src may exist without conflict). May be a file (if src
	 *            is a file) or a folder (if src is a folder).
	 */
	public static void copyRecursive(File src, File dstDir, File dstName)
			throws IOException {
		File dstPath = dstName == null ? new File(dstDir, src.getName())
				: dstName;
		if (src.isDirectory()) {
			// recurse
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}
			for (File file : src.listFiles()) {
				copyRecursive(file, dstPath, null);
			}
		} else {
			// copy a file
			FileInputStream fis = new FileInputStream(src);
			FileOutputStream fos = new FileOutputStream(dstPath);
			copy(fis, fos);
		}
	}

	public static File createTempDir(String prefix, String suffix)
			throws IOException {
		try {
			if (suffix == null) {
				suffix = "" + (System.currentTimeMillis() % 1000);
			}
			File tempFile = File.createTempFile(prefix, suffix);
			tempFile.delete();
			tempFile.mkdirs();
			return tempFile;
		} catch (IOException ex) {
			logger.error(
					"Could not create temp dir with prefix {} and suffix {}",
					new Object[] { prefix, suffix });
			throw ex;
		}
	}

	/**
	 * Loads a file into a string, using UTF-8 encoding
	 * 
	 * @param f
	 *            the file
	 * @return the string
	 * @throws IOException
	 */
	public static String loadFileToString(File f) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), Charset.forName("UTF-8")));
			String line;
			while ((line = r.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} finally {
			if (r != null) {
				r.close();
			}
		}
	}

	/**
	 * Loads a zip entry into a string, using UTF-8 encoding
	 * 
	 * @param f
	 *            the zip-file
	 * @param e
	 *            the entry-name
	 * @return the string
	 * @throws IOException
	 */
	public static String loadZipEntryToString(File f, String e)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader r = null;
		try {
			ZipFile zip = new ZipFile(f);
			r = new BufferedReader(new InputStreamReader(readEntryFromZip(zip,
					e), Charset.forName("UTF-8")));
			String line;
			while ((line = r.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} finally {
			if (r != null) {
				r.close();
			}
		}
	}

	/**
	 * Writes a string into a file, using UTF-8 encoding
	 * 
	 * @param s
	 *            the string
	 * @param f
	 *            the file
	 * @throws IOException
	 */
	public static void writeStringToFile(String s, File f) throws IOException {
		BufferedWriter w = null;
		try {
			w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					f), Charset.forName("UTF-8")));
			w.append(s);
		} finally {
			if (w != null) {
				w.close();
			}
		}
	}

	/**
	 * Utility method to write generic to files
	 * 
	 * @param is
	 * @param f
	 * @throws IOException
	 */
	public static void writeToFile(InputStream is, File f) throws IOException {
		copy(is, new FileOutputStream(f));
	}

	/**
	 * Writes a string into a file, using UTF-8 encoding
	 * 
	 * @param s
	 *            the string
	 * @param f
	 *            the zip-file
	 * @param e
	 *            the path of the entry
	 * @throws IOException
	 */
	public static void writeStringToZipEntry(String s, File f, String e)
			throws IOException {
		appendEntryToZip(f, e, new ByteArrayInputStream(s.getBytes("UTF-8")));
	}

	public static boolean isFileBinaryEqual(File first, File second)
			throws IOException {
		return sameContents(
				new BufferedInputStream(new FileInputStream(first)),
				new BufferedInputStream(new FileInputStream(second)));
	}

	public static boolean sameContents(InputStream a, InputStream b)
			throws IOException {
		boolean same = true;
		boolean finished = false;
		try {
			while (same && !finished) {
				int one = a.read();
				int another = b.read();
				if (one != another) {
					same = false;
				} else if (one == -1) {
					finished = true;
				}
			}
		} finally {
			try {
				if (a != null) {
					a.close();
				}
			} finally {
				if (b != null) {
					b.close();
				}
			}
		}
		return same;
	}

	/**
	 * Reads a text file and returns a string with it. Returns null if error
	 * 
	 * @param f
	 * @return
	 */
	public static String getText(File f) {
		BufferedReader reader = null;
		boolean error = false;
		StringBuilder s = new StringBuilder();
		String newline = System.getProperty("line.separator");
		try {
			reader = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = reader.readLine()) != null) {
				s.append(line);
				s.append(newline);
			}
		} catch (IOException e) {
			error = true;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("Error closing reader", e);
				}
			}
		}
		return error ? null : s.toString();
	}
}
