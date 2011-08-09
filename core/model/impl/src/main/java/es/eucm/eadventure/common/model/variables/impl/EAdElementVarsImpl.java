package es.eucm.eadventure.common.model.variables.impl;

import java.util.Collection;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;
import es.eucm.eadventure.common.model.variables.EAdElementVars;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.EAdVarDef;

public class EAdElementVarsImpl implements EAdElementVars {

	private EAdMap<String, EAdVar<?>> vars;

	private EAdElement element;

	public EAdElementVarsImpl(EAdElement element) {
		this.element = element;
		vars = new EAdMapImpl<String, EAdVar<?>>(String.class, EAdVar.class);

	}

	@Override
	public EAdElement getElement() {
		return element;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> EAdVar<T> getVar(EAdVarDef<T> var) {
		return (EAdVar<T>) vars.get(var.getName());
	}

	@Override
	public <T> void add(EAdVarDef<T> var) {
		vars.put(var.getName(), new EAdVarImpl<T>(var, element));

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> EAdVar<T> getVar(String name, Class<T> clazz) {
		if (vars.containsKey(name))
			return (EAdVar<T>) vars.get(name);
		else
			return null;

	}

	@Override
	public <T> void add(EAdVar<T> var) {
		vars.put(var.getName(), var);
	}

	@Override
	public Collection<EAdVar<?>> getVars() {
		return vars.values();
	}

	@Override
	public void add(EAdVarDef<?>[] eAdVarDefs) {
		for (EAdVarDef<?> var : eAdVarDefs) {
			this.add(var);
		}

	}

}
