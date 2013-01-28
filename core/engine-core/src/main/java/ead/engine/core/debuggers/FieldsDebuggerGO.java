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

package ead.engine.core.debuggers;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.widgets.Label;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.variables.EAdVarDef;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGOImpl;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

/**
 * A debugger showing all the fields and their values of the element under the
 * pointer
 * 
 */
public class FieldsDebuggerGO extends SceneElementGOImpl {

	public static final int DELTA_Y = 20;

	public static final int MARGIN_LEFT = 10;

	private InputHandler inputHandler;

	private SceneElementGO<?> currentGO;

	private List<EAdVarDef<?>> vars;

	private int y;

	@Inject
	public FieldsDebuggerGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			InputHandler inputHandler) {
		super(assetHandler, sceneElementFactory, gui, gameState, eventFactory);
		this.inputHandler = inputHandler;
		this.vars = new ArrayList<EAdVarDef<?>>();
	}

	public void update() {
		SceneElementGO<?> go = inputHandler.getGameObjectUnderPointer();
		if (currentGO != go) {
			currentGO = go;
			if (currentGO != null) {
				y = 0;
				getChildren().clear();
				vars.clear();
				EAdSceneElement s = (EAdSceneElement) go.getElement();

				Label l = new Label("Id: " + s.getId());
				l.setBgColor(ColorFill.WHITE);
				y += DELTA_Y;
				l.setPosition(MARGIN_LEFT, y);
				addSceneElement(l);

				// Position
				y += DELTA_Y;
				Label position = new Label("Position: ([0]:[1]:[2]:[3])");
				position.setPosition(MARGIN_LEFT, y);
				position.setBgColor(ColorFill.WHITE);
				position.getCaption().getFields().add(
						go.getField(SceneElement.VAR_DISP_X));
				position.getCaption().getFields().add(
						go.getField(SceneElement.VAR_DISP_Y));
				position.getCaption().getFields().add(
						go.getField(SceneElement.VAR_X));
				position.getCaption().getFields().add(
						go.getField(SceneElement.VAR_Y));
				addSceneElement(position);

				// Rotation
				y += DELTA_Y;
				Label rotation = new Label("Rotation: [0]");
				rotation.setPosition(MARGIN_LEFT, y);
				rotation.setBgColor(ColorFill.WHITE);
				rotation.getCaption().getFields().add(
						go.getField(SceneElement.VAR_ROTATION));
				addSceneElement(rotation);

				// Scale
				y += DELTA_Y;
				Label scale = new Label("Scale: [0] - [1]:[2]");
				scale.setPosition(MARGIN_LEFT, y);
				scale.setBgColor(ColorFill.WHITE);
				scale.getCaption().getFields().add(
						go.getField(SceneElement.VAR_SCALE));
				scale.getCaption().getFields().add(
						go.getField(SceneElement.VAR_SCALE_X));
				scale.getCaption().getFields().add(
						go.getField(SceneElement.VAR_SCALE_Y));
				addSceneElement(scale);
			}
		}
		super.update();
	}

}
