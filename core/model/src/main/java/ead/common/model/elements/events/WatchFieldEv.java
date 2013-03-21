package ead.common.model.elements.events;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.events.enums.WatchFieldEvType;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.EAdField;

@Element
public class WatchFieldEv extends AbstractEvent {

	@Param
	private EAdList<EAdField<?>> fields;

	public WatchFieldEv() {
		fields = new EAdList<EAdField<?>>();
	}

	public EAdList<EAdField<?>> getFields() {
		return fields;
	}

	public void setFields(EAdList<EAdField<?>> fields) {
		this.fields = fields;
	}

	public void watchField(EAdField<?> f) {
		fields.add(f);
	}

	public void addEffect(EAdEffect effect) {
		addEffect(WatchFieldEvType.WATCH, effect);
	}

}
