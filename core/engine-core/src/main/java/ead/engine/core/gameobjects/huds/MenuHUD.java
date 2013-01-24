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

package ead.engine.core.gameobjects.huds;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.QuitGameEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.fills.Paint;
import ead.common.params.guievents.EAdGUIEvent;
import ead.common.params.guievents.KeyGEv;
import ead.common.params.guievents.MouseGEv;
import ead.common.params.guievents.enums.KeyEventType;
import ead.common.params.guievents.enums.KeyGEvCode;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.text.BasicFont;
import ead.common.util.EAdPosition;
import ead.common.widgets.containers.ColumnContainer;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.game.enginefilters.EngineFilter;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

/**
 * <p>
 * Abstract implementation of the Menu HUD
 * </p>
 * 
 */
public class MenuHUD extends AbstractHUD implements
		EngineFilter<InputAction<?>> {

	private static final EAdGUIEvent MENU_EVENT = new KeyGEv(
			KeyEventType.KEY_PRESSED, KeyGEvCode.ESCAPE);
	public static String ID = "Hud.Menu";

	public MenuHUD(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(ID, assetHandler, gameObjectFactory, gui, gameState,
				eventFactory, 100);
	}

	public void init() {

		ColumnContainer menu = new ColumnContainer();
		menu.setId(ID);
		menu.setPosition(new EAdPosition(EAdPosition.Corner.CENTER, 400, 300));

		EAdString exitString = new EAdString("engine.Exit");

		menu.add(createMenuButton(exitString, new QuitGameEf()));

		// Languages
		String languagesProperty = gameState.getValue(SystemFields.LANGUAGES);

		EAdString defaultLanguage = new EAdString("engine.language");

		menu.add(createMenuButton(defaultLanguage, new ChangeFieldEf(
				SystemFields.LANGUAGE, new ValueOp(""))));

		String[] languages = languagesProperty.split(",");
		for (String language : languages) {
			EAdString languageString = new EAdString("engine.language."
					+ language);

			menu.add(createMenuButton(languageString, new ChangeFieldEf(
					SystemFields.LANGUAGE, new ValueOp(language))));
		}

		BasicField<Boolean> visibleField = new BasicField<Boolean>(menu,
				SceneElement.VAR_VISIBLE);

		EAdString resumeString = new EAdString("engine.Resume");

		menu.add(createMenuButton(resumeString, new ChangeFieldEf(visibleField,
				BooleanOp.FALSE_OP)));

		menu.setVarInitialValue(SceneElement.VAR_VISIBLE, false);

		setElement(menu);

		BasicField<Boolean> field = new BasicField<Boolean>(this.getElement(),
				SceneElement.VAR_VISIBLE);

		menu.addBehavior(MENU_EVENT, new ChangeFieldEf(field, new BooleanOp(
				new NOTCond(new OperationCond(field)))));
	}

	public EAdSceneElement createMenuButton(EAdString string, EAdEffect effect) {
		Caption c1 = new Caption(string);
		Caption c2 = new Caption(string);

		SceneElement button = new SceneElement(c1, c2);
		button.setId(string.toString());

		c1.setBubblePaint(new Paint(new LinearGradientFill(ColorFill.ORANGE,
				new ColorFill(255, 200, 0), 0, 40), ColorFill.BLACK, 2));
		c1.setPadding(10);
		c1.setFont(new BasicFont(18));

		c2
				.setBubblePaint(new Paint(new LinearGradientFill(new ColorFill(
						255, 200, 0), new ColorFill(255, 150, 0), 0, 40),
						ColorFill.BLACK, 3));
		c2.setPadding(11);
		c2.setFont(new BasicFont(18));

		button.getBehavior().addBehavior(MouseGEv.MOUSE_LEFT_CLICK, effect);
		return button;
	}

	public void update() {
		super.update();
		gameState.setPaused(isVisible());
	}

	@Override
	public int compareTo(EngineFilter<?> o) {
		return this.getPriority() - o.getPriority();
	}

	@Override
	public InputAction<?> filter(InputAction<?> o, Object[] params) {
		if (MENU_EVENT.equals(o.getGUIEvent())) {
			processAction(o);
		}
		return o;
	}

}
