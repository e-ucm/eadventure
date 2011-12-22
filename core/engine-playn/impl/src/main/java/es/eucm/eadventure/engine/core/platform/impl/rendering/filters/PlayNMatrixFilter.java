package es.eucm.eadventure.engine.core.platform.impl.rendering.filters;

import playn.core.Canvas;
import es.eucm.eadventure.common.resources.assets.drawable.filters.MatrixFilter;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.rendering.GenericCanvas;
import es.eucm.eadventure.engine.core.platform.rendering.filters.RuntimeFilter;

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
