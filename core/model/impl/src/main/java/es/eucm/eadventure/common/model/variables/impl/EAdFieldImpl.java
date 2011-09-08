package es.eucm.eadventure.common.model.variables.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdVarDef;

@Element(detailed = EAdFieldImpl.class, runtime = EAdFieldImpl.class)
public class EAdFieldImpl<T> implements EAdField<T> {

	@Param("element")
	private EAdElement element;

	@Param("variable")
	private EAdVarDef<T> varDef;

	public EAdFieldImpl() {

	}

	public EAdFieldImpl(EAdElement element, EAdVarDef<T> varDef) {
		this.element = element;
		this.varDef = varDef;
	}

	@Override
	public EAdElement getElement() {
		return element;
	}

	@Override
	public EAdVarDef<T> getVarDefinition() {
		return varDef;
	}

	@Override
	public String getId() {
		return element.getId() + "_" + varDef.getId() + "_field";
	}

	@Override
	public EAdElement copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean equals(Object o) {
		if (o instanceof EAdField) {
			return ((EAdField<?>) o).getElement() == element
					&& ((EAdField<?>) o).getVarDefinition() == varDef;
		}
		return false;
	}

}
