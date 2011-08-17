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
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.impl.EAdGeneralElementImpl;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.resources.EAdString;

/**
 * <p>Default implementation of the {@link EAdScene} interface</p>
 *
 */
@Element(runtime = EAdSceneImpl.class, detailed = EAdSceneImpl.class)
public class EAdSceneImpl extends EAdGeneralElementImpl implements EAdScene {
	
	@Param("name")
	private EAdString name;
	
	@Param("documentation")
	private EAdString documentation;

	@Param("background")
	protected EAdBasicSceneElement background;
	
	@Param("trajectoryGenerator")
	protected TrajectoryDefinition trajectoryGenerator;
	
	@Param("acceptsVisualEffects")
	protected boolean acceptsVisualEffects;
	

	/**
	 * This property indicates if the game can return to this scene after a cutscene or similiar
	 */
	@Param("returnable")
	protected boolean returnable;
	
	private EAdList<EAdSceneElement> sceneElements;
	
	private BooleanVar sceneLoaded;
	
	/**
	 * Default constructor.
	 * 
	 * @param parent The parent element in the model
	 */
	public EAdSceneImpl(String id) {
		super(id);
		sceneElements = new EAdListImpl<EAdSceneElement>(EAdSceneElement.class);
		background = new EAdBasicSceneElement(id + "_background");
		returnable = true;
		sceneLoaded = new BooleanVar("sceneLoaded");
		acceptsVisualEffects = true;
	}

	@Override
	public EAdList<EAdSceneElement> getSceneElements() {
		return sceneElements;
	}

	/**
	 * @return the name
	 */
	public EAdString getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(EAdString name) {
		this.name = name;
	}

	/**
	 * @return the documentation
	 */
	public EAdString getDocumentation() {
		return documentation;
	}

	/**
	 * @param documentation the documentation to set
	 */
	public void setDocumentation(EAdString documentation) {
		this.documentation = documentation;
	}	
	
	public EAdBasicSceneElement getBackground() {
		return background;
	}
	
	public void setBackground(EAdBasicSceneElement background) {
		this.background = background;
	}

	@Override
	public boolean isReturnable() {
		return returnable;
	}

	public void setReturnable(boolean returnable) {
		this.returnable = returnable;
	}

	@Override
	public BooleanVar sceneLoaded() {
		return sceneLoaded;
	}
	
	public void setTrajectoryGenerator(TrajectoryDefinition trajectoryGenerator){
		this.trajectoryGenerator = trajectoryGenerator;
	}

	@Override
	public TrajectoryDefinition getTrajectoryDefinition() {
		return trajectoryGenerator;
	}

	@Override
	public boolean acceptsVisualEffects() {
		return acceptsVisualEffects;
	}

}
