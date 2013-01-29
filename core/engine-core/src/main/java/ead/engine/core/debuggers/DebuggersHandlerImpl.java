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

package ead.engine.core.debuggers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.debuggers.FieldsDebugger;
import ead.common.model.elements.debuggers.GhostDebugger;
import ead.common.model.elements.debuggers.TrajectoryDebugger;
import ead.common.model.elements.scenes.SceneElement;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.platform.GUI;

@Singleton
public class DebuggersHandlerImpl implements DebuggersHandler {

	private static final Logger logger = LoggerFactory
			.getLogger("DebuggersHandler");

	public static final String TRAJECTORY_DEBUGGER = "trajectory_debugger";

	public static final String GHOST_DEBUGGER = "ghost_debugger";

	public static final String FIELDS_DEBUGGER = "fields_debugger";

	private GUI gui;

	private SceneElementGOFactory sceneElementFactory;

	private SceneElementGO<?> debuggersHud;

	private Map<String, SceneElementGO<?>> debuggers;

	@Inject
	public DebuggersHandlerImpl(SceneElementGOFactory sceneElementFactory,
			GUI gui) {
		this.gui = gui;
		this.sceneElementFactory = sceneElementFactory;
		this.debuggers = new HashMap<String, SceneElementGO<?>>();
	}

	@Override
	public void toggleDebugger(String debuggerId) {

		logger.info("{} toggled", debuggerId);

		if (debuggersHud == null) {
			debuggersHud = gui.getHUD(GUI.DEBBUGERS_HUD_ID);
		}

		SceneElementGO<?> d = debuggers.get(debuggerId);
		if (d == null) {
			d = createDebugger(debuggerId);
			debuggers.put(debuggerId, d);
		}

		if (debuggersHud.getChildren().contains(d)) {
			d.remove();
		} else {
			debuggersHud.addSceneElement(d);
		}

	}

	private SceneElementGO<?> createDebugger(String id) {
		SceneElement e = null;
		if (id.equals(TRAJECTORY_DEBUGGER)) {
			e = new TrajectoryDebugger();
		} else if (id.equals(GHOST_DEBUGGER)) {
			e = new GhostDebugger();
		} else if (id.equals(FIELDS_DEBUGGER)) {
			e = new FieldsDebugger();
		}
		e.setId(id);
		return sceneElementFactory.get(e);
	}
}
