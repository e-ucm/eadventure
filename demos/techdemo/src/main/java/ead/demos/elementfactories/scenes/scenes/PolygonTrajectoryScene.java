package ead.demos.elementfactories.scenes.scenes;

import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.predef.effects.MakeActiveElementEf;
import ead.common.model.elements.predef.effects.MoveActiveElementToMouseEf;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.trajectories.PolygonTrajectory;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.util.EAdPosition;
import ead.common.model.params.util.EAdPosition.Corner;
import ead.demos.elementfactories.scenes.normalguy.NgCommon;

public class PolygonTrajectoryScene extends EmptyScene {

	public PolygonTrajectoryScene() {
		this.setId("PolygonTrajectoryScene");
		NgCommon.init();

		getBackground().getDefinition().setAppearance(
				new Image("@drawable/polygon.png"));
		SceneElement element = new SceneElement(NgCommon.getMainCharacter());

		element.setPosition(new EAdPosition(Corner.BOTTOM_CENTER, 520, 300));

		MakeActiveElementEf effect = new MakeActiveElementEf(element);

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.FIRST_UPDATE, effect);
		element.getEvents().add(event);
		getSceneElements().add(element);
		addBehavior(MouseGEv.MOUSE_LEFT_CLICK, new MoveActiveElementToMouseEf());

		// Create trajectory
		PolygonTrajectory polygon = new PolygonTrajectory();
		polygon.addPoint(242, 9);
		polygon.addPoint(726, 7);
		polygon.addPoint(729, 87);
		polygon.addPoint(750, 87);
		polygon.addPoint(752, 184);
		polygon.addPoint(680, 183);
		polygon.addPoint(680, 275);
		polygon.addPoint(621, 278);
		polygon.addPoint(621, 340);
		polygon.addPoint(590, 343);
		polygon.addPoint(589, 397);
		polygon.addPoint(700, 398);
		polygon.addPoint(700, 294);
		polygon.addPoint(778, 293);
		polygon.addPoint(771, 456);
		polygon.addPoint(588, 458);
		polygon.addPoint(589, 493);
		polygon.addPoint(770, 495);
		polygon.addPoint(770, 581);
		polygon.addPoint(579, 582);
		polygon.addPoint(575, 538);
		polygon.addPoint(534, 535);
		polygon.addPoint(533, 582);
		polygon.addPoint(111, 584);
		polygon.addPoint(109, 499);
		polygon.addPoint(191, 499);
		polygon.addPoint(190, 394);
		polygon.addPoint(26, 391);
		polygon.addPoint(26, 85);
		polygon.addPoint(640, 87);
		polygon.addPoint(640, 60);
		polygon.addPoint(238, 61);

		this.setTrajectoryDefinition(polygon);
	}

}
