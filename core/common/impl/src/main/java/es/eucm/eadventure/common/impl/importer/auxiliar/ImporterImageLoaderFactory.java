package es.eucm.eadventure.common.impl.importer.auxiliar;

import java.awt.Image;
import java.awt.image.BufferedImage;

import es.eucm.eadventure.common.data.animation.ImageLoaderFactory;

public class ImporterImageLoaderFactory implements ImageLoaderFactory {
	
	private static final Image image;
	
	static {
		image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB );
	}

	@Override
	public Image getImageFromPath(String arg0) {
		return image;
	}

	@Override
	public void showErrorDialog(String arg0, String arg1) {
		
	}

}
