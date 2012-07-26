package ead.engine.utils.assetviewer;

import java.awt.Canvas;
import java.util.List;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.resources.assets.drawable.EAdDrawable;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;
import ead.engine.core.utils.assetviewer.AssetApplicationListener;

/**
 * Contains a canvas in which assets can be represented as they will be
 * represented in the game engine
 * 
 */
public class AssetViewer {

	/**
	 * Injector is shared by all the instances
	 */
	private static Injector injector;

	private LwjglAWTCanvas lwjglCanvas;

	private AssetApplicationListener app;

	private AssetHandler assetHandler;

	public AssetViewer() {
		this(null);
	}

	public AssetViewer(LwjglAWTCanvas canvas) {
		if (injector == null) {
			injector = Guice.createInjector(new AssetViewerModule());
			injector.getInstance(AssetHandler.class).setCacheEnabled(false);
		}

		assetHandler = injector.getInstance(AssetHandler.class);

		app = new AssetApplicationListener(
				injector.getInstance(FontHandler.class));

		if (canvas == null) {
			lwjglCanvas = new LwjglAWTCanvas(app, true);
		} else {
			lwjglCanvas = new LwjglAWTCanvas(app, true, canvas);
		}
		app.setGraphics(lwjglCanvas.getGraphics());
	}

	public void setDrawable(final EAdDrawable drawable) {
		lwjglCanvas.postRunnable(new Runnable() {

			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				app.setDrawable((RuntimeCompoundDrawable) assetHandler
						.getRuntimeAsset(drawable));
			}

		});

	}

	public void setList(final List<String> states) {
		lwjglCanvas.postRunnable(new Runnable() {

			@Override
			public void run() {
				app.setStates(states);
			}

		});
	}

	/**
	 * Returns a canvas displaying assets
	 * 
	 * @return
	 */
	public Canvas getCanvas() {
		return lwjglCanvas.getCanvas();

	}

	public LwjglAWTCanvas getLwjglAWTCanvas() {
		return lwjglCanvas;
	}

	/**
	 * This method must be called when the asset viewer disappears or is
	 * destroyed
	 */
	public void stop() {
		lwjglCanvas.stop();
	}

}
