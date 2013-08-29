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

package es.eucm.ead.engine.desktop.debugger;

import es.eucm.ead.engine.desktop.debugger.components.ChangeSceneComboBox;
import es.eucm.ead.engine.desktop.debugger.hooks.ChapterLoadedHook;
import es.eucm.ead.engine.desktop.debugger.hooks.EffectsHook;
import es.eucm.ead.engine.desktop.debugger.hooks.ModelLoadedHook;
import es.eucm.ead.engine.game.GameImpl;
import es.eucm.ead.engine.game.interfaces.EngineHook;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.model.elements.debuggers.FieldsDebugger;
import es.eucm.ead.model.elements.debuggers.GhostDebugger;
import es.eucm.ead.model.elements.debuggers.TrajectoryDebugger;
import es.eucm.ead.model.elements.effects.AddChildEf;
import es.eucm.ead.model.elements.effects.RemoveEf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class DebuggerFrame extends JFrame {

	private static final long serialVersionUID = -7678228724129188732L;

	private Map<String, EngineHook> hooks;

	private Game game;

	private EffectsHook effectsHook;

	private ModelLoadedHook modelLoadedHook;

	private ChapterLoadedHook chapterLoadedHook;

	public DebuggerFrame(Game g) {
		setTitle("eAdventure Debugger");
		this.game = g;
		hooks = new HashMap<String, EngineHook>();
		hooks.put(GameImpl.HOOK_AFTER_UPDATE, effectsHook = new EffectsHook());
		hooks.put(GameImpl.HOOK_AFTER_MODEL_READ,
				modelLoadedHook = new ModelLoadedHook());
		hooks.put(GameImpl.HOOK_AFTER_CHAPTER_READ,
				chapterLoadedHook = new ChapterLoadedHook());
		for (Map.Entry<String, EngineHook> e : hooks.entrySet()) {
			game.addHook(e.getKey(), e.getValue());
		}

		JComboBox changeScene = new ChangeSceneComboBox(effectsHook,
				chapterLoadedHook);

		// Trajectories
		final JCheckBox showTrajectory = new JCheckBox("Show trajectories");
		final TrajectoryDebugger trajectoryDebugger = new TrajectoryDebugger();
		trajectoryDebugger.setId("#trajectory_debugger");
		showTrajectory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (showTrajectory.isSelected()) {
					effectsHook.addEffect(new AddChildEf(GUI.DEBBUGERS_HUD_ID,
							trajectoryDebugger));
				} else {
					effectsHook.addEffect(new RemoveEf(trajectoryDebugger));
				}
			}
		});

		// Ghost elements
		final JCheckBox showGhostElements = new JCheckBox(
				"Show ghost elements (Active Areas and Exits)");
		final GhostDebugger ghostDebugger = new GhostDebugger();
		ghostDebugger.setId("#ghost_debugger");
		showGhostElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (showGhostElements.isSelected()) {
					effectsHook.addEffect(new AddChildEf(GUI.DEBBUGERS_HUD_ID,
							ghostDebugger));
				} else {
					effectsHook.addEffect(new RemoveEf(ghostDebugger));
				}
			}
		});

		// Ghost elements
		final JCheckBox showFields = new JCheckBox("Show fields");
		final FieldsDebugger fieldDebugger = new FieldsDebugger();
		fieldDebugger.setId("#fields_debugger");
		showFields.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (showFields.isSelected()) {
					effectsHook.addEffect(new AddChildEf(GUI.DEBBUGERS_HUD_ID,
							fieldDebugger));
				} else {
					effectsHook.addEffect(new RemoveEf(fieldDebugger));
				}
			}
		});

		// Skip videos
		final JCheckBox skipVideos = new JCheckBox("Skip videos");
		skipVideos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				/*if (skipVideos.isSelected()) {
					game.getSceneElementFactory().put(VideoScene.class,
							SkipVideoSceneGO.class);
				} else {
					game.getSceneElementFactory().put(VideoScene.class,
							VideoSceneGO.class);
				}*/
			}
		});
		GridBagLayout layout = new GridBagLayout();
		JPanel panel = new JPanel(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(changeScene, c);
		c.gridy = 1;
		panel.add(showTrajectory, c);
		c.gridy = 2;
		panel.add(showGhostElements, c);
		c.gridy = 3;
		panel.add(showFields, c);
		c.gridy = 4;
		panel.add(skipVideos, c);
		this.add(panel);
		pack();
	}

}
