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

package ead.common.test.readwrite;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.params.fills.ColorFill;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;

public class TriggerMacroRW extends ReadWriteTest {

	@Test
	public void test() {
		BasicAdventureModel model = new BasicAdventureModel();
		BasicChapter chapter = new BasicChapter();
		model.getChapters().add(chapter);
		BezierShape shape = new BezierShape();
		shape.moveTo(100, 200);
		shape.lineTo(50, -23);
		shape.lineTo(1000, 12200);
		shape.setClosed(true);
		shape.setPaintAsVector(true);
		shape.setPaint(ColorFill.BLUE);
		BasicScene scene = new BasicScene(shape);
		chapter.getScenes().add(scene);
		TriggerMacroEf triggerMacro = new TriggerMacroEf();
		EffectsMacro macro1 = new EffectsMacro();
		EffectsMacro macro2 = new EffectsMacro();
		macro1.getEffects().add(triggerMacro);
		macro2.getEffects().add(triggerMacro);
		macro2.getEffects().add(triggerMacro);
		triggerMacro.putMacro(macro1, EmptyCond.TRUE_EMPTY_CONDITION);
		triggerMacro.putMacro(macro1, EmptyCond.TRUE_EMPTY_CONDITION);
		triggerMacro.putMacro(macro1, EmptyCond.TRUE_EMPTY_CONDITION);
		triggerMacro.putMacro(macro1, EmptyCond.TRUE_EMPTY_CONDITION);
		triggerMacro.putMacro(macro2, EmptyCond.FALSE_EMPTY_CONDITION);
		scene.getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
				triggerMacro);

		EAdAdventureModel model2 = super.writeAndRead(model);
		EAdSceneElement background = model2.getChapters().get(0).getScenes()
				.get(0).getBackground();

		TriggerMacroEf triggerMacro2 = (TriggerMacroEf) background
				.getBehavior().getEffects(MouseGEv.MOUSE_LEFT_PRESSED).get(0);
		assertTrue(triggerMacro2.getConditions().size() == triggerMacro
				.getConditions().size());
		assertTrue(triggerMacro2.getMacros().size() == triggerMacro.getMacros()
				.size());

		BezierShape shape2 = (BezierShape) background
				.getDefinition()
				.getResources()
				.getAsset(background.getDefinition().getInitialBundle(),
						SceneElementDef.appearance);
		
		assertTrue(shape2.equals(shape));

	}
}
