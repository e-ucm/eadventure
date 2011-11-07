package es.eucm.eadventure.engine.core.platform;

public interface EAdInjector {
	
	<T> T getInstance( Class<T> clazz );

}
