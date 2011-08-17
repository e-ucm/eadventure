package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.Barrier;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

@Singleton
public class BarrierImporter implements
		EAdElementImporter<Barrier, EAdSceneElement> {

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	@Inject
	public BarrierImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter) {
		this.conditionsImporter = conditionsImporter;
	}

	@Override
	public EAdSceneElement init(Barrier oldObject) {
		return new EAdBasicSceneElement(oldObject.getId());
	}

	@Override
	public EAdSceneElement convert(Barrier oldObject, Object newElement) {
		EAdSceneElement barrier = (EAdSceneElement) newElement;
		barrier.getVars().add(NodeTrajectoryDefinition.VAR_BARRIER_ON);

		if (oldObject.getConditions() != null) {
			EAdCondition condition = conditionsImporter.init(oldObject
					.getConditions());
			condition = conditionsImporter.convert(oldObject.getConditions(),
					condition);
			EAdConditionEvent event = new EAdConditionEventImpl(
					"barrierCondition");
			event.setCondition(condition);
			EAdVar<Boolean> barrierOn = barrier.getVars().getVar(
					NodeTrajectoryDefinition.VAR_BARRIER_ON);
			event.addEffect(
					EAdConditionEvent.ConditionedEvent.CONDITIONS_MET,
					new EAdChangeVarValueEffect(oldObject.getId()
							+ "_barrierOn", barrierOn, BooleanOperation.TRUE_OP));
			event.addEffect(
					EAdConditionEvent.ConditionedEvent.CONDITIONS_UNMET,
					new EAdChangeVarValueEffect(oldObject.getId()
							+ "_barrierOn", barrierOn,
							BooleanOperation.FALSE_OP));
			
			barrier.getEvents().add(event);
		}
		RectangleShape rectangle = new RectangleShape( oldObject.getWidth(), oldObject.getHeight() );
		barrier.getResources().addAsset(barrier.getInitialBundle(), EAdBasicSceneElement.appearance, rectangle);
		barrier.setPosition( new EAdPositionImpl( Corner.TOP_LEFT, oldObject.getX(), oldObject.getY()));

		return barrier;
	}

}
