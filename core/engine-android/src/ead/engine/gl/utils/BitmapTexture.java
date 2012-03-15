package ead.engine.gl.utils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.engine.gl.GLAssetHandler;

public class BitmapTexture {

	private static final Logger logger = LoggerFactory
			.getLogger("BitmapTexture");

	private GLAssetHandler assetHandler;

	private BitmapNode root;

	private Bitmap bitmap;

	private int textureId = -1;

	private FloatBuffer coordsBuffer;

	private Map<EAdDrawable, Integer> positions;

	private Canvas canvas;

	private Paint paint;

	public BitmapTexture(GLAssetHandler assetHandler, int width, int height) {
		this.assetHandler = assetHandler;
		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		root = new BitmapNode(width, height);
		paint = new Paint();
	}

	public boolean addBitmap(EAdDrawable drawable, Bitmap bitmap) {
		BitmapNode n = root.insert(drawable, bitmap.getWidth(),
				bitmap.getHeight());
		if (n != null) {
			canvas.drawBitmap(bitmap, n.left, n.top, paint);
			return true;
		}
		return false;
	}

	public void generateTexture(boolean generateBuffer) {
		textureId = generateTexture(bitmap);
		if (coordsBuffer == null || generateBuffer) {
			generateCoordinates();
		}
	}

	private void generateCoordinates() {
		positions = new HashMap<EAdDrawable, Integer>();
		int i = 0;
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		List<BitmapNode> nodes = root.getNodes();
		float coords[] = new float[4 * 2 * nodes.size()];
		for (BitmapNode n : nodes) {
			positions.put(n.drawable, i);
			coords[i++] = (float) n.left / width;
			coords[i++] = (float) n.top / height;
			coords[i++] = (float) n.right / width;
			coords[i++] = (float) n.top / height;
			coords[i++] = (float) n.right / width;
			coords[i++] = (float) n.bottom / height;
			coords[i++] = (float) n.left / width;
			coords[i++] = (float) n.bottom / height;
		}
		coordsBuffer = assetHandler.getFloatBuffer(coords);
	}

	private int generateTexture(Bitmap b) {

		int textureHandle[] = new int[1];

		GLES20.glGenTextures(1, textureHandle, 0);

		if (textureHandle[0] != 0) {
			// Bind to the texture in OpenGL
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

			// Set filtering
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);
			return textureHandle[0];
		} else {
			logger.warn("Texture couldn't be created.");
			return -1;
		}

	}

	public int getTextureId() {
		if (textureId == -1) {
			generateTexture(true);
		}
		return textureId;
	}

	public FloatBuffer getCoordsBuffer() {
		if (coordsBuffer == null) {
			generateTexture(true);
		}
		return coordsBuffer;
	}

	public int getPosition(EAdDrawable d) {
		return positions.get(d);
	}

}
