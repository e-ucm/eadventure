package ead.engine.core.debuggers;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.EAdAdventureModel;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.platform.GenericInjector;

@Singleton
public class DebuggerHandlerImpl implements DebuggerHandler {
	
	private GenericInjector injector;
	
	private List<Debugger> debuggers;
	
	private List<DrawableGO<?>> drawables;
	
	@Inject
	public DebuggerHandlerImpl( GenericInjector injector ){
		this.injector = injector;
		debuggers = new ArrayList<Debugger>();
		drawables = new ArrayList<DrawableGO<?>>();
	}
	
	public void init( List<Class<? extends Debugger>> classes ){
		debuggers.clear();
		for ( Class<? extends Debugger> c: classes ){
			Debugger d = injector.getInstance(c);
			debuggers.add(d);
		}
	}

	@Override
	public List<DrawableGO<?>> getGameObjects() {
		drawables.clear();
		for ( Debugger debugger: debuggers ){
			drawables.addAll(debugger.getGameObjects());
		}
		return drawables;
	}

	@Override
	public void setUp(EAdAdventureModel model) {
		for ( Debugger debugger: debuggers ){
			debugger.setUp(model);
		}
	}

}
