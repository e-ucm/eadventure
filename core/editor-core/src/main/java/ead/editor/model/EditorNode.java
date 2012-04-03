/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.model;


import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * A pattern-node allows editing a set of "engine-model" EditorNodes as a unit.
 * There can be several pattern-nodes for a single editor-node (corresponding
 * to alternate view-points).
 * @author mfreire
 */
public class EditorNode extends DependencyNode<HashSet<DependencyNode<?>>> {	
	
	private static final Logger logger = LoggerFactory.getLogger("EditorNode");
	
	public EditorNode(int id) {
        super(id, new HashSet<DependencyNode<?>>());
    }
	
	public HashSet<DependencyNode<?>> getContents() {
		return content;
	}
	
	public boolean addChild(DependencyNode<?> node) {
		return content.add(node);
	}
	
	/**
	 * Generates an XML snippet with the contents of this EditorNode.
	 * Format is similar to
	 * <node class="ead.editor.model.MyClass" id="editor-id" 
	 *		 contents="comma-separted-list-of-children">
	 *    (element details here)
	 * </node>
	 */
	public void write(StringBuilder sb) {
		sb.append("<node class='")
		  .append(getClass().getName())
		  .append("' id='")
		  .append(getId())
	      .append("' contents='");
		for (DependencyNode<?> n : getContents()) {
			sb.append(n.getId()).append(",");
		}
		sb.setCharAt(sb.length()-1, '\'');
		sb.append(">\n\t");
		writeInner(sb);
		sb.append("\n</node>\n");
	}
	
	public void writeInner(StringBuilder sb) {
		// by default, nothing to write
	}
	
	@SuppressWarnings("unchecked")
	public static EditorNode restore(Element element, EditorModel em) {
		String className = element.getAttribute("class");
		int id = Integer.parseInt(element.getAttribute("id"));
		String contentIdStrings[] = element.getAttribute("contents").split(",");
		
		EditorNode instance = null;
		ClassLoader cl = EditorNode.class.getClassLoader();
		try {
			Class<EditorNode> c = (Class<EditorNode>)cl.loadClass(className);
			instance = c.getConstructor(Integer.TYPE).newInstance(id);
			instance.restoreInner(element);
		} catch (Exception e) {
			logger.error("Could not restore editorNode for class {}", 
					className, e);
		}
		return instance;
	}
	
	public void restoreInner(Element element) {
		// by default, nothing to restore
	}
}
