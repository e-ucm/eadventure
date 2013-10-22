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
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.operations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeFieldGO extends AbstractEffectGO<ChangeFieldEf> {

	private static final Logger logger = LoggerFactory
			.getLogger(ChangeFieldGO.class);

	@Inject
	public ChangeFieldGO(Game game) {
		super(game);
	}

	@Override
	public void initialize() {
		BasicElement owner = effect.getElement() == null ? this.parent : effect
				.getElement();
		String varName = effect.getVarName();
		Operation operation = effect.getOperation();

		if (owner != null && varName != null && operation != null) {
			game.getGameState().setValue(owner, varName,
					game.getGameState().operate(operation));
		} else {
			logger
					.debug(
							"No proper parameters for change field: owner={}, varname={}, operation={}",
							owner, varName, operation);
		}
	}

}
