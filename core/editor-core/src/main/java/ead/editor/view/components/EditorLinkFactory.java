/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.view.components;

import ead.editor.control.Controller;
import ead.editor.model.nodes.DependencyNode;
import javax.swing.ImageIcon;

/**
 * Creates EditorLinks for EditorNodes.
 *
 * @author mfreire
 */
public class EditorLinkFactory {
	public EditorLink createLink(DependencyNode node, Controller controller) {
		ImageIcon icon = new ImageIcon(node.getLinkIcon());
		return new EditorLink(node.getLinkText(), ""+node.getId(), icon, controller);
	}
}
