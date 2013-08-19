package es.eucm.ead.tools;

public interface TextFileWriter {

	/**
	 * Writes the text in a file with the given name
	 * @param text
	 * @param fileName
	 */
	boolean write(String text, String fileName);
}
