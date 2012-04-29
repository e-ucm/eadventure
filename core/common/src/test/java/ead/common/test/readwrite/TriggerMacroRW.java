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
