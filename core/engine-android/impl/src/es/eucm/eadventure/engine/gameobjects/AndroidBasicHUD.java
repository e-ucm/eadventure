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

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.AndroidPlatformConfiguration;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.EAdCanvas;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.extra.BitmapCanvas;

@Singleton
public class AndroidBasicHUD extends BasicHUDImpl {

	private Paint borderPaint;
	private Path clip;
	private Rect rect;
	private Paint textPaint;
	private Bitmap magGlass;
	private AndroidPlatformConfiguration platformConfiguration;

	@Inject
	public AndroidBasicHUD(GUI gui, MenuHUD menuHUD,
			SceneElementGOFactory gameObjectFactory, GameState gameState,
			GameObjectManager gameObjectManager, MouseState mouseState,
			StringHandler stringHandler,
			PlatformConfiguration platformConfiguration,
			AssetHandler assetHandler) {
		super(menuHUD, gameObjectFactory, gameState, mouseState, stringHandler,
				gui, assetHandler);
		this.platformConfiguration = (AndroidPlatformConfiguration) platformConfiguration;

		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(Color.WHITE);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(8);

		clip = new Path();
		magGlass = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_4444);

		clip.addCircle(100, 100, 100, Path.Direction.CCW);

		rect = new Rect(0, 0, 200, 200);

		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(0xFFFFFFFF);
		textPaint.setShadowLayer(4f, 0, 0, Color.BLACK);
		textPaint.setTextSize(25);
		textPaint.setTypeface(Typeface.SANS_SERIF);
		textPaint.setTextAlign(Align.CENTER);

		logger.info("New instance");
	}

	@SuppressWarnings("unchecked")
	public void render(EAdCanvas<?> canvas) {
		if (mouseState.isMousePressed()) {

			BitmapCanvas graphicContext = (BitmapCanvas) canvas
					.getNativeGraphicContext();
			if (mouseState.getMouseX() != -1 && mouseState.getMouseY() != -1) {

				Canvas c = new Canvas(magGlass);
				c.clipPath(clip);

				c.drawBitmap(
						((BitmapCanvas) graphicContext).getBitmap(),
						new Rect(mouseState.getMouseX() - 50, mouseState
								.getMouseY() - 100,
								mouseState.getMouseX() + 50, mouseState
										.getMouseY()), rect, null);

				c.drawPath(clip, borderPaint);
				c.drawCircle(100, 100, 3, borderPaint);

				int magX, magY, textX, textY;
				magX = mouseState.getMouseX() - 100;
				magY = mouseState.getMouseY() - 150;

				if (magX + 100 >= this.platformConfiguration.getVirtualWidth()) {
					magX = this.platformConfiguration.getVirtualWidth() - 100;
				} else if (magX <= -100) {
					magX = -100;
				}
				if (magY + 100 >= this.platformConfiguration.getVirtualHeight()) {
					magY = this.platformConfiguration.getVirtualHeight() - 100;
				} else if (magY <= -100) {
					magY = -100;
				}

				textX = magX;
				textY = magY - 10;

				graphicContext.save();
				graphicContext.setMatrix(null);
				graphicContext.drawBitmap(magGlass, magX, magY, null);

				GameObject<? extends EAdElement> go = (GameObject<? extends EAdElement>) mouseState
						.getGameObjectUnderMouse();

				if (go != null && go.getElement() instanceof EAdElement) {
					EAdString name = gameState.getValueMap().getValue(
							(EAdElement) go.getElement(),
							EAdBasicSceneElement.VAR_NAME);
					graphicContext.drawText("Nombre GO", textX, textY,
							textPaint);
					if (name != null) {
						graphicContext.drawText(name.toString(), textX, textY,
								textPaint);
					}
				}

				graphicContext.restore();

			}
		}
	}

}
