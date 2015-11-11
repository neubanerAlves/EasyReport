import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class JPanelCustom extends JPanel{

	JPanelCustom(GridBagLayout gbl){
		super(gbl);
	}
	
	protected void paintComponent(final Graphics g) {

		super.paintComponent(g);

		g.setColor(new Color(0,204,254));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
	}
	
}
