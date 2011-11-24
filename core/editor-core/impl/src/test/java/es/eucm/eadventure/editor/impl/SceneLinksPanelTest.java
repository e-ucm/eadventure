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

package es.eucm.eadventure.editor.impl;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.mockito.MockitoAnnotations;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class SceneLinksPanelTest {
	
	/**
	 * Constructor that initiates both the mock objects and the regular objects of the class
	 */
    public SceneLinksPanelTest(){
 
    	MockitoAnnotations.initMocks(this);

		EAdList<EAdScene> list = new EAdListImpl<EAdScene>(EAdScene.class);

		EAdScene space = new EAdSceneImpl();
		space.getBackground().getResources().addAsset(space.getInitialBundle(), EAdBasicSceneElement.appearance, new ImageImpl("@drawable/loading.png"));

		//Change the number of elements in the list to see how the scale works
		list.add(space);
		list.add(space);
		list.add(space);
		list.add(space);
		list.add(space);
		list.add(space);
		list.add(space);
		list.add(space);

		SceneLinksPanel sctp = new SceneLinksPanel(list);

		JFrame f = new JFrame();		
		f.getContentPane().add(sctp, BorderLayout.CENTER);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		f.setVisible(true);
    	
    }
    
    public static void main(String args[]){    	
    	new SceneLinksPanelTest();
    }
	
}
