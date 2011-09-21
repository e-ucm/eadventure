package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition.Node;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition.Side;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;
import es.eucm.eadventure.engine.core.trajectories.impl.extra.PathImpl;
import es.eucm.eadventure.engine.core.trajectories.impl.extra.PathSide;

@Singleton
public class NodeTrajectoryGenerator implements
		TrajectoryGenerator<NodeTrajectoryDefinition> {

	private Map<NodeTrajectoryDefinition, List<PathSide>> sides;
	
	private Map<NodeTrajectoryDefinition, Side> currentSide;

	private GameObjectFactory gameObjectFactory;
	
	@Inject
	public NodeTrajectoryGenerator(GameObjectFactory gameObjectFactory) {
		sides = new HashMap<NodeTrajectoryDefinition, List<PathSide>>();
		currentSide = new HashMap<NodeTrajectoryDefinition, Side>();
		this.gameObjectFactory = gameObjectFactory;
	}
	
	@Override
	public Path getTrajectory(
			NodeTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, int x, int y) {

		return pathToNearestPoint( trajectoryDefinition, currentPosition, x, y, null);
	}

	@Override
	public Path getTrajectory(
			NodeTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, int x, int y, SceneElementGO<?> sceneElement) {

		return pathToNearestPoint( trajectoryDefinition, currentPosition, x, y, sceneElement);
	}

	@Override
	public boolean canGetTo(NodeTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, SceneElementGO<?> sceneElement) {
		return pathToNearestPoint( trajectoryDefinition, currentPosition, sceneElement.getCenterX(), sceneElement.getCenterY(), sceneElement).isGetsTo();
	}
		
    /**
     * Returns a {@link Path} from the a point to another. If
     * there is a destinationElement the destination point is ignored.
     * 
     * @param fromX
     *            The current position along the x-axis
     * @param fromY
     *            The current position along the y-axis
     * @param toX
     *            The current position along the x-axis
     * @param toY
     *            The current position along the y-axis
     * @return The path to the destination
     */
    private PathImpl pathToNearestPoint( NodeTrajectoryDefinition trajectoryDefinition, EAdPosition currentPosition, int toX, int toY, SceneElementGO<?> sceneElement ) {

        List<PathSide> currentSides = getCurrentValidSides(trajectoryDefinition);

        List<PathImpl> tempPaths = new ArrayList<PathImpl>( );

        for( PathSide currentSide : currentSides ) {
            List<PathSide> tempSides = new ArrayList<PathSide>( );
            tempSides.add( currentSide );
            float distReal = getDistanceFast( currentPosition.getX(), currentPosition.getY(), currentSide.getEndNode( ).getX( ), currentSide.getEndNode( ).getY( ) );
            float dist = currentSide.getLenght( ) / currentSide.getRealLength( ) * distReal;
            PathImpl newPath = new PathImpl( dist, Float.MAX_VALUE, tempSides );
            tempPaths.add( newPath );
        }

        List<PathImpl> fullPathList = getFullPathList( trajectoryDefinition, tempPaths );


        PathImpl bestPath = getValidPaths( trajectoryDefinition, fullPathList, currentPosition, toX, toY, sceneElement );

        if( bestPath != null ) {
        	//this.nearestX = (int) bestPath.getDestX( );
            //this.nearestY = (int) bestPath.getDestY( );
            currentSide.put(trajectoryDefinition, bestPath.getSides().get(0).getSide());
            //this.getsTo = bestPath.isGetsTo( );
            bestPath.getSides( );
        }
        else {
            Node currentNode = trajectoryDefinition.getInitial( );
            //this.nearestX = currentNode.getX( );
            //this.nearestY = currentNode.getY( );
            for (PathSide side : getSides(trajectoryDefinition)) {
                if (side.getStartNode( ) == currentNode) {
                	currentSide.put(trajectoryDefinition, side.getSide());
                }
            }
            //this.getsTo = false;
        	bestPath = new PathImpl(currentNode.getX( ), currentNode.getY( ), null);
        	bestPath.setGetsTo(false);
            //return new ArrayList<FunctionalSide>( );
        }
        return bestPath;

    }

    /**
     * Returns the valid starting sides.
     * <p>
     * @param trajectoryDefinition 
     * 
     * @return
     */
    private List<PathSide> getCurrentValidSides(NodeTrajectoryDefinition trajectoryDefinition ) {
        List<PathSide> tempList = new ArrayList<PathSide>( );
            for( PathSide side : getSides(trajectoryDefinition) ) {
                if( side.getSide( ) == getCurrentSide(trajectoryDefinition) )
                    tempList.add( side );
            }
        return tempList;
    }

    
	private List<PathSide> getSides(NodeTrajectoryDefinition nodeTrajectoryDefinition) {
		if (sides.containsKey(nodeTrajectoryDefinition))
			return sides.get(nodeTrajectoryDefinition);
		List<PathSide> currentSides = new ArrayList<PathSide>();
		for (Side side : nodeTrajectoryDefinition.getSides()) {
            PathSide temp = new PathSide( side, nodeTrajectoryDefinition, false, gameObjectFactory );
            if( !currentSides.contains( temp ) )
            	currentSides.add( temp );
            temp = new PathSide( side, nodeTrajectoryDefinition, true, gameObjectFactory );
            if( !currentSides.contains( temp ) )
            	currentSides.add( temp );
		}
        for (PathSide side : currentSides) {
        	for (PathSide tempSide : currentSides) {
        		if (tempSide.getStartNode() == side.getEndNode()
        				&& tempSide.getEndNode() != side.getStartNode())
        			side.getFollowingSides().add(tempSide);
        	}
            if (side.getStartNode( ) == nodeTrajectoryDefinition.getInitial( )) {
                currentSide.put(nodeTrajectoryDefinition, side.getSide( ));
            }
        }
        sides.put(nodeTrajectoryDefinition, currentSides);
        return currentSides;
	}
	
	private Side getCurrentSide(NodeTrajectoryDefinition nodeTrajectoryDefinition) {
		return currentSide.get(nodeTrajectoryDefinition);
	}
	
    /**
     * Get all the possible paths from the list of full paths.
     * <p>
     * Example: <code>
     *    B - D
     *   /   / \
     * A    /   \
     *   \ /     \
     *    C -----E
     * </code> with the player
     * in A, the full paths will be: <code>
     *                     |-D
     *             |-E---C-|-A
     *             |
     *             |   |-A 
     *   A-|-B---D-|-C-|-D
     *     |           |-E---D
     *     |
     *     |-C-|-D-|-E---C
     *         |   |-B---A
     *         |           
     *         |-E---D-|-C
     *                 |-B---A
     * </code>
     * 
     * @param tempPaths
     *            A list of the full paths
     * @return A list of all the possible paths for the given full paths
     */
    private List<PathImpl> getFullPathList(NodeTrajectoryDefinition nodeTrajectoryDefinition, List<PathImpl> tempPaths ) {

        List<PathImpl> fullPathList = new ArrayList<PathImpl>( );

        while( !tempPaths.isEmpty( ) ) {
            PathImpl originalPath = tempPaths.get( 0 );
            tempPaths.remove( 0 );

            PathSide lastSide = originalPath.getSides( ).get( originalPath.getSides( ).size( ) - 1 );

            boolean continues = false;
            for( PathSide side : lastSide.getFollowingSides() ) {
                if( !originalPath.getSides().contains(side)
                		&& !originalPath.getNodes().contains(side.getEndNode())) {
                    PathImpl temp = originalPath.newFunctionalPath( side.getLenght( ), 0, side );
                    if( temp != null ) {
                        tempPaths.add( temp );
                        continues = true;
                    }
                }
            }
            if( !continues ) 
                fullPathList.add( originalPath );
        }
        return fullPathList;
    }
    
    /**
     * Returns a list of the valid paths (paths that get to the desired
     * destination) from a list of all the possible paths form the starting
     * position. If an element is set as the destination, the valid paths will
     * be those that get to the influence area of said element.
     * <p>
     * The method starts out by precalculating the values for the sides.<br>
     * Then it searches though every "full path" to determine it's distance to
     * the destination, if it is inside a active area and the length of the
     * path, always keeping the best path found up to that moment.<br>
     * When the side is in the middle (because the player doesn't start out in a
     * node) then it "moves along" it calculating the different values. When a
     * side must be search form beging to end, it uses the pre-calculated
     * values.
     * 
     * @param fullPathList
     *            A list of the paths from the current position
     * @param fromX
     *            The current position along the x-axis
     * @param fromY
     *            The current position along the y-axis
     * @param toX
     *            The destination position along the x-axis
     * @param toY
     *            The destination position along the y-axis
     * @return A list with all the paths that get to the destination.
     */
    private PathImpl getValidPaths( NodeTrajectoryDefinition nodeTrajectoryDefinition,
    		List<PathImpl> fullPathList, EAdPosition currentPosition, int toX, int toY,
    		SceneElementGO<?> sceneElement) {

        PathImpl best = null;

        for( PathSide side : getSides(nodeTrajectoryDefinition) ) {
            side.updateMinimunDistance( toX, toY, sceneElement, nodeTrajectoryDefinition.getBarriers() );
        }

        for( PathImpl tempPath : fullPathList ) {
            PathImpl newPath = new PathImpl( 0, Float.MAX_VALUE, new ArrayList<PathSide>( ) );
            float length = getDistance( currentPosition.getX(), currentPosition.getY(), tempPath.getSides( ).get( 0 ).getEndNode( ).getX( ), tempPath.getSides( ).get( 0 ).getEndNode( ).getY( ) ) / tempPath.getSides( ).get( 0 ).getRealLength() * tempPath.getSides( ).get( 0 ).getLenght( );
            newPath.addSide( length, Float.MAX_VALUE, tempPath.getSides( ).get( 0 ) );

            float posX = currentPosition.getX();
            float posY = currentPosition.getY();

            boolean end = false;
            int sideNr = 1;
            while( !end && sideNr <= tempPath.getSides( ).size( ) ) {
                if( sideNr == 1 ) {
                    Node endNode = newPath.getSides( ).get( sideNr - 1 ).getEndNode( );
                    float deltaX = endNode.getX( ) - posX;
                    float deltaY = endNode.getY( ) - posY;

                    int delta = (int) ( Math.abs( deltaX ) > Math.abs( deltaY ) ? Math.abs( deltaX ) : Math.abs( deltaY ) );

                    for( int i = 0; i < delta && !end; i++ ) {
                        posY = posY + deltaY / delta;
                        posX = posX + deltaX / delta;
                        if( inBarrier( nodeTrajectoryDefinition, posX, posY ) )
                            end = true;
                        else if( sceneElement != null && inInfluenceArea( sceneElement, posX, posY ) ) {
                            float dist = 0;
                            newPath.updateUpTo( dist, posX, posY );
                            newPath.setGetsTo( true );
                            end = true;
                        }
                        else if( sceneElement == null ) {
                            float dist = getDistanceFast( posX, posY, toX, toY );
                            newPath.updateUpTo( dist, posX, posY );
                        }
                    }
                }
                else {
                    PathSide side = newPath.getSides( ).get( sideNr - 1 );
                    end = side.end;
                    newPath.updateUpTo( side.dist, side.posX, side.posY );
                    newPath.setGetsTo( side.getsTo );
                    posX = side.posX;
                    posY = side.posY;
                }

                if( best == null ) {
                    best = newPath;
                }
                else if( best.compareTo( newPath ) < 0 ) {
                    best = newPath;
                }

                if( sideNr < tempPath.getSides( ).size( ) ) {
                    newPath = newPath.newFunctionalPath( tempPath.getSides( ).get( sideNr ).getLenght( ), Float.MAX_VALUE, tempPath.getSides( ).get( sideNr ) );
                    posX = tempPath.getSides( ).get( sideNr ).getStartNode( ).getX( );
                    posY = tempPath.getSides( ).get( sideNr ).getStartNode( ).getY( );
                }

                sideNr++;
            }
        }

        return best;
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
    private boolean inBarrier( NodeTrajectoryDefinition nodeTrajectoryDefinition, float posX, float posY ) {

        boolean temp = false;
        for( EAdSceneElement barrier : nodeTrajectoryDefinition.getBarriers() ) {
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
    private boolean inInfluenceArea( SceneElementGO<?> sceneElement, float posX, float posY ) {
        if( sceneElement == null )
            return false;
        else {
            EAdRectangle area;
            if( ((EAdSceneElement) sceneElement.getElement()).getInfluenceArea() != null )
                area = ((EAdSceneElement) sceneElement.getElement()).getInfluenceArea();
            else
                area = new EAdRectangleImpl( -20, -20, sceneElement.getWidth( ) + 40, sceneElement.getHeight( ) + 40 );

            int x1 = (int) ( sceneElement.getPosition().getJavaX(sceneElement.getWidth()) ) + area.getX( );
            int y1 = (int) ( sceneElement.getPosition().getJavaY(sceneElement.getHeight()) ) + area.getY( );
            int x2 = x1 + area.getWidth( );
            int y2 = y1 + area.getHeight( );

            if( posX > x1 && posX < x2 && posY > y1 && posY < y2 )
                return true;
        }
        return false;

    }
    
    /**
     * Get the distance from one point to another.
     * 
     * @param x1
     *            The position along the x-axis of the first point
     * @param y1
     *            The position along the y-axis of the first point
     * @param x2
     *            The position along the x-axis of the second point
     * @param y2
     *            The position along the y-axis of the second point
     * @return The distance between to given points
     */
    public static float getDistance( float x1, float y1, float x2, float y2 ) {

         return (float) Math.sqrt( Math.pow( x1 - x2, 2 ) + Math.pow( y1 - y2, 2 ) );
    }

    /**
     * Get a fast estimate of the distance from one point to another.
     * 
     * @param x1
     *            The position along the x-axis of the first point
     * @param y1
     *            The position along the y-axis of the first point
     * @param x2
     *            The position along the x-axis of the second point
     * @param y2
     *            The position along the y-axis of the second point
     * @return An estimate of the distance between to given points
     */
    public static float getDistanceFast( float x1, float y1, float x2, float y2 ) {

        return ( x1 - x2 ) * ( x1 - x2 ) + ( y1 - y2 ) * ( y1 - y2 );
    }


}
