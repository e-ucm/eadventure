/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.ead.techdemo.elementfactories.scenes.scenes;

import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.text.SpeakEf;
import es.eucm.ead.model.elements.predef.sceneelements.Button;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.model.elements.scenes.EAdSceneElementDef;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.elements.transitions.DisplaceTransition;
import es.eucm.ead.model.elements.transitions.FadeInTransition;
import es.eucm.ead.model.elements.transitions.enums.DisplaceTransitionType;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.paint.EAdFill;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.util.Position.Corner;
import es.eucm.ead.techdemo.elementfactories.scenes.normalguy.NgMainScreen;

import java.util.ArrayList;
import java.util.List;

public class InitScene extends EmptyScene {

	private List<EAdScene> sceneDemos;

	private SceneElementDef goBack;

	private EAdSceneElementDef infoButton;

	private EAdFill fill = new ColorFill(255, 255, 255, 200);

	private EAdFont font = BasicFont.REGULAR;

	private Paint speakPaint = new Paint(fill, ColorFill.LIGHT_GRAY, 5);

	public InitScene() {
		this.setId("InitScene");
		this.setBackground(new SceneElement(new Image(
				"@drawable/techdemo-bg.png")));
		initList();
		initGOBackButton();
		initInfoButton();
		int y = 200;
		int x = 120;
		for (EAdScene s : sceneDemos) {
			EAdString name = new EAdString("techDemo." + s.getId());
			EAdString description = new EAdString("techDemo." + s.getId()
					+ ".description");
			Button b = new Button(name);
			b.setPosition(x, y);
			b.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, new ChangeSceneEf(s,
					new DisplaceTransition(1000,
							DisplaceTransitionType.VERTICAL, true)));
			this.getSceneElements().add(b);
			SceneElement goBackButton = new SceneElement(goBack);
			goBackButton.setInitialScale(0.5f);
			goBackButton.setPosition(Corner.BOTTOM_LEFT, 10, 590);
			s.getSceneElements().add(goBackButton);

			SceneElement info = new SceneElement(infoButton);
			info.setPosition(Corner.BOTTOM_LEFT, 80, 590);
			SpeakEf effect = new SpeakEf(description);
			effect.setColor(ColorFill.GRAY, speakPaint);
			effect.setFont(font);
			info.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effect);
			// info.setScale(0.5f);
			s.getSceneElements().add(info);
			y += 45;
			if (y > 520) {
				y = 200;
				x += 210;
			}
		}
	}

	private void initInfoButton() {
		infoButton = new SceneElementDef(new Image("@drawable/infobutton.png"));
	}

	private void initGOBackButton() {
		goBack = new SceneElementDef(new Image("@drawable/goback.png"));
		goBack.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, new ChangeSceneEf(this,
				new FadeInTransition(1000)));

	}

	private void initList() {
		sceneDemos = new ArrayList<EAdScene>();
		sceneDemos.add(new EmptyScene());
		sceneDemos.add(new ShapeScene());
		sceneDemos.add(new TextsScene());
		sceneDemos.add(new CharacterScene());
		sceneDemos.add(new SpeakAndMoveScene());
		sceneDemos.add(new ComplexElementScene());
		sceneDemos.add(new SoundScene());
		sceneDemos.add(new DrawablesScene());
		// sceneDemos.add(new MoleGame());
		sceneDemos.add(new ShowQuestionScene());
		sceneDemos.add(new TrajectoriesScene());
		sceneDemos.add(new PolygonTrajectoryScene());
		sceneDemos.add(new PhysicsScene());
		sceneDemos.add(new PhysicsScene2());
		sceneDemos.add(new DragDropScene());
		sceneDemos.add(new PositionScene());
		sceneDemos.add(new DepthZScene());
		sceneDemos.add(new SharingEffectsScene());
		sceneDemos.add(new ScrollScene());
		sceneDemos.add(new FiltersDemo());
		//		sceneDemos.add(new VideoSceneDemo());
		sceneDemos.add(new WebMVideoScene());
		sceneDemos.add(new WidgetsScene());
		sceneDemos.add(new NgMainScreen(this));
		// sceneDemos.add(new NgRoom1());

	}

	public String getSceneDescription() {
		return "A scene containing the demos scene";
	}

	public String getDemoName() {
		return "Scene Demo Chooser";
	}

}
