/*******************************************************************************
 * <e-Adventure> (formerly <e-Game>) is a research project of the <e-UCM>
 *         research group.
 *  
 *   Copyright 2005-2010 <e-UCM> research group.
 * 
 *   You can access a list of all the contributors to <e-Adventure> at:
 *         http://e-adventure.e-ucm.es/contributors
 * 
 *   <e-UCM> is a research group of the Department of Software Engineering
 *         and Artificial Intelligence at the Complutense University of Madrid
 *         (School of Computer Science).
 * 
 *         C Profesor Jose Garcia Santesmases sn,
 *         28040 Madrid (Madrid), Spain.
 * 
 *         For more info please visit:  <http://e-adventure.e-ucm.es> or
 *         <http://www.e-ucm.es>
 * 
 * ****************************************************************************
 * 
 * This file is part of <e-Adventure>, version 1.2.
 * 
 *     <e-Adventure> is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     <e-Adventure> is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with <e-Adventure>.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition.Node;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition.Side;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.PathSide;

public class PathImpl implements Comparable<PathImpl>, Path {

    private float length;

    private List<PathSide> sides;
    
    private List<Node> nodes;

    private float destX;

    private float destY;

    private boolean getsTo;

    private float distance;

    public PathImpl( float length, float distance, List<PathSide> sides ) {

        this.length = length;
        this.sides = new ArrayList<PathSide>( sides );
        this.nodes = new ArrayList<Node>();
        if (this.sides.size() > 0) {
        	nodes.add(((PathSideImpl) sides.get(0)).getStartNode());
	        for (PathSide side : sides) {
	        	nodes.add(((PathSideImpl) side).getEndNode());
	        }
        }
        this.distance = distance;
        getsTo = false;
    }

    public void setLength( float length ) {

        this.length = length;
    }

    public void setGetsTo( boolean b ) {

        getsTo = b;
    }

    public boolean isGetsTo( ) {

        return getsTo;
    }

    public void updateUpTo( float dist, float posX, float posY ) {

        if( dist < distance ) {
            destX = posX;
            destY = posY;
            distance = dist;
        }
    }

    @Override
    public List<PathSide> getSides( ) {

        return sides;
    }

    public List<Side> getNormalSides( ) {

        List<Side> temp = new ArrayList<Side>( );
        for( PathSide side : sides )
            temp.add( ((PathSideImpl) side).getSide( ) );
        return temp;
    }

    public float getLength( ) {

        return length;
    }

    public float getDistance( ) {

        return distance;
    }

    public void addSide( float lenght, float distance, PathSide pathSide ) {

        sides.add( ((PathSideImpl) pathSide) );
        this.length += lenght;
        this.distance = distance;
        this.nodes.add(((PathSideImpl) pathSide).getEndNode());
    }

    public PathImpl newFunctionalPath( float length, float distance, PathSide side ) {
        PathImpl temp = new PathImpl( this.length, this.distance, this.sides );
        temp.addSide( length, distance, side );
        return temp;
    }

    public int compareTo( PathImpl arg0 ) {

        if( this.getsTo && !arg0.getsTo )
            return 1;
        else if( !this.getsTo && arg0.getsTo )
            return -1;

        int distDif = (int) ( arg0.distance - distance );
        if( Math.abs( distDif ) < 200 )
            return (int) ( arg0.length - length );
        else
            return distDif;
    }

    public float getDestX( ) {

        return destX;
    }

    public float getDestY( ) {

        return destY;
    }

    public void print( ) {
        for( PathSide side : sides ) {
            System.out.print( ((PathSideImpl) side).getStartNode( ).getId( ) + "->" );
        }
        System.out.println( ((PathSideImpl) sides.get( sides.size( ) - 1 )).getEndNode( ).getId( ) );
    }

	public List<Node> getNodes() {
		return nodes;
	}
	
}
