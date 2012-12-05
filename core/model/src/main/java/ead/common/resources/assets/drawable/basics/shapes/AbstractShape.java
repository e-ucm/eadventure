package ead.common.resources.assets.drawable.basics.shapes;

import ead.common.interfaces.Param;
import ead.common.params.paint.EAdPaint;
import ead.common.resources.assets.AbstractAssetDescriptor;
import ead.common.resources.assets.drawable.basics.EAdShape;

public abstract class AbstractShape extends AbstractAssetDescriptor implements
		EAdShape {

	@Param("paint")
	private EAdPaint paint;

	public AbstractShape(EAdPaint paint) {
		this.paint = paint;
	}

	public AbstractShape() {

	}

	@Override
	public EAdPaint getPaint() {
		return paint;
	}

	@Override
	public void setPaint(EAdPaint paint) {
		this.paint = paint;
	}

	@Override
	public abstract Object clone();

	@Override
	public boolean equals(Object o) {
		if (!super.equals(o)) {
			return false;
		}
		EAdPaint oPaint = ((AbstractShape) o).getPaint();
		return (oPaint == null && paint == null) || oPaint.equals(paint);

	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + super.hashCode();
		hash = 79 * hash + (this.paint != null ? this.paint.hashCode() : 0);
		return hash;
	}
}
