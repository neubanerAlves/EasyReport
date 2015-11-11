import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.channels.NetworkChannel;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tutorial extends JFrame {

	private BufferedImage bImage;
	private ImageIcon okIcon = new ImageIcon(getClass().getResource("/Imagem/okEntendi.jpg"));
		
	
	Tutorial(){
		try {
			
			this.bImage = ImageIO.read(new File("res/tutorial.png"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		JPanelCustom panelCustom = new JPanelCustom(gridBagLayout);
		
		NewContentPane contentPane = new NewContentPane();
		
		JButton button = new JButton(okIcon);
		button.setPreferredSize(new Dimension(98,32));
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		
		panelCustom.add(button);
		
		this.add(contentPane);
		
		this.add(BorderLayout.SOUTH, panelCustom);
		
		this.setSize(new Dimension(bImage.getWidth(),
				bImage.getHeight()));
		
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setUndecorated(true);
		this.setVisible(true);
	}
	
	private class NewContentPane extends JPanel{

		protected void paintComponent(final Graphics g) {

			super.paintComponent(g);

			

			g.drawImage(bImage, 0, 0,
					bImage.getWidth(),
					bImage.getHeight(), this);

		}
	}
}
