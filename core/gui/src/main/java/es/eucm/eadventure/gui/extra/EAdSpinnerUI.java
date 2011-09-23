package es.eucm.eadventure.gui.extra;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

import es.eucm.eadventure.gui.EAdButton;
import es.eucm.eadventure.gui.EAdGUILookAndFeel;

public class EAdSpinnerUI extends BasicSpinnerUI {

    public static ComponentUI createUI(JComponent c) {
    	
        c.setBackground(EAdGUILookAndFeel.getBackgroundColor());
        c.setBorder(null);
        
        return new EAdSpinnerUI();
    }
    
    public EAdSpinnerUI() {
        super();
    }
    
    @Override
    public void installUI(JComponent c) {
    	super.installUI(c);
    	EAdBorder border = new EAdBorder();
    	spinner.getEditor().setBorder(border);
		EAdBorderListener eAdButtonListener = new EAdBorderListener( border, spinner );
		spinner.addFocusListener(eAdButtonListener);
		spinner.addMouseListener(eAdButtonListener);
		spinner.addPropertyChangeListener("enabled", eAdButtonListener);
    }
    
    protected void installDefaults() {
        spinner.setLayout(createLayout());
        LookAndFeel.installColorsAndFont(spinner, "Spinner.background",
                "Spinner.foreground", "Spinner.font");
        LookAndFeel.installProperty(spinner, "opaque", Boolean.TRUE);
    }
    

    @Override
    protected JButton createNextButton() {
    	EAdButton button = new EAdButton(EAdButton.NORTH);
    	installNextButtonListeners(button);
    	return button;
    }

    @Override
    protected JButton createPreviousButton() {
    	EAdButton button = new EAdButton(EAdButton.SOUTH);
    	installPreviousButtonListeners(button);
    	return button;
    } 
}
