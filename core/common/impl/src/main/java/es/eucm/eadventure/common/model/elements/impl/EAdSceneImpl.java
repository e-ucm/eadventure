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

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.impl.AbstractEAdElement;
import es.eucm.eadventure.common.model.impl.EAdElementListImpl;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.resources.EAdString;

/**
 * @author eugenio
 *
 */
@Element(runtime = EAdSceneImpl.class, detailed = EAdSceneImpl.class)
public class EAdSceneImpl extends AbstractEAdElement implements EAdScene {
	
	@Param("name")
	private EAdString name;
	
	@Param("documentation")
	private EAdString documentation;

	@Param("background")
	protected EAdBasicSceneElement background;

	/**
	 * This property indicates if the game can return to this scene after a cutscene or similiar
	 */
	@Param("returnable")
	protected boolean returnable;
	
	private EAdElementList<EAdSceneElement> actorReferences;
	
	private BooleanVar sceneLoaded;
	
	/**
	 * Default constructor.
	 * 
	 * @param parent The parent element in the model
	 */
	public EAdSceneImpl(String id) {
		super(id);
		actorReferences = new EAdElementListImpl<EAdSceneElement>();
		background = new EAdBasicSceneElement(id + "_background");
		returnable = false;
		sceneLoaded = new BooleanVar("sceneLoaded");
	}

	@Override
	public EAdElementList<EAdSceneElement> getSceneElements() {
		return actorReferences;
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
	
	@Override
	public BooleanVar sceneLoaded() {
		return sceneLoaded;
	}

}
