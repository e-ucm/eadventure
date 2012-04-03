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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.utils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfreire
 */
public class FileUtils {
	private static final Logger logger = LoggerFactory.getLogger("FileUtils");

	/** All zip files start with these bytes */
	private static int[] zipMagic = new int[] {0x50, 0x4b};
	
	/** Size of buffer for stream operations */
	private static final int BUFFER_SIZE = 1024;
	
	private static InputStream readEntryFromZip(File zipFile, String entryName) throws IOException {
		ZipFile zip = new ZipFile(zipFile);
		return zip.getInputStream(zip.getEntry(entryName));
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
	public static void appendEntryToZip(File zipFile, String entryName, InputStream is) throws IOException {
		boolean errors = false;
		byte[] data = new byte[BUFFER_SIZE];
		ZipOutputStream out = null;
		File tempFile = File.createTempFile("ead-copy-zip", null);
		try {
			ZipFile source = new ZipFile(zipFile);
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
				if ( ! ze.isDirectory()) {
					copy(source.getInputStream(ze), out);
				}
				out.closeEntry();
			}			
			
			// now, append the file
			ZipEntry ze = new ZipEntry(entryName);			
			out.putNextEntry(ze);
			copy(is, out);
			out.closeEntry();
		} catch (Exception e) {
			logger.error("Error outputting zip to {}", zipFile, e);
			errors = true;
		} finally {
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
		if ( ! errors) {
			// try to switch original source with temp target
			zipFile.delete();
			if (! tempFile.renameTo(zipFile)) {
				logger.error("Could not replace input stream");
				errors = true;
			}
		}	
		
		if (errors) {
			throw new IOException("Could not write '"
					+ entryName + "' into '"
					+ zipFile + "'; see log for details");
		}
	}	

    /**
	 * Pump from input to output.
	 * @param input
	 * @param output
	 * @throws IOException 
	 */
    public static void copy(InputStream input, OutputStream output) throws IOException {
        int len;
        byte [] b = new byte[BUFFER_SIZE];
        while ((len = input.read(b)) != -1) {
            output.write(b, 0, len);
        }
    }

    public static void main(String[] args) throws Exception {
        // read war.zip and write to append.zip
        ZipFile war = new ZipFile("war.zip");
        ZipOutputStream append = new ZipOutputStream(new FileOutputStream("append.zip"));

        // first, copy contents from existing war
        Enumeration<? extends ZipEntry> entries = war.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e = entries.nextElement();
            System.out.println("copy: " + e.getName());
            append.putNextEntry(e);
            if (!e.isDirectory()) {
                copy(war.getInputStream(e), append);
            }
            append.closeEntry();
        }

        // now append some extra content
        ZipEntry e = new ZipEntry("answer.txt");
        System.out.println("append: " + e.getName());
        append.putNextEntry(e);
        append.write("42\n".getBytes());
        append.closeEntry();

        // close
        war.close();
        append.close();
    }	
	
	/**
	 * Uncompresses a zip file into a directory
	 */
    public static void expand(File source, File destDir) throws IOException {

        if ( ! FileUtils.startMatches(source, zipMagic)) {
            throw new IOException("File is not a zip archive");
        }        

        ZipFile zf = new ZipFile(source);
        byte [] b = new byte[BUFFER_SIZE];
        
        try {
            logger.debug("Extracting zip: "+source.getName());
            Enumeration entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = (ZipEntry)entries.nextElement();

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
                if ( ! outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(outFile);

                InputStream zis = zf.getInputStream(e);
                int len = 0;
                while ((len=zis.read(b))!= -1) fos.write(b,0,len);
                fos.close();
                zis.close();
            }                        
        }
        finally {
            zf.close();
        }
    }
	
    /**
     * Canonicalizes a path, transforming windows '\' to unix '/', 
     * stripping off any './' or '../' occurrences, and trimming 
     * start and end whitespace
     */
    public static String toCanonicalPath(String name) {
        return name
			.replaceAll("\\\\", "/")
			.replaceAll("(\\.)+/", "")
		    .trim();
    }	

    /**
     * Check the magic in a file
     */
    public static boolean startMatches(File f, int[] magic) throws IOException {
        FileInputStream in = null;
        try {
            in =  new FileInputStream(f);
            return startMatches(in, magic);
        }
        finally {        
            try { if (in != null) in.close(); } catch(Exception e) {};
        }
    }       
	
	/**
	 * Check the first few bytes in a stream against a pattern.
	 * @param is source to check
	 * @param magic to check it against
	 * @return true if first bytes match magic
	 * @throws IOException 
	 */
    public static boolean startMatches(InputStream is, int[] magic) throws IOException {
        for (int i=0; i<magic.length; i++) {
            int r = is.read();
            if (r == -1 || r != magic[i]) return false;                
        }
        return true;
    }	
}
