package ead.common.model.assets.drawable.filters;

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.basics.EAdBasicDrawable;

/**
 * A drawable with a filter that changes it
 *
 */
public interface EAdFilteredDrawable extends EAdBasicDrawable {

	EAdDrawableFilter getFilter();

	EAdDrawable getDrawable();

}
