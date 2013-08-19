package es.eucm.ead.writer2.model;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.tools.xml.XMLNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ReferenceResolver {

	private static final Logger logger = LoggerFactory
			.getLogger("ReferenceResolver");

	private Map<String, XMLNode> objects;

	private Map<Integer, Map<String, XMLNode>> pendingReferences;

	public ReferenceResolver() {
		objects = new HashMap<String, XMLNode>();
		pendingReferences = new HashMap<Integer, Map<String, XMLNode>>();
	}

	public void check(Identified i, XMLNode node, WriterContext context) {
		// We check if it is a reference
		if (i.getClass() == BasicElement.class) {
			// If the context contains the id, or we already have a pending reference with this id in this context,
			// we don't need to worry, just let the writer to create the reference
			// If not, we need to queue the reference
			Map<String, XMLNode> contextRefs = pendingReferences.get(context
					.getContextId());
			if ((contextRefs == null || !contextRefs.containsKey(i.getId()))
					&& !context.containsId(i.getId())) {
				if (contextRefs == null) {
					contextRefs = new HashMap<String, XMLNode>();
					pendingReferences.put(new Integer(context.getContextId()),
							contextRefs);
				}
				contextRefs.put(i.getId(), node);
			}
		} else {
			if (!objects.containsKey(i.getId())) {
				objects.put(i.getId(), node);
			}
		}

	}

	public void resolveReferences() {
		for (Map.Entry<Integer, Map<String, XMLNode>> e : pendingReferences
				.entrySet()) {
			for (Map.Entry<String, XMLNode> ee : e.getValue().entrySet()) {
				XMLNode o = objects.get(ee.getKey());
				if (o == null) {
					logger.warn("Reference {} unresolved", ee.getKey());
				} else {
					ee.getValue().copy(o);
				}
			}
		}
	}

}
