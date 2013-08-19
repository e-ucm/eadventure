package es.eucm.ead.tools.java;

import es.eucm.ead.tools.TextFileWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JavaTextFileWriter implements TextFileWriter {
	@Override
	public boolean write(String text, String fileName) {
		boolean done = true;
		File f = new File(fileName);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(f));
			writer.write(text);
		} catch (IOException e) {
			done = false;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					done = false;
				}
			}
		}
		return done;
	}
}
