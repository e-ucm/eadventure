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

package es.eucm.ead.editor.model;

import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.DependencyEdge;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 Exports editor models as graphs
 * @author mfreire
 */
public class GraphExporter {

	private static final Logger logger = LoggerFactory
			.getLogger(GraphExporter.class);

	public static void export(DirectedGraph<DependencyNode, DependencyEdge> g,
			File target) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(target),
					"UTF-8");
			IdProvider idp = new IdProvider();
			LabelProvider ldp = new LabelProvider();

			//            GmlExporter<EditorNode, DependencyEdge> exporter
			//                    = new GmlExporter<EditorNode, DependencyEdge>(idp, idp, idp, idp);
			//            DOTExporter<EditorNode, DependencyEdge> exporter
			//                    = new DOTExporter<EditorNode, DependencyEdge>(idp, idp, idp);
			GraphMLExporter<DependencyNode, DependencyEdge> exporter = new GraphMLExporter<DependencyNode, DependencyEdge>(
					idp, ldp, idp, ldp);
			exporter.export(writer, g);
		} catch (Exception e) {
			logger.warn("Error during export to {}", target, e);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (Exception e) {
				logger.error("Could not close writer", e);
			}
		}
	}

	private static class IdProvider implements
			VertexNameProvider<DependencyNode>,
			EdgeNameProvider<DependencyEdge> {
		@Override
		public String getVertexName(DependencyNode v) {
			return "" + v.getId();
		}

		@Override
		public String getEdgeName(DependencyEdge e) {
			return "";
		}
	}

	private static class LabelProvider implements
			VertexNameProvider<DependencyNode>,
			EdgeNameProvider<DependencyEdge> {
		@Override
		public String getVertexName(DependencyNode v) {
			return v.getContent().getClass().getSimpleName();
		}

		@Override
		public String getEdgeName(DependencyEdge e) {
			return e.getType();
		}
	}
}
