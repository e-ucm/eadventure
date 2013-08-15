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

package es.eucm.ead.engine.tracking.selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.ead.engine.gameobjects.effects.EffectGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.params.guievents.EAdGUIEvent;
import es.eucm.ead.model.params.guievents.MouseGEv;

@Singleton
public class DefaultTrackerSelector implements TrackerSelector {

	private static Map<String, Class<?>> effectCorrespondencies;

	private static Map<String, EAdGUIEvent> actionCorrespondencies;

	static {
		effectCorrespondencies = new HashMap<String, Class<?>>();
		effectCorrespondencies.put("changeScene", ChangeSceneEf.class);
		effectCorrespondencies.put("changeField", ChangeFieldEf.class);

		actionCorrespondencies = new HashMap<String, EAdGUIEvent>();
		actionCorrespondencies.put("lpressed", MouseGEv.MOUSE_LEFT_PRESSED);
		actionCorrespondencies.put("rpressed", MouseGEv.MOUSE_RIGHT_PRESSED);

	}

	private List<Class<?>> effectsAccepted;

	private List<String> varsAccepted;

	private List<EAdGUIEvent> actionsAccepted;

	@Inject
	public DefaultTrackerSelector() {

	}

	public void setSelection(List<String> text) {
		this.effectsAccepted = new ArrayList<Class<?>>();
		this.varsAccepted = new ArrayList<String>();
		this.actionsAccepted = new ArrayList<EAdGUIEvent>();
		if (text != null) {
			for (String s : text) {
				String[] parts = s.split(";");
				if (parts[0].equals("actions")) {
					for (int i = 1; i < parts.length; i++) {
						EAdGUIEvent ev = actionCorrespondencies.get(parts[i]);
						if (ev != null) {
							actionsAccepted.add(ev);
						}

					}
				} else {
					Class<?> effectClass = effectCorrespondencies.get(parts[0]);
					if (effectClass != null) {
						effectsAccepted.add(effectClass);
						if (parts[0].equals("changeField")) {
							for (int i = 0; i < parts.length; i++) {
								varsAccepted.add(parts[i]);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public boolean accept(Event action, SceneElementGO target) {
		//		return actionsAccepted.contains(action.getGUIEvent());
		return true;
	}

	@Override
	public boolean accept(EffectGO<?> effect) {
		Class<?> c = effect.getElement().getClass();
		if (effectsAccepted.contains(c)) {
			if (effect.getElement() instanceof ChangeFieldEf) {
				ChangeFieldEf cf = (ChangeFieldEf) effect.getElement();
				for (EAdField<?> f : cf.getFields()) {
					if (varsAccepted.contains(f.getVarDef().getName())) {
						return true;
					}
				}
				return false;
			}
			return true;
		}
		return false;
	}

}
