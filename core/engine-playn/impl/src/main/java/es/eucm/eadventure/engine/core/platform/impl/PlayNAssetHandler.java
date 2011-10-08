package es.eucm.eadventure.engine.core.platform.impl;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.EAdEngine;
import es.eucm.eadventure.engine.core.impl.VariableMap;
import es.eucm.eadventure.engine.core.platform.FontCache;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.assets.impl.*;

@Singleton
public class PlayNAssetHandler extends AbstractAssetHandler {

	private EAdEngine engine;
	
	private FontCache fontCache;

	private VariableMap valueMap;

	private StringHandler stringHandler;

	private PlatformConfiguration platformConfiguration;
	
	private Logger logger = Logger.getLogger("PlayNAssetHandler");
	
	@Inject
	public PlayNAssetHandler(
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> classMap) {
		super(classMap);
	}
	
	public void setEngine(EAdEngine engine) {
		this.engine = engine;
	}

	@Override
	public void initilize() {
		// TODO Auto-generated method stub
		
		setLoaded(true);

	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAbsolutePath(String uri) {
		return uri.replaceAll("@", "");
	}

	@Override
	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {
		if (clazz == PlayNEngineImage.class)
			return new PlayNEngineImage(this, engine);
		if (clazz == PlayNBezierShape.class)
			return new PlayNBezierShape(engine);
		if (clazz == PlayNEngineCaption.class)
			return new PlayNEngineCaption(fontCache, valueMap, stringHandler, platformConfiguration);
		if (clazz == PlayNEngineSpriteImage.class)
			return new PlayNEngineSpriteImage(this);
		if (clazz == PlayNEngineSpriteImage.class)
			return new PlayNEngineSpriteImage(this);
		if (clazz == PlayNSound.class)
			return new PlayNSound(this);
		
		logger.log(Level.SEVERE, "No instance for runtime asset: " + clazz);
		return null;
	}

}
