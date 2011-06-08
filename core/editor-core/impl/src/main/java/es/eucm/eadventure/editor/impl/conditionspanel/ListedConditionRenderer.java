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

package es.eucm.eadventure.editor.impl.conditionspanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.ListedCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.conditions.impl.ORCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarVarCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.editor.impl.Messages;

/**
 * The renderer for the ListedCondition type
 * @author Roberto Tornero
 *  
 */
public class ListedConditionRenderer extends JPanel {


	private static final long serialVersionUID = -1517208187052240741L;

	private static final Logger logger = LoggerFactory.getLogger("ListedConditionRenderer");
	/**
	 * Map with the relations between the EAdCondition class and the renderers class
	 */
	private HashMap<Class<? extends EAdCondition>, Class<? extends JPanel>> panelMap = new HashMap<Class<? extends EAdCondition>, Class<? extends JPanel>>();

	/**
	 * Constructor for the listed condition type
	 */
	public ListedConditionRenderer(){

		super();
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBackground(Color.white);
		initializeMap();
	}	

	/**
	 * Method for initializing the map
	 */
	private void initializeMap(){

		panelMap.put(VarVarCondition.class, VarVarConditionRenderer.class);
		panelMap.put(VarValCondition.class, VarValConditionRenderer.class);
		panelMap.put(FlagCondition.class, FlagConditionRenderer.class);
		panelMap.put(ANDCondition.class, ListedConditionRenderer.class);
		panelMap.put(ORCondition.class, ListedConditionRenderer.class);
		panelMap.put(ListedCondition.class, ListedConditionRenderer.class);
		panelMap.put(NOTCondition.class, NOTConditionRenderer.class);
	}

	/**
	 * Method for displaying the renderers of the list of conditions  
	 * @param controller
	 * @param cond
	 */
	public void addComponents(ConditionsController controller, ListedCondition cond) {

		removeAll();

		//Left bracket
		if (cond.getConds().size() > 1) {
			JPanel leftBracket = new BracketPanel(true);
			add(leftBracket);
		}

		boolean not;

		for (int i = 0; i < cond.getConds().size(); i++) {     

			EAdCondition condition = cond.getConds().get(i);

			try {
				Class<? extends EAdCondition> conClass = condition.getClass();
				Class<? extends JPanel> rendClass = panelMap.get(conClass);
				if (!conClass.equals(EmptyCondition.class)){
					if (conClass.equals(NOTCondition.class))
						not = true;
					else 
						not = false;

					add(rendClass.getConstructor(new Class[]{ConditionsController.class, conClass, boolean.class}).newInstance(new Object[]{controller, condition, not}));
				}

			} catch (IllegalArgumentException e) {

				logger.error(Messages.illegal_argument_exception);
			} catch (SecurityException e) {

				logger.error(Messages.security_exception);
			} catch (InstantiationException e) {

				logger.error(Messages.instatiation_failed_exception);
			} catch (IllegalAccessException e) {

				logger.error(Messages.illegal_access_exception);
			} catch (InvocationTargetException e) {

				logger.error(Messages.wrong_invocation_exception);
			} catch (NoSuchMethodException e) {

				logger.error(Messages.no_such_method);
			}        	

			if (i < cond.getConds().size() - 1) 
				add(new BoolFunctionRenderer(controller, cond.getOperator()));           

		}

		//Right bracket
		if(cond.getConds().size() > 1) {
			JPanel rightBracket = new BracketPanel(false);
			add(rightBracket);
		}

		revalidate();
		repaint();        
	}

	/**
	 * Class that represents the brackets to be displayed on the listed condition renderer
	 * @author Roberto Tornero
	 *
	 */
	private class BracketPanel extends JPanel {


		private static final long serialVersionUID = -5089559898788716264L;
		/**
		 * Boolean value to determine if the bracket is a left one
		 */
		private boolean isLeft;

		/**
		 * Constructor for left and right brackets
		 * @param isLeft
		 */
		public BracketPanel(boolean isLeft) {

			this.isLeft = isLeft;
			this.setMinimumSize(new Dimension(32, 80));
			this.setPreferredSize(new Dimension(32, 80));
			this.setMaximumSize(new Dimension(32, 80));
		}

		@Override
		public void paint(Graphics g) {

			g.setColor(Color.black);
			int width = getWidth();
			int height = getHeight();
			if(isLeft)
				g.drawArc(0, 0, 2 * width/3, height, 90, 180);
			else
				g.drawArc(0, 0, 2 * width/3, height, 90, -180);

		}
	}

}
