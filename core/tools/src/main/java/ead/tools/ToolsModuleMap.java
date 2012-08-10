package ead.tools;


public class ToolsModuleMap extends ModuleMap {
	
	public ToolsModuleMap( ){
		setBind(StringHandler.class, StringHandlerImpl.class);
	}

}
