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

package ead.engine.core.gdx.desktop.debugger;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.scenes.EAdScene;
import ead.engine.core.game.GameImpl;
import ead.engine.core.game.enginefilters.EngineHook;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;

public class DebuggerFrame extends JFrame {

	private static final long serialVersionUID = -7678228724129188732L;

	private Game game;

	private GameState gameState;

	private GUI gui;

	private EffectsHook effectsHook;

	public DebuggerFrame(Game g, GameState gameState, GUI gui) {
		setTitle("eAdventure debugger");
		this.game = g;
		effectsHook = new EffectsHook();
		this.game.addHook(GameImpl.HOOK_AFTER_UPDATE, effectsHook);
		game.addHook(GameImpl.HOOK_AFTER_MODEL_READ, new ModelLoadedHook());
		this.gameState = gameState;
		this.gui = gui;
		setSize(400, 300);
	}

	public class EffectsHook implements EngineHook {

		private List<EAdEffect> effects;

		public EffectsHook() {
			effects = new ArrayList<EAdEffect>();
		}

		public void addEffect(EAdEffect e) {
			synchronized (effects) {
				effects.add(e);
			}
		}

		@Override
		public int compareTo(EngineHook o) {
			return 0;
		}

		@Override
		public void execute(Game game, GameState gameState, GUI gui) {
			synchronized (gameState) {
				while (!effects.isEmpty()) {
					EAdEffect e = effects.remove(0);
					gameState.addEffect(e);
				}
			}
		}

	}

	public class ModelLoadedHook implements EngineHook {

		@Override
		public int compareTo(EngineHook o) {
			return 0;
		}

		@Override
		public void execute(Game g, GameState gameState, GUI gui) {
			EAdChapter c = game.getCurrentChapter();
			String[] scenes = new String[c.getScenes().size()];
			int i = 0;
			for (EAdScene s : c.getScenes()) {
				scenes[i++] = s.getId();
			}
			JComboBox box = new JComboBox(scenes);
			box.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					ChangeSceneEf changeScene = new ChangeSceneEf(game
							.getCurrentChapter().getSceneById(
									e.getItem().toString()));

					effectsHook.addEffect(changeScene);

				}

			});
			add(box);
			DebuggerFrame.this.repaint();
			DebuggerFrame.this.invalidate();
		}

	}
}
