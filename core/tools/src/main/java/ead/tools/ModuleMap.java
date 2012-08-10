package ead.tools;

import java.util.HashMap;
import java.util.Map;

public class ModuleMap {
	
	protected Map<Class<?>, Class<?>> binds;
	
	public ModuleMap( ){
		binds = new HashMap<Class<?>, Class<?>>();
	}
	
	public Map<Class<?>, Class<?>> getBinds(){
		return binds;
	}
	
	void setBind(Class<?> clazz, Class<?> bind){
		binds.put(clazz, bind);
	}

}
