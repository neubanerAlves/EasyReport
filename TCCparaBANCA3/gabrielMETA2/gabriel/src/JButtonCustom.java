import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class JButtonCustom extends JButton{

	JButtonCustom(ImageIcon i){
		super(i);
	}
	
	protected void paintComponent(final Graphics g) {

		super.paintComponent(g);

		g.setColor(new Color(139,160,137));
		g.fillRect(10, 10, this.getWidth(), this.getHeight());

	}
	
}
