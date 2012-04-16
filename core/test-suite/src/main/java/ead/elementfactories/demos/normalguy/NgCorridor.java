package ead.elementfactories.demos.normalguy;

import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.resources.assets.drawable.basics.Image;
import ead.elementfactories.demos.scenes.EmptyScene;

public class NgCorridor extends EmptyScene{
	
	private SceneElement door1;
	private SceneElement door2;
	private SceneElement door3;
	private SceneElement door4;
	private SceneElement window;  // Displays a video
	
	private OperationCond allRoomsVisitated;  // Enables room4's door
	
	
	public NgCorridor() {
		// Configurar fondo de la escena
		setBounds(1000, 1213);
		setBackground(new SceneElement(new Image("@drawable/ng_corridor_bg.png")));
		// Crear y posicionar al personaje principal en la escena
		
		// Definir el espacio por el que se puede mover el personaje dentro de la escena
		
		// Crear el resto de elementos de la escena
		
		// Añadir los elementos de escena a la escena en el orden correspondiente
		
		// Definir el efecto para mover el personaje
		
		// Configurar interacciones, eventos, ... para los elementos de escena
		
		
	}
	
	public void setWindow() {
		
	}
	
	public void setDoors() {
		
	}
	
	
	
}
