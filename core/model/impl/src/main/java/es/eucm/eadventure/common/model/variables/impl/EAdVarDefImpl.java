package es.eucm.eadventure.common.model.variables.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdVarDef;

@Element(detailed = EAdVarDefImpl.class, runtime = EAdVarDefImpl.class)
public class EAdVarDefImpl<T> implements EAdVarDef<T> {

	@Param("id")
	private String id;

	@Param("name")
	private String name;

	@Param("class")
	private Class<T> type;

	@Param("initialValue")
	private T initialValue;

	/**
	 * Constructs a variable definition
	 * 
	 * @param name
	 *            variable's name
	 * @param type
	 *            variable's type
	 * @param initialValue
	 *            variable's initial value
	 * @param constant
	 *            if the variable is constant
	 * @param global
	 *            if the variable is global
	 */
	public EAdVarDefImpl(String name, Class<T> type, T initialValue) {
		this.id = "var_" + "name_" + Math.round(Math.random() * 100);
		this.name = name;
		this.type = type;
		this.initialValue = initialValue;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public T getInitialValue() {
		return initialValue;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public EAdElement copy() {
		return copy(true);
	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		return new EAdVarDefImpl<T>(name, type, initialValue);
	}

}
