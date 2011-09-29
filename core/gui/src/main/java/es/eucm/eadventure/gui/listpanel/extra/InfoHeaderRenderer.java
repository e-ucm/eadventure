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

package es.eucm.eadventure.gui.listpanel.extra;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.gui.EAdGUILookAndFeel;
import es.eucm.eadventure.gui.R;
import es.eucm.eadventure.gui.listpanel.ListPanel;

/**
 * Edit the header of each column to include a name and a help icon.
 *
 */
public class InfoHeaderRenderer extends JPanel implements TableCellRenderer {

    private static final long serialVersionUID = 3777807533524838812L;
	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory
			.getLogger(ListPanel.class);

    private JButton infoButton;

    private MouseListener old = null;

    private String helpPath;

    /**
     * Constructor by default, no include the help.
     */
    public InfoHeaderRenderer( ) {

        this( null );
    }

    /**
     * Constructor with a help icon, if it's pressed the help is shown
     * @param helpPath
     * 		Patch with the help file.
     */
    public InfoHeaderRenderer( String helpPath ) {

        this.helpPath = helpPath;
        setLayout( new GridBagLayout( ) );
    }

    public Component getTableCellRendererComponent( final JTable table, final Object value, boolean isSelected, boolean hasFocus, int row, final int column ) {
        removeAll( );
        this.setBackground(EAdGUILookAndFeel.getBackgroundColor());
        setBorder( BorderFactory.createCompoundBorder(
        		BorderFactory.createMatteBorder( 0, 0, 0, 1, Color.DARK_GRAY ),
        		BorderFactory.createMatteBorder( 0, 0, 2, 0, Color.BLACK )));
        GridBagConstraints c = new GridBagConstraints( );
        c.gridx = 0;
        c.gridy = 0;
        JLabel label = new JLabel( value.toString( ) );
        label.setFont( EAdGUILookAndFeel.getBoldFont().deriveFont(16.0f) );
        add( label, c );
        if( helpPath != null ) {
        	InputStream is = ClassLoader.getSystemResourceAsStream( R.Drawable.information_png );   
    	    try {
				infoButton = new JButton( new ImageIcon( ImageIO.read(is) ) );
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            infoButton.setContentAreaFilled( false );
            infoButton.setBorder( BorderFactory.createEmptyBorder( 0, 6, 0, 0 ) );
            c.gridx = 1;
            add( infoButton, c );
            JTableHeader header = table.getTableHeader( );
            if( old != null )
                header.removeMouseListener( old );
            old = new MouseAdapter( ) {

                @Override
                public void mouseClicked( MouseEvent e ) {

                    int mouseX = e.getX( );
                    int mouseY = e.getY( );
                    mouseX -= infoButton.getX( );
                    mouseY -= infoButton.getY( );
                    for( int i = 0; i < column; i++ ) {
                        mouseX -= table.getColumnModel( ).getColumn( i ).getWidth( );
                    }
                    if( infoButton.contains( mouseX, mouseY ) ) {
                    	//TODO open a dialog with the help
                    	//new HelpDialog( helpPath ); 
                    	logger.info("Help button pressed, " + helpPath);
                    }
                }
            };
            header.addMouseListener( old );
        }
        return this;
    }

}
