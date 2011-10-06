package es.eucm.eadventure.common.impl.strings;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import es.eucm.eadventure.common.params.EAdString;

public class StringXMLHandler extends DefaultHandler {
	
	private Map<EAdString, String> strings;
	private String key;
	private StringBuffer text;
	
	public StringXMLHandler(){
		strings = new HashMap<EAdString,String>();
	}
	
	public Map<EAdString, String> getStrings(){
		return strings;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("string")){
			key = attributes.getValue("name");
			text = new StringBuffer();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		text.append( new String( ch, start, length ) );
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ( qName.equals("string")){
			String value = text.toString();
			EAdString string = EAdString.newEAdString(key);
			string.parse("key");
			strings.put(string, value);
		}
	}

}
