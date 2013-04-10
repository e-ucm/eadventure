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

package ead.converter.subconverters.actors;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.compounds.StateDrawable;
import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.converter.UtilsConverter;
import ead.converter.resources.ResourceConverter;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

@Singleton
public abstract class ElementConverter {

	protected ResourceConverter resourceConverter;

	protected UtilsConverter utilsConverter;

	@Inject
	public ElementConverter(ResourceConverter resourceConverter,
			UtilsConverter utilsConverter) {
		this.resourceConverter = resourceConverter;
		this.utilsConverter = utilsConverter;
	}

	public EAdSceneElementDef convert(Element a) {
		SceneElementDef definition = new SceneElementDef();
		definition.setId(a.getId());
		convert(a, getResourceType(), definition,
				ResourcedElement.INITIAL_BUNDLE, SceneElementDef.appearance);
		return definition;
	}

	protected EAdSceneElementDef convert(Element a, String resourceType,
			EAdSceneElementDef definition, String bundle, String resourceId) {
		// One state for each bundle
		StateDrawable stateDrawable = new StateDrawable();
		int i = 0;
		for (Resources r : a.getResources()) {
			EAdDrawable drawable = getDrawable(r, resourceType);
			// The item has no over appearance
			if (drawable != null) {
				stateDrawable.addDrawable(
						utilsConverter.getResourceBundleId(i), drawable);
				if (i == 0) {
					definition.setVarInitialValue(SceneElement.VAR_STATE,
							utilsConverter.getResourceBundleId(i));
				}
			}
			i++;
		}

		definition.addAsset(bundle, resourceId, utilsConverter
				.simplifyStateDrawable(stateDrawable));

		// Add conditioned resources
		// The variable that changes is IN THE SCENE ELEMENT DEFINITION, so
		// every element that refers to this actor, must watch this field in
		// order to update its own state
		utilsConverter.addResourcesConditions(a.getResources(), definition,
				SceneElement.VAR_STATE);
		return definition;
	}

	protected EAdDrawable getDrawable(Resources r, String resourceId) {
		String assetPath = r.getAssetPath(resourceId);
		return assetPath == null ? null : resourceConverter.getImage(assetPath);
	}

	protected abstract String getResourceType();
}
