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

package ead.editor.view.generic.structure.extra;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.List;

/**
 * Layout for the whole structure panel
 */
public class StructurePanelLayout implements LayoutManager2 {

    /**
     * List of the structure components
     */
    List<StructureComponent> components = new ArrayList<StructureComponent>( );

    @Override
    public void addLayoutComponent( Component arg0, Object arg1 ) {

        components.add( new StructureComponent( arg0, (Integer) arg1 ) );
    }

    @Override
    public float getLayoutAlignmentX( Container arg0 ) {

        return 0;
    }

    @Override
    public float getLayoutAlignmentY( Container arg0 ) {

        return 0;
    }

    @Override
    public void invalidateLayout( Container arg0 ) {

    }

    @Override
    public Dimension maximumLayoutSize( Container arg0 ) {

        return null;
    }

    @Override
    public void addLayoutComponent( String arg0, Component arg1 ) {

    }

    @Override
    public void layoutContainer( Container parent ) {

        int width = parent.getWidth( );
        int y = 0;
        for( StructureComponent c : components ) {
            int height2 = c.getSize( );
            if( c.getSize( ) == -1 ) {
                height2 = parent.getHeight( );
                for( StructureComponent d : components ) {
                    if( d != c )
                        height2 -= d.getSize( );
                }
            }
            c.getComponent( ).setBounds( 0, y, width, height2 );
            y += height2;
        }
    }

    @Override
    public Dimension minimumLayoutSize( Container arg0 ) {

        return new Dimension( 0, 0 );
    }

    @Override
    public Dimension preferredLayoutSize( Container arg0 ) {

        return new Dimension( arg0.getSize( ).width, arg0.getSize( ).height );
    }

    @Override
    public void removeLayoutComponent( Component arg0 ) {

        int k = -1;
        for( int i = 0; i < components.size( ); i++ )
            if( components.get( i ).getComponent( ) == arg0 )
                k = i;
        if( k != -1 )
            components.remove( k );
    }

    private class StructureComponent {

        private Component component;

        private Integer size;

        public StructureComponent( Component component, Integer size ) {

            this.component = component;
            this.size = size;
        }

        public Component getComponent( ) {

            return component;
        }

        public int getSize( ) {

            return size.intValue( );
        }
    }

}
