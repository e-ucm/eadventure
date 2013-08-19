package es.eucm.ead.writer2.model;

import es.eucm.ead.tools.xml.XMLNode;

public interface WriterContext {

	/**
	 * An id for the context
	 * @return
	 */
	int getContextId();

	/**
	 * Generates a new id, valid for this context
	 * @return
	 */
	String generateNewId();

	/**
	 * Returns if the given id has already been processed by this context
	 * @param id
	 * @return
	 */
	boolean containsId(String id);

	/**
	 * Translate the class to a symbolic string
	 * @param type
	 * @return
	 */
	String translateClass(Class<?> type);

	/**
	 * Translates the field to a symbolic string
	 * @param name
	 * @return
	 */
	String translateField(String name);

	/**
	 * Translates the param to a symbolic string
	 * @param param
	 * @return
	 */
	String translateParam(String param);

	/**
	 * Process the object that is going to be written. This method can be used to analyze de model from the context,
	 * since ALL objects in the model pass over here.
	 *
	 * @param object
	 * @param node
	 */
	Object process(Object object, XMLNode node);
}
