/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.model;

import org.apache.lucene.document.Document;

/**
 * An engine-model node. Used as a base for the dependency-tracking mechanism
 * for the editor model.
 * @author mfreire
 */
public class EngineNode<T> extends DependencyNode<T> {
        
	public EngineNode(int id, T content) {
        super(id, content);
    }
}
