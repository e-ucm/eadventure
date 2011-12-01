package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.elements;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.InfluenceArea;
import es.eucm.eadventure.common.data.chapter.Rectangle;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.ShapedElementImporter;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.common.predef.model.effects.EAdMoveActiveElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.enums.Alignment;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;

public abstract class ElementImporter<T> implements
		EAdElementImporter<T, EAdSceneElement> {

	protected EAdElementFactory factory;

	protected EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	protected StringHandler stringHandler;

	public ElementImporter(EAdElementFactory factory,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			StringHandler stringHandler) {
		this.factory = factory;
		this.conditionsImporter = conditionsImporter;
		this.stringHandler = stringHandler;
	}

	protected void addGoToExit(EAdBasicSceneElement newExit, Exit oldObject,
			EAdCondition enableCondition, EAdEffect finalEffect) {

		if (factory.isFirstPerson()) {
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, finalEffect);
		} else {
			EAdMoveActiveElement move = new EAdMoveActiveElement();
			move.setTarget(newExit);
			move.getNextEffects().add(finalEffect);
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, move);
		}

	}

	protected void addInfluenceArea(EAdSceneElement sceneElement,
			EAdRectangle bounds, InfluenceArea influenceArea) {
		boolean hasInfluenceArea = influenceArea != null
				&& influenceArea.getWidth() != 0
				&& influenceArea.getHeight() != 0;

		EAdRectangle rect = null;
		if (hasInfluenceArea) {
			rect = new EAdRectangleImpl(influenceArea.getX() + bounds.getX(),
					influenceArea.getY() + bounds.getY(),
					influenceArea.getWidth(), influenceArea.getHeight());
		} else {
			rect = bounds;
		}
		sceneElement.setVarInitialValue(
				NodeTrajectoryDefinition.VAR_INFLUENCE_AREA, rect);
	}

	protected void addInfluenceArea(EAdSceneElement sceneElement,
			Rectangle element, InfluenceArea influenceArea) {
		addInfluenceArea(sceneElement,
				ShapedElementImporter.getBounds(element), influenceArea);
	}

	protected void setShape(EAdBasicSceneElement sceneElement, Rectangle exit) {
		Shape shape = ShapedElementImporter.importShape(exit, sceneElement);
		shape.setPaint(EAdColor.TRANSPARENT);

		sceneElement.getResources().addAsset(sceneElement.getInitialBundle(),
				EAdBasicSceneElement.appearance, shape);
	}

	protected EAdCondition getEnableCondition(Conditions c) {
		EAdCondition condition = conditionsImporter.init(c);
		condition = conditionsImporter.convert(c, condition);

		return condition;

	}

	protected void setDocumentation(EAdSceneElementDefImpl newElement,
			Element oldObject) {
		stringHandler.setString(newElement.getName(), oldObject.getName());
		stringHandler.setString(newElement.getDesc(),
				oldObject.getDescription());
		stringHandler.setString(newElement.getDetailedDesc(),
				oldObject.getDetailedDescription());
		stringHandler.setString(newElement.getDoc(),
				oldObject.getDocumentation());
		newElement.setId(oldObject.getId() + "_element");
		
	}

	protected void addDefaultBehavior(EAdBasicSceneElement sceneElement,
			String shortDescription) {
		sceneElement.setVarInitialValue(EAdBasicSceneElement.VAR_NAME,
				sceneElement.getDefinition().getName());
		if (shortDescription != null) {
			EAdSpeakEffect showDescription = new EAdSpeakEffect();
			showDescription.setAlignment(Alignment.CENTER);
			showDescription.setColor(EAdPaintImpl.WHITE_ON_BLACK,
					EAdColor.TRANSPARENT);
			stringHandler.setString(showDescription.getString(),
					shortDescription);
			sceneElement.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
					showDescription);
		}
	}

	protected void addEnableEvent(EAdBasicSceneElement newActiveAreaReference,
			EAdCondition condition) {

		EAdConditionEventImpl event = new EAdConditionEventImpl();
		event.setCondition(condition);

		EAdField<Boolean> enableField = new EAdFieldImpl<Boolean>(
				newActiveAreaReference, EAdBasicSceneElement.VAR_VISIBLE);

		EAdChangeFieldValueEffect changeEnable = new EAdChangeFieldValueEffect();

		changeEnable.addField(enableField);
		changeEnable.setOperation(new BooleanOperation(condition));
		event.addEffect(ConditionedEventType.CONDITIONS_MET, changeEnable);
		event.addEffect(ConditionedEventType.CONDITIONS_UNMET, changeEnable);

		newActiveAreaReference.getEvents().add(event);
	}

}
