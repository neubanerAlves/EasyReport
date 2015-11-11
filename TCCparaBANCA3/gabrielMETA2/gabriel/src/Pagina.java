import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;

public class Pagina implements KeyListener, MouseListener, MouseMotionListener, Serializable {

	private StyledDocument documentAux;

	private StorageDocsForUndo undoAndRedo = new StorageDocsForUndo();

	private Spelling spelling;

	private static boolean paintRectInJText = true;

	private RedZigZagPainter painter = new RedZigZagPainter();

	private int[][] high;

	// Criando contador (necessario para atribuiçao em cada pagina de seu
	// determinado numero)
	private static int count;

	private int index;

	private boolean ctrlOn = false;
	private boolean shiftOn = false;

	private boolean itIsCopy = false;

	// Booleano que define se a pagina serao deletada
	private boolean deletePage = false;

	// Instanciando estilos
	private static Estilos estilos = new Estilos();

	// Instanciando alinhametos
	private static Alinhamentos alinhamentos = new Alinhamentos();

	// Criando retangulo
	private Rectangle rectangle;

	// Criando Array de imagens
	private ArrayList<Imagem> imagens = new ArrayList<Imagem>();

	// Criando documento
	private DefaultStyledDocument doc = new DefaultStyledDocument(estilos.getSC());

	// Criando verificaçaoo de sublinhado automatico
	private static boolean autoSubCheck = true;

	// Valor de uma polegada em centimetros
	private double INCH = 2.53999995;

	// Valor de pixel em uma polegada
	private double pixelPerInch = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();

	// Criando TextPane
	private MyTextPane textPane = new MyTextPane(doc);

	private static boolean checkNegrito, checkItalico, checkStyleAll, checkSuperscript;

	// Criando booleanos de verificação
	private boolean newPage = false;
	private boolean textForNextPage = false;
	private boolean toNextPage = false;
	private boolean enterToAddAttr = false;
	private boolean remindToAddLastUndo = false;
	private boolean charWasTyped = false;
	private boolean lastTextWasCopy = false;

	public static boolean selectAllPages = false;

	private int space_position = 0;

	private boolean verify = false;

	Pagina() {

		index = count;
		count++;

		// Adicionando keyListener no textPane
		textPane.addKeyListener(this);
		textPane.addMouseListener(this);
		textPane.addMouseMotionListener(this);

		// Definindo
		// Margem----------------------------------------------------------
		textPane.setMargin(new Insets(centimetersToPixels(3), centimetersToPixels(3), centimetersToPixels(2),
				centimetersToPixels(2)));
		// ----------------------------------------------------------------

		textPane.setPreferredSize(new Dimension(centimetersToPixels(21), centimetersToPixels(29.7)));
		textPane.setSize(new Dimension(centimetersToPixels(21), centimetersToPixels(29.7)));
		textPane.setMaximumSize(new Dimension(centimetersToPixels(21), centimetersToPixels(29.7)));

		System.out.println("Pagina criada");

		// Criando Caret

		textPane.setCaret(new DefaultCaret() {

			public void paint(Graphics g) {

				JTextComponent comp = getComponent();
				if (comp == null)
					return;

				Rectangle r = null;
				try {
					r = comp.modelToView(getDot());
					if (r == null)
						return;
				} catch (BadLocationException e) {
					return;
				}
				r.height = 15; // this value changes the caret size
				if (isVisible())
					g.fillRect(r.x, r.y, 1, r.height);
			}
		});

		try {
			spelling = new Spelling("src/pt.dic");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ----------------------------------------------------------
		// Create an undo action and add it to the text component
		textPane.getActionMap().put("Undo", new AbstractAction("Undo") {
			public void actionPerformed(ActionEvent evt) {

				if (!lastTextWasCopy)
					documentAux = saveDocFinal();

				textPane.setDocument(undoAndRedo.returnAction());
				textPane.setCaretPosition(textPane.getDocument().getLength());

				if (!lastTextWasCopy)
					remindToAddLastUndo = true;

			}
		});

		// Bind the undo action to ctl-Z
		textPane.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

		// Create a redo action and add it to the text component
		textPane.getActionMap().put("Redo", new AbstractAction("Redo") {
			public void actionPerformed(ActionEvent evt) {

				textPane.setDocument(undoAndRedo.remakeAction());
				textPane.setCaretPosition(textPane.getDocument().getLength());

			}
		});

		// Bind the redo action to ctl-Y
		textPane.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");

	}

	// AREA DE
	// LISTENERS-------------------------------------------------------------

	// MouseListener

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

		for (Imagem imagem : imagens) {

			if (arg0.getX() > imagem.getxImage() - 5 && arg0.getY() > imagem.getyImage() - 5
					&& arg0.getX() < imagem.getWidthImage() + imagem.getxImage() + 5
					&& arg0.getY() < imagem.getHeightImage() + imagem.getyImage() + 5) {

				imagem.setCurrentImageRect(true);

				imagem.setxImageAux(imagem.getxImage());
				imagem.setyImageAux(imagem.getyImage());

				imagem.setHeightImageAux(imagem.getHeightImage());
				imagem.setWidthImageAux(imagem.getWidthImage());

				// widthImage = arg0.getX() - 50; // 50 = inicio da imagem
				// heightImage = arg0.getY() - 50;

				imagem.setxMouseBeg(arg0.getX());
				imagem.setyMouseBeg(arg0.getY());

				imagem.setScale((double) imagem.getWidthImageAux() / (double) imagem.getHeightImageAux());

			} else {
				imagem.setCurrentImageRect(false);
			}

			// Controlando movimentaÃ§Ã£o

			if (arg0.getX() > imagem.getxImage() + 5 && arg0.getY() > imagem.getyImage() + 5)
				if (arg0.getX() < imagem.getWidthImage() + imagem.getxImage() - 5
						&& arg0.getY() < imagem.getHeightImage() + imagem.getyImage() - 5) {
					imagem.setMove(true);
				}
			// Controlando redimensionamento
			// ------------------------------------------------

			// Circulo 1
			if (arg0.getX() > (imagem.getxImage() - 5) && arg0.getY() > (imagem.getyImage() - 5))
				if (arg0.getX() < (imagem.getxImage() + 10) && arg0.getY() < (imagem.getyImage() + 5)) {

					imagem.getResizing()[0] = true;

				}

			// Circulo 2
			if (arg0.getX() > ((imagem.getWidthImage() / 2 - 5) + imagem.getxImage())
					&& arg0.getY() > (imagem.getyImage() - 5))
				if (arg0.getX() < ((imagem.getWidthImage() / 2 + 10) + imagem.getxImage())
						&& arg0.getY() < (imagem.getyImage() + 5)) {

					imagem.getResizing()[1] = true;

				}

			// Circulo 3
			if (arg0.getX() > ((imagem.getWidthImage() - 5) + imagem.getxImage())
					&& arg0.getY() > (imagem.getyImage() - 5))
				if (arg0.getX() < ((imagem.getWidthImage() + 10) + imagem.getxImage())
						&& arg0.getY() < (imagem.getyImage() + 5)) {

					imagem.getResizing()[2] = true;

				}

			// Circulo 4
			if (arg0.getX() > ((imagem.getWidthImage() - 5) + imagem.getxImage())
					&& arg0.getY() > ((imagem.getHeightImage() / 2) + imagem.getyImage() - 5))
				if (arg0.getX() < ((imagem.getWidthImage() + 10) + imagem.getxImage())
						&& arg0.getY() < ((imagem.getHeightImage() / 2) + imagem.getyImage() + 5)) {

					imagem.getResizing()[3] = true;

				}

			// Circulo 5
			if (arg0.getX() > ((imagem.getWidthImage() - 5) + imagem.getxImage())
					&& arg0.getY() > ((imagem.getHeightImage()) + imagem.getyImage() - 5))
				if (arg0.getX() < ((imagem.getWidthImage() + 10) + imagem.getxImage())
						&& arg0.getY() < ((imagem.getHeightImage()) + imagem.getyImage() + 5)) {

					imagem.getResizing()[4] = true;

				}
			// Circulo 6
			if (arg0.getX() > ((imagem.getWidthImage() / 2 - 5) + imagem.getxImage())
					&& arg0.getY() > ((imagem.getHeightImage()) + imagem.getyImage() - 5))
				if (arg0.getX() < ((imagem.getWidthImage() / 2 + 10) + imagem.getxImage())
						&& arg0.getY() < ((imagem.getHeightImage()) + imagem.getyImage() + 5)) {

					imagem.getResizing()[5] = true;

				}

			// Circulo 7
			if (arg0.getX() > (imagem.getxImage() - 5)
					&& arg0.getY() > ((imagem.getHeightImage()) + imagem.getyImage() - 5))
				if (arg0.getX() < (imagem.getxImage() + 10)
						&& arg0.getY() < ((imagem.getHeightImage()) + imagem.getyImage() + 5)) {

					imagem.getResizing()[6] = true;

				}
			// Circulo 8
			if (arg0.getX() > (imagem.getxImage() - 5)
					&& arg0.getY() > ((imagem.getHeightImage() / 2) + imagem.getyImage() - 5))
				if (arg0.getX() < (imagem.getxImage() + 10)
						&& arg0.getY() < ((imagem.getHeightImage() / 2) + imagem.getyImage() + 5)) {

					imagem.getResizing()[7] = true;

				}

		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

		if (arg0.getButton() == 3) {
			doPop(arg0);
		}
		for (Imagem imagem : imagens) {
			for (int i = 0; i < imagem.getResizing().length; i++)
				if (imagem.getResizing()[i])
					imagem.getResizing()[i] = false;
			imagem.setNotMove_resizing(false);
			imagem.setMove(false);
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

		for (Imagem imagem : imagens) {

			if (imagem.getCurrentImageRect()) {

				for (int i = 0; i < imagem.getResizing().length; i++) {
					if (imagem.getResizing()[i])
						imagem.setNotMove_resizing(true);
				}

				if (imagem.getMove())
					if (!imagem.getNotMove_resizing()) {
						int x = imagem.getxImageAux() - (imagem.getxMouseBeg() - arg0.getX());
						int y = imagem.getyImageAux() - (imagem.getyMouseBeg() - arg0.getY());

						if ((x > centimetersToPixels(3)
								&& (x + imagem.getWidthImage()) < (centimetersToPixels(21) - centimetersToPixels(2))))
							imagem.setxImage(x);

						if ((y > centimetersToPixels(3) && (y + imagem.getHeightImage()) < (centimetersToPixels(29.7)
								- centimetersToPixels(2))))
							imagem.setyImage(y);

						textPane.repaint();
					}

				if (imagem.getResizing()[0]) {

					imagem.setxImage(imagem.getxImageAux() - (imagem.getxMouseBeg() - arg0.getX()));
					imagem.setyImage((int) ((imagem.getyImageAux() - (imagem.getxMouseBeg() - arg0.getX()))
							* imagem.getScale()));
					imagem.setWidthImage(imagem.getWidthImageAux() + (imagem.getxMouseBeg() - arg0.getX()));
					imagem.setHeightImage((int) ((imagem.getWidthImageAux() + (imagem.getxMouseBeg() - arg0.getX()))
							/ imagem.getScale()));

					textPane.repaint();

				}

				if (imagem.getResizing()[1]) {

					imagem.setyImage(imagem.getyImageAux() - (imagem.getyMouseBeg() - arg0.getY()));
					imagem.setHeightImage(((imagem.getHeightImageAux() + (imagem.getyMouseBeg() - arg0.getY()))));

					textPane.repaint();
				}

				if (imagem.getResizing()[2]) {

					imagem.setyImage(imagem.getyImageAux() - (imagem.getyMouseBeg() - arg0.getY()));
					imagem.setWidthImage((int) ((imagem.getHeightImageAux() + (imagem.getyMouseBeg() - arg0.getY()))
							* imagem.getScale()));
					imagem.setHeightImage(((imagem.getHeightImageAux() + (imagem.getyMouseBeg() - arg0.getY()))));

					textPane.repaint();
				}

				if (imagem.getResizing()[3]) {

					imagem.setWidthImage(imagem.getWidthImageAux() - (imagem.getxMouseBeg() - arg0.getX()));

					textPane.repaint();
				}

				if (imagem.getResizing()[4]) {

					imagem.setWidthImage(imagem.getWidthImageAux() - (imagem.getxMouseBeg() - arg0.getX()));
					imagem.setHeightImage((int) ((imagem.getWidthImageAux() - (imagem.getxMouseBeg() - arg0.getX()))
							/ imagem.getScale()));

					textPane.repaint();

				}

				if (imagem.getResizing()[5]) {

					imagem.setHeightImage(((imagem.getHeightImageAux() - (imagem.getyMouseBeg() - arg0.getY()))));

					textPane.repaint();

				}

				if (imagem.getResizing()[6]) {

					imagem.setWidthImage(imagem.getWidthImageAux() + (imagem.getxMouseBeg() - arg0.getX()));
					imagem.setxImage(imagem.getxImageAux() - (imagem.getxMouseBeg() - arg0.getX()));
					imagem.setHeightImage((int) ((imagem.getWidthImageAux() + (imagem.getxMouseBeg() - arg0.getX()))
							/ imagem.getScale()));

					textPane.repaint();

				}

				if (imagem.getResizing()[7]) {

					imagem.setWidthImage(imagem.getWidthImageAux() + (imagem.getxMouseBeg() - arg0.getX()));
					imagem.setxImage(imagem.getxImageAux() - (imagem.getxMouseBeg() - arg0.getX()));

					textPane.repaint();

				}
			}

		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	
	// KeyListener-----------------------------------------------------------

	@Override
	public void keyPressed(KeyEvent character) {

		if (remindToAddLastUndo) {

			undoAndRedo.addDoc(documentAux, 1);
			remindToAddLastUndo = false;
			lastTextWasCopy = true;
		}

		if (character.getKeyCode() == KeyEvent.VK_CONTROL)
			ctrlOn = true;
		if (character.getKeyCode() == KeyEvent.VK_SHIFT)
			shiftOn = true;

		if (!ctrlOn) {
			Chronometer.stop();

			int charValue = (int) character.getKeyChar();

			if (Chronometer.stime() > 1.0) {
				undoAndRedo.addDoc(this.doc, 0);

			}

			if ((charValue > 64 && charValue < 91) || (charValue > 96 && charValue < 123)) {
				Chronometer.start();
				charWasTyped = true;
				lastTextWasCopy = false;
			}
		} else {
			charWasTyped = false;
		}

		char letter = character.getKeyChar();

		if (character.getKeyCode() == KeyEvent.VK_SPACE || letter == ' ' || letter == '!' || letter == ','
				|| letter == ':' || letter == '.' || letter == '?' || letter == '+' || letter == '-') {
			verify = true;
			space_position = textPane.getCaretPosition();
		}

		for (Imagem imagem : imagens)
			if (imagem.getCurrentImageRect()) {
				if (character.getKeyCode() == KeyEvent.VK_DELETE)
					imagens.remove(imagem.getIndex());
				Imagem.setCount(Imagem.getCount() - 1);
				indexImagesCorrection();
			}
		if (character.getKeyCode() == KeyEvent.VK_V && ctrlOn) {
			itIsCopy = true;
		}

		if (character.getKeyCode() == KeyEvent.VK_ENTER && ctrlOn) {
			newPage = true;
		}

		/*
		 * if (character.getKeyCode() == KeyEvent.VK_A && ctrlOn) {
		 * selectAllPages = true; }
		 */

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

		int caretPosAux = textPane.getCaretPosition();

		if (caretPosAux > 2)
			try {
				if (textPane.getText(caretPosAux - 3, 3).equals("-->")) {

					textPane.setSelectionStart(caretPosAux - 3);
					textPane.setSelectionEnd(caretPosAux);
					textPane.replaceSelection("");

					doc.insertString(caretPosAux - 3, Character.toString((char) (8594)), null);
				}
			} catch (BadLocationException e2) { // TODO Auto-generated
			}

		// Valor da bolinha: 9679

		// TODO Auto-generated method stub

		caretPosAux = textPane.getCaretPosition();

		if (doc.getLength() >= 1 && doc.getLength() <= 6) {
			doc.setParagraphAttributes(0, 1, alinhamentos.getEstiloJustificado(), false);
			doc.setParagraphAttributes(0, 1, alinhamentos.getEspacamento(), false);

		}

		// Verificando se o texto passou da margem
		if (!ctrlOn)
			if (!shiftOn) {
				if (textPane.getCaretPosition() > 0) {

					try {
						textPane.setCaretPosition(doc.getLength()-1);
					} catch (IllegalArgumentException e) {
					}

					if (verify)
						if (!textForNextPage)
							if (doc.getLength() >= 2) {
								try {
									if (!textPane.getText(space_position - 1, 1).equals(" "))
										checkElement(doc.getCharacterElement(space_position - 1),
												calcPositionOfEndWord(space_position - 1));
								} catch (BadLocationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								verify = false;
							}
				}
				
			}

		verifyTextLocation(textPane.getCaretPosition());
		textPane.setCaretPosition(caretPosAux);
		
		if (textPane.getCaretPosition() == 0) {

			System.out.println("ind> " + index);

			if (index != 0)
				if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					deletePage = true;
				}
		}
		if (arg0.getKeyCode() == KeyEvent.VK_CONTROL)
			ctrlOn = false;
		if (arg0.getKeyCode() == KeyEvent.VK_SHIFT)
			shiftOn = false;

		int caretP = textPane.getCaretPosition();
		char cAux[] = new char[1];

		// Adicionando espaçamento e alinhamento ABNT no texto
		if (enterToAddAttr) {
			this.setABNT(caretP - 1, caretP);
			enterToAddAttr = false;
		}

		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				rectangle = textPane.modelToView(textPane.getCaretPosition());
				if (rectangle.y >= (centimetersToPixels(29.7) - centimetersToPixels(2))) {
					newPage = true;
				}

				// Booleano para adicionar espaçamento e alinhamento
				enterToAddAttr = true;

			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		if (textPane.getCaretPosition() > 1)
			// Boolean para verificar se o usuÃ¡rio deseja um subscrito
			// automatico
			if (autoSubCheck) {

				// Fazendo a verifiaÃ§Ã£o para saber se hÃ¡ algo para
				// subscrever ou sobrescrever.

				verifySubscript(caretP, cAux);
				verifySuperscript(caretP, cAux);

				if (textPane.getCaretPosition() >= 1) {
					try {
						if (textPane.getText(caretP - 1, 1).equals(" ")) {
							doc.setCharacterAttributes(caretP - 1, 1, estilos.getEstiloSubscritoOff(), false);
							doc.setCharacterAttributes(caretP - 1, 1, estilos.getEstiloSuperscritoOff(), false);

						}
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		// Verificando se o texto e uma copia para colocar na ABNT
		if (itIsCopy)
			setABNT(0, doc.getLength());

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	// FIM DA Ã�REA DE
	// LISTENERS-------------------------------------------------------------

	

	// Alinhamento
	public void setAlignement(String alignement) {

		int inicio = textPane.getSelectionStart();
		int fim = textPane.getSelectionEnd();

		// Verificando se existe seleÃ§Ã£o no textpane
		if (fim != 0) {
			if (alignement.equals("Justificado"))
				doc.setParagraphAttributes(inicio, fim, alinhamentos.getEstiloJustificado(), false);
			if (alignement.equals("Centro"))
				doc.setParagraphAttributes(inicio, fim, alinhamentos.getEstiloCentro(), false);
			if (alignement.equals("Esquerda"))
				doc.setParagraphAttributes(inicio, fim, alinhamentos.getEstiloEsquerda(), false);
			if (alignement.equals("Direita"))
				doc.setParagraphAttributes(inicio, fim, alinhamentos.getEstiloDireita(), false);

		}
	}

	// Adicionando Estilo de fonte
	public void setStyle(String style) {
		// TODO Auto-generated method stub

		int inicio = textPane.getSelectionStart();
		int fim = textPane.getSelectionEnd();

		Object styleForCheck = new Object();
		SimpleAttributeSet styleForAply = new SimpleAttributeSet();
		SimpleAttributeSet styleForRemove = new SimpleAttributeSet();

		// Verificando estilo a ser aplicado
		if (style.equals("Italico")) {
			styleForCheck = StyleConstants.Italic;
			StyleConstants.setItalic(styleForAply, true);
			StyleConstants.setItalic(styleForRemove, false);
		}
		if (style.equals("Sublinhado")) {
			styleForCheck = StyleConstants.Underline;
			StyleConstants.setUnderline(styleForAply, true);
			StyleConstants.setUnderline(styleForRemove, false);
		}
		if (style.equals("Negrito")) {
			styleForCheck = StyleConstants.Bold;
			StyleConstants.setBold(styleForAply, true);
			StyleConstants.setBold(styleForRemove, false);
		}
		if (style.equals("Superescrito")) {
			styleForCheck = StyleConstants.Superscript;
			StyleConstants.setSuperscript(styleForAply, true);
			StyleConstants.setSuperscript(styleForRemove, false);
		}
		if (style.equals("Subscrito")) {
			styleForCheck = StyleConstants.Subscript;
			StyleConstants.setSubscript(styleForAply, true);
			StyleConstants.setSubscript(styleForRemove, false);
		}

		// Verificando se existe seleção no textpane
		if (fim != 0) {
			int count = 0;

			checkStyleAll = false;

			for (int i = inicio; i < fim; i++) {
				// VerificaÃ§Ã£o de atributo
				if ((doc.getCharacterElement(i).getAttributes().getAttribute(styleForCheck)) == null) {
					// SE for nullo nao faz nada
				} else if ((doc.getCharacterElement(i).getAttributes().getAttribute(styleForCheck))
						.equals(new Boolean(Boolean.TRUE))) {
					count++;
				}
			}

			if (count == (fim - inicio))
				checkStyleAll = true;

			if (!checkStyleAll) {
				doc.setCharacterAttributes(inicio, fim - inicio, styleForAply, false);

			} else {
				doc.setCharacterAttributes(inicio, fim - inicio, styleForRemove, false);
				System.out.println("entrou");
			}
		}
	}

	
	// TAMANHO E TIPO DE
	// FONTE------------------------------------------------------
	void setFamilyFont(SimpleAttributeSet familyFont) {

		int inicio = textPane.getSelectionStart();
		int fim = textPane.getSelectionEnd();

		if (fim != 0)
			doc.setCharacterAttributes(inicio, fim - inicio, familyFont, false);
	}

	void setFontSize(SimpleAttributeSet sizeFont) {

		int inicio = textPane.getSelectionStart();
		int fim = textPane.getSelectionEnd();

		if (fim != 0)
			doc.setCharacterAttributes(inicio, fim - inicio, sizeFont, false);

	}
	//-----------------------------------------------------------

	
	
	//Getters e Setters------------------------------------------

	void setIndex(int index) {
		this.index = index;
		Pagina.count = index + 1;
	}

	boolean getNewPage() {
		return newPage;
	}

	boolean getTextForNextPage() {
		return textForNextPage;
	}

	void setTextForNextPage(boolean textForNextPage) {
		this.textForNextPage = textForNextPage;
	}

	boolean getToNextPage() {
		return toNextPage;
	}

	ArrayList<Imagem> getImagens() {
		return imagens;
	}

	void setImagens(ArrayList<Imagem> imagens) {
		this.imagens = imagens;
	}

	void setToNextPage(boolean toNextPage) {
		this.toNextPage = toNextPage;
	}

	void setNewPage(boolean newPage) {
		this.newPage = newPage;
	}
	
	
	//Funções fora de uso---------------------------------------
	public boolean isSelectAllPages() {
		return selectAllPages;
	}

	public void setSelectAllPages(boolean selectAllPages) {
		this.selectAllPages = selectAllPages;
	}

	//----------------------------------------------------------


	void setDoc(DefaultStyledDocument doc) {
		this.doc = doc;
		textPane.setStyledDocument(this.doc);
	}


	void setPaintRectTextPane(boolean paintRectTextPane) {
		this.paintRectInJText = paintRectTextPane;
	}


	public void setAutoSubCheck(boolean autoSubCheck) {
		this.autoSubCheck = autoSubCheck;
	}

	public MyTextPane getTextPane() {
		return textPane;
	}

	public DefaultStyledDocument getDoc() {
		return doc;
	}

	public boolean isDeletePage() {
		return deletePage;
	}
	
	//Fim de getters e setters---------------------------------
	
	public void decressCount() {
		count--;
	}

	//Remove as seleções da pagina para fins de salvamento
	void removeSelections() {
		textPane.getCaret().setVisible(false);

		for (Imagem imagem : imagens) {
			imagem.setCurrentImageRect(false);
		}

	}

	//Seta um texto em ABNT
	void setABNT(int beginOffSet, int endOffSet) {
		doc.setParagraphAttributes(beginOffSet, endOffSet, alinhamentos.getEspacamento(), false);
		doc.setParagraphAttributes(beginOffSet, endOffSet, alinhamentos.getEstiloJustificado(), false);

	}

	//Verifica se o texto passou dos limites da pagina
	boolean verifyTextLocation(int positionOfCaret) {
		try {
			rectangle = textPane.modelToView(positionOfCaret);
			if (rectangle.y >= (centimetersToPixels(29.7) - centimetersToPixels(2))) {

				textForNextPage = true;
				return true;
			}
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}
	
	//Salva os elementos para proxima pagina
	ArrayList<Element> saveElementsToNextPage() throws BadLocationException {

		ArrayList<Element> elementos = new ArrayList<Element>();

		boolean addLine = true;

		int caretPosAux = textPane.getCaretPosition();
		int caretPosBeg = textPane.getCaretPosition();

		textPane.setCaretPosition(doc.getLength() - 1);

		while (addLine) {

			caretPosBeg = textPane.getCaretPosition();

			textPane.setCaretPosition(Utilities.getRowStart(textPane, textPane.getCaretPosition()) - 1);
			try {
				rectangle = textPane.modelToView(textPane.getCaretPosition());
				if (!(rectangle.y >= (centimetersToPixels(29.7) - centimetersToPixels(2)))) {
					addLine = false;
				}

			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		for (int i = Utilities.getRowStart(textPane, caretPosBeg); i < doc.getLength(); i++)
			elementos.add(doc.getCharacterElement(i));

		textPane.setCaretPosition(caretPosAux);

		return elementos;
	}

	//Salva o texto para proxima pagina
	String saveTextForNextPage() throws BadLocationException {

		boolean addLine = true;

		int caretPosAux = textPane.getCaretPosition();
		int caretPosBeg = textPane.getCaretPosition();

		textPane.setCaretPosition(doc.getLength() - 1);

		while (addLine) {

			caretPosBeg = textPane.getCaretPosition();
			textPane.setCaretPosition(Utilities.getRowStart(textPane, textPane.getCaretPosition()) - 1);

			try {
				rectangle = textPane.modelToView(textPane.getCaretPosition());
				if (!(rectangle.y >= (centimetersToPixels(29.7) - centimetersToPixels(2)))) {
					addLine = false;

				}
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		String textForNextPage = textPane.getText(Utilities.getRowStart(textPane, caretPosBeg),
				(doc.getLength() - Utilities.getRowStart(textPane, caretPosBeg)));

		textPane.setSelectionStart(Utilities.getRowStart(textPane, caretPosBeg));
		textPane.setSelectionEnd(doc.getLength());

		textPane.replaceSelection("");
		doc.insertString(doc.getLength() - 1, Character.toString((char) 8), null);

		try {
			textPane.setCaretPosition(caretPosAux);
		} catch (IllegalArgumentException e) {
			textPane.setCaretPosition(doc.getLength());
		}
		return textForNextPage;
	}
	
	// Conversão de centimetros para pixels
	int centimetersToPixels(double cm) {

		double inchAux;

		inchAux = cm / INCH;
		return (int) (pixelPerInch * inchAux);

	}
	
	//Função que armazena a ultima alteração no documento
	private StyledDocument saveDocFinal() {

		StyledDocument document = textPane.getStyledDocument();
		StyledDocument documentToReturn = new DefaultStyledDocument(estilos.getSC());

		Element element;
		String text = null;

		try {
			text = document.getText(0, document.getLength());
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < document.getLength(); i++) {

			element = document.getCharacterElement(i);
			try {
				documentToReturn.insertString(i, Character.toString(text.charAt(i)), element.getAttributes());
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return documentToReturn;
	}

	//Remove os erros onde lhe for indicado
	
//Função que remove o erro de palavras corrigidas
private void removeHighLight(int i, int j, Highlighter highlighter, Highlight[] highlights) {

			highlighter = textPane.getHighlighter();
			highlights = highlighter.getHighlights();

			for (int k = highlights.length; --k >= 0;) {
				Highlight highlight = highlights[k];
				int hlStartOffset = highlight.getStartOffset();
				int hlEndOffset = highlight.getEndOffset();
				if (i == hlStartOffset && hlEndOffset == j)
					if (highlight.getPainter() == painter) {
						highlighter.removeHighlight(highlight);
					}
				if (i > hlStartOffset && i < hlEndOffset && hlEndOffset == j) {
					try {
						highlighter.changeHighlight(highlight, i, j - 1);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
	
	//Calcula o fim de uma palavra a partir de uma posição 
	//Função que calcula o fim da palavra a partir de uma posição
	int calcPositionOfEndWord(int position) {
		int aux;
		char letter;

		for (aux = 0; aux < doc.getLength() - position; aux++) {
			try {
				letter = textPane.getText(position + aux, 1).charAt(0);
				if (letter == ' ' || letter == (char) 10 || letter == '!' || letter == ',' || letter == ':'
						|| letter == '.' || letter == '?' || letter == '+' || letter == '-')
					break;
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return position = position + aux;
	}

	
	//Função que salva os erros no texto
	//Função que salva os erros no texto (usada devido a remoção necessaria ao imprimir ou gerar o PDF)
	public void saveHighlighters() {

		Highlighter highlighter = textPane.getHighlighter();
		Highlight[] highlights = highlighter.getHighlights();

		high = new int[highlights.length][2];

		for (int k = highlights.length; --k >= 0;) {
			Highlight highlight = highlights[k];
			high[k][0] = highlight.getStartOffset();
			high[k][1] = highlight.getEndOffset();
		}

		highlighter.removeAllHighlights();
	}

	
	//Função que lê os erros no texto
	//Função que lê os erros no texto (usada devido a remoção necessaria ao imprimir ou gerar o PDF)
	public void loadHighlighters() {

		Highlighter highlighter = textPane.getHighlighter();

		for (int k = high.length; --k >= 0;) {

			try {
				highlighter.addHighlight(high[k][0], high[k][1], painter);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	//Função que verifica se a palavra esta correta
	//Função que verifica se deve ser superescrito o texto	
	private String[] checkElement(javax.swing.text.Element element, int position) throws BadLocationException {

		int i = 0;
		int j = position;

		for (i = j - 1; i >= 0; i--) {
			if (textPane.getText(i, 1).equals(" ") || textPane.getText(i, 1).charAt(0) == (char) (10))
				break;
		}
		if (i != 0)
			i++;

		Highlighter highlighter = textPane.getHighlighter();
		Highlight[] highlights = highlighter.getHighlights();

		String word = null;

		try {
			word = textPane.getText(i, (j - i));
		} catch (BadLocationException e) {

		}

		if (i == 0)
			word = word.toLowerCase();
		else if (doc.getText(i - 1, 1).equals(Integer.toString((int) (10))))
			word = word.toLowerCase();

		String[] correct = spelling.correct(word);

		if (!(correct == null))
			highlighter.addHighlight(i, j, painter);
		else
			removeHighLight(i, j, highlighter, highlights);

		if (space_position == j)
			removeHighLight(j, j + 1, highlighter, highlights);

		return correct;
	}

	// Funcao que verifica o que deve ser superescrito
	
	//Função que verifica se deve ser superescrito o texto
	//Função que verifica se deve ser subscrito o texto
	void verifySuperscript(int caretP, char[] cAux) {
		int charValue;

		try {
			if (textPane.getText(caretP - 1, 1).equals("+") || textPane.getText(caretP - 1, 1).equals("-")) {
				try {

					// Definindo o numero em ASCII do argumento
					// anteriormente digitado.
					cAux = textPane.getText(caretP - 2, 1).toCharArray();
					charValue = (int) cAux[0];

					// Verifica se o argumento digitado era um caractere
					// alfabÃ©tico.
					if ((charValue > 64 && charValue < 91) || (charValue > 96 && charValue < 123)) {

						System.out.println("entrou");
						doc.setCharacterAttributes(caretP - 1, 1, estilos.getEstiloSuperscrito(), false);
					}
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// Funcao que verifica o que deve ser subescrito
	
	//Função que verifica se deve ser subscrito o texto
	void verifySubscript(int caretP, char[] cAux) {

		int charValue;

		try {
			if (textPane.getText(caretP - 1, 1).matches("[0-9]*")) {
				try {

					// Definindo o numero em ASCII do argumento
					// anteriormente digitado.
					cAux = textPane.getText(caretP - 2, 1).toCharArray();
					charValue = (int) cAux[0];

					// Verifica se o argumento digitado era um caractere
					// alfabÃ©tico.
					if ((charValue > 64 && charValue < 91) || (charValue > 96 && charValue < 123) || cAux[0] == ')') {

						System.out.println("entrou");
						doc.setCharacterAttributes(caretP - 1, 1, estilos.getEstiloSubscrito(), false);
					}
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			if (textPane.getText(caretP - 2, 2).matches("[0-99]*")) {
				try {

					// Definindo o numero em ASCII do argumento
					// anteriormente digitado.
					cAux = textPane.getText(caretP - 3, 1).toCharArray();
					charValue = (int) cAux[0];

					// Verifica se o argumento digitado era um caractere
					// alfabÃ©tico.
					if ((charValue > 64 && charValue < 91) || (charValue > 96 && charValue < 123) || cAux[0] == ')') {

						System.out.println("entrou");
						doc.setCharacterAttributes(caretP - 2, 2, estilos.getEstiloSubscrito(), false);
					}
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (textPane.getCaretPosition() >= 4)
			try {
				if (textPane.getText(caretP - 4, 4).equals("(aq)")) {
					doc.setCharacterAttributes(caretP - 4, 4, estilos.getEstiloSubscrito(), false);
				}
				if (textPane.getText(caretP - 3, 3).equals("(l)")) {
					doc.setCharacterAttributes(caretP - 3, 3, estilos.getEstiloSubscrito(), false);
				}

				if (textPane.getText(caretP - 3, 3).equals("(g)")) {
					doc.setCharacterAttributes(caretP - 3, 3, estilos.getEstiloSubscrito(), false);
				}
				if (textPane.getText(caretP - 3, 3).equals("(s)")) {
					doc.setCharacterAttributes(caretP - 3, 3, estilos.getEstiloSubscrito(), false);
				}
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	
	//Função que corrige o index das imagens
	void indexImagesCorrection() {

		for (int i = 0; i < imagens.size(); i++) {

			imagens.get(i).setIndex(i);

		}
	}

	
	private void doPop(MouseEvent e) {
		PopUpDemo menu = new PopUpDemo(e.getX(), e.getY());
		menu.show(e.getComponent(), e.getX(), e.getY());
	}

	
	public class PopUpDemo extends JPopupMenu {

		JMenuItem anItem;
		ArrayList<JMenuItem> correct = new ArrayList<JMenuItem>();
		JMenuItem addToDictionary;

		public PopUpDemo(final int x, final int y) {

			int positionAux = (textPane.viewToModel(new Point(x, y)));
			String[] sugestions = null;

			final int position = calcPositionOfEndWord(positionAux);

			try {
				sugestions = checkElement(doc.getCharacterElement(position), position);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			ActionListener correctWord = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int i = 0;
					int j = position;

					for (i = j - 1; i >= 0; i--) {
						try {
							if (textPane.getText(i, 1).equals(" ") || textPane.getText(i, 1).charAt(0) == (char) (10))
								break;
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					if (i != 0)
						i++;

					textPane.setSelectionStart(i);
					textPane.setSelectionEnd(j);
					textPane.replaceSelection(e.getActionCommand());
				}

			};

			if (sugestions == null) {
				anItem = new JMenuItem("Inserir imagem");
				add(anItem);
				anItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						imagens.add(new Imagem());
						imagens.get(imagens.size() - 1).insereImagem(x, y);

						textPane.setImagens(imagens);
						textPane.repaint();
					}
				});
			} else {

				if (!sugestions[0].equals("noCand"))
					for (int i = 0; i < sugestions.length && i < 8; i++) {
						correct.add(new JMenuItem(sugestions[i]));
						correct.get(i).setActionCommand(sugestions[i]);
						correct.get(i).addActionListener(correctWord);
						add(correct.get(i));
					}

				addToDictionary = new JMenuItem("Adicionar ao dicionario");
				addToDictionary.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						int i = 0;
						int j = position;
						String word = null;

						for (i = j - 1; i >= 0; i--) {
							try {
								if (textPane.getText(i, 1).equals(" ")
										|| textPane.getText(i, 1).charAt(0) == (char) (10))
									break;
							} catch (BadLocationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						if (i != 0) {
							i++;
							try {
								word = textPane.getText(i, j - i);
							} catch (BadLocationException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						} else {
							try {
								word = textPane.getText(i, j - i);
							} catch (BadLocationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							word = word.toLowerCase();
						}

						try {
							spelling.addToDictionary(word, "src/pt.dic");
							checkElement(doc.getCharacterElement(position), position);
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				addSeparator();
				add(addToDictionary);
			}
		}
	}

	public class Componente extends JComponent {

		protected void paintComponent(final Graphics g) {

			super.paintComponent(g);

		}

	}

	public static class MyTextPane extends JTextPane {

		private ArrayList<Imagem> imagens = new ArrayList<Imagem>();

		int inch = Toolkit.getDefaultToolkit().getScreenResolution();
		float pixelToPoint = (float) 1.4;

		public MyTextPane(DefaultStyledDocument doc) {
			super(doc);
			setOpaque(false);

			// this is needed if using Nimbus L&F - see
			// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6687960
			setBackground(new Color(0, 0, 0, 0));
			// getDocument().putProperty("ZOOM_FACTOR", new Double(1.5));
		}

		public void setImagens(ArrayList<Imagem> imagens) {

			this.imagens = imagens;

		}

		public float convertToPoints(int pixels) {
			return (float) (pixels * pixelToPoint);
		}

		public float convertToPixels(int points) {
			return (float) (points / pixelToPoint);
		}

		@Override
		protected void paintComponent(Graphics g) {
			// set background green - but can draw image here too

			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.BLACK);

			if (paintRectInJText)
				g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
			else {
				g.setColor(Color.white);
				g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
			}
			try {
				for (Imagem imagem : imagens) {

					g.drawImage(imagem.getCurrentImage(), imagem.getxImage(), imagem.getyImage(),
							imagem.getWidthImage(), imagem.getHeightImage(), null);

					if (imagem.getCurrentImageRect()) {
						g.setColor(Color.BLACK);
						g.drawRect(imagem.getxImage(), imagem.getyImage(), imagem.getWidthImage(),
								imagem.getHeightImage());
						g.setColor(Color.GRAY);
						g.fillOval(imagem.getxImage() - 5, imagem.getyImage() - 5, 10, 10);
						g.fillOval((((imagem.getWidthImage() / 2) - 5) + imagem.getxImage()), (imagem.getyImage() - 5),
								10, 10);
						g.fillOval(((imagem.getWidthImage() - 5) + imagem.getxImage()), (imagem.getyImage() - 5), 10,
								10);

						g.fillOval(((imagem.getWidthImage() - 5) + imagem.getxImage()),
								((imagem.getHeightImage() / 2) + imagem.getyImage() - 5), 10, 10);

						g.fillOval(imagem.getxImage() - 5, (imagem.getyImage() + imagem.getHeightImage() - 5), 10, 10);
						g.fillOval((((imagem.getWidthImage() / 2) - 5) + imagem.getxImage()),
								(imagem.getyImage() + imagem.getHeightImage() - 5), 10, 10);
						g.fillOval((imagem.getWidthImage() - 5) + imagem.getxImage(),
								(imagem.getyImage() + imagem.getHeightImage() - 5), 10, 10);

						g.fillOval((imagem.getxImage() - 5), ((imagem.getHeightImage() / 2) + imagem.getyImage() - 5),
								10, 10);

						g.setColor(Color.BLACK);

						g.drawOval(imagem.getxImage() - 5, imagem.getyImage() - 5, 10, 10);
						g.drawOval((((imagem.getWidthImage() / 2) - 5) + imagem.getxImage()), (imagem.getyImage() - 5),
								10, 10);
						g.drawOval(((imagem.getWidthImage() - 5) + imagem.getxImage()), (imagem.getyImage() - 5), 10,
								10);

						g.drawOval(((imagem.getWidthImage() - 5) + imagem.getxImage()),
								((imagem.getHeightImage() / 2) + imagem.getyImage() - 5), 10, 10);

						g.drawOval(imagem.getxImage() - 5, (imagem.getyImage() + imagem.getHeightImage() - 5), 10, 10);
						g.drawOval((((imagem.getWidthImage() / 2) - 5) + imagem.getxImage()),
								(imagem.getyImage() + imagem.getHeightImage() - 5), 10, 10);
						g.drawOval((imagem.getWidthImage() - 5) + imagem.getxImage(),
								(imagem.getyImage() + imagem.getHeightImage() - 5), 10, 10);

						g.drawOval((imagem.getxImage() - 5), ((imagem.getHeightImage() / 2) + imagem.getyImage() - 5),
								10, 10);
					}

				}
				super.paintComponent(g);

			} catch (NullPointerException e) {
			}
		}

	}

}
