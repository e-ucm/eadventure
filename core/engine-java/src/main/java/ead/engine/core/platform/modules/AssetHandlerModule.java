package ead.engine.core.platform.modules;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.EAdCaption;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.resources.assets.drawable.compounds.EAdComposedDrawable;
import ead.common.resources.assets.drawable.compounds.EAdStateDrawable;
import ead.common.resources.assets.drawable.compounds.StateDrawable;
import ead.common.resources.assets.drawable.filters.EAdFilteredDrawable;
import ead.common.resources.assets.drawable.filters.FilteredDrawable;
import ead.common.util.StringHandler;
import ead.engine.core.platform.StringHandlerImpl;
import ead.engine.core.platform.assets.RuntimeAsset;
import ead.engine.core.platform.assets.drawables.basics.RuntimeCaption;
import ead.engine.core.platform.assets.drawables.basics.RuntimeFramesAnimation;
import ead.engine.core.platform.assets.drawables.compunds.RuntimeComposedDrawable;
import ead.engine.core.platform.assets.drawables.compunds.RuntimeFilteredDrawable;
import ead.engine.core.platform.assets.drawables.compunds.RuntimeStateDrawable;

public abstract class AssetHandlerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StringHandler.class).to(StringHandlerImpl.class);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> provideMap(){
		Map map = new HashMap( );
		map.put(EAdCaption.class, RuntimeCaption.class);
		map.put(Caption.class, RuntimeCaption.class);
		map.put(EAdComposedDrawable.class, RuntimeComposedDrawable.class);
		map.put(ComposedDrawable.class, RuntimeComposedDrawable.class);
		map.put(StateDrawable.class, RuntimeStateDrawable.class);
		map.put(EAdStateDrawable.class, RuntimeStateDrawable.class);
		map.put(FramesAnimation.class, RuntimeFramesAnimation.class);
		map.put(EAdFilteredDrawable.class, RuntimeFilteredDrawable.class);
		map.put(FilteredDrawable.class, RuntimeFilteredDrawable.class);
		return (Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>>)map;
	}

}
