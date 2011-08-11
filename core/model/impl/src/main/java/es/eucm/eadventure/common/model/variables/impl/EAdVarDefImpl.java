package es.eucm.eadventure.common.model.variables.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdVarDef;

@Element(detailed = EAdVarDefImpl.class, runtime = EAdVarDefImpl.class)
public class EAdVarDefImpl<T> implements EAdVarDef<T> {

	@Param("id")
	private String id;

	@Param("class")
	private Class<T> type;

	@Param("initialValue")
	private T initialValue;

	@Param("constant")
	private boolean constant;

	@Param("global")
	private boolean global;
	
	public EAdVarDefImpl( String id ){
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
	public EAdVarDefImpl(String name, Class<T> type, T initialValue,
			boolean constant, boolean global) {
		super();
		this.id = name;
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
		return id;
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

	@Override
	public String getId() {
		return id;
	}

	@Override
	public EAdElement copy() {
		// TODO copy var def
		return null;
	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		// TODO copy var def
		return null;
	}

}
