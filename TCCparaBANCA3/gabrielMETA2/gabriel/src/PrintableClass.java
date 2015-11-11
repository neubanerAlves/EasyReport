import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrintableClass implements Pageable, Printable{

	
	ArrayList<Pagina> paginas = new ArrayList<Pagina>();
	
	int height;
	int width;
	
	PageFormat format = new PageFormat();
	
	
	PrintableClass(int height, int width, ArrayList<Pagina> paginas){
		
		this.height = height;
		this.width = width;
		
		this.paginas = paginas;
		
		Paper paper = new Paper();
	
		paper.setImageableArea(0, 0, width, height);
		
		paper.setSize(595, 842);
		
		format.setPaper(paper);
				
	}
	
	@Override
	public int getNumberOfPages() {
		// TODO Auto-generated method stub
		return paginas.size();
	}

	@Override
	public PageFormat getPageFormat(int arg0) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return format;
	}

	@Override
	public Printable getPrintable(int arg0) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public int print(Graphics graphics, PageFormat arg1, int arg2) throws PrinterException {
		// TODO Auto-generated method stub
				
		if(paginas.size()==arg2){
			return NO_SUCH_PAGE;
		}
		
		int w = width;
        int h = height;
        
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage image = new BufferedImage(w, h, type);

        double pixelPerInch = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();
    	float pixelToPoint = (float) 72 / (float) pixelPerInch;

        Graphics2D g2 = image.createGraphics();
        
        AffineTransform at = new AffineTransform();
	    at.scale(pixelToPoint, pixelToPoint);

		g2.transform(at);
		
        paginas.get(arg2).getTextPane().paint(g2);
        g2.dispose();
		
        graphics.drawImage(image, 0, 0, w, h, null);
	    	
	    return PAGE_EXISTS;
	}

}
