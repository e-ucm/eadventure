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

package es.eucm.ead.engine.desktop.utils.assetviewer;

import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.utils.ScreenUtils;
import com.google.inject.Inject;

import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.drawables.RuntimeDrawable;
import es.eucm.ead.engine.utils.assetviewer.AssetApplicationListener;

/**
 * Contains a canvas in which assets can be represented as they will be
 * represented in the game engine
 *
 */
public class AssetViewer {

	static private Logger logger = LoggerFactory.getLogger(AssetViewer.class);

	private static LwjglAWTCanvas sharedContext;
	private LwjglAWTCanvas canvas;
	private AssetApplicationListener app;
	private AssetHandler assetHandler;

	@Inject
	public AssetViewer(AssetHandler assetHandler, AssetApplicationListener app) {
		this.assetHandler = assetHandler;
		this.app = app;
		if (sharedContext == null) {
			this.canvas = new LwjglAWTCanvas(app, true);
			sharedContext = canvas;
		} else {
			this.canvas = new LwjglAWTCanvas(app, true, sharedContext);
		}
	}

	/**
	 * used to grab images of whatever is being rendered.
	 */
	public static class ImageGrabber implements Runnable {

		private BufferedImage image;
		private Runnable callback;
		private int w;
		private int h;

		public void setCallback(Runnable callback) {
			this.callback = callback;
		}

		private void setSize(int w, int h) {
			this.w = w;
			this.h = h;
		}

		public BufferedImage getImage() {
			return image;
		}

		public void writeToFile(File f) {
			try {
				ImageIO.write(image, "png", f);
			} catch (IOException ex) {
				logger.error("could not write image", ex);
			}
		}

		@Override
		public void run() {
			if (w <= 0 || h <= 0) {
				throw new IllegalStateException("Invalid size");
			}

			BufferedImage bi = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_ARGB);
			WritableRaster wr = bi.getRaster();
			byte[] bytes = ScreenUtils.getFrameBufferPixels(0, 0, w, h, true);
			int[] pixel = new int[4];
			for (int y = 0, b = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					pixel[0] = bytes[b++];
					pixel[1] = bytes[b++];
					pixel[2] = bytes[b++];
					pixel[3] = bytes[b++];
					wr.setPixel(x, y, pixel);
				}
			}
			image = bi;
			if (callback != null) {
				callback.run();
			}
		}
	}

	public void grabImage(ImageGrabber grabber) {
		grabber.setSize(canvas.getGraphics().getWidth(), canvas.getGraphics()
				.getHeight());
		canvas.postRunnable(grabber);
	}

	public void setDrawable(final EAdDrawable drawable) {
		app.setGraphics(canvas.getGraphics());
		canvas.postRunnable(new Runnable() {
			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				app.setDrawable((RuntimeDrawable) assetHandler
						.getRuntimeAsset(drawable));
			}
		});
	}

	public void setList(final List<String> states) {
		canvas.postRunnable(new Runnable() {
			@Override
			public void run() {
				app.setStates(states);
			}
		});
	}

	/**
	 * Replaces the current canvas with a copy of an existing one.
	 *
	 * @param canvas
	 */
	public void setCanvas(LwjglAWTCanvas canvas) {
		this.canvas.stop();
		this.canvas = new LwjglAWTCanvas(app, true, canvas);
	}

	/**
	 * Returns a canvas displaying assets
	 *
	 * @return
	 */
	public Canvas getCanvas() {
		return canvas.getCanvas();

	}

	public LwjglAWTCanvas getLwjglAWTCanvas() {
		return canvas;
	}

	/**
	 * This method must be called when the asset viewer disappears or is
	 * destroyed
	 */
	public void stop() {
		canvas.stop();
	}
}
