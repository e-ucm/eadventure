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

	@Param("elementField")
	private EAdField<EAdElement> elementField;

	public EAdFieldImpl() {

	}

	@SuppressWarnings("unchecked")
	public EAdFieldImpl(EAdElement element, EAdVarDef<T> varDef) {
		if (element instanceof EAdField) {
			this.elementField = (EAdField<EAdElement>) element;
		} else
			this.element = element;
		this.varDef = varDef;
	}

	public EAdFieldImpl(EAdFieldImpl<EAdElement> elementField,
			EAdVarDef<T> varDef) {
		this.elementField = elementField;
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
		return (element != null ? element.getId() : "") + "_" + varDef.getId()
				+ "_field";
	}

	@Override
	public EAdElement copy() {
		return copy(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EAdElement copy(boolean deepCopy) {
		EAdFieldImpl<T> f = new EAdFieldImpl<T>();
		if (deepCopy) {
			f.element = element.copy();
			f.elementField = (EAdFieldImpl<EAdElement>) elementField.copy();
			f.varDef = (EAdVarDef<T>) varDef.copy();
		} else {
			f.element = element;
			f.elementField = elementField;
			f.varDef = varDef;
		}
		return f;
	}

	public boolean equals(Object o) {
		if (o instanceof EAdField) {
			return ((EAdField<?>) o).getElement() == element
					&& ((EAdField<?>) o).getVarDefinition() == varDef;
		}
		return false;
	}

	public String toString() {
		return element + "." + varDef.getName();
	}

	@Override
	public EAdField<EAdElement> getElementField() {
		return elementField;
	}

}
