package es.eucm.ead.legacyplugins.model;

import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.params.text.EAdString;

/**
 * @author anserran
 *         Date: 8/07/13
 *         Time: 12:50
 */
@Element
public class HtmlReportEf extends Effect {

	@Param
	private EAdList<Operation> operations;

	@Param
	private EAdList<EAdString> labels;

	@Param
	private EAdString title;

	public HtmlReportEf() {

	}

	public EAdList<Operation> getOperations() {
		return operations;
	}

	public void setOperations(EAdList<Operation> variables) {
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
