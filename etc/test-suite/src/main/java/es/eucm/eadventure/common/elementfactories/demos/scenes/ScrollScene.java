package es.eucm.eadventure.common.elementfactories.demos.scenes;

import es.eucm.eadventure.common.elementfactories.demos.normalguy.NgCommon;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.effects.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.predef.model.effects.EAdMoveActiveElement;
import es.eucm.eadventure.common.predef.model.events.ScrollWithSceneElement;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class ScrollScene extends EmptyScene {

	public ScrollScene() {
		setBounds(1000, 1213);
		setBackground(new EAdBasicSceneElement(new ImageImpl(
				"@drawable/scrollbg.png")));
		
		NgCommon.init();
		EAdSceneElementDef d = NgCommon.getMainCharacter();
		EAdBasicSceneElement character = new EAdBasicSceneElement( d );
		character.setPosition(Corner.BOTTOM_CENTER, 1000 / 2, 1213 / 2);
		
		this.getComponents().add(character);

		EAdMakeActiveElementEffect makeActive = new EAdMakeActiveElementEffect(
				character);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		event.setId("makeAcitveCharacter");
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, makeActive);
		character.getEvents().add(event);
		
		this.getEvents().add(new ScrollWithSceneElement(this, character));

		SimpleTrajectoryDefinition trajectory = new SimpleTrajectoryDefinition(false);
		trajectory.setLimits(0, 0, 1000, 1213);
		setTrajectoryDefinition(trajectory);

		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED,
				new EAdMoveActiveElement());
		
	}

	@Override
	public String getSceneDescription() {
		return "A scene scrolling with the character";
	}

	public String getDemoName() {
		return "Scroll Scene";
	}

}
