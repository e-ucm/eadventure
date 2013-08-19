package es.eucm.ead.tools.java;

import es.eucm.ead.tools.TextFileReader;

import java.io.*;

public class JavaTextFileReader implements TextFileReader {
	@Override
	public String read(String fileName) {
        String text = "";
		File f = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(f));
			String line;
            while ((line = reader.readLine())!= null){
                text += line + System.getProperty("line.separator");
            }
		} catch (IOException e) {
            return null;
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
