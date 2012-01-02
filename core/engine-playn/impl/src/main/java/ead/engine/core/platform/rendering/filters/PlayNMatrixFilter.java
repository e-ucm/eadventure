package ead.engine.core.platform.rendering.filters;

import playn.core.Canvas;
import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.engine.core.platform.DrawableAsset;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.platform.rendering.filters.RuntimeFilter;

public class PlayNMatrixFilter implements RuntimeFilter<MatrixFilter, Canvas> {

	@Override
	public void applyFilter(DrawableAsset<?, Canvas> drawable, MatrixFilter filter, GenericCanvas<Canvas> c) {
		Canvas g = c.getNativeGraphicContext();
		float deltaX = filter.getOriginX() * drawable.getWidth();
		float deltaY = filter.getOriginY() * drawable.getHeight();
		g.translate(deltaX, deltaY);
		
		float m[] = filter.getMatrix().getFlatMatrix();
		g.transform(m[0], m[1], m[3], m[4], m[6], m[7]);
	}

}
