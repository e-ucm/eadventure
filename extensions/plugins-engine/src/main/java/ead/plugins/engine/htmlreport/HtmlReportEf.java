package ead.plugins.engine.htmlreport;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.effects.AbstractEffect;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.params.text.EAdString;

/**
 * @author anserran
 *         Date: 8/07/13
 *         Time: 12:50
 */
@Element
public class HtmlReportEf extends AbstractEffect {

	@Param
	private EAdList<EAdOperation> operations;

	@Param
	private EAdList<EAdString> labels;

	@Param
	private EAdString title;

	public HtmlReportEf() {

	}

	public EAdList<EAdOperation> getOperations() {
		return operations;
	}

	public void setOperations(EAdList<EAdOperation> variables) {
		this.operations = variables;
	}

	public EAdList<EAdString> getLabels() {
		return labels;
	}

	public void setLabels(EAdList<EAdString> labels) {
		this.labels = labels;
	}

	public EAdString getTitle() {
		return title;
	}

	public void setTitle(EAdString title) {
		this.title = title;
	}

}
