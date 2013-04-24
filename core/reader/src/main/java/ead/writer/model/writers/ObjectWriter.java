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

package ead.writer.model.writers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.interfaces.features.Identified;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.ListedCond;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.operations.MathOp;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.params.variables.EAdVarDef;
import ead.reader.DOMTags;
import ead.tools.MathEvaluator;
import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;
import ead.tools.xml.XMLNode;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ModelVisitor.VisitorListener;

public class ObjectWriter extends AbstractWriter<Identified> {

	private static final Logger logger = LoggerFactory
			.getLogger("ObjectWriter");

	/**
	 * Map to aggregate all repeated fields
	 */
	private Map<Object, Map<EAdVarDef<?>, EAdField<?>>> fields;

	/**
	 * Lists to aggregate conditions
	 */
	private List<EAdCondition> conditions;

	/**
	 * Fields in the conditions (helper to simplify conditions)
	 */
	private List<EAdField<?>> conditionFields;

	/**
	 * An auxiliary field
	 */
	private List<EAdCondition> conditionsAux;
	private List<EAdCondition> conditionsAuxToAdd;

	/**
	 * Lists to aggregate operations
	 */
	private List<EAdOperation> operations;

	/**
	 * MathEvaluator to help simplify MathOp with no values
	 */
	private MathEvaluator mathEvaluator;

	private boolean asset;

	/**
	 * List with ids of elements already written
	 */
	private List<String> elements;

	/**
	 * List with ids of assets already written
	 */
	private List<String> assets;

	private int simplifications;

	public ObjectWriter(ModelVisitor modelVisitor) {
		super(modelVisitor);
		elements = new ArrayList<String>();
		assets = new ArrayList<String>();
		fields = new HashMap<Object, Map<EAdVarDef<?>, EAdField<?>>>();
		simplifications = 0;
		conditions = new ArrayList<EAdCondition>();
		conditionFields = new ArrayList<EAdField<?>>();
		conditionsAux = new ArrayList<EAdCondition>();
		conditionsAuxToAdd = new ArrayList<EAdCondition>();
		operations = new ArrayList<EAdOperation>();
		mathEvaluator = new MathEvaluator();
	}

	@Override
	public XMLNode write(Identified object) {
		if (object == null) {
			return null;
		}

		// Special cases
		if (!asset && object instanceof EAdField) {
			object = simplifyField((EAdField<?>) object);
		} else if (!asset && object instanceof EAdCondition) {
			object = simplifyCondition((EAdCondition) object);
		} else if (!asset && object instanceof EAdOperation) {
			object = simplifyOperation((EAdOperation) object);
		}

		XMLNode node = null;
		String id = object.getId();
		if (asset) {
			node = modelVisitor.newNode(DOMTags.ASSET_TAG);
		} else {
			node = modelVisitor.newNode(DOMTags.ELEMENT_TAG);
		}

		if ((asset && assets.contains(id)) || (!asset && elements.contains(id))) {
			node.setText(id);
		} else {
			ReflectionClass<?> clazz = ReflectionClassLoader
					.getReflectionClass(object.getClass());

			if (asset) {
				assets.add(id);
			} else {
				ReflectionClass<?> c = ReflectionClassLoader
						.getReflectionClass(object.getClass());
				while (c != null && !c.hasAnnotation(Element.class)) {
					c = c.getSuperclass();
				}

				if (c == null) {
					logger
							.warn(
									"{} (and any of its superclasses) is not annotated with elements. The object is stored as null (an empty node).",
									object.getClass());
					return node;
				} else {
					// When an element is defined with BasicElement, it is a
					// reference to another element, so its real content is
					// stored somewhere else.
					// We proceed as if the element was already in the cache
					if (c.getType() == BasicElement.class) {
						node.setText(id);
						return node;
					}
					elements.add(id);
					clazz = c;
				}
			}
			node.setAttribute(DOMTags.ID_AT, id);
			node
					.setAttribute(DOMTags.CLASS_AT, translateClass(clazz
							.getType()));
			while (clazz != null) {
				for (ReflectionField f : clazz.getFields()) {
					// Only store fields annotated with param
					if (f.getAnnotation(Param.class) != null) {
						Object value = f.getFieldValue(object);
						if (value != null) {
							modelVisitor
									.writeElement(value,
											new ObjectWriterListener(f
													.getName(), node));
						}
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
		return node;
	}

	public void setAsset(boolean asset) {
		this.asset = asset;
	}

	public void clear() {
		asset = false;
		assets.clear();
		elements.clear();
	}

	public static class ObjectWriterListener implements VisitorListener {

		private String fieldName;

		private XMLNode parent;

		public ObjectWriterListener(String fieldName, XMLNode parent) {
			this.fieldName = fieldName;
			this.parent = parent;
		}

		@Override
		public void load(XMLNode node, Object object) {
			node.setAttribute(DOMTags.FIELD_AT, fieldName);
			parent.append(node);
		}

	}

	public int getSimplifications() {
		return simplifications;
	}

	public EAdField<?> simplifyField(EAdField<?> field) {
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

		if (!(condition instanceof EmptyCond)) {
			conditionFields.clear();
			condition.addFields(conditionFields);
			if (conditionFields.size() == 0) {
				logger.debug("No fields in {}", condition);
			}
		}

		int index = conditions.indexOf(condition);
		if (index != -1) {

			EAdCondition copy = conditions.get(index);
			if (copy != condition) {
				simplifications++;
				return copy;
			}
		} else {
			conditions.add(condition);
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
				if (c.equals(condition.getNullOperator())) {
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

		conditionFields.clear();
		condition.addFields(conditionFields);
		if (conditionFields.size() == 0) {
			logger.debug("No fields in {}", condition);
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
			operation = new ValueOp(value);
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

}
