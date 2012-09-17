package ead.engine.core.gdx.assets.drawables;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ead.common.params.fills.ColorFill;
import ead.common.resources.assets.drawable.basics.shapes.AbstractShape;
import ead.engine.core.platform.assets.drawables.basics.RuntimeBezierShape;
import ead.engine.core.platform.rendering.GenericCanvas;

public abstract class GdxShape<T extends AbstractShape> extends RuntimeBezierShape<T,SpriteBatch> {

	private TextureRegion textureRegion;
	private Pixmap pixmapContains;

	public GdxShape() {

	}

	public boolean loadAsset() {
		pixmapContains = generatePixmap();		
		textureRegion = new TextureRegion(new Texture(pixmapContains));
		textureRegion.flip(false, true);
		width = pixmapContains.getWidth();
		height = pixmapContains.getHeight();
		loaded = true;
		return true;
	}

	protected abstract Pixmap generatePixmap();

	@Override
	public boolean contains(int x, int y) {
		if (x > 0 && y > 0 && x < getWidth() && y < getHeight()) {
			int alpha = pixmapContains.getPixel(x, y) & 255;
			return alpha > 128;
		}
		return false;
	}

	@Override
	public void freeMemory() {
		if (pixmapContains != null) {
			this.pixmapContains.dispose();
		}
	}
	
	@Override
	public void render(GenericCanvas<SpriteBatch> c) {
		render(c.getNativeGraphicContext());
	}

	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, 0, 0);
	}
	
	protected static float vBx, vBy;

	protected static float vMod2;

	protected static float rI, gI, bI, aI;

	protected static float rE, gE, bE, aE;

	protected static float vcr, vcg, vcb, vca;

	protected void initGradientParams(ColorFill c1, float x0, float y0,
			ColorFill c2, float x1, float y1) {
		vBx = x1 - x0;
		vBy = y1 - y0;
		vMod2 = vBx * vBx + vBy * vBy;
		rI = c1.getRed() / 255.f;
		gI = c1.getGreen() / 255.f;
		bI = c1.getBlue() / 255.f;
		aI = c1.getAlpha() / 255.f;
		rE = c2.getRed() / 255.f;
		gE = c2.getGreen() / 255.f;
		bE = c2.getBlue() / 255.f;
		aE = c2.getAlpha() / 255.f;
		vcr = rE - rI;
		vcg = gE - gI;
		vcb = bE - bI;
		vca = aE - aI;
	}

	protected void setColor(Pixmap p, float x, float y) {
		float proj = (vBx * x + vBy * y) / vMod2;
		if (proj <= 0) {
			p.setColor(rI, gI, bI, aI);
		} else if (proj >= 1) {
			p.setColor(rE, gE, bE, aE);
		} else {
			p.setColor(rI + vcr * proj, gI + vcg * proj, bI + vcb * proj, aI
					+ vca * proj);
		}
	}


}
