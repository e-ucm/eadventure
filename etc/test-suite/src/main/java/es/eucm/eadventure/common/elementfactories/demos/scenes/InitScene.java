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

package es.eucm.eadventure.common.elementfactories.demos.scenes;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elementfactories.StringFactory;
import es.eucm.eadventure.common.elementfactories.demos.SceneDemo;
import es.eucm.eadventure.common.elementfactories.demos.normalguy.NgMainScreen;
import es.eucm.eadventure.common.model.elements.effects.ChangeSceneEf;
import es.eucm.eadventure.common.model.elements.effects.text.SpeakEf;
import es.eucm.eadventure.common.model.elements.guievents.MouseEventImpl;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.transitions.EAdTransition;
import es.eucm.eadventure.common.model.predef.sceneelements.Button;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.fills.EAdPaintImpl;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.common.params.text.EAdFont;
import es.eucm.eadventure.common.resources.assets.drawable.basics.ImageImpl;
import es.eucm.eadventure.common.util.EAdPositionImpl.Corner;

public class InitScene extends EmptyScene {

	private List<SceneDemo> sceneDemos;

	private SceneElementImpl goBack;

	private EAdSceneElementDef infoButton;
	
	private EAdFill fill = new EAdColor(255, 255, 255, 200);
	
	private EAdFont font = new EAdFontImpl( 18 );

	private EAdPaintImpl speakPaint = new EAdPaintImpl(fill, EAdColor.LIGHT_GRAY, 5);

	public InitScene() {
		this.setBackground(new SceneElementImpl(new ImageImpl("@drawable/techdemo-bg.png")));
		getBackground().setId("background");
		initList();
		initGOBackButton();
		initInfoButton();
		int y = 200;
		int x = 120;
		StringFactory sf = EAdElementsFactory.getInstance().getStringFactory();
		for (SceneDemo s : sceneDemos) {
			Button b = new Button();
			sf.setString(b.getLabel(), s.getDemoName());
			b.setPosition(x, y);
			b.addBehavior(MouseEventImpl.MOUSE_LEFT_PRESSED,
					new ChangeSceneEf( s, EAdTransition.BASIC));
			this.getComponents().add(b);
			s.getComponents().add(goBack);
			SceneElementImpl info = new SceneElementImpl(infoButton);
			info.setPosition(Corner.BOTTOM_LEFT, 80, 590);
			SpeakEf effect = new SpeakEf();
			effect.setId("infoSpeak");
			effect.setColor(EAdColor.GRAY, speakPaint);
			effect.setFont(font);
			sf.setString(effect.getString(), s.getSceneDescription());
			info.addBehavior(MouseEventImpl.MOUSE_LEFT_PRESSED, effect);
			// info.setScale(0.5f);
			s.getComponents().add(info);
			y += 45;
			if (y > 520) {
				y = 200;
				x += 210;
			}
		}
	}

	private void initInfoButton() {
		infoButton = new SceneElementDefImpl(new ImageImpl("@drawable/infobutton.png"));
		infoButton.setId("info");
	}

	private void initGOBackButton() {
		goBack = new SceneElementImpl(new ImageImpl(
				"@drawable/goback.png"));
		goBack.setId("goBack");
		goBack.setPosition(Corner.BOTTOM_LEFT, 10, 590);
		goBack.addBehavior(MouseEventImpl.MOUSE_LEFT_PRESSED,
				new ChangeSceneEf( this, EAdTransition.BASIC));
		goBack.setScale(0.5f);

	}

	private void initList() {
		sceneDemos = new ArrayList<SceneDemo>();
		sceneDemos.add(new EmptyScene());
		sceneDemos.add(new ShapeScene());
		// sceneDemos.add(new TextsScene());
		sceneDemos.add(new CharacterScene());
		sceneDemos.add(new SpeakAndMoveScene());
		sceneDemos.add(new ComplexElementScene());
		// sceneDemos.add(new SoundScene());
		sceneDemos.add(new DrawablesScene());
		// sceneDemos.add(new MoleGame());
		sceneDemos.add(new ShowQuestionScene());
		// sceneDemos.add(new TrajectoriesScene());
		// sceneDemos.add(new PhysicsScene());
		sceneDemos.add(new PhysicsScene2());
		sceneDemos.add(new DragDropScene());
		sceneDemos.add(new PositionScene());
		sceneDemos.add(new DepthZScene());
		sceneDemos.add(new SharingEffectsScene());
		sceneDemos.add(new InventoryScene());
		sceneDemos.add(new ScrollScene());
		sceneDemos.add(new FiltersDemo());
		// sceneDemos.add(new VideoScene());
		sceneDemos.add(new NgMainScreen());
		// sceneDemos.add(new NgRoom1());

	}

	@Override
	public String getSceneDescription() {
		return "A scene containing the demos scene";
	}

	public String getDemoName() {
		return "Scene Demo Chooser";
	}

}
