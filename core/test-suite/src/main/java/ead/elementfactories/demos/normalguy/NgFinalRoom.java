package ead.elementfactories.demos.normalguy;

import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.physics.PhysicsEffect;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.variables.BasicField;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.elementfactories.demos.scenes.EmptyScene;

/**
 * Final scene. Main character walks out the house and gets freedom
 */
public class NgFinalRoom extends EmptyScene{
	
	private SceneElement house;
	private SceneElement ng;
	
	
	public NgFinalRoom() {
		init();
	}
	
	protected void init() {
		setBackgroundFill(new LinearGradientFill(ColorFill.CYAN, ColorFill.BLUE,
				800, 500));

		addSky();
		PhysicsEffect effect = new PhysicsEffect();

		ConditionedEv event = new ConditionedEv();
		OperationCond condition = new OperationCond(
				new BasicField<Boolean>(this, BasicScene.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, effect);

		// getEvents().add(event);
		getBackground().addBehavior(MouseGEv.MOUSE_ENTERED, effect);
		
		addGround(effect);
		setElements();
		
	}
	
	private void setElements() {
		house = new SceneElement(new Image("@drawable/ng_finalroom_house.jpg"));
		house.setId("house");
		house.setPosition(Corner.TOP_LEFT , 540, 175);
		
	}
	
	private void addSky() {
		EAdSceneElementDef backgroundDef = getBackground().getDefinition();
		backgroundDef.getResources().addAsset(
				backgroundDef.getInitialBundle(),
				SceneElementDef.appearance,
				new Image("@drawable/sky.png"));

		SceneElementEv event = new SceneElementEv();

		InterpolationEf effect = new InterpolationEf(
				new BasicField<Integer>(getBackground(),
						SceneElement.VAR_X), 0, -800, 100000,
				InterpolationLoopType.REVERSE, InterpolationType.LINEAR);

		event.addEffect(SceneElementEvType.FIRST_UPDATE,
				effect);

		this.getBackground().getEvents().add(event);

	}
	
	protected void addGround(PhysicsEffect effect) {
		RectangleShape groundS = new RectangleShape(750, 50);
		groundS.setPaint(new LinearGradientFill(ColorFill.BROWN,
				ColorFill.DARK_BROWN, 750, 50));
		SceneElement ground = new SceneElement(groundS);
		ground.setId("ground");
		ground.setPosition(new EAdPosition(Corner.CENTER, 400, 575));


		effect.addSceneElement(ground);
		getSceneElements().add(ground);


	}
	
}
