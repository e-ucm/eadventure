package ead.writer;

import com.google.inject.Inject;

import ead.tools.xml.XMLParser;

public class AdventureWriter {

	private XMLParser parser;

	@Inject
	public AdventureWriter(XMLParser parser) {
		this.parser = parser;
	}

}
