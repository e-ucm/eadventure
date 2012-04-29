package ead.common.test.readwrite;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.reader.EAdAdventureDOMModelReader;
import ead.common.reader.extra.ObjectFactory;
import ead.common.reader.visitors.NodeVisitor;
import ead.common.writer.DOMWriter;
import ead.common.writer.EAdAdventureModelWriter;

public class ReadWriteTest {
	protected File resultFile;

	protected EAdAdventureModelWriter writer;

	protected EAdAdventureDOMModelReader reader;

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
	
	public EAdAdventureModel writeAndRead(EAdAdventureModel model) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(resultFile);
			assertTrue(writer.write(model, out));
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
			EAdAdventureModel model2 = reader.read(in);
			assertTrue(model2 != null);
			assertTrue("Node visitor had errors.", !NodeVisitor.getError());
			assertTrue("Object Factory had errors.", !ObjectFactory.getError());
			return model2;
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
		return null;
	}
}
