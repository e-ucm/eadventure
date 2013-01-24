package ead.common.model.assets.drawable.filters;

import ead.common.interfaces.Param;
import ead.common.model.assets.AbstractAssetDescriptor;
import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.filters.EAdDrawableFilter;
import ead.common.model.assets.drawable.filters.EAdFilteredDrawable;

public class FilteredDrawable extends AbstractAssetDescriptor implements
		EAdFilteredDrawable {

	@Param
	private EAdDrawable drawable;

	@Param
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
