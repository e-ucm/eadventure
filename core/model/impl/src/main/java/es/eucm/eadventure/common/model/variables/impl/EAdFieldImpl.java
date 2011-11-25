package es.eucm.eadventure.common.model.variables.impl;

import com.gwtent.reflection.client.Reflectable;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdVarDef;

@Reflectable
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
		System.out.println();
	}

	@Override
	public EAdElement getElement() {
		return element;
	}

	@Override
	public EAdVarDef<T> getVarDef() {
		return varDef;
	}

	@Override
	public String getId() {
		return (element != null ? element.getId() : "") + "_" + varDef.getId()
				+ "_field";
	}
	
	@Override
	public void setId(String id) {
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
			f.varDef = (EAdVarDef<T>) varDef.copy();
		} else {
			f.element = element;
			f.varDef = varDef;
		}
		return f;
	}

	public boolean equals(Object o) {
		if (o instanceof EAdField) {
			return ((EAdField<?>) o).getElement() == element
					&& ((EAdField<?>) o).getVarDef() == varDef;
		}
		return false;
	}

	public String toString() {
		return element + "." + varDef.getName();
	}
	
	public void setVarDef(EAdVarDef<T> varDef) {
		this.varDef = varDef;
	}

	public void setElement(EAdElement element) {
		this.element = element;
	}

	
}
