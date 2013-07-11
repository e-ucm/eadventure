package ead.plugins.engine.drag15;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.AbstractEffect;
import ead.common.model.elements.extra.EAdList;

/**
 * A plugin to handle drag & drop from 1.5
 *
 */
@Element
public class Drag15Ef extends AbstractEffect {

	@Param
	private String zoneId;

	@Param
	private EAdList<EAdEffect> dropEffects;

	public Drag15Ef() {
		dropEffects = new EAdList<EAdEffect>();
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public EAdList<EAdEffect> getDropEffects() {
		return dropEffects;
	}

	public void setDropEffects(EAdList<EAdEffect> dropEffects) {
		this.dropEffects = dropEffects;
	}

}
