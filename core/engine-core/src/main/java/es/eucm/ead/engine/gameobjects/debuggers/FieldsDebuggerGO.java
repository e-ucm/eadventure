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

package es.eucm.ead.engine.gameobjects.debuggers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;
import es.eucm.ead.model.assets.drawable.basics.shapes.CircleShape;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.widgets.Label;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.variables.EAdVarDef;

import java.util.ArrayList;
import java.util.List;

/**
 * A debugger showing all the fields and their values of the element under the
 * pointer
 * 
 */
public class FieldsDebuggerGO extends SceneElementGO {

	public static final int DELTA_Y = 20;

	public static final int MARGIN_LEFT = 10;

	private SceneElementGO currentGO;

	private List<EAdVarDef<?>> vars;

	private List<SceneElementGO> labels;

	private SceneGO scene;

	private List<Bounds> bounds;

	private int y;

	private CircleShape circle = new CircleShape(2, new Paint(ColorFill.RED,
			ColorFill.BLACK));
	private CircleShape circleTopLeft = new CircleShape(2, new Paint(
			ColorFill.GREEN, ColorFill.BLACK));

	@Inject
	public FieldsDebuggerGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, Game game,
			EventGOFactory eventFactory) {
		super(assetHandler, sceneElementFactory, game, eventFactory);
		this.vars = new ArrayList<EAdVarDef<?>>();
		this.bounds = new ArrayList<Bounds>();
		this.labels = new ArrayList<SceneElementGO>();
	}

	public void act(float delta) {
		SceneGO currentScene = gui.getScene();
		if (currentScene != scene) {
			scene = currentScene;
			generateBounds();
		}
		updateBounds();
		SceneElementGO go = gui.getGameObjectUnderPointer();
		if (currentGO != go) {
			currentGO = go;
			if (currentGO != null) {
				y = 0;
				for (SceneElementGO l : labels) {
					l.remove();
				}
				labels.clear();
				vars.clear();
				EAdSceneElement s = (EAdSceneElement) go.getElement();

				Label l = new Label("Id: " + s.getId());
				l.setBgColor(ColorFill.WHITE);
				y += DELTA_Y;
				l.setPosition(MARGIN_LEFT, y);
				SceneElementGO lgo = sceneElementFactory.get(l);
				labels.add(lgo);
				addSceneElement(lgo);

				// Position
				y += DELTA_Y;
				Label position = new Label("Position: ([0]:[1]:[2]:[3])");
				position.setPosition(MARGIN_LEFT, y);
				position.setBgColor(ColorFill.WHITE);
				position.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_DISP_X));
				position.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_DISP_Y));
				position.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_X));
				position.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_Y));
				lgo = sceneElementFactory.get(position);
				labels.add(lgo);
				addSceneElement(lgo);

				// Rotation
				y += DELTA_Y;
				Label rotation = new Label("Rotation: [0]");
				rotation.setPosition(MARGIN_LEFT, y);
				rotation.setBgColor(ColorFill.WHITE);
				rotation.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_ROTATION));
				lgo = sceneElementFactory.get(rotation);
				labels.add(lgo);
				addSceneElement(lgo);

				// Scale
				y += DELTA_Y;
				Label scale = new Label("Scale: [0] - [1]:[2]");
				scale.setPosition(MARGIN_LEFT, y);
				scale.setBgColor(ColorFill.WHITE);
				scale.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_SCALE));
				scale.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_SCALE_X));
				scale.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_SCALE_Y));
				lgo = sceneElementFactory.get(scale);
				labels.add(lgo);
				addSceneElement(lgo);
				// Z
				y += DELTA_Y;
				Label z = new Label("Z: [0]");
				z.setPosition(MARGIN_LEFT, y);
				z.setBgColor(ColorFill.WHITE);
				z.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_Z));
				lgo = sceneElementFactory.get(z);
				labels.add(lgo);
				addSceneElement(lgo);
				// Bundle
				y += DELTA_Y;
				Label bundle = new Label("Bundle: [0]");
				bundle.setPosition(MARGIN_LEFT, y);
				bundle.setBgColor(ColorFill.WHITE);
				bundle.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_BUNDLE_ID));
				lgo = sceneElementFactory.get(bundle);
				labels.add(lgo);
				addSceneElement(lgo);
				// State
				y += DELTA_Y;
				Label state = new Label("Bundle: [0]");
				state.setPosition(MARGIN_LEFT, y);
				state.setBgColor(ColorFill.WHITE);
				state.getCaption().getOperations().add(
						go.getField(SceneElement.VAR_STATE));
				lgo = sceneElementFactory.get(state);
				labels.add(lgo);
				addSceneElement(lgo);
			}
		}
		super.act(delta);
	}

	private void generateBounds() {
		for (Bounds b : bounds) {
			b.remove();
		}
		bounds.clear();
		if (scene != null) {
			for (Actor a : scene.getChildren()) {
				if (a instanceof SceneElementGO) {
					SceneElementGO e = sceneElementFactory
							.get(new SceneElement(circle));
					addSceneElement(e);
					SceneElementGO e2 = sceneElementFactory
							.get(new SceneElement(circleTopLeft));
					addSceneElement(e2);
					bounds.add(new Bounds((SceneElementGO) a, e, e2));
				}
			}
		}
	}

	private void updateBounds() {
		for (Bounds b : bounds) {
			b.update();
		}
	}

	public class Bounds {

		private SceneElementGO parent;

		private SceneElementGO bounds;

		private SceneElementGO topRight;

		public Bounds(SceneElementGO parent, SceneElementGO center,
				SceneElementGO topCenter) {
			this.parent = parent;
			this.bounds = center;
			bounds.setDispX(0.5f);
			bounds.setDispY(0.5f);
			this.topRight = topCenter;
			topCenter.setDispX(0.5f);
			topCenter.setDispY(0.5f);
		}

		public void remove() {
			bounds.remove();
			topRight.remove();
		}

		public void update() {
			bounds.setPosition(parent.getCenterX(), parent.getCenterY());
			topRight.setPosition(parent.getLeft(), parent.getTop());
		}

		public SceneElementGO getParent() {
			return parent;
		}

		public SceneElementGO getBounds() {
			return bounds;
		}

	}

}
