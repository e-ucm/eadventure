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

package es.eucm.ead.engine.gameobjects.effects;

import com.google.inject.Inject;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.model.elements.EAdCondition;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.extra.EAdList;

public class TriggerMacroGO extends AbstractEffectGO<TriggerMacroEf> {

	@Inject
	public TriggerMacroGO(Game game) {
		super(game);
	}

	@Override
	public void initialize() {
		super.initialize();

		EAdList<EAdEffect> macro = null;

		for (int i = 0; i < effect.getMacros().size() && macro == null; i++) {
			EAdCondition c = effect.getConditions().get(i);
			if (game.getGameState().evaluate(c)) {
				macro = effect.getMacros().get(i);
				break;
			}
		}

		if (macro != null) {
			for (EAdEffect e : macro) {
				game.addEffect(e, action, parent);
			}
		}
	}

}
