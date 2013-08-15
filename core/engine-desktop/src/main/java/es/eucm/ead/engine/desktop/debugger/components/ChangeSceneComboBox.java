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

package es.eucm.ead.engine.desktop.debugger.components;

import es.eucm.ead.engine.desktop.debugger.hooks.ChapterLoadedHook;
import es.eucm.ead.engine.desktop.debugger.hooks.EffectsHook;
import es.eucm.ead.model.elements.EAdChapter;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.engine.desktop.debugger.hooks.DebuggerHook;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Change scene debugger
 */
public class ChangeSceneComboBox extends JComboBox implements
		DebuggerHook.HookListener<EAdChapter> {

	private EffectsHook effects;

	private ScenesModel model;

	public ChangeSceneComboBox(EffectsHook effectsHook,
			ChapterLoadedHook chapterLoadedHook) {
		this.setModel(model = new ScenesModel());
		effects = effectsHook;
		chapterLoadedHook.addListener(this);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				EAdScene scene = (EAdScene) getSelectedItem();
				effects.addEffect(new ChangeSceneEf(scene));
			}
		});
	}

	@Override
	public void handle(EAdChapter element) {
		model.loadScenes(element);
	}

	public class ScenesModel extends AbstractListModel implements ComboBoxModel {

		private List<EAdScene> scenes;

		private EAdScene currentScene;

		public ScenesModel() {
			scenes = new ArrayList<EAdScene>();
		}

		public void loadScenes(EAdChapter chapter) {
			scenes.clear();
			for (EAdScene s : chapter.getScenes()) {
				scenes.add(s);
			}
		}

		@Override
		public void setSelectedItem(Object o) {
			currentScene = (EAdScene) o;
		}

		@Override
		public Object getSelectedItem() {
			return currentScene;
		}

		@Override
		public int getSize() {
			return scenes.size();
		}

		@Override
		public Object getElementAt(int i) {
			return scenes.get(i);
		}
	}
}
