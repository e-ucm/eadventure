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
import es.eucm.eadventure.engine.core.platform.FontHandler;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNBezierShape;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineCaption;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineSpriteImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNSound;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeComposedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeDisplacedDrawable;

@Singleton
public class PlayNAssetHandler extends AbstractAssetHandler {

	private EAdEngine engine;

	private FontHandler fontHandler;

	private VariableMap valueMap;

	private StringHandler stringHandler;

	private Logger logger = Logger.getLogger("PlayNAssetHandler");

	@Inject
	public PlayNAssetHandler(
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> classMap,
			FontHandler fontCache, VariableMap valueMap,
			StringHandler stringHandler) {
		super(classMap, fontCache);
		this.fontHandler = fontCache;
		this.valueMap = valueMap;
		this.stringHandler = stringHandler;
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
		return uri.replaceAll("@", "eadengine/");
	}

	@Override
	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {
		if (clazz == PlayNEngineImage.class)
			return new PlayNEngineImage(this, engine);
		if (clazz == PlayNBezierShape.class)
			return new PlayNBezierShape(engine);
		if (clazz == PlayNEngineCaption.class)
			return new PlayNEngineCaption(fontHandler, valueMap, stringHandler,
					this);
		if (clazz == PlayNEngineSpriteImage.class)
			return new PlayNEngineSpriteImage(this);
		if (clazz == PlayNEngineSpriteImage.class)
			return new PlayNEngineSpriteImage(this);
		if (clazz == PlayNSound.class)
			return new PlayNSound(this);
		if ( clazz == RuntimeComposedDrawable.class )
			return new RuntimeComposedDrawable( this );
		if ( clazz == RuntimeDisplacedDrawable.class )
			return new RuntimeDisplacedDrawable( this );

		logger.log(Level.SEVERE, "No instance for runtime asset: " + clazz);
		return null;
	}

}
