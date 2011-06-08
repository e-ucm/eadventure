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

package es.eucm.eadventure.gui.extra.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.ArrayList;

public class ColumnsLayout implements LayoutManager2 {

	private ArrayList<ArrayList<ComponentProperties>> columns;
	private int widths[];
	private int minimumWidths[];
	private int heights[];

	private int width = 0;
	private int height = 0;
	private int columnMargin = 10;

	private boolean rowAlignment;

	public ColumnsLayout(int nColumns, int widths[], boolean rowAlignment) {
		if (widths == null) {
			throw new IllegalArgumentException("widths is null");
		} else if (widths.length < nColumns) {
			throw new IllegalArgumentException("Not enough widths specified");
		} else {
			init(nColumns);
			this.minimumWidths = widths;
			this.rowAlignment = rowAlignment;
		}
	}

	public ColumnsLayout(int nColumns, int widths[]) {
		this(nColumns, widths, false);
	}

	public ColumnsLayout(int nColumns, boolean rowAlignment) {
		init(nColumns);
		this.rowAlignment = rowAlignment;
	}

	public ColumnsLayout(int nColumns) {
		this(nColumns, false);
	}

	private void init(int nColumns) {
		columns = new ArrayList<ArrayList<ComponentProperties>>(nColumns);
		for (int i = 0; i < nColumns; i++) {
			columns.add(new ArrayList<ComponentProperties>());
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		ColumnsConstraints c = constraints != null && constraints instanceof ColumnsConstraints ? (ColumnsConstraints) constraints : new ColumnsConstraints();

		if (c.nColumn >= 0 && c.nColumn < columns.size()) {
			columns.get(c.nColumn).add(new ComponentProperties(comp, c.stretchToColumn, c.alignment, c.margin));
			layoutContainer(null);
		}

	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {

	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(width, height);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {

	}

	@Override
	public void layoutContainer(Container parent) {
		calculateWidths();

		if (rowAlignment) {
			calculateHeights();
		}

		int xLayout = 10;
		int maxHeight = 0;
		int nColumn = 0;
		for (ArrayList<ComponentProperties> column : columns) {
			int nRow = 0;
			int yLayout = 15;
			for (ComponentProperties cp : column) {
				int cWidth = 0;
				int cX = xLayout;
				if (cp.stretchToColumn) {
					cWidth = widths[nColumn];
				} else {
					cWidth = cp.c.getPreferredSize().width > widths[nColumn] ? widths[nColumn] : cp.c.getPreferredSize().width;
					switch (cp.alignment) {
					case ColumnsConstraints.LEFT_ALIGNMENT:
						cX = xLayout;
						break;
					case ColumnsConstraints.RIGHT_ALIGNMENT:
						cX = xLayout + widths[nColumn] - cWidth;
					case ColumnsConstraints.CENTER_ALIGNMENT:
						cX = xLayout + widths[nColumn] / 2 - cWidth / 2;
					}
				}
				yLayout += rowAlignment ? ColumnsConstraints.DEFAULT_MARGIN_Y : cp.margin;
				int cY = yLayout;
				if (rowAlignment) {
					cY = yLayout + heights[nRow] / 2 - cp.c.getPreferredSize().height / 2;
				}
				cp.c.setBounds(cX, cY, cWidth, cp.c.getPreferredSize().height);
				if (rowAlignment) {
					yLayout += heights[nRow];
				} else
					yLayout += cp.c.getPreferredSize().height;
				maxHeight = yLayout > maxHeight ? yLayout : maxHeight;
				nRow++;
			}
			xLayout += widths[nColumn] + columnMargin;
			nColumn++;
		}

		this.width = xLayout + 5;
		this.height = maxHeight + 10;

	}

	private void calculateHeights() {
		int nRows = 0;
		for (ArrayList<ComponentProperties> column : columns) {
			nRows = column.size() > nRows ? column.size() : nRows;
		}
		heights = new int[nRows];

		for (ArrayList<ComponentProperties> column : columns) {
			int row = 0;
			for (ComponentProperties cp : column) {
				heights[row] = cp.c.getPreferredSize().height > heights[row] ? cp.c.getPreferredSize().height : heights[row];
				row++;
			}
		}

	}

	private void calculateWidths() {
		int nColumn = 0;
		widths = new int[columns.size()];
		for (ArrayList<ComponentProperties> column : columns) {
			int maxWidth = 0;
			for (ComponentProperties cp : column) {
				maxWidth = cp.c.getPreferredSize().width > maxWidth ? cp.c.getPreferredSize().width : maxWidth;
			}
			if (minimumWidths != null) {
				widths[nColumn] = maxWidth > minimumWidths[nColumn] ? maxWidth : minimumWidths[nColumn];
			} else
				widths[nColumn] = maxWidth;
			nColumn++;
		}

	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(width, height);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(width, height);
	}

	@Override
	public void removeLayoutComponent(Component comp) {

	}

	private class ComponentProperties {

		public Component c;
		public boolean stretchToColumn;
		public int alignment;
		public int margin;

		public ComponentProperties(Component c, boolean stretchToColumn, int alignment, int margin) {
			this.c = c;
			this.stretchToColumn = stretchToColumn;
			this.alignment = alignment;
			this.margin = margin;
		}

	}

}
