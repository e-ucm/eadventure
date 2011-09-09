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

	public EAdVarDefImpl(String id) {
		this.id = id;
	}

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
	
	public boolean equals( Object o ){
		if ( o instanceof EAdVarDefImpl ){
			EAdVarDefImpl<?> var = (EAdVarDefImpl<?>) o;
			return var.name.equals(name) && var.type.equals(type) && var.initialValue.equals(initialValue);
		}
		return false;
	}
	
	public int hashCode(){
		return (name + type + initialValue + "").hashCode();
	}

}
