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

package ead.engine.core.gameobjects.debuggers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.assets.text.BasicFont;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.fonts.FontHandler;
import ead.engine.core.assets.fonts.RuntimeFont;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;

@Singleton
public class ChangeSceneDebugger extends SceneElementGO {

	private Game game;

	private FontHandler fontHandler;

	private EAdScene[] scenes;

	private SelectBox selectBox;

	@Inject
	public ChangeSceneDebugger(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory, Game game,
			FontHandler fontHandler) {
		super(assetHandler, sceneElementFactory, gui, gameState, eventFactory);
		this.game = game;
		this.fontHandler = fontHandler;
	}

	public void setElement(EAdSceneElement e) {
		super.setElement(e);
		setPosition(10, 10);
		scenes = new EAdScene[game.getCurrentChapter().getScenes().size()];
		int i = 0;
		for (EAdScene s : game.getCurrentChapter().getScenes()) {
			scenes[i] = s;
			i++;
		}
		SelectBox.SelectBoxStyle style = new SelectBoxStyle();
		Pixmap p = new Pixmap(200, 20, Pixmap.Format.RGBA8888);
		p.setColor(1.0f, 0.0f, 0.0f, 0.5f);
		p.fillRectangle(0, 0, 200, 20);

		TextureRegion t1 = new TextureRegion(new Texture(p));
		p.dispose();

		p = new Pixmap(200, 20, Pixmap.Format.RGBA8888);
		p.setColor(0.0f, 1.0f, 0.0f, 1.0f);
		p.fillRectangle(0, 0, 200, 20);

		TextureRegion t2 = new TextureRegion(new Texture(p));
		p.dispose();

		p = new Pixmap(200, 20, Pixmap.Format.RGBA8888);
		p.setColor(0.0f, 0.0f, 1.0f, 1.0f);
		p.fillRectangle(0, 0, 200, 20);

		TextureRegion t3 = new TextureRegion(new Texture(p));
		p.dispose();

		RuntimeFont f = fontHandler.get(BasicFont.REGULAR);
		style.font = f.getBitmapFont();
		style.background = new TextureRegionDrawable(t1);
		style.background.setTopHeight(0);
		style.background.setBottomHeight(0);
		style.listBackground = new TextureRegionDrawable(t2);
		style.listSelection = new TextureRegionDrawable(t3);
		style.fontColor = Color.BLACK;
		selectBox = new SelectBox(scenes, style);
		addActor(selectBox);

		selectBox.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				EAdScene selectScene = scenes[selectBox.getSelectionIndex()];
				EAdScene currentScene = (EAdScene) gui.getScene().getElement();
				if (selectScene != currentScene) {
					gameState.addEffect(new ChangeSceneEf(selectScene), null,
							null);
				}
			}

		});
	}
}
