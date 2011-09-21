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
package es.eucm.eadventure.engine.core.trajectories.impl.extra;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition.Node;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition.Side;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.trajectories.impl.NodeTrajectoryGenerator;

public class PathSide {

    private Side side;

    private float length;

    private float realLength;
    
    private Node startNode;

    private Node endNode;
    
    private GameObjectFactory gameObjectFactory;
    
    private List<PathSide> followingSides;

    public PathSide( Side side, NodeTrajectoryDefinition trajectory, boolean inverted,  GameObjectFactory gameObjectFactory ) {
    	this.gameObjectFactory = gameObjectFactory;
        this.side = side;
        this.followingSides = new ArrayList<PathSide>();
        length = side.getLength( );
        if( !inverted ) {
            startNode = trajectory.getNodeForId( side.getIDStart( ) );
            endNode = trajectory.getNodeForId( side.getIDEnd( ) );
        }
        else {
            startNode = trajectory.getNodeForId( side.getIDEnd( ) );
            endNode = trajectory.getNodeForId( side.getIDStart( ) );
        }
        double x = endNode.getX( ) - startNode.getX( );
        double y = endNode.getY( ) - startNode.getY( );
        realLength = (float) Math.sqrt( Math.pow(x,2) + Math.pow(y,2) );
    }

    public float getLenght( ) {

        return length;
    }

    public Node getStartNode( ) {

        return startNode;
    }

    public Node getEndNode( ) {

        return endNode;
    }

    public Side getSide( ) {

        return side;
    }

    @Override
    public boolean equals( Object other ) {

        if( other == null || !( other instanceof PathSide ) ) {
            return false;
        }
        else if( this == other ) {
            return true;
        }
        else {
            PathSide temp = (PathSide) other;
            if( temp.getStartNode( ) == getStartNode( ) && temp.getEndNode( ) == getEndNode( ) )
                return true;
            return false;
        }
    }

    public boolean end;

    public float dist;

    public float posX, posY;

    public boolean getsTo;

    public void updateMinimunDistance( int toX, int toY, SceneElementGO<?> destinationElement, EAdList<EAdSceneElement> eAdList ) {

        end = false;
        dist = Float.MAX_VALUE;
        getsTo = false;
        posX = startNode.getX( );
        posY = startNode.getY( );

        float posX = startNode.getX( );
        float posY = startNode.getY( );

        float deltaX = endNode.getX( ) - startNode.getX( );
        float deltaY = endNode.getY( ) - startNode.getY( );

        int delta = (int) ( Math.abs( deltaX ) > Math.abs( deltaY ) ? Math.abs( deltaX ) : Math.abs( deltaY ) );

        for( int i = 0; i < delta && !end; i++ ) {
            posY = posY + deltaY / delta;
            posX = posX + deltaX / delta;
            if( inBarrier( posX, posY, eAdList ) ) {
                end = true;
            }
            else if( destinationElement != null && inInfluenceArea( posX, posY, destinationElement ) ) {
//                dist = FunctionalTrajectory.getDistanceFast( posX, posY, destinationElement.getX( ), destinationElement.getY( ) );
                dist = 0;
                this.posX = posX;
                this.posY = posY;
                getsTo = true;
                end = true;
            }
            else if( destinationElement == null ) {
                float tempdist = NodeTrajectoryGenerator.getDistanceFast( posX, posY, toX, toY );
                if( tempdist < dist ) {
                    dist = tempdist;
                    this.posX = posX;
                    this.posY = posY;
                }
            }
        }

    }

    /**
     * Returns true if the point is inside a barrier.
     * 
     * @param posX
     *            the position along the x-axis
     * @param posY
     *            the position along the y-axis
     * @return True if the point is inside a barrier
     */
    private boolean inBarrier( float posX, float posY, EAdList<EAdSceneElement> eAdList ) {

        boolean temp = false;
        for( EAdSceneElement barrier : eAdList ) {
        	SceneElementGO<?> seGO = ((SceneElementGO<?>) gameObjectFactory.get(barrier));
        	int x = seGO.getPosition().getJavaX(seGO.getWidth());
        	int y = seGO.getPosition().getJavaY(seGO.getHeight());
        	if (x < posX && y < posY && x + seGO.getWidth() > posX && y + seGO.getHeight() > posY)
        		return true;
        }
        return temp;
    }

    /**
     * Returns true if the given point is inside the influence area of the
     * destination element, false in another case.
     * 
     * @param posX
     *            The position along the x-axis
     * @param posY
     *            The position along the y-axis
     * @return True if the point is inside the destination elements influence
     *         area.
     */
    private boolean inInfluenceArea( float posX, float posY, SceneElementGO<?> destinationElement ) {

        if( destinationElement == null )
            return false;
        else {
            EAdRectangle area;
            if( ((EAdSceneElement) destinationElement.getElement()).getInfluenceArea() != null )
                area = ((EAdSceneElement) destinationElement.getElement()).getInfluenceArea();
            else
                area = new EAdRectangleImpl( -20, -20, destinationElement.getWidth( ) + 40, destinationElement.getHeight( ) + 40 );

            int x1 = (int) ( destinationElement.getPosition().getJavaX(destinationElement.getWidth()) ) + area.getX( );
            int y1 = (int) ( destinationElement.getPosition().getJavaY(destinationElement.getHeight()) ) + area.getY( );
            int x2 = x1 + area.getWidth( );
            int y2 = y1 + area.getHeight( );

            if( posX > x1 && posX < x2 && posY > y1 && posY < y2 )
                return true;
        }
        return false;
    }

    public float getRealLength( ) {
        return realLength;
    }

	public List<PathSide> getFollowingSides() {
		return followingSides;
	}

}
