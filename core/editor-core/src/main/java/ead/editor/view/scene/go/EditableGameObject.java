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

package ead.editor.view.scene.go;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.google.inject.Inject;

import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.util.EAdRectangle;
import ead.engine.core.gdx.platform.GdxCanvas;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;
import ead.engine.core.platform.assets.RuntimeDrawable;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EditableGameObject {

	// Platform
	private AssetHandler assetHandler;
	private GdxCanvas canvas;

	// Position
	private int x;
	private int y;
	private float dispX;
	private float dispY;
	
	// Deltas (used for drag)
	private int deltaX;
	private int deltaY;

	// Top left position
	private float xT;
	private float yL;

	// Rotation
	float rotation;

	// Scale
	private float scaleX;
	private float scaleY;

	// Dimensions
	private float width;
	private float height;

	// Bounds
	private EAdRectangle bounds;

	// Alpha
	private float alpha;

	private Matrix4 matrix;
	private Matrix4 invMatrix;

	private EAdSceneElement element;

	private List<String> states;

	private RuntimeCompoundDrawable bundle;

	private RuntimeDrawable drawable;

	@Inject
	public EditableGameObject(AssetHandler assetHandler, GdxCanvas canvas) {
		super();
		this.assetHandler = assetHandler;
		this.canvas = canvas;
		this.x = 0;
		this.y = 0;
		this.deltaX = 0;
		this.deltaY = 0;
		this.dispX = 0.5f;
		this.dispY = 0.5f;
		this.rotation = 0;
		this.alpha = 1.0f;
		this.scaleX = 1.0f;
		this.scaleY = 1.0f;
		this.matrix = new Matrix4();
		this.invMatrix = new Matrix4();
		this.width = 1;
		this.height = 1;
		this.states = new ArrayList<String>();
		xT = x - dispX * width;
		yL = y - dispY * height;
		this.bounds = new EAdRectangle((int) xT, (int) yL, (int) width,
				(int) height);
	}

	public void setSceneElement(EAdSceneElement element) {
		this.element = element;
		for (Entry<EAdVarDef<?>, Object> var : element.getVars().entrySet()) {
			if (var.getKey().equals(SceneElement.VAR_X)) {
				this.x = (Integer) var.getValue();
			} else if (var.getKey().equals(SceneElement.VAR_Y)) {
				this.y = (Integer) var.getValue();
			} else if (var.getKey().equals(SceneElement.VAR_DISP_X)) {
				this.dispX = (Float) var.getValue();
			} else if (var.getKey().equals(SceneElement.VAR_DISP_Y)) {
				this.dispY = (Float) var.getValue();
			} else if (var.getKey().equals(SceneElement.VAR_SCALE_X)) {
				this.scaleX = (Float) var.getValue();
			} else if (var.getKey().equals(SceneElement.VAR_SCALE_Y)) {
				this.scaleY = (Float) var.getValue();
			} else if (var.getKey().equals(SceneElement.VAR_ALPHA)) {
				this.alpha = (Float) var.getValue();
			} else if (var.getKey().equals(SceneElement.VAR_ROTATION)) {
				this.rotation = (Float) var.getValue();
			}
		}
		this.bundle = (RuntimeCompoundDrawable) assetHandler
				.getRuntimeAsset(element.getDefinition().getAppearance());
		this.drawable = bundle.getDrawable(0, states, 0);
		updateMatrix();

	}

	public void render(int time) {
		((SpriteBatch) canvas.getNativeGraphicContext()).setColor(1.0f, 1.0f,
				1.0f, alpha);
		((SpriteBatch) canvas.getNativeGraphicContext())
				.setTransformMatrix(matrix);
		RuntimeDrawable current = bundle.getDrawable(time, states, 0);
		if (current != drawable) {
			drawable = current;
			updateMatrix();
		}
		drawable.render(canvas);
	}

	public void updateMatrix() {
		matrix.idt();
		if (drawable != null) {
			width = drawable.getWidth();
			height = drawable.getHeight();
			int x = this.x + deltaX;
			int y = this.y + deltaY;

			xT = x - dispX * width;
			yL = y - dispY * height;
			bounds.setBounds((int) xT, (int) yL, (int) width, (int) height);

			matrix.translate(xT, yL, 0);
			float deltaX = xT - x;
			float deltaY = yL - y;
			matrix.translate(deltaX, deltaY, 0);
			matrix.rotate(0, 0, 1, rotation);
			matrix.scale(scaleX, scaleY, 1);
			matrix.translate(-deltaX, -deltaY, 0);
			
			invMatrix.set(matrix);
			invMatrix.inv();
		}
	}

	public EAdSceneElement getSceneElement() {
		return element;
	}

	private Vector3 tmp = new Vector3();

	public boolean contains(float x, float y) {
		tmp.set(x, y, 0);
		tmp.mul(invMatrix);
		return tmp.x >= 0 && tmp.x < width && tmp.y >= 0 && tmp.y < height;
	}

	public EAdRectangle getBounds() {
		return bounds;
	}
	
	public void setDelta( int deltaX, int deltaY ){
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		updateMatrix();
	}

	public void addDelta() {
		this.x += deltaX;
		this.y += deltaY;
		this.deltaX = 0;
		this.deltaY = 0;
		updateMatrix();
	}

}
