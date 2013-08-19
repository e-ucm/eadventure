package es.eucm.ead.writer2.model.writers;

import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.writer2.model.WriterContext;

/**
 * Basic class for writing any type of element in eAdventure
 *
 * @param <T>
 */
public interface Writer<T> {
	/**
	 * Writes the element in a node
	 * @param object
	 * @return
	 */
	XMLNode write(T object, WriterContext context);
}
