package ead.common.resources.assets.drawable.filters;

import ead.common.interfaces.Param;
import ead.common.resources.assets.drawable.Drawable;
import ead.common.resources.assets.drawable.filters.DrawableFilter;
import ead.common.resources.assets.drawable.filters.FilteredDrawable;

public class FilteredDrawableImpl implements FilteredDrawable {
	
	@Param("drawable")
	private Drawable drawable;
	
	@Param("filter")
	private DrawableFilter filter;

	public FilteredDrawableImpl( ){
	}

	public FilteredDrawableImpl( Drawable drawable, DrawableFilter filter ){
		this.drawable = drawable;
		this.filter = filter;
	}

	@Override
	public DrawableFilter getFilter() {
		return filter;
	}

	@Override
	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public void setFilter(DrawableFilter filter) {
		this.filter = filter;
	}

	
}
