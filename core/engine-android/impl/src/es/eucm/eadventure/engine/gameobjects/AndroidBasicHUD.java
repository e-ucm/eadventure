package es.eucm.eadventure.engine.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.AndroidPlatformConfiguration;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.platform.EAdCanvas;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.extra.AndroidCanvas;

@Singleton
public class AndroidBasicHUD extends BasicHUDImpl {

	private Paint borderPaint;
	private Path clip;
	private Rect rect;
	private Paint textP;
	private Bitmap magGlass;
	private AndroidPlatformConfiguration platformConfiguration;

	@Inject
	public AndroidBasicHUD(MenuHUD menuHUD,
			GameObjectFactory gameObjectFactory, GameState gameState,
			GameObjectManager gameObjectManager, MouseState mouseState,
			ValueMap valueMap, StringHandler stringHandler, PlatformConfiguration platformConfiguration) {
		super(menuHUD, gameObjectFactory, gameState, gameObjectManager, mouseState,
				valueMap, stringHandler);
		this.platformConfiguration = (AndroidPlatformConfiguration) platformConfiguration;
		
		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(Color.WHITE);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(8);

		clip = new Path();
		//if (this.platformConfiguration.isFullscreen()) {
		magGlass = Bitmap.createBitmap(200,200,Bitmap.Config.ARGB_4444);
		
		clip.addCircle(100,100,100,Path.Direction.CCW);
			
		rect = new Rect(0, 0, 200, 200);
		
		textP = new Paint(Paint.ANTI_ALIAS_FLAG);
		textP.setColor(0xFFFFFFFF);
		textP.setShadowLayer(4f, 0, 0, Color.BLACK);
		textP.setTextSize(20);
		textP.setTypeface(Typeface.SANS_SERIF);
		textP.setTextAlign(Align.CENTER);

		/*} else {
			magGlass = Bitmap.createBitmap(
					(int) (this.platformConfiguration.getScale() * 200),
					(int) (this.platformConfiguration.getScale() * 200),
					Bitmap.Config.ARGB_4444);
			clip.addCircle(
					(float) (this.platformConfiguration.getScale() * 100),
					(float) (this.platformConfiguration.getScale() * 100),
					(float) (this.platformConfiguration.getScale()
							* this.platformConfiguration.getScale() * 100),
					Path.Direction.CCW);
			rect = new Rect(0, 0,
					(int) (this.platformConfiguration.getScale() * 200),
					(int) (this.platformConfiguration.getScale() * 200));
		}*/

		logger.info("New instance");
	}
	
	public void render( EAdCanvas<?> canvas ){
		Canvas graphicContext = (Canvas) canvas.getNativeGraphicContext();
		if (mouseState.getMouseX() != -1
				&& mouseState.getMouseY() != -1) {
			
			Canvas c = new Canvas(magGlass);
			c.clipPath(clip);			
			
			c.drawBitmap(((AndroidCanvas) graphicContext).getBitmap(), new Rect(mouseState.getMouseX() - 50, 
					mouseState.getMouseY() - 50, mouseState.getMouseX() + 50, 
					mouseState.getMouseY() + 50), rect, null);

			c.drawPath(clip, borderPaint);
			c.drawCircle(100, 100, 3, borderPaint);

			int x = mouseState.getMouseX() - 100;
			int y = mouseState.getMouseY() - 100;

			/*
			 * if (x - TextRenderer.paint.getFontMetricsInt().ascent - (int)
			 * (150*platformConfiguration.getScaleW()) <= 0) x =
			 * TextRenderer.paint.getFontMetricsInt().ascent + (int)
			 * (150*platformConfiguration.getScaleW());
			 */
			if (x + 200 >= this.platformConfiguration.getVirtualWidth())
				x = this.platformConfiguration.getVirtualWidth() - 200;
			else if (x <= 0)
				x = 0;
			if (y + 200 >= this.platformConfiguration.getVirtualHeight())
				y = this.platformConfiguration.getVirtualHeight() - 200;
			else if (y <= 0)
				y = 0;
			/*
			 * if (y - TextRenderer.paint.getFontMetricsInt().ascent - (int)
			 * (150*platformConfiguration.getScale()) <= 0) y =
			 * TextRenderer.paint.getFontMetricsInt().ascent + (int)
			 * (150*platformConfiguration.getScale());
			 */

			graphicContext.save();
			
			graphicContext.setMatrix(null);
			
			graphicContext.drawBitmap(magGlass, x, y, null);
			graphicContext.drawText("PRUEBAS", mouseState.getMouseX() - 110, mouseState.getMouseY() - 160, textP);

			graphicContext.restore();
		
			/*
			 * TODO draw element name if (mouseState.getGameObjectUnderMouse()
			 * != null && mouseState.getGameObjectUnderMouse() instanceof Named)
			 * graphicRendererFactory.render(g, new TextGOImpl(((Named)
			 * mouseState.getGameObjectUnderMouse()).getName(), x +
			 * (int)(this.platformConfiguration.getScaleW()*100), y -
			 * (int)(this.platformConfiguration.getScale()), null, null), 0.0f);
			 */

		}
	}

}
