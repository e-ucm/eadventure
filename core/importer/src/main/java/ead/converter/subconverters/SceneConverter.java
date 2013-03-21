package ead.converter.subconverters;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.basics.shapes.AbstractShape;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.util.Position.Corner;
import ead.converter.EAdElementsCache;
import ead.converter.UtilsConverter;
import ead.converter.resources.ResourceConverter;
import ead.converter.subconverters.conditions.ConditionConverter;
import ead.converter.subconverters.effects.EffectsConverter;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;

@Singleton
public class SceneConverter {

	private static final int EXIT_Z = 20000;

	private static final int ACTIVE_AREA_Z = 10000;

	private static final ColorFill EXIT_FILL = new ColorFill(255, 0, 0, 100);

	private static final ColorFill ACTIVE_AREA_FILL = new ColorFill(0, 255, 0,
			100);

	private TransitionConverter transitionConverter;

	private ResourceConverter resourceConverter;

	private EAdElementsCache elementsCache;

	private RectangleConverter rectangleConverter;

	private UtilsConverter utilsConverter;

	private EffectsConverter effectConverter;

	private ConditionConverter conditionsConverter;

	@Inject
	public SceneConverter(ResourceConverter resourceConverter,
			EAdElementsCache elementsCache,
			TransitionConverter transitionConverter,
			RectangleConverter rectangleConverter,
			UtilsConverter utilsConverter, EffectsConverter effectConverter,
			ConditionConverter conditionsConverter) {
		this.resourceConverter = resourceConverter;
		this.elementsCache = elementsCache;
		this.transitionConverter = transitionConverter;
		this.rectangleConverter = rectangleConverter;
		this.utilsConverter = utilsConverter;
		this.effectConverter = effectConverter;
		this.conditionsConverter = conditionsConverter;
	}

	public EAdScene convert(Scene s) {

		SceneElement background = new SceneElement();
		BasicScene scene = new BasicScene(background);
		scene.setId(s.getId());

		addAppearance(scene, s);
		// XXX Information
		addReferences(scene, s);
		addActiveZones(scene, s);
		addExits(scene, s);

		return scene;
	}

	public void addAppearance(BasicScene scene, Scene s) {
		// Appearance tab
		SceneElement background = (SceneElement) scene.getBackground();
		// Resources blocks
		int i = 0;
		for (Resources r : s.getResources()) {
			// Background
			String backgroundPath = r
					.getAssetPath(Scene.RESOURCE_TYPE_BACKGROUND);
			EAdDrawable drawable = resourceConverter.getImage(backgroundPath);
			background.setAppearance(utilsConverter.getResourceBundleId(i),
					drawable);
			if (i == 0) {
				background.setInitialBundle(utilsConverter
						.getResourceBundleId(i));
			}

			// XXX Front mask

			// XXX Music scene

			i++;
		}

		// Add conditioned resources
		utilsConverter.addResourcesConditions(s.getResources(), scene
				.getBackground(), SceneElement.VAR_BUNDLE_ID);
	}

	private void addReferences(BasicScene scene, Scene s) {
		addReferences(scene, s.getAtrezzoReferences());
		addReferences(scene, s.getItemReferences());
		addReferences(scene, s.getCharacterReferences());
	}

	private void addReferences(BasicScene scene,
			List<ElementReference> references) {
		for (ElementReference e : references) {
			SceneElement sceneElement = new SceneElement(
					(EAdSceneElementDef) elementsCache.get(e.getTargetId()));
			sceneElement.setId(e.getTargetId() + "_" + sceneElement.getId());
			sceneElement.setPosition(Corner.BOTTOM_CENTER, e.getX(), e.getY());
			sceneElement.setInitialZ(e.getLayer());
			sceneElement.setInitialScale(e.getScale());
			// XXX Influence area
			scene.add(sceneElement);

			// Add event to change appearance when required by the actor's
			// definition
			utilsConverter.addWatchDefinitionField(sceneElement,
					SceneElement.VAR_STATE);

			// Add visibility condition
			utilsConverter.addWatchCondition(sceneElement, sceneElement
					.getField(SceneElement.VAR_VISIBLE), e.getConditions());
		}
	}

	private void addExits(BasicScene scene, Scene s) {
		int i = 0;
		for (Exit e : s.getExits()) {
			AbstractShape shape = rectangleConverter.convert(e, EXIT_FILL);

			GhostElement exit = new GhostElement(shape);
			if (e.isRectangular()) {
				exit.setPosition(Corner.TOP_LEFT, e.getX(), e.getY());
			}

			EAdEffect effectWhenClick = null;

			// Next scene
			ChangeSceneEf nextScene = new ChangeSceneEf();
			nextScene.setNextScene(new BasicElement(e.getNextSceneId()));
			nextScene.setTransition(transitionConverter.getTransition(e
					.getTransitionType(), e.getTransitionTime()));

			// Add effects
			List<EAdEffect> effects = effectConverter.convert(e.getEffects());
			if (effects.size() > 0) {
				effectWhenClick = effects.get(0);
				effects.get(effects.size() - 1).getNextEffects().add(nextScene);
			} else {
				effectWhenClick = nextScene;
			}

			// Add next effects
			effects = effectConverter.convert(e.getPostEffects());
			if (effects.size() > 0) {
				nextScene.getNextEffects().add(effects.get(0));
			}

			// Set Z
			exit.setInitialZ(EXIT_Z + i);

			// Add the exit to the scene
			scene.add(exit);

			// If it has not-effects
			if (e.isHasNotEffects()) {
				TriggerMacroEf triggerMacro = new TriggerMacroEf();
				EAdCondition cond = conditionsConverter.convert(e
						.getConditions());
				// Add ACTIVE effects
				triggerMacro.putEffect(effectWhenClick, cond);
				// Add INACTIVE effects
				EffectsMacro macro = new EffectsMacro();
				effects = effectConverter.convert(e.getNotEffects());
				if (effects.size() > 0) {
					macro.getEffects().add(effects.get(0));
				}
				// The macro only executes if the first condition fails
				triggerMacro.putMacro(macro, EmptyCond.TRUE);
				exit.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, triggerMacro);

			} else {
				exit.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effectWhenClick);
				// Add visibility condition
				utilsConverter.addWatchCondition(exit, exit
						.getField(SceneElement.VAR_VISIBLE), e.getConditions());
			}

			i++;
		}

	}

	private void addActiveZones(BasicScene scene, Scene s) {
		int i = 0;
		for (ActiveArea a : s.getActiveAreas()) {
			AbstractShape shape = rectangleConverter.convert(a,
					ACTIVE_AREA_FILL);
			GhostElement activeArea = new GhostElement(shape);
			if (a.isRectangular()) {
				activeArea.setPosition(Corner.TOP_LEFT, a.getX(), a.getY());
			}
			// Set Z
			activeArea.setInitialZ(ACTIVE_AREA_Z + i);
			// Add visibility condition
			utilsConverter.addWatchCondition(activeArea, activeArea
					.getField(SceneElement.VAR_VISIBLE), a.getConditions());
			i++;
		}

	}

}
