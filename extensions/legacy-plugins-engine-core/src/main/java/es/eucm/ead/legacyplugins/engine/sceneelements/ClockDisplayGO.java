package es.eucm.ead.legacyplugins.engine.sceneelements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.fonts.RuntimeFont;
import es.eucm.ead.engine.canvas.GdxCanvas;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.legacyplugins.model.elements.ClockDisplay;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.params.paint.EAdPaint;

public class ClockDisplayGO extends SceneElementGO {

	private EAdPaint paint;

	private RuntimeFont font;

	private int hours;

	private int minutes;

	private int seconds;

	@Inject
	public ClockDisplayGO(AssetHandler assetHandler,
			SceneElementFactory sceneElementFactory, Game game,
			EventFactory eventFactory) {
		super(assetHandler, sceneElementFactory, game, eventFactory);
	}

	@Override
	public void setElement(EAdSceneElement element) {
		super.setElement(element);
		if (element instanceof ClockDisplay) {
			paint = (((ClockDisplay) element).getColor());
			font = assetHandler.getFont(((ClockDisplay) element).getFont());
		}
	}

	@Override
	public void drawChildren(SpriteBatch batch, float parentAlpha) {
		super.drawChildren(batch, parentAlpha);
		GdxCanvas c = (GdxCanvas) batch;
		String time = "";
		time += (hours < 10 ? "0" : "") + hours + ":";
		time += (minutes < 10 ? "0" : "") + minutes + ":";
		time += (seconds < 10 ? "0" : "") + seconds;
		c.drawText(time, 0, 0, font, paint);
	}

	public void setTime(int hours, int minutes, int seconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
}
