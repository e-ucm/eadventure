package es.eucm.eadventure.common.model.variables.impl;

import es.eucm.eadventure.common.model.variables.EAdVarDef;

public class EAdVarDefImpl<T> implements EAdVarDef<T> {

	private String name;

	private Class<T> type;

	private T initialValue;

	private boolean constant;

	private boolean global;

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
	public EAdVarDefImpl(String name, Class<T> type, T initialValue,
			boolean constant, boolean global) {
		super();
		this.name = name;
		this.type = type;
		this.initialValue = initialValue;
		this.constant = constant;
		this.global = global;
	}
	
	public EAdVarDefImpl( String name, Class<T> type, T initialValue ){
		this( name, type, initialValue, false, false);
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
	public boolean isConstant() {
		return constant;
	}

	@Override
	public boolean isGlobal() {
		return global;
	}

}
