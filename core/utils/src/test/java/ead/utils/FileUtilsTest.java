/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.utils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author mfreire
 */
public class FileUtilsTest {
	
	public FileUtilsTest() {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Info, null);
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	private static File getFile(String name) {
		File f = new File(ClassLoader.getSystemResource(name).getPath());
		assertTrue(f.exists());
		return f;
	}
	
	/**
	 * Test of readEntryFromZip method, of class FileUtils.
	 */
	@Test
	public void testReadEntryFromZip() throws Exception {
		System.out.println("readEntryFromZip");
		File zipFile = getFile("ead/utils/sample.zip");
		String entryName = "META-INF/MANIFEST.MF";
		InputStream result = FileUtils.readEntryFromZip(zipFile, entryName);
		BufferedReader r = new BufferedReader(new InputStreamReader(result));		
		assertEquals("Manifest-Version: 1.0", r.readLine());
	}

	/**
	 * Test of zipContainsEntry method, of class FileUtils.
	 */
	@Test
	public void testZipContainsEntry() throws Exception {
		System.out.println("zipContainsEntry");
		File zipFile = getFile("ead/utils/sample.zip");
		assertTrue(FileUtils.zipContainsEntry(zipFile, "META-INF/MANIFEST.MF"));
		assertFalse(FileUtils.zipContainsEntry(zipFile, "META-INF/Manifiesto.MF"));
	}

	/**
	 * Test of folderContainsEntry method, of class FileUtils.
	 */
	@Test
	public void testFolderContainsEntry() throws Exception {
		System.out.println("folderContainsEntry");
		File tmp = null;
		try {
			tmp = FileUtils.createTempDir("test", "test");
			FileUtils.expand(getFile("ead/utils/sample.zip"), tmp);
			File folder = new File(tmp, "META-INF");
			assertTrue(FileUtils.folderContainsEntry(folder, "MANIFEST.MF"));
			assertFalse(FileUtils.folderContainsEntry(folder, "Manifiesto.MF"));
		} finally {
			if (tmp != null) {
				FileUtils.deleteRecursive(tmp);
			}
		}
	}

	/**
	 * Test of appendEntryToZip method, of class FileUtils.
	 */
	@Test
	public void testAppendEntryToZip() throws Exception {
		System.out.println("appendEntryToZip");
		File zipFile = getFile("ead/utils/sample.zip");
		File tmp = File.createTempFile("test", "test");
		String entryName = "META-INF/Manifiesto.MF";
		String testContents = "this is a simple test";
		try {
			FileUtils.copyRecursive(zipFile, null, tmp);
			assertFalse(FileUtils.zipContainsEntry(tmp, entryName));
			FileUtils.appendEntryToZip(tmp, entryName,
					new ByteArrayInputStream(testContents.getBytes()));
			assertTrue(FileUtils.zipContainsEntry(tmp, entryName));
			
			InputStream result = FileUtils.readEntryFromZip(tmp, entryName);
			BufferedReader r = new BufferedReader(new InputStreamReader(result));		
			assertEquals(testContents, r.readLine());
		} finally {
			if (tmp != null) {
				FileUtils.deleteRecursive(tmp);
			}
		}	
	}

	/**
	 * Test of copy method, of class FileUtils.
	 */
	@Test
	public void testCopy() throws Exception {
		System.out.println("copy");
		String testString = "this is a test";
		InputStream input = new ByteArrayInputStream(testString.getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		FileUtils.copy(input, output);
		assertEquals(testString, new String(output.toByteArray()));
	}

	/**
	 * Test of expand method, of class FileUtils.
	 */
	@Test
	public void testExpand() throws Exception {
		System.out.println("expand");
		File source = getFile("ead/utils/sample.zip");
		File tmp = null;
		try {
			tmp = FileUtils.createTempDir("test", "test");
			FileUtils.expand(source, tmp);
			assertEquals(41, countFiles(tmp));
		} finally {
			if (tmp != null) {
				FileUtils.deleteRecursive(tmp);
			}
		}
	}

	public static int countFiles(File f) {
		if (f.isFile()) {
			return 1;
		} else {
			int rc = 0;
			for (File o : f.listFiles()) {
				rc += countFiles(o);
			}
			return rc;
		}
	}
	
	/**
	 * Test of toCanonicalPath method, of class FileUtils.
	 */
	@Test
	public void testToCanonicalPath() {
		System.out.println("toCanonicalPath");
		assertEquals("blah/etc", FileUtils.toCanonicalPath("  ../../../blah/etc "));
		assertEquals("blah/etc", FileUtils.toCanonicalPath("  ..\\.\\..\\blah/etc "));
	}

	/**
	 * Test of startMatches method, of class FileUtils.
	 */
	@Test
	public void testStartMatches_File_intArr() throws Exception {
		System.out.println("startMatches");
		File f = null;
		int[] magic = FileUtils.zipMagic;
		assertTrue(FileUtils.startMatches(getFile("ead/utils/sample.zip"), magic));
		assertFalse(FileUtils.startMatches(getFile("ead/utils/sample.txt"), magic));
	}

	/**
	 * Test of loadFileToString method, of class FileUtils.
	 */
	@Test
	public void testLoadFileToString() throws Exception {
		System.out.println("loadFileToString");
		File zipFile = getFile("ead/utils/sample.zip");
		File tmp = FileUtils.createTempDir("test", "test");
		String entryName = "META-INF/MANIFEST.MF";
		try {
			FileUtils.expand(zipFile, tmp);
			String one = FileUtils.loadZipEntryToString(zipFile, entryName);
			String two = FileUtils.loadFileToString(new File(tmp, entryName));
			assertEquals(one, two);
		} finally {
			if (tmp != null) {
				FileUtils.deleteRecursive(tmp);
			}
		}	
	}

	/**
	 * Test of writeStringToFile method, of class FileUtils.
	 */
	@Test
	public void testWriteStringToFile() throws Exception {
		System.out.println("writeStringToFile");
		File src = getFile("ead/utils/sample.txt");
		File tmp = FileUtils.createTempDir("test", "test");
		try {
			String one = FileUtils.loadFileToString(src);
			File dest = new File(tmp, "test");
			FileUtils.writeStringToFile(one, dest);
			String two = FileUtils.loadFileToString(dest);			
			assertEquals(one, two);
		} finally {
			if (tmp != null) {
				FileUtils.deleteRecursive(tmp);
			}
		}	
	}

	/**
	 * Test of writeToFile method, of class FileUtils.
	 */
	@Test
	public void testWriteToFile() throws Exception {
		System.out.println("writeToFile");
		File zipFile = getFile("ead/utils/sample.zip");
		String path = "ead/utils/FileUtils.class";
		File tmp = FileUtils.createTempDir("test", "test");
		try {
			FileUtils.expand(zipFile, tmp);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			FileUtils.copy(FileUtils.readEntryFromZip(zipFile, path), bos);
			byte[] one = bos.toByteArray();
			File temp = new File(tmp, "temp");
			FileUtils.writeToFile(new ByteArrayInputStream(one), temp);
			bos = new ByteArrayOutputStream();
			FileUtils.copy(new FileInputStream(temp), bos);
			byte[] two = bos.toByteArray();
			assertArrayEquals(one, two);
		} finally {
			if (tmp != null) {
				FileUtils.deleteRecursive(tmp);
			}
		}	
	}

	/**
	 * Test of isFileBinaryEqual method, of class FileUtils.
	 */
	@Test
	public void testSameContents() throws Exception {
		System.out.println("sameContents");
		File zipFile = getFile("ead/utils/sample.zip");
		String path = "ead/utils/FileUtils.class";
		File tmp = FileUtils.createTempDir("test", "test");
		File original = new File(tmp, path);
		try {
			FileUtils.expand(zipFile, tmp);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			FileUtils.copy(new FileInputStream(original), bos);
			byte[] one = bos.toByteArray();
			byte[] two = new byte[one.length];
			System.arraycopy(one, 0, two, 0, one.length);
			
			assertTrue(FileUtils.sameContents(
					new ByteArrayInputStream(one), 
					new ByteArrayInputStream(two)));
			// change a byte
			two[two.length / 2] ++;
			assertFalse(FileUtils.sameContents(
					new ByteArrayInputStream(one), 
					new ByteArrayInputStream(two)));
		} finally {
			if (tmp != null) {
				FileUtils.deleteRecursive(tmp);
			}
		}	
	}
}
