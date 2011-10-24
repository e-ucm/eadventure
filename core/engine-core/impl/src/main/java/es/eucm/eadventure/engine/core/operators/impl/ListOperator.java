package es.eucm.eadventure.engine.core.operators.impl;

import java.util.Random;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.ListOperation;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.operator.Operator;

public class ListOperator implements Operator<ListOperation> {

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
	public <S> S operate(Class<S> clazz, ListOperation operation) {
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
					randomList
							.add(auxList.remove(random.nextInt(auxList.size())));
				
				return (S) randomList;
			}
			break;
		}
		return null;
	}
}
