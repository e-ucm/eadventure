package ead.reader.java;

import com.google.inject.AbstractModule;

import ead.reader.StringFileHandler;

public class ReaderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StringFileHandler.class).to(DefaultStringFileHandler.class);
	}

}
