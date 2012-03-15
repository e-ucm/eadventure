package ead.engine.gl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import android.graphics.Bitmap;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.engine.core.platform.AndroidAssetHandler;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.assets.RuntimeAsset;
import ead.engine.gl.utils.BitmapTexture;

@Singleton
public class GLAssetHandler extends AndroidAssetHandler {

	public static final int BYTES_PER_FLOAT = 4;

	private static final int MINIMUM_SIZE = 1024;

	private HashMap<String, FloatBuffer> floatBuffers;

	private List<BitmapTexture> bitmapTextures;

	private Map<EAdDrawable, Integer> bitmapIndexes;

	@Inject
	public GLAssetHandler(
			Injector injector,
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> classMap,
			FontHandler fontHandler) {
		super(injector, classMap, fontHandler);
		floatBuffers = new HashMap<String, FloatBuffer>();
		bitmapTextures = new ArrayList<BitmapTexture>();
		bitmapIndexes = new HashMap<EAdDrawable, Integer>();
	}

	public FloatBuffer getFloatBuffer(int width, int height) {
		FloatBuffer buffer = floatBuffers.get(width + "" + height);
		if (buffer == null) {
			float[] vertices = new float[] { 0.0f, 0.0f, 0.0f, width, 0.0f,
					0.0f, width, height, 0.0f, 0.0f, height, 0.0f };

			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length
					* BYTES_PER_FLOAT);
			buffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
			buffer.put(vertices).position(0);

			floatBuffers.put(width + "" + height, buffer);
		}
		return buffer;
	}

	public FloatBuffer getFloatBuffer(float[] f) {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(f.length
				* BYTES_PER_FLOAT);
		FloatBuffer buffer = byteBuffer.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		buffer.put(f).position(0);
		return buffer;
	}

	public StringBuilder getTextFromFile(String file) {
		StringBuilder text = new StringBuilder();
		InputStream in = null;
		Scanner scanner = null;
		try {
			in = this.resources.getAssets().open(file);
			String NL = System.getProperty("line.separator");
			scanner = new Scanner(in, "UTF-8");
			while (scanner.hasNext()) {
				text.append(scanner.nextLine() + NL);
			}

		} catch (IOException e) {
			logger.warn("Couldn't extract text from {}. Invalid file.", file);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}

			if (scanner != null) {
				scanner.close();
			}
		}
		return text;
	}

	public int getBitmapTextureIndex(EAdDrawable d) {
		return bitmapIndexes.get(d);
	}
	
	public FloatBuffer getBuffer(int bitmapIndex ){
		return bitmapTextures.get(bitmapIndex).getCoordsBuffer();
	}
	
	public int getTextureId(int bitmapIndex){
		return bitmapTextures.get(bitmapIndex).getTextureId();
	}
	
	public int getPosition(int bitmapIndex, EAdDrawable d){
		return bitmapTextures.get(bitmapIndex).getPosition(d);
	}

	public void addTexture(EAdDrawable d, Bitmap b) {
		int i = 0;
		int index = -1;
		boolean added = false;
		while (!added && i < bitmapTextures.size()) {
			added = bitmapTextures.get(i).addBitmap(d, b);
			if (added) {
				index = i;
			}
			i++;
		}

		if (!added) {
			int size = MINIMUM_SIZE;
			while (size < b.getWidth() || size < b.getHeight()) {
				size = size * 2;
			}
			BitmapTexture bitmapTexture = new BitmapTexture(this, size, size);
			added = bitmapTexture.addBitmap(d, b);
			if (!added) {
				logger.warn("{} couldn't be added to the textures tree",
						d.toString());
			} else {
				index = bitmapTextures.size();
				bitmapTextures.add(bitmapTexture);
			}
		}

		if (index == -1) {
			logger.warn("{} couldn't be added to bitmap textures", d.toString());
		} else {
			bitmapIndexes.put(d, new Integer(index));
		}

	}
	
	public void onResume( ){
		for ( BitmapTexture b: bitmapTextures ){
			b.generateTexture(false);
		}
	}

}
