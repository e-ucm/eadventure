package ead.tools.gwt;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

import ead.tools.StringHandler;
import ead.tools.StringHandlerImpl;
import ead.tools.gwt.reflection.GwtReflectionClassLoader;
import ead.tools.gwt.reflection.GwtReflectionProvider;
import ead.tools.gwt.xml.GwtXMLParser;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionProvider;
import ead.tools.xml.XMLParser;

public class GWTToolsModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(StringHandler.class).to(StringHandlerImpl.class).in(
				Singleton.class);
		bind(ReflectionProvider.class).to(GwtReflectionProvider.class).in(
				Singleton.class);
		bind(XMLParser.class).to(GwtXMLParser.class).in(Singleton.class);
		bind(ReflectionClassLoader.class).to(GwtReflectionClassLoader.class)
				.in(Singleton.class);
	}

}
