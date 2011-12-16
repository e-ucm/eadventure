package es.eucm.eadventure.engine;

import gwt.g2d.client.graphics.KnownColor;
import gwt.g2d.client.graphics.Surface;
import gwt.g2d.client.graphics.shapes.ShapeBuilder;
import gwt.g2d.client.util.FpsTimer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class MainDemo implements EntryPoint {
	  private Surface surface;
	  
	  private double radio = 89;

	public void onModuleLoad() {
	    // Creates a surface of dimension 200 x 200.
	    surface = new Surface(200, 200);
	    RootPanel.get().add(surface);

	    FpsTimer timer = new FpsTimer( ){

			@Override
			public void update() {
				radio -= 0.1f;
			    surface.fillBackground(KnownColor.CORNFLOWER_BLUE)
		        .setFillStyle(KnownColor.GREEN_YELLOW)
		        .fillShape(new ShapeBuilder()
		           .drawCircle(100, 100, radio).build());    
			}
	    	
	    };

	    timer.start();
	    
	  }
	}
