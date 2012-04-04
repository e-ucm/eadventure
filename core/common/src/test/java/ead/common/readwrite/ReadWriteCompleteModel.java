package ead.common.readwrite;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ead.common.CompleteModel;
import ead.common.reader.EAdAdventureDOMModelReader;
import ead.common.reader.extra.ObjectFactory;
import ead.common.reader.visitors.NodeVisitor;
import ead.common.writer.DOMWriter;
import ead.common.writer.EAdAdventureModelWriter;

/**
 * Test for general writing/reading on a complete model (with all possibles
 * eAdventure elements). It doesn't test if the read model equals to the written
 * model
 * 
 * @author anserran
 * 
 */
public class ReadWriteCompleteModel {

	private File resultFile;

	private EAdAdventureModelWriter writer;

	private EAdAdventureDOMModelReader reader;

	@Before
	public void setUp() {
		try {
			resultFile = File.createTempFile("data", ".xml");
			resultFile.deleteOnExit();
			writer = new EAdAdventureModelWriter();
			reader = new EAdAdventureDOMModelReader();
		} catch (IOException e) {

		}
	}

	@Test
	public void testWrite() {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(resultFile);
			assertTrue(writer.write(new CompleteModel(), out));
			assertTrue("DOM Writer had errors.", !DOMWriter.getError());
		} catch (FileNotFoundException e) {
			fail("Temp file impossible to be created to write XML.");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

				}
			}
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(resultFile);
			assertTrue(reader.read(in) != null);
			assertTrue("Node visitor had errors.", !NodeVisitor.getError());
			assertTrue("Object Factory had errors.", !ObjectFactory.getError());
		} catch (FileNotFoundException e) {
			fail("Temp file impossible to be created to write XML.");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}
	}
}
