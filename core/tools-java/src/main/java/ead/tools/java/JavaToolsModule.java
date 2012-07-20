package ead.tools.java;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import ead.tools.GenericInjector;
import ead.tools.ReflectionProvider;
import ead.tools.StringHandler;
import ead.tools.StringHandlerImpl;

public class JavaToolsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StringHandler.class).to(StringHandlerImpl.class).in(
				Singleton.class);
		bind(ReflectionProvider.class).to(JavaReflectionProvider.class).in(
				Singleton.class);
		bind(GenericInjector.class).to(JavaInjector.class).in(Singleton.class);
	}

}
