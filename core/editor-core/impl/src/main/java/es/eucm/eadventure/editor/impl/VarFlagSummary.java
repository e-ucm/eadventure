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

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.model.variables.impl.BooleanVar;
import es.eucm.eadventure.common.model.variables.impl.NumberVar;

/**
 * This class holds the name of all the active flags in the script. * 
 * @author Bruno Torijano Bueno, Roberto Tornero
 * 
 */
public class VarFlagSummary {

	/**
	 * List of flags.
	 */
	private List<BooleanVar> flags;

	/**
	 * List of flag references.
	 */
	private List<Integer> flagReferences;

	/**
	 * List of vars.
	 */
	private List<NumberVar<?>> vars;

	/**
	 * List of var references.
	 */
	private List<Integer> varReferences;

	/**
	 * Constructor.
	 */
	public VarFlagSummary( ) {

		// Create the lists
		flags = new ArrayList<BooleanVar>( );
		flagReferences = new ArrayList<Integer>( );
		vars = new ArrayList<NumberVar<?>>( );
		varReferences = new ArrayList<Integer>( );
	}

	/**
	 * Clears the summary, deleting all references.
	 */
	public void clearReferences( ) {

		// Clear both lists
		//flags.clear( );
		flagReferences.clear( );
		for ( int i = 0; i < flags.size( ); i++ ){
			flagReferences.add( 0 );
		}
		//vars.clear( );
		varReferences.clear( );
		for ( int i = 0; i < vars.size( ); i++ ){
			varReferences.add( 0 );
		}
	}

	/**
	 * Deletes all var and flags that have 0 references
	 */
	public void clean( ){
		for ( int i = 0 ; i < flagReferences.size( ); i++ ){
			if ( flagReferences.get( i ) == 0 ){
				flags.remove( i );
			}
		}

		while ( flagReferences.contains( 0 )){
			flagReferences.remove( new Integer(0) );
		}

		for ( int i = 0 ; i < varReferences.size( ); i++ ){
			if ( varReferences.get( i ) == 0 ){
				vars.remove( i );
			}
		}

		while ( varReferences.contains( 0 )){
			varReferences.remove( new Integer(0) );
		}
	}

	/**
	 * Adds a new flag to the list (with zero references).
	 * 
	 * @param flag
	 *            New flag
	 * @return True if the flag was added, false otherwise
	 */
	public boolean addFlag( BooleanVar flag ) {

		boolean addedFlag = false;

		// Add it only if it doesn't exist
		if( !existsFlag( flag ) ) {
			flags.add( flag );
			flagReferences.add( 0 );
			addedFlag = true;

			// Sort the list
			sortList( flags, flagReferences );
		}

		return addedFlag;
	}

	/**
	 * Deletes the given flag from the list.
	 * 
	 * @param flag
	 *            Flag to be deleted
	 * @return True if the flag was deleted, false otherwise
	 */
	public boolean deleteFlag( String flag ) {

		boolean deletedFlag = false;

		// Get the index of the flag
		int flagIndex = flags.indexOf( flag );

		// If the flag exists, delete the info
		if( flagIndex >= 0 ) {
			flags.remove( flagIndex );
			flagReferences.remove( flagIndex );
			deletedFlag = true;
		}

		return deletedFlag;
	}

	/**
	 * Adds a new var to the list (with zero references).
	 * 
	 * @param flag
	 *            New var
	 * @return True if the var was added, false otherwise
	 */
	public boolean addVar( NumberVar<?> var ) {

		boolean addedVar = false;

		// Add it only if it doesn't exist
		if( !existsVar( var ) ) {
			vars.add( var );
			varReferences.add( 0 );
			addedVar = true;

			// Sort the list
			sortListVar( vars, varReferences );
		}

		return addedVar;
	}

	/**
	 * Deletes the given var from the list.
	 * 
	 * @param var
	 *            Var to be deleted
	 * @return True if the var was deleted, false otherwise
	 */
	public boolean deleteVar( NumberVar<?> var ) {

		boolean deletedVar = false;

		// Get the index of the flag
		int varIndex = vars.indexOf( var );

		// If the var exists, delete the info
		if( varIndex >= 0 ) {
			vars.remove( varIndex );
			varReferences.remove( varIndex );
			deletedVar = true;
		}

		return deletedVar;
	}

	/**
	 * Adds a new reference (if the id provided is a flag addFlagReference is
	 * invoked, if the id provided is a var addVarReference is called).
	 * 
	 * @param id
	 *            New ref id
	 */
	public void addReference( String id ) {

		if( flags.contains( getFlagForName(id) ) ) {
			addFlagReference( getFlagForName(id) );
		}
		else if( vars.contains( getVarForName(id) ) ) {
			addVarReference( getVarForName(id) );
		}
	}

	/**
	 * Adds a new flag reference (creates the flag with one reference, or
	 * updates the references).
	 * 
	 * @param flag
	 *            New flag
	 */
	public void addFlagReference( BooleanVar flag ) {

		// Get the index of the flag
		int flagIndex = flags.indexOf( flag );

		// If the flag was on the list, update the references
		if( flagIndex >= 0 ) {
			int references = flagReferences.get( flagIndex ) + 1;
			flagReferences.set( flagIndex, references );
		}

		// If the flag wasn't on the list, add it
		else {
			flags.add( flag );
			flagReferences.add( 1 );

			// Sort the list
			sortList( flags, flagReferences );
		}
	}

	/**
	 * Deletes the given flag from the list
	 * 
	 * @param flag
	 *            Flag to be deleted
	 */
	public void deleteFlagReference( String flag ) {

		// Get the index of the flag
		int flagIndex = flags.indexOf( flag );

		// If the flag is on the list
		if( flagIndex >= 0 ) {
			// Get the number of references, decrease it and update
			int references = flagReferences.get( flagIndex ) - 1;
			flagReferences.set( flagIndex, references );
		}

		// If it is not, show an error message
		else
			System.err.println( "Error: Trying to delete a nonexistent flag" );
	}

	/**
	 * Deletes the given if (either flag or var) from the list
	 * 
	 * @param id
	 *            Id to be deleted
	 */
	public void deleteReference( String id ) {

		if( flags.contains( id ) ) {
			deleteFlagReference( id );
		}
		else if( vars.contains( getVarForName(id) ) ) {
			deleteVarReference( getVarForName(id) );
		}
	}

	/**
	 * Adds a new var reference (creates the var with one reference, or updates
	 * the references).
	 * 
	 * @param var
	 *            New var
	 */
	public void addVarReference( NumberVar<?> var ) {

		// Get the index of the var
		int varIndex = vars.indexOf( var );

		// If the var was on the list, update the references
		if( varIndex >= 0 ) {
			int references = varReferences.get( varIndex ) + 1;
			varReferences.set( varIndex, references );
		}

		// If the var wasn't on the list, add it
		else {
			vars.add( var );
			varReferences.add( 1 );

			// Sort the list
			sortListVar( vars, varReferences );
		}
	}

	/**
	 * Deletes the given var from the list
	 * 
	 * @param var
	 *            Var to be deleted
	 */
	public void deleteVarReference( NumberVar<?> var ) {

		// Get the index of the var
		int varIndex = vars.indexOf( var );

		// If the var is on the list
		if( varIndex >= 0 ) {
			// Get the number of references, decrease it and update
			int references = varReferences.get( varIndex ) - 1;
			varReferences.set( varIndex, references );
		}

		// If it is not, show an error message
		else
			System.err.println( "Error: Trying to delete a nonexistent var" );
	}

	/**
	 * Returns if the flag summary contains the given flag.
	 * 
	 * @param flag
	 *            Flag to be checked
	 * @return True if the list contains the flag, false otherwise
	 */
	public boolean existsFlag( BooleanVar flag ) {

		return flags.contains( flag );
	}

	/**
	 * Returns if the var summary contains the given var.
	 * 
	 * @param var
	 *            Var to be checked
	 * @return True if the list contains the var, false otherwise
	 */
	public boolean existsVar( NumberVar<?> var ) {

		return vars.contains( var );
	}

	/**
	 * Returns if the summary contains the given id (both flags & vars are
	 * checked).
	 * 
	 * @param id
	 *            Id to be checked
	 * @return True if some of the lists contain the id, false otherwise
	 */
	public boolean existsId( String id ) {

		return existsFlag( id ) || existsVar( getVarForName(id) );
	}

	/**
	 * Returns the number of flags present in the summary.
	 * 
	 * @return Number of flags
	 */
	public int getFlagCount( ) {

		return flags.size( );
	}

	/**
	 * Returns the number of varss present in the summary.
	 * 
	 * @return Number of vars
	 */
	public int getVarCount( ) {

		return vars.size( );
	}

	/**
	 * Returns the flag name in the given position.
	 * 
	 * @param index
	 *            Index for the flag
	 * @return Flag name
	 */
	public BooleanVar getFlag( int index ) {

		return flags.get( index );
	}

	/**
	 * Returns the var name in the given position.
	 * 
	 * @param index
	 *            Index for the var
	 * @return Var name
	 */
	public NumberVar<?> getVar( int index ) {

		return vars.get( index );
	}

	/**
	 * Returns the var name in the given position.
	 * 
	 * @param index
	 *            Index for the var
	 * @return Var name
	 */
	public NumberVar<?> getVarForName( String name ) {

		ArrayList<String> namesList = getVarNames();
		int index = namesList.indexOf(name);
		return vars.get(index);
	}

	/**
	 * Returns the var name in the given position.
	 * 
	 * @param index
	 *            Index for the var
	 * @return BoolanVar name
	 */
	public BooleanVar getFlagForName( String name ) {

		for (BooleanVar flag : flags) {
			if (flag.getName().equals(name))
				return flag;
		}
		return null;
	}

	/**
	 * Returns the flag references number in the given position.
	 * 
	 * @param index
	 *            Index for the flag
	 * @return Number of references of the flag
	 */
	public int getFlagReferences( int index ) {

		return flagReferences.get( index );
	}

	/**
	 * Returns the var references number in the given position.
	 * 
	 * @param index
	 *            Index for the var
	 * @return Number of references of the var
	 */
	public int getVarReferences( int index ) {

		return varReferences.get( index );
	}

	/**
	 * Returns an array with all the flags of the list
	 * 
	 * @return Array with all the flags
	 */
	public String[] getFlags( ) {

		return flags.toArray( new String[] {} );
	}

	/**
	 * Returns an array with all the vars of the list
	 * 
	 * @return Array with all the vars
	 */
	public NumberVar<?>[] getVars( ) {

		return vars.toArray( new NumberVar<?>[] {} );
	}

	/**
	 * Returns an arraylist with all the var names of the list
	 * 
	 * @return ArrayList with all the var names
	 */
	public ArrayList<String> getVarNames( ) {

		ArrayList<String> namesList = new ArrayList<String>();

		for (NumberVar<?> var : vars)
			namesList.add(var.getName());    	

		return namesList;
	}

	/**
	 * Returns an array with the names of the variables
	 * @return
	 */
	public String[] getVarNamesArray( ) {

		ArrayList<String> namesList = getVarNames();   	

		return namesList.toArray(new String[]{});
	}

	//It is only used to show all the flags and vars in assessment profiles
	public String[] getVarsAndFlags(){
		ArrayList<String> mix = new ArrayList<String>();
		mix.addAll( getVarNames() );
		for (BooleanVar flag : flags)
			mix.add(flag.getName());
		mix.add( "report" );
		return mix.toArray( new String[]{});
	}

	/**
	 * Sorts the lists of flags and resources, by the name of the flags.
	 */
	private void sortList( List<BooleanVar> list, List<Integer> refsList ) {

		// Bubble sorting
		try {
			for( int i = 0; i < list.size( ) - 1; i++ ) {
				for( int j = 0; j < ( list.size( ) - 1 ) - i; j++ ) {
					// If the current flag is greater than the next one, swap values (flag and references)
					if( list.get( j ).compareTo( list.get( j + 1 ) ) > 0 ) {
						list.add( j + 1, list.remove( j ) );
						refsList.add( j + 1, refsList.remove( j ) );
					}
				}
			}
		}
		catch( NullPointerException e ) {
			//ReportDialog.GenerateErrorReport( e, true, "UNKNOWERROR" );
		}
	}

	/**
	 * Sorts the lists of flags and resources, by the name of the flags.
	 */
	private void sortListVar( List<NumberVar<?>> list, List<Integer> refsList ) {

		// Bubble sorting
		try {
			for( int i = 0; i < list.size( ) - 1; i++ ) {
				for( int j = 0; j < ( list.size( ) - 1 ) - i; j++ ) {
					// If the current flag is greater than the next one, swap values (flag and references)
					if( list.get( j ).getName().compareTo( list.get( j + 1 ).getName() ) > 0 ) {
						list.add( j + 1, list.remove( j ) );
						refsList.add( j + 1, refsList.remove( j ) );
					}
				}
			}
		}
		catch( NullPointerException e ) {
			//ReportDialog.GenerateErrorReport( e, true, "UNKNOWERROR" );
		}
	}

}
