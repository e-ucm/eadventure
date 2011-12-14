package es.eucm.eadventure.common.resources.assets.drawable.filters.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.filters.DrawableFilter;
import es.eucm.eadventure.common.resources.assets.drawable.filters.FilteredDrawable;

public class FilteredDrawableImpl implements FilteredDrawable {
	
	@Param("drawable")
	private Drawable drawable;
	
	@Param("filter")
	private DrawableFilter filter;
	
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

}
