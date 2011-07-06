package es.eucm.eadventure.engine.extra;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class AndroidCanvas extends Canvas {

	private Bitmap bitmap;
	
	public AndroidCanvas(Bitmap c) {
		super(c);
		this.bitmap = c;
		
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
}
