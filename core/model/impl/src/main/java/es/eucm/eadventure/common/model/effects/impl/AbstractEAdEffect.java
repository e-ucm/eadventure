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

package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;
import es.eucm.eadventure.common.model.impl.AbstractEAdConditionedElement;
import es.eucm.eadventure.common.model.variables.EAdVarDef;

/**
 * <p>
 * Abstract implementation of {@link EAdEffect} which includes the basic methods
 * </p>
 * 
 * 
 */
public abstract class AbstractEAdEffect extends AbstractEAdConditionedElement
		implements EAdEffect {

	/**
	 * Indicates that this effect blocks the effect queue until finished
	 */
	@Param("blocking")
	private boolean blocking;

	/**
	 * Indicates that this effect is opaque and captures the interactions in the
	 * screen
	 */
	@Param("opaque")
	private boolean opaque;

	/**
	 * Indicates that this effect is queued and can be blocked
	 */
	@Param("queueable")
	private boolean queueable;

	@Param("events")
	private EAdList<EAdEvent> events;

	@Param("initialVars")
	private EAdMap<EAdVarDef<?>, Object> initialVars;

	@Param("nextEffects")
	private EAdList<EAdEffect> nextEffects;

	/**
	 * Creates an non-blocking and non-opaque effect with next effects list
	 * empty
	 * 
	 * @param parent
	 *            parent for this element
	 * @param id
	 *            id for this element
	 */
	public AbstractEAdEffect(String id) {
		super(id);
		blocking = false;
		opaque = false;
		queueable = false;
		events = new EAdListImpl<EAdEvent>(EAdEvent.class);
		initialVars = new EAdMapImpl<EAdVarDef<?>, Object>(EAdVarDef.class,
				Object.class);
		nextEffects = new EAdListImpl<EAdEffect>(EAdEffect.class);
	}

	public AbstractEAdEffect() {
		this("effect");
	}

	/**
	 * Sets blocking property for this effect. {@code true} means no subsequent
	 * effects will be triggered until this effect is finished. By default, this
	 * value is set to {@code false}
	 * 
	 * @param blocking
	 */
	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	/**
	 * Sets opaque property for this effect. {@code true} means that GUI events
	 * will be only processed for this effect and those which were over it. By
	 * default, this value is set to {@code true}
	 * 
	 * @param opaque
	 */
	public void setOpaque(boolean opaque) {
		this.opaque = opaque;
	}

	public void setQueueable(boolean queueable) {
		this.queueable = queueable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.effects.EAdEffect#isBlocking()
	 */
	@Override
	public boolean isBlocking() {
		return blocking;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.effects.EAdEffect#isOpaque()
	 */
	@Override
	public boolean isOpaque() {
		return opaque;
	}

	@Override
	public boolean isQueueable() {
		return queueable;
	}

	public EAdList<EAdEvent> getEvents() {
		return events;
	}

	public EAdMap<EAdVarDef<?>, Object> getVars() {
		return initialVars;
	}

	public <T> void setVarInitialValue(EAdVarDef<T> var, T value) {
		initialVars.put(var, value);
	}

	public EAdList<EAdEffect> getFinalEffects() {
		return nextEffects;
	}

}
