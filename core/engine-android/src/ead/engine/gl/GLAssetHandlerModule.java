package ead.engine.gl;

import java.util.Map;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.multimedia.EAdSound;
import ead.common.resources.assets.multimedia.EAdVideo;
import ead.common.resources.assets.multimedia.Sound;
import ead.common.resources.assets.text.BasicFont;
import ead.common.resources.assets.text.EAdFont;
import ead.engine.core.platform.assets.AndroidFont;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeAsset;
import ead.engine.core.platform.assets.SpecialAssetRenderer;
import ead.engine.core.platform.assets.multimedia.AndroidSound;
import ead.engine.core.platform.assets.specialassetrenderers.AndroidVideoRenderer;
import ead.engine.core.platform.modules.AssetHandlerModule;

public class GLAssetHandlerModule extends AssetHandlerModule {
	@Override
	protected void configure() {
		super.configure();
		bind(AssetHandler.class).to(GLAssetHandler.class);
		// Bind to AndroidVideoRenderer (uses API mediaplayer) or
		// RockPlayerAndroidVideoRenderer
		bind(new TypeLiteral<SpecialAssetRenderer<EAdVideo, ?>>() {
		}).to(AndroidVideoRenderer.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Provides
	@Singleton
	public Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> provideMap() {
		Map map = super.provideMap();
		map.put(Image.class, GLImage.class);
		map.put(RectangleShape.class, GLBezierShape.class);
		map.put(BezierShape.class, GLBezierShape.class);
		map.put(EAdSound.class, AndroidSound.class);
		map.put(Sound.class, AndroidSound.class);
		map.put(EAdFont.class, AndroidFont.class);
		map.put(BasicFont.class, AndroidFont.class);
		return (Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>>) map;
	}
}
