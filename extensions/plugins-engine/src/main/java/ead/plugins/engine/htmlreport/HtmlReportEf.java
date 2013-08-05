package ead.plugins.engine.htmlreport;

import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.elements.effects.AbstractEffect;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.EAdOperation;
import es.eucm.ead.model.params.text.EAdString;

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
