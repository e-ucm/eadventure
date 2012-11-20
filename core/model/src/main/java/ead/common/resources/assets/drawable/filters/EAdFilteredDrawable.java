package ead.common.resources.assets.drawable.filters;

import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.basics.EAdBasicDrawable;

/**
 * A drawable with a filter that changes it
 *
 */
public interface EAdFilteredDrawable extends EAdBasicDrawable {

	EAdDrawableFilter getFilter();

	EAdDrawable getDrawable();

}
