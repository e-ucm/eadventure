package ead.writer.model.writers.simplifiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.interfaces.features.Identified;
import ead.common.interfaces.features.Variabled;
import ead.common.model.assets.AssetDescriptor;
import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.assets.drawable.basics.animation.FramesAnimation;
import ead.common.model.assets.drawable.compounds.StateDrawable;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.behaviors.Behavior;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.ListedCond;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.operations.MathOp;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.params.variables.EAdVarDef;
import ead.tools.EAdUtils;
import ead.tools.MathEvaluator;

public class Simplifier {

	private static final Logger logger = LoggerFactory.getLogger("Simplifier");

	private static final Behavior EMPTY_BEHAVIOR = new Behavior();
	private static final SceneElementDef EMPTY_DEF = new SceneElementDef();

	/**
	 * Map to aggregate all repeated fields
	 */
	private Map<Object, Map<EAdVarDef<?>, EAdField<?>>> fields;

	private Map<String, Image> images;

	private Map<Class<?>, List<Identified>> objectsLists;

	/**
	 * An auxiliary field
	 */
	private List<EAdCondition> conditionsAux;
	private List<EAdCondition> conditionsAuxToAdd;

	private List<EAdVarDef<?>> varDefsAux;

	/**
	 * Lists to aggregate operations
	 */
	private List<EAdOperation> operations;

	/**
	 * MathEvaluator to help simplify MathOp with no values
	 */
	private MathEvaluator mathEvaluator;

	private int simplifications;

	private Map<Class<?>, CheckEquals<?>> checkEquals;
	private Map<Class<?>, List<?>> listEquals;

	public Simplifier() {
		fields = new HashMap<Object, Map<EAdVarDef<?>, EAdField<?>>>();
		simplifications = 0;
		conditionsAux = new ArrayList<EAdCondition>();
		conditionsAuxToAdd = new ArrayList<EAdCondition>();
		operations = new ArrayList<EAdOperation>();
		mathEvaluator = new MathEvaluator();
		images = new HashMap<String, Image>();
		checkEquals = new HashMap<Class<?>, CheckEquals<?>>();
		listEquals = new HashMap<Class<?>, List<?>>();
		objectsLists = new HashMap<Class<?>, List<Identified>>();
		varDefsAux = new ArrayList<EAdVarDef<?>>();
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public EAdElement simplifyEAdElement(EAdElement object) {
		if (object instanceof Variabled) {
			object = (EAdElement) simplifyVariabled((Variabled) object);
		}

		if (object instanceof EAdSceneElement
				|| object.getClass() == BasicElement.class) {
			// EAdSceneElement can not be simplified
			// BaiscElement are already simplified
			return object;
		} else if (object instanceof EAdField<?>) {
			return simplifyField((EAdField<?>) object);
		} else if (object instanceof EAdCondition) {
			object = simplifyCondition((EAdCondition) object);
		} else if (object instanceof EAdOperation) {
			object = simplifyOperation((EAdOperation) object);
		} else if (object instanceof Behavior) {
			object = simplifyBehavior((Behavior) object);
		} else if (object instanceof SceneElementDef) {
			object = simplifySceneElementDef((SceneElementDef) object);
		} else {
			// CheckEquals ce = checkEquals.get(object.getClass());
			// if (ce != null) {
			// List list = listEquals.get(object.getClass());
			// if (list == null) {
			// list = new ArrayList();
			// listEquals.put(object.getClass(), list);
			// }
			// object = simplifyCheckEquals(object, list, ce);
			// }
		}

		if (object == null) {
			logger.warn("Error simplifiying asset: {}", object);
			return null;
		}

		List<Identified> list = objectsLists.get(object.getClass());
		if (list == null) {
			list = new ArrayList();
			objectsLists.put(object.getClass(), list);
		}
		object = (EAdElement) simplifyCheckEquals(object, list, generalEq);
		return object;
	}

	private Variabled simplifyVariabled(Variabled object) {
		varDefsAux.clear();
		for (Entry<EAdVarDef<?>, Object> e : object.getVars().entrySet()) {
			Object value = e.getKey().getInitialValue();
			if (value == e.getValue()
					|| (value != null && value.equals(e.getValue()))) {
				varDefsAux.add(e.getKey());
			}
		}

		for (EAdVarDef<?> v : varDefsAux) {
			object.getVars().remove(v);
		}
		return object;
	}

	private EAdElement simplifySceneElementDef(SceneElementDef object) {
		if (object.getResources().isEmpty() && object.getEvents().isEmpty()
				&& object.getBehavior().isEmpty() && object.getVars().isEmpty()) {
			return EMPTY_DEF;
		}
		return object;
	}

	public AssetDescriptor simplifyAssetDescriptor(AssetDescriptor a) {
		if (a instanceof StateDrawable) {
			a = simplifyStateDrawable((StateDrawable) a);
		} else if (a instanceof FramesAnimation) {
			a = simplifyFramesAnimation((FramesAnimation) a);
		} else if (a instanceof Image) {
			a = simplifyImage((Image) a);
		}

		if (a == null) {
			logger.warn("Error simplifiying asset: {}", a);
			return null;
		}
		List<Identified> list = objectsLists.get(a.getClass());
		if (list == null) {
			list = new ArrayList<Identified>();
			objectsLists.put(a.getClass(), list);
		}
		a = (AssetDescriptor) simplifyCheckEquals(a, list, generalEq);
		return a;
	}

	private AssetDescriptor simplifyImage(Image i) {
		// XXX Add to atlas
		Image copy = images.get(i.getUri());
		if (copy == null) {
			copy = i;
			images.put(i.getUri(), copy);
		}
		return copy;
	}

	private AssetDescriptor simplifyStateDrawable(StateDrawable s) {
		if (s.getStates().size() == 1) {
			return simplifyAssetDescriptor(s.getDrawablesCollection()
					.iterator().next());
		} else {
			boolean allequals = true;
			EAdDrawable d = s.getDrawablesCollection().iterator().next();
			for (EAdDrawable d2 : s.getDrawablesCollection()) {
				if (d != d2) {
					allequals = false;
					break;
				}
			}

			if (allequals) {
				return simplifyAssetDescriptor(d);
			}
		}
		return s;
	}

	private AssetDescriptor simplifyFramesAnimation(FramesAnimation f) {
		if (f.getFrameCount() == 1) {
			return f.getFrame(0).getDrawable();
		}
		return f;
	}

	public int getSimplifications() {
		return simplifications;
	}

	private EAdField<?> simplifyField(EAdField<?> field) {
		Map<EAdVarDef<?>, EAdField<?>> elementFields = fields.get(field
				.getElement());
		if (elementFields == null) {
			elementFields = new HashMap<EAdVarDef<?>, EAdField<?>>();
			fields.put(field.getElement(), elementFields);
		}
		EAdField<?> copy = elementFields.get(field.getVarDef());
		if (copy == null) {
			copy = field;
			elementFields.put(field.getVarDef(), copy);
		} else {
			if (copy != field) {
				simplifications++;
			}
		}
		return copy;
	}

	public EAdCondition simplifyCondition(EAdCondition condition) {
		if (condition instanceof ListedCond) {
			condition = simplifyListed((ListedCond) condition);
		}
		return condition;
	}

	private EAdCondition simplifyListed(ListedCond condition) {
		conditionsAux.clear();
		conditionsAuxToAdd.clear();
		for (EAdCondition c : condition.getConditions()) {
			if (c instanceof EmptyCond) {
				// If it is the condition null operator, the whole condition is
				// false
				if (!c.equals(condition.getNullOperator())) {
					logger.debug("{}", condition);
					logger.debug("->");
					logger.debug("{}", condition.getNullOperator());
					return condition.getNullOperator();
				}
				// If not, it is not necessary, so we delete it
				else {
					conditionsAux.add(c);
				}
			} else if (c instanceof ListedCond) {
				EAdCondition c2 = simplifyListed((ListedCond) c);
				if (c2 != c) {
					conditionsAux.add(c);
					// If it's an OR inside another OR, we group them and remove
					// unnecessary conditions
					if (c2 instanceof ListedCond) {
						ListedCond listed = (ListedCond) c2;
						if (listed.getNullOperator().equals(
								((ListedCond) c).getNullOperator())) {
							conditionsAuxToAdd.addAll(listed.getConditions());
						} else {
							conditionsAuxToAdd.add(c2);
						}
					} else {
						conditionsAuxToAdd.add(c2);
					}
				}
			}
		}

		for (EAdCondition c : conditionsAux) {
			condition.getConditions().remove(c);
		}

		for (EAdCondition c : conditionsAuxToAdd) {
			condition.getConditions().add(c);
		}

		if (condition.getConditions().size() == 1) {
			return condition.getConditions().get(0);
		}

		return condition;
	}

	private EAdOperation simplifyOperation(EAdOperation operation) {
		// If it is math expression with no operands, simplify to a ValueOp
		if (operation instanceof MathOp
				&& !((MathOp) operation).getExpression().contains("[")) {
			String expression = ((MathOp) operation).getExpression();
			mathEvaluator.setExpression(expression, null, null);
			Float value = mathEvaluator.getValue();
			operation = new ValueOp(
					((MathOp) operation).isResultAsInteger() ? value.intValue()
							: value);
		}

		int index = operations.indexOf(operation);
		if (index != -1) {
			EAdOperation op = operations.get(index);
			if (op != operation) {
				simplifications++;
			}
			return op;
		} else {
			operations.add(operation);
		}
		return operation;
	}

	private EAdElement simplifyBehavior(Behavior b) {
		if (b.getBehavior().isEmpty()) {
			return EMPTY_BEHAVIOR;
		}
		return b;
	}

	public interface CheckEquals<T> {

		boolean equals(T o1, T o2);
	}

	private class CheckEqualsEffect<T extends EAdEffect> implements
			CheckEquals<T> {

		@Override
		public boolean equals(T o1, T o2) {
			if (o1.getNextEffects().size() > 0
					|| o2.getNextEffects().size() > 0
					|| o1.getSimultaneousEffects().size() > 0
					|| o2.getSimultaneousEffects().size() > 0) {
				return false;
			}
			if (o1.getCondition() == o2.getCondition()
					|| (o1.getCondition() != null && o1.getCondition().equals(
							o2.getCondition()))) {
				return true;
			}
			return false;
		}

	}

	private CheckEquals<Identified> generalEq = new CheckEquals<Identified>() {

		@Override
		public boolean equals(Identified o1, Identified o2) {
			if (o1 == o2) {
				return true;
			}
			boolean equals = EAdUtils.equals(o1, o2, true);
			if (equals) {
				logger.debug("Equals match {}={}", new Object[] { o1, o2 });
			}
			return equals;
			// return false;
		}

	};

	private <T> T simplifyCheckEquals(T object, List<T> list,
			CheckEquals<T> checkEquals) {
		T copy = find(object, list, checkEquals);
		if (copy == null) {
			copy = object;
			list.add(object);
		} else {
			simplifications++;
		}
		return copy;
	}

	public <T> T find(T e, List<T> list, CheckEquals<T> checkEquals) {
		for (T o : list) {
			if (checkEquals.equals(e, o)) {
				return o;
			}
		}
		return null;
	}

	public Map<Object, Map<EAdVarDef<?>, EAdField<?>>> getFields() {
		return fields;
	}

	public Map<Class<?>, List<Identified>> getIdentified() {
		return objectsLists;
	}

}
