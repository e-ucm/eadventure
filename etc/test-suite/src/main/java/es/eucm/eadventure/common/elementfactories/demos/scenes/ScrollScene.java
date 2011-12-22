package es.eucm.eadventure.common.elementfactories.demos.scenes;

import es.eucm.eadventure.common.elementfactories.demos.normalguy.NgCommon;
import es.eucm.eadventure.common.model.elements.events.SceneElementEv;
import es.eucm.eadventure.common.model.elements.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.elements.guievents.MouseEventImpl;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.model.predef.effects.MakeActiveElementEf;
import es.eucm.eadventure.common.model.predef.effects.MoveActiveElementEf;
import es.eucm.eadventure.common.model.predef.events.ScrollWithSceneElementEv;
import es.eucm.eadventure.common.resources.assets.drawable.basics.ImageImpl;
import es.eucm.eadventure.common.util.EAdPositionImpl.Corner;

public class ScrollScene extends EmptyScene {

	public ScrollScene() {
		setBounds(1000, 1213);
		setBackground(new SceneElementImpl(new ImageImpl(
				"@drawable/scrollbg.png")));
		
		NgCommon.init();
		EAdSceneElementDef d = NgCommon.getMainCharacter();
		SceneElementImpl character = new SceneElementImpl( d );
		character.setPosition(Corner.BOTTOM_CENTER, 1000 / 2, 1213 / 2);
		
		this.getComponents().add(character);

		MakeActiveElementEf makeActive = new MakeActiveElementEf(
				character);

		SceneElementEv event = new SceneElementEv();
		event.setId("makeAcitveCharacter");
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, makeActive);
		character.getEvents().add(event);
		
		this.getEvents().add(new ScrollWithSceneElementEv(this, character));

		SimpleTrajectoryDefinition trajectory = new SimpleTrajectoryDefinition(false);
		trajectory.setLimits(0, 0, 1000, 1213);
		setTrajectoryDefinition(trajectory);

		getBackground().addBehavior(MouseEventImpl.MOUSE_LEFT_PRESSED,
				new MoveActiveElementEf());
		
	}

	@Override
	public String getSceneDescription() {
		return "A scene scrolling with the character";
	}

	public String getDemoName() {
		return "Scroll Scene";
	}

}
