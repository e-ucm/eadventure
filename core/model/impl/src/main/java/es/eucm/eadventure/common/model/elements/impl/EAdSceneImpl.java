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
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;

/**
 * <p>
 * Default implementation of the {@link EAdScene} interface
 * </p>
 * 
 */
@Element(runtime = EAdSceneImpl.class, detailed = EAdSceneImpl.class)
public class EAdSceneImpl extends EAdComplexElementImpl implements EAdScene {

	public static final EAdVarDef<Boolean> VAR_SCENE_LOADED = new EAdVarDefImpl<Boolean>(
			"scene_loaded", Boolean.class, Boolean.FALSE);

	public static final EAdVarDef<TrajectoryDefinition> VAR_TRAJECTORY_DEFINITION = new EAdVarDefImpl<TrajectoryDefinition>(
			"trajectory_generator", TrajectoryDefinition.class, null);

	@Param("background")
	protected EAdBasicSceneElement background;

	@Param(value = "acceptsVisualEffects", defaultValue = "true")
	protected Boolean acceptsVisualEffects;

	/**
	 * This property indicates if the game can return to this scene after a
	 * cutscene or similiar
	 */
	@Param(value = "returnable", defaultValue = "true")
	protected Boolean returnable;

	/**
	 * Default constructor.
	 * 
	 * @param parent
	 *            The parent element in the model
	 */
	public EAdSceneImpl() {
		this(null);
	}

	public EAdSceneImpl(Drawable backgroundDrawable) {
		super();
		background = new EAdBasicSceneElement(backgroundDrawable);
		background.setId("background");
		returnable = true;
		acceptsVisualEffects = true;
	}

	public EAdBasicSceneElement getBackground() {
		return background;
	}

	public void setBackground(EAdBasicSceneElement background) {
		this.background = background;
	}

	@Override
	public Boolean getReturnable() {
		return returnable;
	}

	public void setReturnable(Boolean returnable) {
		this.returnable = returnable;
	}

	public void setTrajectoryDefinition(
			TrajectoryDefinition trajectoryDefinition) {
		this.setVarInitialValue(VAR_TRAJECTORY_DEFINITION, trajectoryDefinition);
	}

	@Override
	public Boolean getAcceptsVisualEffects() {
		return acceptsVisualEffects;
	}

	public void setAcceptsVisualEffects(Boolean acceptsVisualEffects) {
		this.acceptsVisualEffects = acceptsVisualEffects;
	}

}
