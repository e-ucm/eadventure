package ead.engine.core.utils.assetviewer;

import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import ead.engine.core.gdx.InvOrtographicCamera;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.GdxCanvas;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AssetApplicationListener implements ApplicationListener {

	public static final int DEFAULT_PATTERN_SIZE = 20;
	public int patternSize = DEFAULT_PATTERN_SIZE;

	private GdxCanvas canvas;
	private InvOrtographicCamera camera;
	private SpriteBatch spriteBatch;
	private Graphics graphics;
	private Texture squareTexture;

	private int canvasWidth;
	private int canvasHeight;
	private int drawableWidth;
	private int drawableHeight;

	private Matrix4 transformation;
	private Matrix4 idt = new Matrix4();

	private RuntimeCompoundDrawable currentDrawable;
	private int time;
	private List<String> states;

	public AssetApplicationListener(FontHandler fontHandler) {
		canvas = new GdxCanvas(fontHandler);
		transformation = new Matrix4();
		transformation.idt();
		idt.idt();
	}

	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

	/**
	 * Sets the size of the side of the background pattern squares
	 * 
	 * @param size
	 *            size in pixels
	 */
	public void setPatternSize(int size) {
		this.patternSize = size;
	}

	public void setDrawable(RuntimeCompoundDrawable drawable) {
		if ( currentDrawable != null ){
			currentDrawable.freeMemory();
		}
		this.currentDrawable = drawable;
		drawableWidth = currentDrawable.getDrawable(time, states, 0).getWidth();
		drawableHeight = currentDrawable.getDrawable(time, states, 0)
				.getHeight();
		updateZoom();

	}

	public void setStates(List<String> states) {
		this.states = states;
	}

	@Override
	public void create() {
		spriteBatch = new SpriteBatch();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA,
				GL20.GL_ONE_MINUS_SRC_ALPHA);
		camera = new InvOrtographicCamera();
		canvas.setGraphicContext(spriteBatch);
		time = 0;
		createBackgroundPattern();
	}

	private void createBackgroundPattern() {
		Pixmap square = new Pixmap(patternSize * 2, patternSize,
				Pixmap.Format.RGB888);
		square.setColor(Color.GRAY);
		square.fillRectangle(0, 0, patternSize, patternSize);
		square.setColor(Color.LIGHT_GRAY);
		square.fillRectangle(patternSize, 0, patternSize, patternSize);
		squareTexture = new Texture(square);
		square.dispose();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render() {
		if (graphics != null) {
			time += graphics.getDeltaTime() * 1000;
			graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		spriteBatch.begin();

		// Background pattern
		camera.zoom = 1;
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.setTransformMatrix(idt);
		for (int i = 0; i <= canvasWidth + patternSize * 2; i += patternSize * 2) {
			for (int j = 0; j <= canvasHeight; j += patternSize) {
				spriteBatch.draw(squareTexture, i
						- ((j / patternSize) % 2 == 0 ? patternSize : 0), j);
			}
		}

		// Asset
		camera.zoom = zoom;
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.setTransformMatrix(transformation);

		if (currentDrawable != null) {
			currentDrawable.getDrawable(time, states, 0).render(canvas);
		}

		spriteBatch.end();
	}

	private float zoom = 1;

	@Override
	public void resize(int w, int h) {
		canvasWidth = w;
		canvasHeight = h;

		float centerX = canvasWidth / 2;
		float centerY = canvasHeight / 2;

		camera.position.set(centerX, centerY, 0);
		camera.viewportWidth = canvasWidth;
		camera.viewportHeight = canvasHeight;

		updateZoom();
	}

	@Override
	public void resume() {

	}

	private void updateZoom() {
		transformation.idt();
		transformation.translate((canvasWidth - drawableWidth) / 2,
				(canvasHeight - drawableHeight) / 2, 0);

		float zoomX = (float) drawableWidth / (float) this.canvasWidth;
		float zoomY = (float) drawableHeight / (float) this.canvasHeight;
		zoom = zoomX > zoomY ? zoomX : zoomY;
	}

}