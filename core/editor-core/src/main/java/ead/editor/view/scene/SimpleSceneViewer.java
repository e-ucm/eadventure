package ead.editor.view.scene;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.util.EAdRectangle;
import ead.editor.view.scene.go.EditableGameObject;
import ead.editor.view.scene.listener.LoggerSceneListener;
import ead.editor.view.scene.listener.SceneViewerInputProcessor;
import ead.engine.core.gdx.InvOrtographicCamera;
import ead.engine.core.platform.GdxCanvas;
import ead.engine.desktop.utils.assetviewer.AssetViewerModule;

public class SimpleSceneViewer extends AbstractSceneViewer implements
		ApplicationListener {

	private LwjglAWTCanvas lwjglCanvas;
	private SpriteBatch batch;
	private List<EditableGameObject> gameObjects;
	private int time;
	private Injector injector;
	private GdxCanvas canvas;
	private InvOrtographicCamera camera;
	private Texture circle;
	private Texture hLine;
	private Texture vLine;
	private EAdRectangle selectionRectangle;
	private List<EditableGameObject> selection;
	private Matrix4 selectionMatrix = new Matrix4();

	public SimpleSceneViewer() {
		lwjglCanvas = new LwjglAWTCanvas(this, true);
		gameObjects = new ArrayList<EditableGameObject>();
		injector = Guice.createInjector(new AssetViewerModule());
		canvas = injector.getInstance(GdxCanvas.class);
		selectionRectangle = new EAdRectangle();
		selection = new ArrayList<EditableGameObject>();
	}

	public void setScene(EAdScene scene) {
		super.setScene(scene);
		addGameObject(scene.getBackground());
		for (EAdSceneElement element : scene.getSceneElements()) {
			addGameObject(element);
		}
	}

	private void addGameObject(final EAdSceneElement sceneElement) {

		lwjglCanvas.postRunnable(new Runnable() {

			@Override
			public void run() {
				EditableGameObject go = injector
						.getInstance(EditableGameObject.class);
				go.setSceneElement(sceneElement);
				gameObjects.add(go);

			}

		});

	}

	@Override
	public void updateView() {

	}

	public Canvas getCanvas() {
		if (lwjglCanvas != null) {
			return lwjglCanvas.getCanvas();
		}
		return null;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.enableBlending();
		camera = new InvOrtographicCamera();
		canvas.setGraphicContext(batch);
		time = 0;
		lwjglCanvas.getInput().setInputProcessor(
				new SceneViewerInputProcessor(this, new LoggerSceneListener()));
		initTextures();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render() {
		lwjglCanvas.getGraphics().getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		time += lwjglCanvas.getGraphics().getDeltaTime() * 1000;
		batch.begin();
		camera.zoom = 1;
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		for (EditableGameObject go : gameObjects) {
			go.render(time);
		}

		batch.setTransformMatrix(selectionMatrix);
		// Draw selection
		if (selection.size() > 0) {			
			batch.draw(circle, selectionRectangle.x, selectionRectangle.y);
			batch.draw(circle, selectionRectangle.x + selectionRectangle.width,
					selectionRectangle.y);
			batch.draw(circle, selectionRectangle.x, selectionRectangle.y
					+ selectionRectangle.height);
			batch.draw(circle, selectionRectangle.x + selectionRectangle.width,
					selectionRectangle.y + selectionRectangle.height);
		}
		batch.end();
	}

	@Override
	public void resize(int w, int h) {
		float centerX = w / 2;
		float centerY = h / 2;

		camera.position.set(centerX, centerY, 0);
		camera.viewportWidth = w;
		camera.viewportHeight = h;
	}

	@Override
	public void resume() {

	}

	private Vector3 tmp = new Vector3();

	public EditableGameObject get(int x, int y) {
		tmp.set(x, y, 0);
		camera.unproject(tmp);
		for (int i = gameObjects.size() - 1; i >= 0; i--) {
			EditableGameObject go = gameObjects.get(i);
			if (go.contains(tmp.x, tmp.y)) {
				return go;
			}
		}
		return null;
	}

	private void initTextures() {
		Pixmap p = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
		p.setColor(Color.GREEN);
		p.fillCircle(5, 5, 4);
		p.setColor(Color.DARK_GRAY);
		p.drawCircle(5, 5, 4);

		circle = new Texture(p);
		p.dispose();
		selectionMatrix.translate(-5f, -5.0f, 0f);
		
		p = new Pixmap(1, 5, Pixmap.Format.RGB888);
		vLine = new Texture(p);
		p.dispose();
		
		p = new Pixmap(5, 1, Pixmap.Format.RGB888);
		hLine = new Texture(p);
		p.dispose();
	}

	public void updateSelectionBounds() {
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (EditableGameObject go : selection) {
			EAdRectangle bounds = go.getBounds();
			minX = bounds.x < minX ? bounds.x : minX;
			minY = bounds.y < minY ? bounds.y : minY;
			maxX = bounds.x + bounds.width > maxX ? bounds.x + bounds.width
					: maxX;
			maxY = bounds.y + bounds.height > maxY ? bounds.y + bounds.height
					: maxY;
		}
		selectionRectangle.setBounds(minX, minY, maxX - minX, maxY - minY);
	}

	public List<EditableGameObject> getSelection() {
		return this.selection;
	}
	
	public void setDelta( int deltaX, int deltaY ){
		selectionMatrix.idt();
		selectionMatrix.translate(deltaX - 5.0f, deltaY - 5.0f, 0);

	}

}
