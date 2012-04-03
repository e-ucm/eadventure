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
	
	private static InputStream readEntryFromZip(File zipFile, String entryName) throws IOException {
		ZipFile zip = new ZipFile(zipFile);
		return zip.getInputStream(zip.getEntry(entryName));
	}

	/**
	 * Writes an entry into a ZipStream. Can be called with any type of
	 * InputStream or entry name.
	 *
	 * @param zipFile
	 * @param entryName
	 * @param is
	 * @throws IOException
	 */
	private static void appendEntryToZip(File zipFile, String entryName, InputStream is) throws IOException {
		boolean errors = false;
		byte[] data = new byte[1024];
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(zipFile)));
			out.putNextEntry(new ZipEntry(entryName));
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
		if (errors) {
			throw new IOException("Could not write '"
					+ entryName + "' into '"
					+ zipFile + "'; see log for details");
		}
	}	
	
	/**
	 * Uncompresses a zip file into a directory
	 */
    public void expand(File source, File destDir) throws IOException {

        if ( ! FileUtils.startMatches(source, zipMagic)) {
            throw new IOException("File is not a zip archive");
        }        

        ZipFile zf = new ZipFile(source);
        byte [] b = new byte[512];
        
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
