package es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public abstract class GameObjectRendererImpl<T extends GameObject<?>>
		implements GameObjectRenderer<Graphics2D, T> {
	
	public Graphics2D prepareGraphics( Graphics2D g, EAdTransformation t ){
		Graphics2D g2 = (Graphics2D) g.create();
		float m[] = t.getMatrix().getFlatMatrix();
		AffineTransform at = new AffineTransform(m[0], m[1], m[3], m[4], m[6], m[7]);
		g2.setTransform(at);
		
		AlphaComposite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, t.getAlpha());
		g2.setComposite(c);
		
		return g2;
	}

}
