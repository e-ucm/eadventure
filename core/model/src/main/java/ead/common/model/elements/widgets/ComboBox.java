package ead.common.model.elements.widgets;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.text.EAdString;
import ead.common.model.weev.story.elements.Effect;

@Element
public class ComboBox extends SceneElement {

	@Param
	private EAdList<EAdString> strings;

	@Param
	private EAdList<Effect> selectionEffects;

	public ComboBox() {
		strings = new EAdList<EAdString>();
		selectionEffects = new EAdList<Effect>();
	}

	public void addString(EAdString string, Effect selectionEffect) {
		strings.add(string);
		selectionEffects.add(selectionEffect);
	}

	public EAdList<EAdString> getStrings() {
		return strings;
	}

	public void setStrings(EAdList<EAdString> strings) {
		this.strings = strings;
	}

	public EAdList<Effect> getSelectionEffects() {
		return selectionEffects;
	}

	public void setSelectionEffects(EAdList<Effect> selectionEffects) {
		this.selectionEffects = selectionEffects;
	}

}
