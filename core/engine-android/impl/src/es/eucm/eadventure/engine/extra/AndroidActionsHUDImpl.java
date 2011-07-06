package es.eucm.eadventure.engine.extra;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.ActionsHUDImpl;
import es.eucm.eadventure.engine.core.platform.GUI;

public class AndroidActionsHUDImpl extends ActionsHUDImpl {

	private List<AndroidActionGO> actionGOs;
	
	private Injector injector;
	
	@Inject
	public AndroidActionsHUDImpl(GUI gui,
			Injector injector, GameObjectManager gameObjectManager) {
		super(gui, gameObjectManager);
		actionGOs = new ArrayList<AndroidActionGO>();
		this.injector = injector;
	}

	@Override
	public void setElement(ActorReferenceGO reference) {
		super.setElement(reference);

		for (EAdAction eAdAction : this.getActions()) {
//			DesktopActionGO action = (DesktopActionGO) goFactory.get(eAdAction);
			AndroidActionGO action = injector.getInstance(AndroidActionGO.class);
			action.setElement(eAdAction);
			action.setX(this.getX() + 10);
			action.setY(this.getY() + 10);
			actionGOs.add(action);
		}

	}
	
	@Override
	public void render() {
		//TODO ...
		
		gui.addElement(this);

		for (AndroidActionGO action : actionGOs)
			gui.addElement(action);

	}


}
