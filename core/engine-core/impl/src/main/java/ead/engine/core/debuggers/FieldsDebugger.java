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
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.BasicFont;
import ead.common.params.fills.ColorFill;
import ead.common.params.text.EAdFont;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.drawable.compounds.EAdComposedDrawable;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.util.StringHandler;
import ead.engine.core.debuggers.Debugger;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.GameObject;
import ead.engine.core.input.InputHandler;

/**
 * A debugger showing all the fields and their values of the element under the
 * pointer
 * 
 */
public class FieldsDebugger implements Debugger {

	private EAdElement element;

	private InputHandler inputHandler;

	private List<DrawableGO<?>> gos;

	private ValueMap valueMap;

	private SceneElementImpl vars;

	private StringHandler stringHandler;

	private SceneElementGOFactory gameObjectFactory;

	private EAdFont font = new BasicFont(12);

	private ColorFill color = new ColorFill(120, 120, 120, 50);

	@Inject
	public FieldsDebugger(InputHandler inputHandler, ValueMap valueMap,
			StringHandler stringHandler, SceneElementGOFactory gameObjectFactory) {
		this.inputHandler = inputHandler;
		this.valueMap = valueMap;
		this.stringHandler = stringHandler;
		this.gameObjectFactory = gameObjectFactory;
		gos = new ArrayList<DrawableGO<?>>();
		vars = new SceneElementImpl();
		vars.setId("vars");
		vars.setVarInitialValue(SceneElementImpl.VAR_ENABLE, false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<DrawableGO<?>> getGameObjects() {
		if (inputHandler.getGameObjectUnderPointer() != null
				&& inputHandler.getGameObjectUnderPointer().getElement() != element
				&& inputHandler.getGameObjectUnderPointer().getElement() instanceof EAdElement) {
			element = (EAdElement) inputHandler.getGameObjectUnderPointer()
					.getElement();
			gos.clear();
			if (element != null) {
				Map<EAdVarDef<?>, Object> fields = valueMap
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

}
