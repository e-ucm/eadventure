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
import java.util.Map;

import com.google.inject.Inject;

import ead.common.model.EAdElement;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.fills.ColorFill;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.resources.assets.drawable.compounds.EAdComposedDrawable;
import ead.common.resources.assets.text.BasicFont;
import ead.common.resources.assets.text.EAdFont;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.GameObject;
import ead.engine.core.input.InputHandler;
import ead.tools.StringHandler;

/**
 * A debugger showing all the fields and their values of the element under the
 * pointer
 * 
 */
public class FieldsDebugger implements Debugger {

	private EAdElement element;

	private InputHandler inputHandler;

	private List<DrawableGO<?>> gos;

	private GameState gameState;

	private SceneElement vars;

	private StringHandler stringHandler;

	private SceneElementGOFactory gameObjectFactory;
	
	private Game game;

	private EAdFont font = new BasicFont(12);

	private ColorFill color = new ColorFill(120, 120, 120, 50);

	@Inject
	public FieldsDebugger(InputHandler inputHandler, GameState gameState,
			StringHandler stringHandler, SceneElementGOFactory gameObjectFactory, Game game) {
		this.inputHandler = inputHandler;
		this.stringHandler = stringHandler;
		this.gameObjectFactory = gameObjectFactory;
		this.gameState = gameState;
		this.game = game;
		gos = new ArrayList<DrawableGO<?>>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<DrawableGO<?>> getGameObjects() {
		GameObject<?> newGO = inputHandler.getGameObjectUnderPointer();
		EAdElement newElement = (EAdElement) (newGO != null ? newGO.getElement() : game.getCurrentChapter());
		if ( newElement != element) {
			element = newElement;
			gos.clear();
			if (element != null) {
				Map<EAdVarDef<?>, Object> fields = gameState.getValueMap()
						.getElementVars(element);

				if (fields != null) {

					EAdComposedDrawable d = new ComposedDrawable();
					RectangleShape shape = new RectangleShape(300, 20 * (fields
							.keySet().size() + 2));
					shape.setPaint(color);
					d.addDrawable(shape, -10, -10);
					Caption c = new Caption();
					stringHandler.setString(c.getText(), element + "");
					c.setFont(font);
					c.setTextPaint(ColorFill.RED);
					c.setBubblePaint(ColorFill.WHITE);
					c.setPadding(2);
					d.addDrawable(c, 0, 0);
					int yOffset = 20;
					for (EAdVarDef<?> var : fields.keySet()) {
						c = new Caption();
						stringHandler.setString(c.getText(), var.getName()
								+ "=[0]");
						c.getFields().add(new BasicField(element, var));
						c.getFields().add(SystemFields.SHOW_MOUSE);
						c.setFont(font);
						c.setTextPaint(ColorFill.WHITE);
						c.setBubblePaint(ColorFill.BLACK);
						c.setPadding(2);
						d.addDrawable(c, 0, yOffset);
						yOffset += 20;
					}
					vars = new SceneElement();
					vars.setId("vars");
					vars.setVarInitialValue(SceneElement.VAR_ENABLE, false);
					vars.setPosition(10, 10);
					vars.getDefinition()
							.getResources()
							.addAsset(vars.getDefinition().getInitialBundle(),
									SceneElementDef.appearance, d);

					gos.add(gameObjectFactory.get(vars));
				}

			}

		}

		for (GameObject<?> go : gos) {
			go.update();
		}
		return gos;
	}

	@Override
	public void setUp(EAdAdventureModel model) {
		
	}

}
