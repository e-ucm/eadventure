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

package es.eucm.ead.importer.utils;

import es.eucm.ead.model.Commands;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conditions.FlagCondition;
import es.eucm.eadventure.common.data.chapter.conditions.GlobalState;
import es.eucm.eadventure.common.data.chapter.conditions.VarCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class VarsMap {

	private Logger logger = LoggerFactory.getLogger(VarsMap.class);

	private Chapter chapter;

	private Map<String, Boolean> flags;

	private Map<String, List<Condition>> trueConditions;

	private Map<String, Integer> vars;

	private List<Condition> conditions;

	private boolean[] combinations;

	private OldConditionsEvaluator oldConditionsEvaluator;

	public VarsMap() {
		flags = new HashMap<String, Boolean>();
		vars = new HashMap<String, Integer>();
		conditions = new ArrayList<Condition>();
		oldConditionsEvaluator = new OldConditionsEvaluator(this);
		trueConditions = new HashMap<String, List<Condition>>();
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public void setVariable(String id, int value) {
		vars.put(id, value);
	}

	public void setFlag(String id, boolean value) {
		flags.put(id, value);
	}

	public boolean isActiveFlag(String id) {
		return flags.containsKey(id) ? flags.get(id) : false;
	}

	public int getValue(String id) {
		return vars.containsKey(id) ? vars.get(id) : 0;
	}

	public GlobalState getGlobalState(String globalStateId) {
		return chapter.getGlobalState(globalStateId);
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void clear() {
		flags.clear();
		vars.clear();
		conditions.clear();
		trueConditions.clear();
	}

	// Conditions manipulation

	public void makeTrue(int index, Conditions... conditions) {
		clear();
		for (int i = 0; i <= index; i++) {
			analyze(conditions[i]);
		}
		combinations = new boolean[this.conditions.size()];
		boolean indexTrue = false;
		boolean restFalse;
		boolean nextCombination;
		do {
			if (updateConditions()) {
				restFalse = true;
				for (int i = 0; i < index - 1 && restFalse; i++) {
					restFalse = !oldConditionsEvaluator
							.allConditionsOk(conditions[i]);
				}

				if (restFalse) {
					indexTrue = oldConditionsEvaluator
							.allConditionsOk(conditions[index]);
				}
			}

			nextCombination = nextCombination();

		} while (!indexTrue && nextCombination);
	}

	private void analyze(Conditions conditions) {
		for (List<Condition> l : conditions.getConditionsList()) {
			for (Condition c : l) {
				if (c.getType() == Condition.GLOBAL_STATE_CONDITION) {
					analyze(chapter.getGlobalState(c.getId()));
				} else {
					if (!contains(c)) {
						this.conditions.add(c);
					}
				}
			}
		}
	}

	private boolean make(Condition c, boolean result) {
		if (result) {
			return makeTrue(c);
		} else {
			return makeFalse(c);
		}
	}

	private boolean makeFalse(Condition c) {
		try {
			Condition c1 = (Condition) c.clone();
			if (c.getType() == Condition.FLAG_CONDITION) {
				c1
						.setState(c1.getState() == FlagCondition.FLAG_ACTIVE ? FlagCondition.FLAG_INACTIVE
								: FlagCondition.FLAG_ACTIVE);
			} else if (c.getType() == Condition.VAR_CONDITION) {
				switch (c.getState()) {
				case VarCondition.VAR_EQUALS:
					c1.setState(VarCondition.VAR_NOT_EQUALS);
					break;
				case VarCondition.VAR_LESS_EQUALS_THAN:
					c1.setState(VarCondition.VAR_GREATER_THAN);
					break;
				case VarCondition.VAR_LESS_THAN:
					c1.setState(VarCondition.VAR_GREATER_EQUALS_THAN);
					break;
				case VarCondition.VAR_NOT_EQUALS:
					c1.setState(VarCondition.VAR_EQUALS);
					break;
				case VarCondition.VAR_GREATER_THAN:
					c1.setState(VarCondition.VAR_LESS_EQUALS_THAN);
					break;
				case VarCondition.VAR_GREATER_EQUALS_THAN:
					c1.setState(VarCondition.VAR_LESS_THAN);
					break;
				}
			}
			return makeTrue(c1);
		} catch (CloneNotSupportedException e) {
			logger.error("{}", e);
		}
		return true;
	}

	private boolean makeTrue(Condition c) {
		if (c.getType() == Condition.FLAG_CONDITION) {
			setFlag(c.getId(), c.getState() == FlagCondition.FLAG_ACTIVE);
		} else if (c.getType() == Condition.VAR_CONDITION) {
			int value = ((VarCondition) c).getValue();
			switch (c.getState()) {
			case VarCondition.VAR_EQUALS:
			case VarCondition.VAR_LESS_EQUALS_THAN:
			case VarCondition.VAR_GREATER_EQUALS_THAN:
				setVariable(c.getId(), value);
				break;
			case VarCondition.VAR_NOT_EQUALS:
			case VarCondition.VAR_LESS_THAN:
				setVariable(c.getId(), value + 1);
				break;
			case VarCondition.VAR_GREATER_THAN:
				setVariable(c.getId(), value - 1);
				break;
			}
		}
		// Check that conditions for the id are still true
		boolean contradicition = false;
		List<Condition> l = this.trueConditions.get(c.getId());
		if (l == null) {
			l = new ArrayList<Condition>();
			trueConditions.put(c.getId(), l);
		}
		for (int i = 0; i < l.size() && !contradicition; i++) {
			contradicition = !oldConditionsEvaluator
					.evaluateCondition(l.get(i));
		}
		l.add(c);
		return contradicition;
	}

	/**
	 * Returns false if there's no more combinations *
	 */
	private boolean nextCombination() {
		boolean allTrue = true;
		for (int i = 0; i < combinations.length && allTrue; i++) {
			allTrue = combinations[i];
		}
		if (allTrue) {
			return false;
		}
		addOne(0);
		updateConditions();
		return true;
	}

	/**
	 * Returns if it was possible to set the conditions, and there was no contradiction with the current values *
	 */
	private boolean updateConditions() {
		boolean contradiction = false;
		for (int i = 0; i < conditions.size() && !contradiction; i++) {
			contradiction = make(conditions.get(i), combinations[i]);
		}
		return contradiction;
	}

	private void addOne(int index) {
		if (index < combinations.length) {
			if (combinations[index]) {
				combinations[index] = false;
				addOne(index + 1);
			} else {
				combinations[index] = true;
			}
		}
	}

	private boolean contains(Condition c) {
		for (Condition c1 : this.conditions) {
			if (c1.getId().equals(c.getId()) && c.getType() == c1.getType()
					&& c.getState().equals(c1.getState())) {
				return true;
			}
		}
		return false;
	}

	public void writeState(String chapterId, ConverterTester converterTester) {
		writeState(chapterId, converterTester, flags);
		writeState(chapterId, converterTester, vars);
	}

	@SuppressWarnings("unchecked")
	private void writeState(String chapterId, ConverterTester converterTester,
			Map map) {
		Set<Entry> s = map.entrySet();
		for (Entry<Object, Object> e : s) {
			converterTester.command(Commands.SET + " " + chapterId + "."
					+ e.getKey() + " " + e.getValue());
		}
	}
}
