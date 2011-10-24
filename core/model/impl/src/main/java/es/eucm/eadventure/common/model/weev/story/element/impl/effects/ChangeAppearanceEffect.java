package es.eucm.eadventure.common.model.weev.story.element.impl.effects;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.interfaces.features.Resourced;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractEffect;
import es.eucm.eadventure.common.resources.EAdBundleId;

/**
 * Change an element (e.g. actor, scene, etc.) appearance effect
 */
@Element(detailed = ChangeAppearanceEffect.class, runtime = ChangeAppearanceEffect.class)
public class ChangeAppearanceEffect extends AbstractEffect {

	/**
	 * The {@link Resourced} element whose appearance can be changed
	 */
	@Param(value = "element")
	private Resourced element;

	/**
	 * The {@link EAdBundleId} of the appearance to be set
	 */
	@Param(value = "bundleId")
	private EAdBundleId bundleId;

	/**
	 * @param element
	 *            The {@link Resourced} element whose appearance can be changed
	 * @param bundleId
	 *            The {@link EAdBundleId} of the appearance to be set
	 */
	public ChangeAppearanceEffect(Resourced element, EAdBundleId bundleId) {
		this.element = element;
		this.bundleId = bundleId;
	}

	public Resourced getElement() {
		return element;
	}

	public void setElement(Resourced element) {
		this.element = element;
	}

	public EAdBundleId getBundleId() {
		return bundleId;
	}

	public void setBundleId(EAdBundleId bundleId) {
		this.bundleId = bundleId;
	}

}
