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

package es.eucm.ead.engine.tracking;

import com.badlogic.gdx.utils.Array;
import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.game.GameState.FieldWatcher;
import es.eucm.ead.model.elements.AdventureGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGameTracker implements GameTracker, FieldWatcher {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractGameTracker.class);

	protected GameState gameState;

	protected AdventureGame model;

	private Array<String> fields;

	private boolean tracking;

	public AbstractGameTracker(GameState gameState) {
		this.gameState = gameState;
		this.fields = new Array<String>();
	}

	@Override
	public void startTracking(AdventureGame model) {
		this.model = model;
		tracking = startTrackingImpl(model);
	}

	/**
	 * Starts the tracking
	 *
	 * @param model the model of the game
	 * @return if tracking was successfully started
	 */
	protected abstract boolean startTrackingImpl(AdventureGame model);

	/**
	 * @return if the tracker is successfully sending traces
	 */
	public boolean isTracking() {
		return tracking;
	}

	/**
	 * Stops the tracking
	 */
	public void stop() {
		tracking = false;
	}

	/**
	 * A watched field has been update
	 *
	 * @param elementId      the object owner of the field
	 * @param varName the variable
	 * @param value   the new value for the variable in the object
	 * @param <T>     the class of the value
	 */
	public <T> boolean setField(String elementId, String varName, T value) {
		String field = (elementId != null ? elementId + "." : "") + varName;
		if (fields.contains(field, false)) {
			varUpdate(field, value);
		}
		return false;
	}

	/**
	 * Watches a field
	 *
	 * @param fieldName the field name
	 */
	public void watchField(String fieldName) {
		String[] parts = fieldName.split("\\.");
		switch (parts.length) {
		case 1:
			gameState.addFieldWatcher(this, null);
			fields.add("." + parts[0]);
			break;
		case 2:
			gameState.addFieldWatcher(parts[0], this);
			fields.add(parts[0] + "." + parts[1]);
			break;
		default:
			logger.warn("{} is not a valid field name. It won't be watched.",
					fieldName);
			break;
		}
	}

}
