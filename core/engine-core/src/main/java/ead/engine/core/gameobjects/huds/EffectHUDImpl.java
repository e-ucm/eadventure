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

package ead.engine.core.gameobjects.huds;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.EffectGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdTransformation;

/**
 * <p>
 * Default implementation of {@link EffectHUD}
 * </p>
 *
 */
@Singleton
public class EffectHUDImpl extends AbstractHUD implements EffectHUD {

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger("EffectHUDImpl");

	/**
	 * List of current {@link EffectGO}
	 */
	private List<EffectGO<?>> effects;

	@Inject
	public EffectHUDImpl(GUI gui) {
		super(gui);
		logger.info("New instance");
	}

	public void setEffects(List<EffectGO<?>> effects) {
		this.effects = effects;		
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#processAction(es
	 * .eucm.eadventure.engine.core.guiactions.GUIAction)
	 */
	@Override
	public DrawableGO<?> processAction(InputAction<?> action) {
		if (effects.size() > 0) {
			effects.get(0).processAction(action);
		}
		action.consume();
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.eucm.eadventure.engine.core.gameobjects.GameObject#doLayout()
	 */
	@Override
	public void doLayout(EAdTransformation transformation) {
		int i = 0;
		boolean block = false;
		while (i < effects.size() && !block) {
			EffectGO<?> e = effects.get(i);
			if (e.isOpaque()) {
				// Adding this layer, we avoid events going deeper

				// TODO this creates a problem, should do some other way
				// gui.addElement(this);
			}
			gui.addElement(e, transformation);
			block = e.isBlocking();
			i++;
		}
	}

}
