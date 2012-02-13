package ead.elementfactories.demos.scenes;

import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEventType;
import ead.common.model.elements.guievents.EAdMouseEvent;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.model.predef.effects.MakeActiveElementEf;
import ead.common.model.predef.effects.MoveActiveElementEf;
import ead.common.model.predef.effects.SpeakSceneElementEf;
import ead.common.model.predef.events.ScrollWithSceneElementEv;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.util.EAdPosition.Corner;
import ead.elementfactories.EAdElementsFactory;
import ead.elementfactories.demos.normalguy.NgCommon;

public class ScrollScene extends EmptyScene {

	public ScrollScene() {
		setBounds(1000, 1213);
		setBackground(new SceneElementImpl(new ImageImpl(
				"@drawable/scrollbg.png")));
		
		NgCommon.init();
		EAdSceneElementDef d = NgCommon.getMainCharacter();
		SceneElementImpl character = new SceneElementImpl( d );
		character.setPosition(Corner.BOTTOM_CENTER, 1000 / 2, 1213 / 2);
		
		SpeakSceneElementEf effect = new SpeakSceneElementEf( character );
		EAdElementsFactory.getInstance().getStringFactory().setString(effect.getCaption().getText(), "Sometimes I don't speak right");
		character.addBehavior(EAdMouseEvent.MOUSE_RIGHT_CLICK, effect);
		
		
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

		getBackground().addBehavior(EAdMouseEvent.MOUSE_LEFT_PRESSED,
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
