package es.eucm.eadventure.engine.extra;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BitmapCanvas extends Canvas {
	
	private Bitmap bitmap;
	
	public BitmapCanvas(Bitmap b){
		super(b);
		this.bitmap = b;
	}
	
	public Bitmap getBitmap(){
		return this.bitmap;
	}

}
