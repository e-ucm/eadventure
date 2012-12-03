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

package ead.engine.core.gameobjects.widgets;

import com.google.inject.Inject;

import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.SystemFields;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.widgets.TextArea;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.gameobjects.go.SceneElementGOImpl;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.util.EAdTransformation;
import ead.tools.StringHandler;

public class TextAreaGO extends SceneElementGOImpl<TextArea> {

	private String currentText;

	private Caption textCaption;

	private SceneElementGO<?> textElement;

	private StringHandler stringHandler;

	@Inject
	public TextAreaGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			StringHandler stringHandler) {
		super(assetHandler, sceneElementFactory, gui, gameState, eventFactory);
		this.stringHandler = stringHandler;
	}

	@Override
	public void setElement(TextArea element) {
		super.setElement(element);
		textCaption = new Caption(stringHandler.generateNewString());
		textCaption.setPreferredHeight(this.getHeight());
		textCaption.setPreferredWidth(this.getWidth());
		textElement = sceneElementFactory.get(new SceneElement(textCaption));
		textElement.setEnabled(false);
	}

	@Override
	public DrawableGO<?> processAction(InputAction<?> action) {
		super.processAction(action);
		if (action instanceof KeyInputAction) {
			KeyInputAction keyAction = (KeyInputAction) action;
			if (keyAction.getType() == KeyEventType.KEY_TYPED) {
				switch (keyAction.getKeyCode()) {
				case BACKSPACE:
					if (currentText.length() > 0) {
						currentText = currentText.substring(0, currentText
								.length() - 2);
					}
					break;
				default:
					if (keyAction.getCharacter() != null) {
						currentText += keyAction.getCharacter();
					}
				}
				stringHandler.setString(textCaption.getLabel(), currentText);
			}
			action.consume();
			return this;
		} else if (action instanceof MouseInputAction) {
			MouseInputAction mouseAction = (MouseInputAction) action;
			if (mouseAction.getType() == MouseGEvType.PRESSED) {
				gameState.getValueMap().setValue(SystemFields.ACTIVE_ELEMENT,
						getElement());
				action.consume();
				return this;
			}
		}
		return null;

	}

	public void update() {
		super.update();
		textElement.update();
	}

	@Override
	public void doLayout(EAdTransformation transformation) {
		super.doLayout(transformation);
		gui.addElement(textElement, transformation);
	}

}
