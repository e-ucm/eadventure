package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.TriggerCutsceneEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;

public class TriggerCutsceneImporter extends EffectImporter<TriggerCutsceneEffect, EAdEffect>{
	
	private EAdElementFactory factory;
	
	@Inject
	public TriggerCutsceneImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter, EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public EAdEffect init(TriggerCutsceneEffect oldObject) {
		return new EAdChangeScene("triggerCutscene");
	}

	@Override
	public EAdEffect convert(TriggerCutsceneEffect oldObject, Object object) {
		EAdChangeScene changeScene =  (EAdChangeScene) object;
		
		changeScene.setQueueable(true);
		
		EAdScene scene = (EAdScene) factory.getElementById(oldObject.getTargetId());
		changeScene.setNextScene(scene);
		
		importConditions(oldObject, changeScene);
		
		return changeScene;
	}

}
