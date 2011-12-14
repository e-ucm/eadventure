package es.eucm.eadventure.engine.core.platform.impl.rendering.filters;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import es.eucm.eadventure.common.resources.assets.drawable.filters.impl.MatrixFilter;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.impl.rendering.DesktopCanvas;
import es.eucm.eadventure.engine.core.platform.rendering.EAdCanvas;
import es.eucm.eadventure.engine.core.platform.rendering.filters.RuntimeFilter;

public class DesktopMatrixFilter implements RuntimeFilter<MatrixFilter, Graphics2D> {

	@Override
	public void applyFilter(DrawableAsset<?, Graphics2D> drawable, MatrixFilter filter, EAdCanvas<Graphics2D> c) {
		Graphics2D g = c.getNativeGraphicContext();
		float deltaX = filter.getOriginX() * drawable.getWidth();
		float deltaY = filter.getOriginY() * drawable.getHeight();
		g.translate(deltaX, deltaY);
		AffineTransform t = ((DesktopCanvas) c).getAffineTransform( filter.getMatrix());
		g.transform(t);
	}

}
