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

package es.eucm.ead.importer.testers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.model.Commands;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.tools.TextFileWriter;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ConverterTester {

	private static final Logger logger = LoggerFactory
			.getLogger(ConverterTester.class);

	private ArrayList<String> instructions;

	private VarsMap varsMap;

	private ModelQuerier modelQuerier;

	private ExitTester exitTester;
	private boolean testing;

	@Inject
	public ConverterTester(ModelQuerier modelQuerier) {
		this.instructions = new ArrayList<String>();
		this.varsMap = new VarsMap();
		this.modelQuerier = modelQuerier;
		this.exitTester = new ExitTester(this, modelQuerier);
	}

	public void check(String command, String result) {
		check(0, command, result);
	}

	public void command(String command) {
		check(0, command, "");
	}

	public void command(int time, String command) {
		check(time, command, "");
	}

	public void command(String command, String... params) {
		String c = command;
		for (String p : params) {
			c += " " + p;
		}
		command(c);
	}

	public void check(int time, String command, String result) {
		if (testing) {
			instructions.add(time + ";" + command + ";" + result);
		}
	}

	public void write(String file, TextFileWriter writer) {
		String text = "";
		for (String s : instructions) {
			text += s + System.lineSeparator();
		}
		writer.write(text, file);
	}

	public void checkBundles(ElementReference ref, String refId) {
		if (testing) {
			Element e = modelQuerier.getElementById(ref.getTargetId());
			varsMap.make(ref.getConditions(), true);
			varsMap.writeState(modelQuerier.getChapterId(), this);
			check(Commands.GET + " " + refId + ".visible", "true");
			if (e.getResources().size() == 1) {
				check(Commands.GET + " " + refId + ".bundleId", "bundle0");
			} else {
				Conditions[] conditions = new Conditions[e.getResources()
						.size()];
				int i = 0;
				for (Resources r : e.getResources()) {
					conditions[i] = r.getConditions();
					command(Commands.LOG + " making true conditions for '"
							+ refId + "' bundle " + i);
					boolean contradiction = varsMap.makeTrue(i, conditions);
					if (contradiction) {
						logger
								.warn(
										"Contradiction: It was impossible to set the conditions to activate the bundle {} in {}",
										i, refId);
					} else {
						varsMap.writeState(modelQuerier.getChapterId(), this);
						command(Commands.PASS);
						check(Commands.GET + " " + refId + ".bundleId",
								"bundle" + i);
					}
					i++;
				}
			}
		}
	}

	public void checkExit(String currentScene, Exit e, SceneElement exit,
			List<Effect> effects, ChangeSceneEf nextScene,
			List<Effect> postEffects, List<Effect> notEffects) {
		if (testing) {
			exitTester.test(currentScene, e, exit, effects, nextScene,
					postEffects, notEffects);
		}
	}

	public void setCondition(Conditions conditions, boolean satisfy) {
		varsMap.make(conditions, satisfy);
		varsMap.writeState(modelQuerier.getChapterId(), this);
	}

	public void setTesting(boolean testing) {
		this.testing = testing;
	}
}
