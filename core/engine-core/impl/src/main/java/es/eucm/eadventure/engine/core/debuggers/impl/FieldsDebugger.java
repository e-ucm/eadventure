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

package es.eucm.eadventure.engine.core.debuggers.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.variables.EAdVarDef;
import es.eucm.eadventure.common.model.elements.variables.FieldImpl;
import es.eucm.eadventure.common.model.elements.variables.SystemFields;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.text.EAdFont;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawableImpl;
import es.eucm.eadventure.engine.core.debuggers.Debugger;
import es.eucm.eadventure.engine.core.game.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.go.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.go.GameObject;
import es.eucm.eadventure.engine.core.input.MouseState;

public class FieldsDebugger implements Debugger {

	private EAdElement element;

	private MouseState mouseState;

	private List<DrawableGO<?>> gos;

	private ValueMap valueMap;

	private SceneElementImpl vars;

	private StringHandler stringHandler;

	private SceneElementGOFactory gameObjectFactory;

	private EAdFont font = new EAdFontImpl(12);

	private EAdColor color = new EAdColor(120, 120, 120, 50);

	@Inject
	public FieldsDebugger(MouseState mouseState, ValueMap valueMap,
			StringHandler stringHandler, SceneElementGOFactory gameObjectFactory) {
		this.mouseState = mouseState;
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
		if (mouseState.getGameObjectUnderMouse() != null
				&& mouseState.getGameObjectUnderMouse().getElement() != element
				&& mouseState.getGameObjectUnderMouse().getElement() instanceof EAdElement) {
			element = (EAdElement) mouseState.getGameObjectUnderMouse()
					.getElement();
			gos.clear();
			if (element != null) {
				Map<EAdVarDef<?>, Object> fields = valueMap
						.getElementVars(element);

				if (fields != null) {

					ComposedDrawable d = new ComposedDrawableImpl();
					RectangleShape shape = new RectangleShape(300, 20 * (fields
							.keySet().size() + 2));
					shape.setPaint(color);
					d.addDrawable(shape, -10, -10);
					CaptionImpl c = new CaptionImpl();
					stringHandler.setString(c.getText(), element + "");
					c.setFont(font);
					c.setTextPaint(EAdColor.RED);
					c.setBubblePaint(EAdColor.WHITE);
					c.setPadding(2);
					d.addDrawable(c, 0, 0);
					int yOffset = 20;
					for (EAdVarDef<?> var : fields.keySet()) {
						c = new CaptionImpl();
						stringHandler.setString(c.getText(), var.getName()
								+ "=[0]");
						c.getFields().add(new FieldImpl(element, var));
						c.getFields().add(SystemFields.SHOW_MOUSE);
						c.setFont(font);
						c.setTextPaint(EAdColor.WHITE);
						c.setBubblePaint(EAdColor.BLACK);
						c.setPadding(2);
						d.addDrawable(c, 0, yOffset);
						yOffset += 20;
					}

					vars.setPosition(10, 10);
					vars.getDefinition()
							.getResources()
							.addAsset(vars.getDefinition().getInitialBundle(),
									SceneElementDefImpl.appearance, d);

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
