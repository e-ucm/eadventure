package ead.writer.model.writers;

import ead.common.model.params.EAdParam;
import ead.reader.DOMTags;
import ead.tools.xml.XMLNode;
import ead.writer.model.ModelVisitor;

public class ParamWriter extends AbstractWriter<Object> {

	public ParamWriter(ModelVisitor modelVisitor) {
		super(modelVisitor);
	}

	@Override
	public XMLNode write(Object o) {
		if (o == null) {
			return null;
		}

		XMLNode node = modelVisitor.newNode(DOMTags.PARAM_TAG);
		if (o != null) {
			String translatedClass = translateClass(o.getClass());
			node.setAttribute(DOMTags.CLASS_AT, translatedClass);
			String value = null;
			if (o instanceof EAdParam) {
				value = ((EAdParam) o).toStringData();
			} else if (o instanceof Class) {
				value = ((Class<?>) o).getName();
			} else {
				value = o.toString();
			}
			node.setText(value);
		}
		return node;
	}
}
