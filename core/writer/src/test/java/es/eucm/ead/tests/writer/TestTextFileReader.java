package es.eucm.ead.tests.writer;

import es.eucm.ead.tools.TextFileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestTextFileReader implements TextFileReader {
	@Override
	public String read(String file) {
		String text = "";
		InputStream is = ClassLoader.getSystemResourceAsStream(file
				.substring(1));
		BufferedReader reader = null;
		String lineSeparator = System.getProperty("line.separator");
		String line = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) {
				text += line + lineSeparator;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {

				}
			}
		}
		return text;
	}
}
