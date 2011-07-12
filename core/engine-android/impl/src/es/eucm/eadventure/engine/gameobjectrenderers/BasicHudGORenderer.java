/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.eadventure.engine.gameobjectrenderers;

import java.util.logging.Logger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.AndroidPlatformConfiguration;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.extra.AndroidCanvas;

@Singleton
public class BasicHudGORenderer implements GameObjectRenderer<Canvas, BasicHUD<?>> {

	private MouseState mouseState;
	
	private Path clip;
	
	private Bitmap magGlass;
	
	private Paint borderPaint;
	
	private Rect rect;
	
	private AndroidPlatformConfiguration platformConfiguration;
	
	private static final Logger logger = Logger.getLogger("BasicHudGORenderer");
	
	@Inject
	public BasicHudGORenderer(PlatformConfiguration platformConfiguration,
			MouseState mouseState) {
		this.mouseState = mouseState;
		this.platformConfiguration = (AndroidPlatformConfiguration) platformConfiguration;
		
		borderPaint = new Paint();
		borderPaint.setColor(Color.WHITE);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth((int)(this.platformConfiguration.getScale()*8));
		
		clip = new Path();
		if (this.platformConfiguration.isFullscreen()){
			magGlass = Bitmap.createBitmap((int) (this.platformConfiguration.getScaleW()*200), (int) (this.platformConfiguration.getScale()*200), Bitmap.Config.ARGB_8888);
			clip.addCircle((float)(this.platformConfiguration.getScaleW()*100), (float)(this.platformConfiguration.getScale()*100), (float)(this.platformConfiguration.getScaleW()*this.platformConfiguration.getScale()*100), Path.Direction.CCW);
			rect = new Rect(0,0,(int)(this.platformConfiguration.getScaleW()*200),(int)(this.platformConfiguration.getScale()*200));
		}
		else {
			magGlass = Bitmap.createBitmap((int) (this.platformConfiguration.getScale()*200), (int) (this.platformConfiguration.getScale()*200), Bitmap.Config.ARGB_8888);
			clip.addCircle((float)(this.platformConfiguration.getScale()*100), (float)(this.platformConfiguration.getScale()*100), (float)(this.platformConfiguration.getScale()*this.platformConfiguration.getScale()*100), Path.Direction.CCW);
			rect = new Rect(0,0,(int)(this.platformConfiguration.getScale()*200),(int)(this.platformConfiguration.getScale()*200));
		}
				
		logger.info("New instance");
	}
	@Override
	public boolean contains(BasicHUD<?> object, int virutalX,
			int virtualY) {
		return false;
	}

	@Override
	public void render(Canvas graphicContext, BasicHUD<?> object,
			float interpolation, int offsetX, int offsetY) {
		if (mouseState.getVirtualMouseX() != -1 && mouseState.getVirtualMouseY() != -1) {			
			Canvas c = new Canvas(magGlass);
			c.clipPath(clip);
	
			c.drawBitmap(((AndroidCanvas) graphicContext).getBitmap(),
					new Rect(mouseState.getRawX() - (int)(this.platformConfiguration.getScaleW()*20), mouseState.getRawY() - (int)(this.platformConfiguration.getScale()*20),
					mouseState.getRawX() + (int)(this.platformConfiguration.getScaleW()*20), mouseState.getRawY() + (int)(this.platformConfiguration.getScale()*20)),
					rect, null);
			
			c.drawPath(clip, borderPaint);
			c.drawCircle((float)(this.platformConfiguration.getScaleW()*100), (float)(this.platformConfiguration.getScale()*100), 3, borderPaint);
			
			int x = mouseState.getRawX() - (int)(this.platformConfiguration.getScaleW()*100);
			int y = mouseState.getRawY() - (int)(this.platformConfiguration.getScale()*100);
			
			/*if (x - TextRenderer.paint.getFontMetricsInt().ascent - (int) (150*platformConfiguration.getScaleW()) <= 0)
				x = TextRenderer.paint.getFontMetricsInt().ascent + (int) (150*platformConfiguration.getScaleW());*/
			if (x + (int) (100*this.platformConfiguration.getScaleW()) >= this.platformConfiguration.getWidth())
				x = this.platformConfiguration.getWidth() - (int) (100*this.platformConfiguration.getScaleW());
			/*if (y - TextRenderer.paint.getFontMetricsInt().ascent - (int) (150*platformConfiguration.getScale()) <= 0)
				y = TextRenderer.paint.getFontMetricsInt().ascent + (int) (150*platformConfiguration.getScale());*/
			if (y + (int) (100*this.platformConfiguration.getScale()) >= this.platformConfiguration.getHeight())
				y = this.platformConfiguration.getHeight() - (int) (100*this.platformConfiguration.getScale());	
	
			
			graphicContext.save();
			graphicContext.setMatrix(null);
			
			graphicContext.drawBitmap(magGlass, x, y, null);
            
			graphicContext.restore();
            
            /* TODO draw element name
            if (mouseState.getGameObjectUnderMouse() != null && mouseState.getGameObjectUnderMouse() instanceof Named)
    			graphicRendererFactory.render(g, new TextGOImpl(((Named) mouseState.getGameObjectUnderMouse()).getName(),
    					x + (int)(this.platformConfiguration.getScaleW()*100), y - (int)(this.platformConfiguration.getScale()), null, null), 0.0f);
              */        
    		
		}
	}

	@Override
	public void render(Canvas graphicContext, BasicHUD<?> object,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		// Do nothing
		
	}
	

}
