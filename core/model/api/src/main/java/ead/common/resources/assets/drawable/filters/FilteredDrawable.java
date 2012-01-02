package ead.common.resources.assets.drawable.filters;

import ead.common.resources.assets.drawable.Drawable;
import ead.common.resources.assets.drawable.basics.BasicDrawable;

/**
 * A drawable with a filter that changes it
 *
 */
public interface FilteredDrawable extends BasicDrawable {
	
	DrawableFilter getFilter();
	
	Drawable getDrawable();

}
