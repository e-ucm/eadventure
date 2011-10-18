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

package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.common.model.impl.EAdElementImpl;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;

@Element(detailed = EAdTimerImpl.class, runtime = EAdTimerImpl.class)
public class EAdTimerImpl extends EAdElementImpl implements EAdTimer {

	public static final EAdVarDef<Boolean> VAR_STARTED = new EAdVarDefImpl<Boolean>(
			"started", Boolean.class, Boolean.FALSE);
	
	public static final EAdVarDef<Boolean> VAR_ENDED = new EAdVarDefImpl<Boolean>(
			"ended", Boolean.class, Boolean.FALSE);

	/**
	 * Time in millisecons
	 */
	@Param("time")
	private Integer time;

	public EAdTimerImpl(String id) {
		super(id);
		time = 5000;
	}

	@Override
	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	@Override
	public EAdElement copy() {
		return copy( false );
	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		EAdTimerImpl t = new EAdTimerImpl( id );
		t.setTime(time);
		return t;
	}

}
