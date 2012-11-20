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

package ead.engine.core.operators;

import java.util.Random;

import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.model.elements.variables.operations.ListOp;
import ead.engine.core.game.ValueMap;

public class ListOperator implements Operator<ListOp> {

	private ValueMap valueMap;

	private Random random;

	private EAdList<Object> auxList;

	public ListOperator(ValueMap valueMap) {
		this.valueMap = valueMap;
		random = new Random(System.currentTimeMillis());
		auxList = new EAdListImpl<Object>(Object.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S operate(Class<S> clazz, ListOp operation) {
		EAdList<?> list = valueMap.getValue(operation.getListField());

		switch (operation.getOperation()) {
		case RANDOM_ELEMENT:
			if (list.getValueClass().equals(clazz)) {
				return (S) list.get(random.nextInt(list.size()));
			}
			break;
		case RANDOM_LIST:
			if (clazz.equals(EAdList.class)) {
				for (Object o : list) {
					auxList.add(o);
				}
				EAdList<Object> randomList = new EAdListImpl<Object>(
						Object.class);
				while (auxList.size() > 0)
					randomList.add(auxList.remove(random
							.nextInt(auxList.size())));

				return (S) randomList;
			}
			break;
		}
		return null;
	}
}
