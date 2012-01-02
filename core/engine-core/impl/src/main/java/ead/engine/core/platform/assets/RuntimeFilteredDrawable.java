package ead.engine.core.platform.assets;

import com.google.inject.Inject;

import ead.common.resources.assets.drawable.Drawable;
import ead.common.resources.assets.drawable.filters.FilteredDrawable;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.DrawableAsset;
import ead.engine.core.platform.rendering.GenericCanvas;

public class RuntimeFilteredDrawable<GraphicContext> extends AbstractRuntimeAsset<FilteredDrawable> implements DrawableAsset<FilteredDrawable, GraphicContext>{
	
	private AssetHandler assetHandler;
	
	private DrawableAsset<?, GraphicContext> drawable;
	
	@Inject
	public RuntimeFilteredDrawable( AssetHandler assetHandler ){
		this.assetHandler = assetHandler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean loadAsset() {
		drawable = (DrawableAsset<?, GraphicContext>) assetHandler.getRuntimeAsset(descriptor.getDrawable());
		if ( drawable != null ){
			return drawable.loadAsset();
		}
		return false;
	}

	@Override
	public void freeMemory() {
		if ( drawable != null )
			drawable.freeMemory();
	}

	@Override
	public boolean isLoaded() {
		return drawable != null && drawable.isLoaded();
	}

	@Override
	public void update() {
		if (drawable != null)
			drawable.update();
	}

	@Override
	public int getWidth() {
		// TODO filter could change width and height
		return drawable.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO filter could change width and height
		return drawable.getHeight();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Drawable> DrawableAsset<S, GraphicContext> getDrawable() {
		return (DrawableAsset<S, GraphicContext>) this;
	}

	@Override
	public void render(GenericCanvas<GraphicContext> c) {
		c.save();
		c.setFilter(drawable, descriptor.getFilter());
		drawable.render(c);
		c.restore();
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO filter could change this
		return drawable.contains(x, y);
	}

}
