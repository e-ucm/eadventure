package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.TriggerSceneEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;

public class TriggerSceneImporter extends EffectImporter<TriggerSceneEffect, EAdEffect>{
	
	private EAdElementFactory factory;
	
	@Inject
	public TriggerSceneImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter, EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public EAdEffect init(TriggerSceneEffect oldObject) {
		return new EAdChangeScene("triggerCutscene");
	}

	@Override
	public EAdEffect convert(TriggerSceneEffect oldObject, Object object) {
		EAdChangeScene changeScene =  (EAdChangeScene) object;
		
		EAdScene scene = (EAdScene) factory.getElementById(oldObject.getTargetId());
		changeScene.setNextScene(scene);
		
		importConditions(oldObject, changeScene);
		
		return changeScene;
	}

}
