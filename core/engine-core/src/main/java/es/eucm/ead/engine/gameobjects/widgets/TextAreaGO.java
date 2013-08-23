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

package es.eucm.ead.engine.gameobjects.widgets;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.tools.StringHandler;

public class TextAreaGO extends SceneElementGO {

	private String currentText;

	private Caption textCaption;

	private SceneElementGO textElement;

	private StringHandler stringHandler;

	@Inject
	public TextAreaGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, Game game,
			EventGOFactory eventFactory, StringHandler stringHandler) {
		super(assetHandler, sceneElementFactory, game, eventFactory);
		this.stringHandler = stringHandler;
	}

	@Override
	public void setElement(EAdSceneElement element) {
		super.setElement(element);
		textCaption = new Caption(stringHandler.generateNewString());
		textCaption.setPreferredHeight((int) this.getHeight());
		textCaption.setPreferredWidth((int) this.getWidth());
		textElement = sceneElementFactory.get(new SceneElement(textCaption));
		textElement.setTouchable(Touchable.disabled);
	}

	public void act(float delta) {
		super.act(delta);
		textElement.act(delta);
	}

}
