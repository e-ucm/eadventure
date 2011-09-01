package es.eucm.eadventure.common.impl.strings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.StringsReader;
import es.eucm.eadventure.common.resources.EAdString;

@Singleton
public class DefaultStringsReader implements StringsReader {

	protected Map<EAdString, String> strings;

	public DefaultStringsReader() {
		this(null);
	}

	/**
	 * Constructs a string reader
	 * 
	 * @param input
	 *            the file where the strings are stored
	 */
	public DefaultStringsReader(File input) {
		if (input == null)
			strings = new HashMap<EAdString, String>();
		else
			setFile(input);
	}

	public void setFile(File file) {
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);

			SAXParser saxParser = factory.newSAXParser();
			StringXMLHandler handler = new StringXMLHandler();
			saxParser.parse(file, handler);

			strings = handler.getStrings();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getString(EAdString string) {
		String value = strings.get(string.toString());
		return value == null ? string.toString() : value;
	}

}
