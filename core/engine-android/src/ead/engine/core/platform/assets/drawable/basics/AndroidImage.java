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

package ead.engine.core.platform.assets.drawable.basics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.google.inject.Inject;

import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.drawables.basics.RuntimeImage;
import ead.engine.core.platform.rendering.GenericCanvas;

public class AndroidImage extends RuntimeImage<Canvas> {

	public Bitmap image;

	// FIXME find a better solution
	private static Bitmap defaultImage;

	private static final Logger logger = LoggerFactory
			.getLogger("AndroidEngineImage");

	@Inject
	public AndroidImage(AssetHandler assetHandler) {
		super(assetHandler);
		if (defaultImage == null) {
			defaultImage = decodeFile(assetHandler
					.getAbsolutePath("@drawable/nocursor.png"));
		}
	}

	public AndroidImage(Bitmap image) {
		super(null);
		this.image = image;
	}

	public Bitmap getImage() {
		return image == null ? defaultImage : image;
	}

	@Override
	public int getWidth() {
		if (image != null)
			return image.getWidth();
		return 1;
	}

	@Override
	public int getHeight() {
		if (image != null)
			return image.getHeight();
		return 1;
	}

	@Override
	public boolean loadAsset() {

		image = decodeFile(assetHandler.getAbsolutePath(descriptor.getUri()
				.getPath()));

		logger.info("New instance, loaded = " + (image != null));
		return image != null;
	}

	@Override
	public void freeMemory() {
		if (image != null) {
			image.recycle();
			logger.info("Image recycled: "
					+ (descriptor != null ? descriptor.getUri()
							: "no descriptor"));
		}
		image = null;
	}

	@Override
	public boolean isLoaded() {
		return image != null;
	}

	/**
	 * Returns a bitmap considering its size in order to reduce the amount of
	 * memory used
	 */
	private Bitmap decodeFile(String path) {

		File f = new File(path);
		Bitmap b = null;
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inInputShareable = true;
			o.inPurgeable = true;
			o.inTempStorage = new byte[16 * 1024];
			o.inPreferredConfig = Bitmap.Config.RGB_565;

			FileInputStream fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o);
			try {
				fis.close();
			} catch (IOException e) {
				logger.info("Couldn't close file input stream");
			}
		} catch (FileNotFoundException e) {
			logger.info("File not found: " + f.getName());
		} catch (OutOfMemoryError e) {
			logger.info("Out of memory error caused by: " + f.getName());
		}
		return b;
	}

	@Override
	public void render(GenericCanvas<Canvas> c) {
		if (getImage() != null) {
			c.getNativeGraphicContext().drawBitmap(getImage(), 0, 0, null);
		}
	}
}
