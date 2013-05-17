package ead.engine.core.gdx.desktop.debugger.components;

import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.scenes.EAdScene;
import ead.engine.core.gdx.desktop.debugger.hooks.ChapterLoadedHook;
import ead.engine.core.gdx.desktop.debugger.hooks.DebuggerHook;
import ead.engine.core.gdx.desktop.debugger.hooks.EffectsHook;

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
