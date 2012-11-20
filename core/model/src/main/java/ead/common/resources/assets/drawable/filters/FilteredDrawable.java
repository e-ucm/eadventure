package ead.common.resources.assets.drawable.filters;

import ead.common.interfaces.Param;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.filters.EAdDrawableFilter;
import ead.common.resources.assets.drawable.filters.EAdFilteredDrawable;

public class FilteredDrawable implements EAdFilteredDrawable {

	@Param("drawable")
	private EAdDrawable drawable;

	@Param("filter")
	private EAdDrawableFilter filter;

	public FilteredDrawable() {
	}

	public FilteredDrawable(EAdDrawable drawable, EAdDrawableFilter filter) {
		this.drawable = drawable;
		this.filter = filter;
	}

	@Override
	public EAdDrawableFilter getFilter() {
		return filter;
	}

	@Override
	public EAdDrawable getDrawable() {
		return drawable;
	}

	public void setDrawable(EAdDrawable drawable) {
		this.drawable = drawable;
	}

	public void setFilter(EAdDrawableFilter filter) {
		this.filter = filter;
	}

}
