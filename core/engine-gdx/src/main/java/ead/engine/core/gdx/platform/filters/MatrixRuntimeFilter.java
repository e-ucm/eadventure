package ead.engine.core.gdx.platform.filters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.common.util.EAdMatrix;
import ead.engine.core.gdx.platform.GdxCanvas;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.platform.rendering.filters.RuntimeFilter;

public class MatrixRuntimeFilter implements
		RuntimeFilter<MatrixFilter, SpriteBatch> {

	@Override
	public void applyFilter(RuntimeDrawable<?, SpriteBatch> drawable,
			MatrixFilter filter, GenericCanvas<SpriteBatch> c) {
		EAdMatrix matrix = filter.getMatrix();
		GdxCanvas c2 = (GdxCanvas) c;
		float deltaX = filter.getOriginX() * drawable.getWidth();
		float deltaY = filter.getOriginY() * drawable.getHeight();
		Matrix4 m = c2.convertMatrix(matrix);

		c2.getNativeGraphicContext().setTransformMatrix(
				c2.getNativeGraphicContext().getTransformMatrix()
						.trn(deltaX, deltaY, 0).mul(m));

	}

}
