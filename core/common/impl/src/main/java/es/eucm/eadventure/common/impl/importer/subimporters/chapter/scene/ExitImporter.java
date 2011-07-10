package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdTransition;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.resources.assets.drawable.Shape;

public class ExitImporter implements EAdElementImporter<Exit, EAdSceneElement> {

	private static int ID_GENERATOR = 0;
	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;
	private EAdElementFactory factory;
	private EffectsImporterFactory effectsImporterFactory;

	@Inject
	public ExitImporter(EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory,
			EffectsImporterFactory effectsImporterFactory) {
		this.conditionsImporter = conditionsImporter;
		this.factory = factory;
		this.effectsImporterFactory = effectsImporterFactory;
	}

	public EAdSceneElement init(Exit oldObject) {
		EAdBasicSceneElement newExit = new EAdBasicSceneElement("exit" + ID_GENERATOR++);
		return newExit;
	}
	@Override
	public EAdSceneElement convert(Exit oldObject, Object object) {
		EAdBasicSceneElement newExit = (EAdBasicSceneElement) object;

		Shape shape = ShapedElementImporter.importShape(oldObject, newExit);

		newExit.getResources().addAsset(newExit.getInitialBundle(),
				EAdBasicSceneElement.appearance, shape);

		// Event to show (or not) the exit
		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject
				.getConditions(), condition);

		EAdScene scene = (EAdScene) factory.getElementById(oldObject.getNextSceneId());
		EAdChangeScene effect = new EAdChangeScene("change_screen_"
				+ newExit.getId(), scene, EAdTransition.BASIC);
		effect.setCondition(condition);

		newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);
				
		EAdConditionEventImpl event = new EAdConditionEventImpl( newExit.getId() + "_VisibleEvent" );
		event.setCondition(condition);
		

		for (Effect e : oldObject.getEffects().getEffects()) {
			EAdEffect eadEffect = effectsImporterFactory.getEffect(e);
			eadEffect.setCondition(condition);
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, eadEffect);
		}

		boolean hasNotEffects = false;
		for (Effect e : oldObject.getNotEffects().getEffects()) {
			hasNotEffects = true;
			EAdEffect eadEffect = effectsImporterFactory.getEffect(e);
			eadEffect.setCondition(new NOTCondition(condition));
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, eadEffect);
		}
		
		if (!hasNotEffects) {
			EAdChangeVarValueEffect visibleVar = new EAdChangeVarValueEffect( newExit.getId() + "_visibleEffect" );
			visibleVar.addVar(newExit.visibleVar());
			BooleanOperation op = new BooleanOperation( "booleanOpTrue" );
			op.setCondition(EmptyCondition.TRUE_EMPTY_CONDITION);
			visibleVar.setOperation( op );
			event.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_MET, visibleVar);
			
			EAdChangeVarValueEffect notVisibleVar = new EAdChangeVarValueEffect( newExit.getId() + "_notVisibleEffect" );
			notVisibleVar.addVar(newExit.visibleVar());
			op = new BooleanOperation( "booleanOpFalse" );
			op.setCondition(EmptyCondition.FALSE_EMPTY_CONDITION);
			notVisibleVar.setOperation( op );
			event.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_UNMET, notVisibleVar);
			
			newExit.getEvents().add(event);
		}

		return newExit;
	}

}
