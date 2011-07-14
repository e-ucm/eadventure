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

package es.eucm.eadventure.engine.assets;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeImage;

public class AndroidEngineImage extends RuntimeImage {

	public Bitmap image;
	
	private static final Logger logger = Logger.getLogger("AndroidEngineImage");
	
	@Inject
	public AndroidEngineImage(AssetHandler assetHandler) {
		 super(assetHandler);
		
	}		
	
	public AndroidEngineImage(Bitmap image) {
		super(null);
		this.image = image;
	}

	public Bitmap getImage() {
		return image;
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
		BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
		sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
	
		image = BitmapFactory.decodeFile(assetHandler.getAbsolutePath(descriptor.getURI()), sBitmapOptions);

		logger.info("New instance, loaded = " + (image != null));
		return image != null;
	}
	
	@Override
	public void freeMemory() {
		if (image != null) {
			image.recycle();
			logger.log(Level.INFO, "Image recycled: " + (descriptor != null ? descriptor.getURI() : "no descriptor"));
		}
		image = null;
	}

	@Override
	public boolean isLoaded() {
		return image != null;
	}
	
}
