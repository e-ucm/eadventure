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
		// every element that
		// refers to this actor, must watch this field in order to update its
		// own state
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
