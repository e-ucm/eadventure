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

import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.scenes.GroupElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.variables.EAdVarDef;

import java.util.Map;

@SuppressWarnings( { "unchecked", "rawtypes" })
public class ModelFieldsDebuggerGO extends SceneElementGO {

	private int currentLength = -1;

	private SceneElementGO container;

	private EAdPaint bubblePaint = ColorFill.WHITE;

	int x = 10;

	@Inject
	public ModelFieldsDebuggerGO(AssetHandler assetHandler,
			SceneElementFactory sceneElementFactory, EventFactory eventFactory,
			Game game) {
		super(assetHandler, sceneElementFactory, game, eventFactory);
	}

	public void act(float delta) {
		super.act(delta);

		Map<EAdVarDef<?>, Object> values = gameState.getElementVars(game
				.getAdventureModel());

		if (values == null || values.size() == 0) {
			values = gameState.getElementVars(game.getCurrentChapter());
		}

		if (values != null) {
			if (values.size() != currentLength) {
				currentLength = values.size();
				int y = 10;
				int marginY = 20;
				if (container != null) {
					container.remove();
					container.free();
					this.getChildren().clear();
				}
				GroupElement fields = new GroupElement();
				fields.setInitialEnable(false);
				for (EAdVarDef<?> var : values.keySet()) {
					Caption c = new Caption(var.getName() + ": [0]");
					c.setPadding(1);
					c.getOperations().add(
							new BasicField(game.getAdventureModel(), var));
					c.setBubblePaint(bubblePaint);
					SceneElement field = new SceneElement(c);
					field.setPosition(x, y);
					y += marginY;
					fields.addSceneElement(field);
				}
				container = this.addSceneElement(fields);
			}
		}
	}

}
