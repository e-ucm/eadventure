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

package ead.common.model.elements.scenes;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.trajectories.EAdTrajectoryDefinition;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.VarDef;
import ead.common.params.fills.ColorFill;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition.Corner;

/**
 * <p>
 * Default implementation of the {@link EAdScene} interface
 * </p>
 * 
 */
@Element(runtime = BasicScene.class, detailed = BasicScene.class)
public class BasicScene extends ComplexSceneElement implements EAdScene {

	public static final EAdVarDef<Boolean> VAR_SCENE_LOADED = new VarDef<Boolean>(
			"scene_loaded", Boolean.class, Boolean.FALSE);

	public static final EAdVarDef<EAdTrajectoryDefinition> VAR_TRAJECTORY_DEFINITION = new VarDef<EAdTrajectoryDefinition>(
			"trajectory_generator", EAdTrajectoryDefinition.class, null);

	@Param("background")
	protected EAdSceneElement background;

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
	public BasicScene() {
		this(new RectangleShape(800, 600, ColorFill.BLACK));
	}

	public BasicScene(EAdDrawable backgroundDrawable) {
		super();
		background = new SceneElement(backgroundDrawable);
		background.setId("background");
		background.setPosition(Corner.TOP_LEFT, 0, 0);
		returnable = true;
		setCenter(Corner.TOP_LEFT);
	}

	public EAdSceneElement getBackground() {
		return background;
	}

	public void setBackground(EAdSceneElement background) {
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
			EAdTrajectoryDefinition trajectoryDefinition) {
		this.setVarInitialValue(VAR_TRAJECTORY_DEFINITION, trajectoryDefinition);
	}

	/**
	 * Adds an scene element to the scene
	 * 
	 * @param element
	 *            the element to be added
	 */
	public void add(SceneElement element) {
		this.getSceneElements().add(element);
	}

}
