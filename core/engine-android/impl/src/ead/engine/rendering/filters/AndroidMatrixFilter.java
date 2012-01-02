package ead.engine.rendering.filters;

import android.graphics.Canvas;
import android.graphics.Matrix;
import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.engine.core.platform.DrawableAsset;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.platform.rendering.filters.RuntimeFilter;
import ead.engine.rendering.AndroidCanvas;

public class AndroidMatrixFilter implements RuntimeFilter<MatrixFilter, Canvas> {

	@Override
	public void applyFilter(DrawableAsset<?, Canvas> drawable, MatrixFilter filter, GenericCanvas<Canvas> c) {
		Canvas g = c.getNativeGraphicContext();
		float deltaX = filter.getOriginX() * drawable.getWidth();
		float deltaY = filter.getOriginY() * drawable.getHeight();
		g.translate(deltaX, deltaY);
		Matrix m = ((AndroidCanvas) c).getMatrix( filter.getMatrix());
		m.postConcat(g.getMatrix());
		g.setMatrix(m);
	}

}
