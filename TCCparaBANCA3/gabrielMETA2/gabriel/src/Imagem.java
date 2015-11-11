import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import com.lowagie.text.pdf.codec.Base64;

public class Imagem extends JComponent implements Serializable{

	private JFileChooser jFileChooser = new JFileChooser();
	
	static private transient int count = 0;
	private int index;
	
	private transient BufferedImage currentImage;

	private String path;

	private byte[] byteForImage;
	
	private int widthImage;
	private int heightImage;
	private int xImage;
	private int yImage;

	private int xMouseBeg;
	private int yMouseBeg;

	private int xImageAux;
	private int yImageAux;

	private int heightImageAux;
	private int widthImageAux;

	private boolean resizing[] = new boolean[8];
	private boolean notMove_resizing = false;
	private boolean move = false;

	private double scale; // Escala da imagem

	private boolean currentImageRect = false;

	public Imagem(){
		
	}
			
	void insereImagem(int x, int y) {

		index = count;
		count++;
		
		// seta para selecionar apenas arquivos
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// desabilita todos os tipos de arquivos
		jFileChooser.setAcceptAllFileFilterUsed(false);

		// mostra janela para salvar
		int acao = jFileChooser.showOpenDialog(null);

		// executa acao conforme opção selecionada
		if (acao == JFileChooser.APPROVE_OPTION) {
			// escolheu arquivo

			try {

				path = (jFileChooser
						.getSelectedFile().getAbsolutePath());
				
				currentImage = ImageIO.read(new File(path));				
				
				byteForImage = imageToByteArray(currentImage);
				
				System.out.println(path);
				
				this.setPreferredSize(new Dimension(currentImage.getWidth(),currentImage.getHeight()));
				
				widthImage = currentImage.getWidth();
				heightImage = currentImage.getHeight();

				xImage = x - currentImage.getHeight()/2;
				yImage = y - currentImage.getWidth()/2;

				// Criando novo arquivo

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Esse arquivo nao existe");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (acao == JFileChooser.CANCEL_OPTION) {
			// apertou botao cancelar
		} else if (acao == JFileChooser.ERROR_OPTION) {
			// outra opcao
		}

	}
	
	public void createImage(){
		try {
			currentImage = byteArrayToImage(byteForImage);
			System.out.println("ck");
			
			this.setPreferredSize(new Dimension(currentImage.getWidth(),currentImage.getHeight()));
			
			widthImage = currentImage.getWidth();
			heightImage = currentImage.getHeight();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static byte[] imageToByteArray(BufferedImage image) throws IOException
	{
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(image, "png", baos);
	    return baos.toByteArray();
	}

	
	public static BufferedImage byteArrayToImage(byte[] imageArray) throws IOException
	{
	    return ImageIO.read(new ByteArrayInputStream(imageArray));
	}
	

	public int getWidthImage() {
		return widthImage;
	}

	public int getWidthImageAux() {
		return widthImageAux;
	}

	public void setWidthImage(int widthImage) {
		this.widthImage = widthImage;
	}

	public int getHeightImage() {
		return heightImage;
	}

	public int getHeightImageAux() {
		return heightImageAux;
	}

	public void setHeightImage(int heightImage) {
		this.heightImage = heightImage;
	}

	public int getxImage() {
		return xImage;
	}

	public void setxImage(int xImage) {
		this.xImage = xImage;
	}

	public int getyImage() {
		return yImage;
	}

	public void setyImage(int yImage) {
		this.yImage = yImage;
	}

	public int getxMouseBeg() {
		return xMouseBeg;
	}

	public void setxMouseBeg(int xMouseBeg) {
		this.xMouseBeg = xMouseBeg;
	}

	public int getyMouseBeg() {
		return yMouseBeg;
	}

	public void setyMouseBeg(int yMouseBeg) {
		this.yMouseBeg = yMouseBeg;
	}

	public boolean[] getResizing() {
		return resizing;
	}

	public void setResizing(boolean[] resizing) {
		this.resizing = resizing;
	}

	public boolean isNotMove_resizing() {
		return notMove_resizing;
	}

	public void setNotMove_resizing(boolean notMove_resizing) {
		this.notMove_resizing = notMove_resizing;
	}

	public boolean isMove() {
		return move;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public boolean isCurrentImageRect() {
		return currentImageRect;
	}

	public void setCurrentImageRect(boolean currentImageRect) {
		this.currentImageRect = currentImageRect;
	}

	public boolean getCurrentImageRect() {
		return this.currentImageRect;
	}

	public boolean getMove() {
		return this.move;
	}

	public boolean getNotMove_resizing() {
		return this.notMove_resizing;
	}

	public int getxImageAux() {
		return this.xImageAux;
	}

	public void setxImageAux(int xImageAux) {
		this.xImageAux = xImageAux;
	}

	public int getyImageAux() {
		return this.yImageAux;
	}

	public void setyImageAux(int yImageAux) {
		this.yImageAux = yImageAux;
	}

	public void setHeightImageAux(int heightImageAux) {
		this.heightImageAux = heightImageAux;
	}

	public void setWidthImageAux(int widthImageAux) {
		this.widthImageAux = widthImageAux;
	}

	BufferedImage getCurrentImage() {
		return currentImage;
	}
	
	int getIndex() {
		return index;
	}
	
	void setIndex(int index){
		this.index = index;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Imagem.count = count;
	}
	
	
}
