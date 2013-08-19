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

package es.eucm.ead.json.reader;

import com.google.gson.internal.StringMap;
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.multimedia.EAdSound;
import es.eucm.ead.model.assets.multimedia.Video;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.EAdElement;
import es.eucm.ead.model.elements.ResourcedElement;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.PlaySoundEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.huds.MouseHud;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.elements.predef.effects.ChangeAppearanceEf;
import es.eucm.ead.model.elements.predef.effects.MoveActiveElementToMouseEf;
import es.eucm.ead.model.elements.scenes.*;
import es.eucm.ead.model.interfaces.features.enums.Orientation;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.util.Rectangle;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.reader.model.ObjectsFactory;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class SceneReader {

	private EAdElement mouse = new BasicElement(MouseHud.CURSOR_ID);

	private ObjectsFactory objectsFactory;

	private TemplateReader templateReader;

	private TrajectoryReader trajectoryReader;

	public SceneReader(ObjectsFactory objectsFactory,
			TemplateReader templateReader) {
		this.objectsFactory = objectsFactory;
		this.templateReader = templateReader;
		this.trajectoryReader = new TrajectoryReader();
	}

	public EAdScene parseScene(StringMap<Object> jsonScene) {
		EAdScene scene = null;
		String type = (String) jsonScene.get("type");
		String id = (String) jsonScene.get("id");
		if (type == null || type.equals("scene")) {
			scene = getScene(jsonScene);
		} else if (type.equals("video")) {
			scene = getVideoScene(jsonScene);
		}
		if (id != null)
			scene.setId(id);
		objectsFactory.putEAdElement(scene.getId(), scene);
		return scene;
	}

	private EAdScene getVideoScene(StringMap<Object> jsonScene) {
		VideoScene scene = new VideoScene();
		scene.setVideo(new Video((String) jsonScene.get("uri")));
		EAdElement nextScene = new BasicElement(jsonScene.get("nextScene")
				.toString());
		scene.getFinalEffects().add(new ChangeSceneEf((EAdScene) nextScene));
		return scene;
	}

	public EAdScene getScene(StringMap<Object> jsonScene) {
		EAdDrawable background = (EAdDrawable) objectsFactory
				.getAsset((String) jsonScene.get("background"));
		BasicScene scene = new BasicScene(background);

		if (jsonScene.containsKey("music")) {
			String music = (String) jsonScene.get("music");
			if (music != null) {
				EAdSound bgMusic = (EAdSound) objectsFactory.getAsset(music);
				PlaySoundEf playBg = new PlaySoundEf(bgMusic);
				scene.addInitEffect(playBg);
			} else {
				PlaySoundEf playBg = new PlaySoundEf(null);
				scene.addInitEffect(playBg);
			}
		}

		Collection<StringMap<Object>> sceneElements = (Collection<StringMap<Object>>) jsonScene
				.get("sceneElements");

		if (sceneElements != null) {
			for (StringMap<Object> e : sceneElements) {
				EAdSceneElement s = parseSceneElement(e);
				scene.add(s);
			}
		}
		// Add trajectory
		StringMap<Object> t = (StringMap<Object>) jsonScene.get("trajectory");
		if (t != null) {
			scene.setTrajectoryDefinition(trajectoryReader.read(t));
			scene.getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
					new MoveActiveElementToMouseEf());
		}

		// Add active scene element
		String activeElement = (String) jsonScene.get("activeElement");
		if (activeElement != null) {
			EAdSceneElement e = (EAdSceneElement) objectsFactory
					.getEAdElement(activeElement);
			SceneElementEv event = new SceneElementEv();
			event.addEffect(SceneElementEvType.ADDED, new ChangeFieldEf(
					SystemFields.ACTIVE_ELEMENT, new ValueOp(e)));
			scene.getEvents().add(event);
		}
		return scene;
	}

	@SuppressWarnings( { "rawtypes" })
	public EAdSceneElement parseSceneElement(StringMap<Object> jsonSceneElement) {
		templateReader.applyTemplates(jsonSceneElement);

		String appearance = (String) jsonSceneElement.get("appearance");

		EAdDrawable d = null;
		if (appearance != null) {
			d = (EAdDrawable) objectsFactory.getAsset(appearance);
		}

		SceneElement e = null;

		Collection<StringMap<Object>> children = (Collection<StringMap<Object>>) jsonSceneElement
				.get("children");

		String type = (String) jsonSceneElement.get("type");
		if (type == null) {
			if (children == null) {
				e = new SceneElement(d);
			} else {
				GroupElement g = new GroupElement(d);
				for (StringMap<Object> s : children) {
					g.addSceneElement(parseSceneElement(s));
				}
				e = g;
			}
		} else if (type.equals("ghost")) {
			e = new GhostElement(d);
		}

		// Over appearance
		String overappearance = (String) jsonSceneElement.get("overappearance");
		if (overappearance != null) {
			d = (EAdDrawable) objectsFactory.getAsset(overappearance);
			e.setOverAppearance(d);
		}

		// Cursor
		String cursor = (String) jsonSceneElement.get("cursor");
		if (cursor != null) {
			e.addBehavior(MouseGEv.MOUSE_ENTERED, new ChangeAppearanceEf(mouse,
					cursor));
			e.addBehavior(MouseGEv.MOUSE_EXITED, new ChangeAppearanceEf(mouse,
					ResourcedElement.INITIAL_BUNDLE));
		}

		// Contain bounds
		Boolean containsBounds = (Boolean) jsonSceneElement
				.get("containsBounds");
		e.setContainsBounds(containsBounds != null
				&& containsBounds.booleanValue());

		for (String key : jsonSceneElement.keySet()) {
			EAdVarDef var = objectsFactory.getVarDef(key);
			if (var != null) {
				Object value = jsonSceneElement.get(key);
				if (var.getType() == Boolean.class) {
					e.setVarInitialValue(var, value);
				} else if (var.getType() == Integer.class) {
					e.setVarInitialValue(var, ((Number) value).intValue());
				} else if (var.getType() == Float.class) {
					e.setVarInitialValue(var, ((Number) value).floatValue());
				} else if (var.getType() == EAdString.class) {
					e.setVarInitialValue(var, new EAdString(value.toString()));
				} else if (var.getType() == String.class) {
					e.setVarInitialValue(var, value.toString());
				} else if (var.getType() == Rectangle.class) {
					e.setVarInitialValue(var, objectsFactory.getParam(value
							.toString(), Rectangle.class));
				} else if (var.getType() == Orientation.class) {
					e.setVarInitialValue(var, Orientation.parse(value
							.toString()));
				}

			}
		}

		// Bundles
		Collection<StringMap<Object>> bundles = (Collection<StringMap<Object>>) jsonSceneElement
				.get("bundles");
		if (bundles != null) {
			for (StringMap<Object> b : bundles) {
				String id = (String) b.get("id");
				appearance = (String) b.get("appearance");
				overappearance = (String) b.get("appearance");
				if (appearance != null) {
					e.setAppearance(id, (EAdDrawable) objectsFactory
							.getAsset(appearance));
				}
				if (overappearance != null) {
					e.setOverAppearance(id, (EAdDrawable) objectsFactory
							.getAsset(overappearance));
				}
			}
		}

		String id = (String) jsonSceneElement.get("id");
		if (id != null)
			e.setId(id);
		objectsFactory.putEAdElement(e.getId(), e);

		return e;
	}
}
