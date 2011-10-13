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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeImage;

public class AndroidEngineImage extends RuntimeImage {

	public Bitmap image;
	
	private boolean loaded;
	
	// FIXME find a better solution
	private static Bitmap defaultImage;
	
	private static final Logger logger = Logger.getLogger("AndroidEngineImage");
	
	@Inject
	public AndroidEngineImage(AssetHandler assetHandler) {
		 super(assetHandler);
		if ( defaultImage == null ){
			defaultImage = decodeFile(assetHandler.getAbsolutePath("@drawable/nocursor.png"));
		}
	}		
	
	public AndroidEngineImage(Bitmap image) {
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
		loaded = true;
		File f = new File(assetHandler.getAbsolutePath(descriptor.getURI().getPath()));
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte [16 * 1024];
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		
		try {
			image = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
		} catch (FileNotFoundException e) {
			logger.info("Image not found: " + descriptor.getURI());
		}
		
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
	
	/**
	 * Returns a bitmap considering its size in order to reduce the amount of memory used 
	 */
	private Bitmap decodeFile(String path){
		
		File f = new File(path);
		final int IMAGE_MAX_SIZE = 800;
	    Bitmap b = null;
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        try {
				fis.close();
			} catch (IOException e) {				
				logger.info("Couldn't close file input stream");
			}

	        int scale = 1;
	        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
	            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inInputShareable = true;
	        o2.inPurgeable = true;
    		o2.inPreferredConfig = Bitmap.Config.RGB_565;
	        o2.inSampleSize = scale;
	        
	        fis = new FileInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        try {
				fis.close();
			} catch (IOException e) {
				logger.info("Couldn't close file input stream");
			}
	    } catch (FileNotFoundException e) {
	    	logger.info("File not found");
	    }
	    return b;
	}
	
	
	
}
