import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class JanelaPrincipal extends JFrame implements KeyListener, Runnable, Serializable {
	
	private GridBagConstraints gbcToPages;

	
	private JMenuBar menuBar = new JMenuBar();
	private ArrayList<DefaultStyledDocument> docCheck = new ArrayList<DefaultStyledDocument>();
	private int intEdicao;
	private boolean existencia;
	private JFileChooser jFileChooser = new JFileChooser();

	DefaultStyledDocument dsdAux = new DefaultStyledDocument();

	private ImageIcon subIcon = new ImageIcon(getClass().getResource("/Imagem/autoSub.png"));

	// Icones Arquivo
	private ImageIcon newIcon = new ImageIcon(getClass().getResource("/Imagem/novo.png"));
	private ImageIcon openIcon = new ImageIcon(getClass().getResource("/Imagem/abrir.png"));
	private ImageIcon saveIcon = new ImageIcon(getClass().getResource("/Imagem/salvar.png"));
	private ImageIcon printIcon = new ImageIcon(getClass().getResource("/Imagem/imprimir.png"));
	private ImageIcon pdfIcon = new ImageIcon(getClass().getResource("/Imagem/exportarPDF.png"));

	// Icones alinhamentos

	private ImageIcon rightAlignmentIcon = new ImageIcon(getClass().getResource("/Imagem/direita.png"));
	private ImageIcon CentertAlignmentIcon = new ImageIcon(getClass().getResource("/Imagem/centro.png"));
	private ImageIcon leftAlignmentIcon = new ImageIcon(getClass().getResource("/Imagem/esquerda.png"));
	private ImageIcon justifyAlignmentIcon = new ImageIcon(getClass().getResource("/Imagem/justificado.png"));

	// Icones estilos

	private ImageIcon boldIcon = new ImageIcon(getClass().getResource("/Imagem/negrito.png"));
	private ImageIcon italicIcon = new ImageIcon(getClass().getResource("/Imagem/italico.png"));
	private ImageIcon underlineIcon = new ImageIcon(getClass().getResource("/Imagem/sublinhado.png"));
	private ImageIcon superescriptIcon = new ImageIcon(getClass().getResource("/Imagem/superescrito.png"));
	private ImageIcon subscriptIcon = new ImageIcon(getClass().getResource("/Imagem/subscrito.png"));

	private boolean autoSubCheck = true;

	private BufferedImage bImage;

	Pagina paginaAux;

	// Definindo tela em
	// polegadas------------------------------------------------------

	GridBagConstraints gbc = new GridBagConstraints();

	private GridBagLayout bordeLayoutNorth0 = new GridBagLayout();
	private GridBagLayout bordeLayoutNorth1 = new GridBagLayout();
	private GridBagLayout bordeLayoutNorth2 = new GridBagLayout();
	private GridBagLayout bordeLayoutNorth3 = new GridBagLayout();

	private JPanelCustom panel0 = new JPanelCustom(bordeLayoutNorth0);
	private JPanelCustom panel1 = new JPanelCustom(bordeLayoutNorth1);
	private JPanelCustom panel2 = new JPanelCustom(bordeLayoutNorth2);
	private JPanelCustom panel3 = new JPanelCustom(bordeLayoutNorth3);

	private double INCH = 2.53999995; // Valor de uma polegada em centimetros

	private double pixelPerInch = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();

	private float pixelToPoint = (float) 72 / (float) pixelPerInch;

	// ----------------------------------------------------------------------------------

	// ComboBox
	private JComboBox typeFont;
	private String[] strTypeFont;

	private JComboBox boxFamilyFont;
	private String[] fonts;
	private JComboBox boxSizeFont;
	private String[] sizes;

	// Familia da fonte
	private SimpleAttributeSet familyFont = new SimpleAttributeSet();

	// Tamanho da fonte
	private SimpleAttributeSet sizeFont = new SimpleAttributeSet();

	private NewContentPane contentPane = new NewContentPane();

	private JPanel panelBorder = new JPanel();

	// Criando pagina
	private ArrayList<Pagina> pagina = new ArrayList<Pagina>();
	ArrayList<Pagina> paginasAux = new ArrayList<Pagina>();

	// Definindo JScrollPane
	private JScrollPane jScroll = new JScrollPane(contentPane);

	JanelaPrincipal() {

		super("e-Report");

		URL url = this.getClass().getResource("e_repLogo.jpg");
		Image imagemTitulo = Toolkit.getDefaultToolkit().getImage(url);
		this.setIconImage(imagemTitulo);

		contentPane.setPreferredSize(new Dimension(centimetersToPixels(21), ((centimetersToPixels(29.7) * 1 + 400))));

		pagina.add(new Pagina());

		try {
			this.bImage = ImageIO.read(new File("res/fundo.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Adicionando Grid Layout superior
		GridBagLayout gridLayoutNorth = new GridBagLayout();
		JPanelCustom panelGrid = new JPanelCustom(gridLayoutNorth);

		jScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScroll.setPreferredSize(new Dimension(new Dimension(centimetersToPixels(21), 600)));

		// Defindo padrao de texto para possiveis alteraÃ§Ãµes
		dsdAux = pagina.get(0).getDoc();
		docCheck.add(dsdAux);

		// Adicionando Barra de Menus
		setJMenuBar(menuBar);

		// Adicionando barra de menus
		JMenu fileMenu = new JMenu("Arquivo");
		JMenu editMenu = new JMenu("Estilos");
		JMenu alignementMenu = new JMenu("Alinhamentos");
		JMenu consultMenu = new JMenu("Glossario");
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(alignementMenu);
		menuBar.add(consultMenu);

		// Adicionando opÃ§Ãµes para os itens da barra de Menus
		// ------------File------------//
		JMenuItem newAction = new JMenuItem("Novo");
		JMenuItem openAction = new JMenuItem("Abrir");
		JMenuItem saveAction = new JMenuItem("Salvar Como");
		JMenuItem printAction = new JMenuItem("Imprimir");
		JMenuItem exportPDFAction = new JMenuItem("Exportar para PDF");
		JMenuItem exitAction = new JMenuItem("Sair");
		// ------------Style------------//
		JMenuItem boldAction = new JMenuItem("Negrito");
		JMenuItem ItalicAction = new JMenuItem("Italico");
		JMenuItem UnderlineAction = new JMenuItem("Sublinhado");
		JMenuItem SuperescriptAction = new JMenuItem("Superescrito");
		// ------------Alignement------------//
		JMenuItem centerAction = new JMenuItem("Centro");
		JMenuItem rightAction = new JMenuItem("Direita");
		JMenuItem leftAction = new JMenuItem("Esquerda");
		JMenuItem JustifiedAction = new JMenuItem("Justificado");
		//------------Materia------------//
		JMenuItem materia = new JMenuItem("Pesquisar");
		
		
		// Criando ComboBox

		GraphicsEnvironment gForFont = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fonts = gForFont.getAvailableFontFamilyNames();

		sizes = new String[] { "8", "9", "11", "12", "14", "16", "18", "24", "36", "42" };

		strTypeFont = new String[] { "Titulo", "SubTitulo", "Texto" };

		typeFont = new JComboBox(strTypeFont);
		boxFamilyFont = new JComboBox(fonts);
		boxSizeFont = new JComboBox(sizes);

		boxFamilyFont.setSelectedItem("Times New Roman");
		boxSizeFont.setSelectedItem("12");

		// Adicionando funÃ§Ãµes a comboBox

		typeFont.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (typeFont.getSelectedItem().toString().equals("Titulo")) {

					boxFamilyFont.setSelectedItem("Arial");
					boxSizeFont.setSelectedItem("16");

					StyleConstants.setFontFamily(familyFont, boxFamilyFont.getSelectedItem().toString());
					StyleConstants.setFontSize(sizeFont, Integer.parseInt(boxSizeFont.getSelectedItem().toString()));

					for (Pagina paginas : pagina) {
						paginas.setFamilyFont(familyFont);
						paginas.setFontSize(sizeFont);
						paginas.setStyle("Negrito");
					}
				}
				if (typeFont.getSelectedItem().toString().equals("SubTitulo")) {

					boxFamilyFont.setSelectedItem("Arial");
					boxSizeFont.setSelectedItem("14");

					StyleConstants.setFontFamily(familyFont, boxFamilyFont.getSelectedItem().toString());
					StyleConstants.setFontSize(sizeFont, Integer.parseInt(boxSizeFont.getSelectedItem().toString()));

					for (Pagina paginas : pagina) {
						paginas.setFamilyFont(familyFont);
						paginas.setFontSize(sizeFont);
						paginas.setStyle("Negrito");
					}
				}
				if (typeFont.getSelectedItem().toString().equals("Texto")) {

					boxFamilyFont.setSelectedItem("Arial");
					boxSizeFont.setSelectedItem("12");

					StyleConstants.setFontFamily(familyFont, boxFamilyFont.getSelectedItem().toString());
					StyleConstants.setFontSize(sizeFont, Integer.parseInt(boxSizeFont.getSelectedItem().toString()));

					for (Pagina paginas : pagina) {
						paginas.setFamilyFont(familyFont);
						paginas.setFontSize(sizeFont);
					}
				}

			}
		});

		boxFamilyFont.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				StyleConstants.setFontFamily(familyFont, (boxFamilyFont.getSelectedItem().toString()));

				for (Pagina paginas : pagina) {
					paginas.setFamilyFont(familyFont);
				}

			}

		});

		boxSizeFont.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				StyleConstants.setFontSize(sizeFont, Integer.parseInt(boxSizeFont.getSelectedItem().toString()));

				for (Pagina paginas : pagina) {
					paginas.setFontSize(sizeFont);
				}

			}

		});

		
		// Funções de Refazer e Desfazer
		/*Action undoAction = new UndoAction(manager);
		Action redoAction = new RedoAction(manager);
*/
		// BOTÃ•ES-------------------------------------------------------------------
		// Criando botÃµes

		JButton newButton = new JButton(newIcon);
		JButton openButton = new JButton(openIcon);
		JButton saveAsButton = new JButton(saveIcon);
		JButton printButton = new JButton(printIcon);
		JButton exportPDFButton = new JButton(pdfIcon);

		JButton autoSubButton = new JButton(subIcon);
		JButton negritoButton = new JButton(boldIcon);
		JButton italicoButton = new JButton(italicIcon);
		JButton sublinhadoButton = new JButton(underlineIcon);
		JButton superEscritoButton = new JButton(superescriptIcon);
		JButton subEscritoButton = new JButton(subscriptIcon);

		JButton centerButton = new JButton(CentertAlignmentIcon);
		JButton rightButton = new JButton(rightAlignmentIcon);
		JButton leftButton = new JButton(leftAlignmentIcon);
		JButton justifiedButton = new JButton(justifyAlignmentIcon);

		// Definindo espaçamento entre componentes
		gbc.insets = new Insets(0, 7, 0, 7);

		// Definindo tamanho dos botões

		newButton.setPreferredSize(new Dimension(24, 24));
		openButton.setPreferredSize(new Dimension(24, 24));
		saveAsButton.setPreferredSize(new Dimension(24, 24));
		printButton.setPreferredSize(new Dimension(24, 24));
		exportPDFButton.setPreferredSize(new Dimension(24, 24));

		autoSubButton.setPreferredSize(new Dimension(24, 24));
		negritoButton.setPreferredSize(new Dimension(24, 24));
		italicoButton.setPreferredSize(new Dimension(24, 24));
		sublinhadoButton.setPreferredSize(new Dimension(24, 24));
		superEscritoButton.setPreferredSize(new Dimension(24, 24));
		subEscritoButton.setPreferredSize(new Dimension(24, 24));

		centerButton.setPreferredSize(new Dimension(24, 24));
		rightButton.setPreferredSize(new Dimension(24, 24));
		leftButton.setPreferredSize(new Dimension(24, 24));
		justifiedButton.setPreferredSize(new Dimension(24, 24));

		// Adicionando funÃ§Ãµes nos botÃµes

		autoSubButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				autoSubCheck = !autoSubCheck;
				for (int i = 0; i < pagina.size(); i++) {
					pagina.get(i).setAutoSubCheck(autoSubCheck);
				}
			}

		});

		ActionListenerPopUp listenerUnd = new ActionListenerPopUp(pagina, "Sublinhado", 1);
		ActionListenerPopUp listenerBol = new ActionListenerPopUp(pagina, "Negrito", 1);
		ActionListenerPopUp listenerIta = new ActionListenerPopUp(pagina, "Italico", 1);
		ActionListenerPopUp listenerSup = new ActionListenerPopUp(pagina, "Superescrito", 1);
		ActionListenerPopUp listenerSub = new ActionListenerPopUp(pagina, "Subscrito", 1);

		negritoButton.addActionListener(listenerBol);
		sublinhadoButton.addActionListener(listenerUnd);
		italicoButton.addActionListener(listenerIta);
		superEscritoButton.addActionListener(listenerSup);

		ActionListenerPopUp listenerCenter = new ActionListenerPopUp(pagina, "Centro", 2);
		ActionListenerPopUp listenerRight = new ActionListenerPopUp(pagina, "Direita", 2);
		ActionListenerPopUp listenerLeft = new ActionListenerPopUp(pagina, "Esquerda", 2);
		ActionListenerPopUp listenerJustified = new ActionListenerPopUp(pagina, "Justificado", 2);

		centerButton.addActionListener(listenerCenter);

		rightButton.addActionListener(listenerRight);

		leftButton.addActionListener(listenerLeft);

		justifiedButton.addActionListener(listenerJustified);

		// FIM DE
		// BOTÃ•ES------------------------------------------------------------

		// Adicionando funÃ§Ãµes nas opÃ§Ãµes

		ActionListener listenerNew = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				boolean saved = true;

				for (int i = 0; i < pagina.size(); i++) {
					if (!(pagina.get(i).getDoc().equals(docCheck.get(i)))) {
						intEdicao = JOptionPane.showConfirmDialog(null, "Deseja salvar alteracoes?");

						if (intEdicao == JOptionPane.YES_OPTION) {
							salvarArquivo();
							novoArquivo();

						} else if (intEdicao == JOptionPane.NO_OPTION) {
							novoArquivo();
						}
						saved = false;
					}
				}
				if (saved)
					novoArquivo();
			}
		};

		ActionListener listenerOpen = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					carregaArquivo();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};

		ActionListener listenerSave = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				salvarArquivo();
			}
		};

		ActionListener listenerPrint = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					imprimirArquivo();
				} catch (PrinterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};

		ActionListener listenerPdfExport = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				exportarPDF();
			}
		};
		
		ActionListener listenerConsult = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
                Consuta dialog = new Consuta(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                    }
                });
                dialog.setVisible(true);
				
			}
			
			
		};

		newAction.addActionListener(listenerNew);
		openAction.addActionListener(listenerOpen);
		saveAction.addActionListener(listenerSave);
		printAction.addActionListener(listenerPrint);
		exportPDFAction.addActionListener(listenerPdfExport);

		boldAction.addActionListener(listenerBol);
		ItalicAction.addActionListener(listenerIta);
		UnderlineAction.addActionListener(listenerUnd);
		SuperescriptAction.addActionListener(listenerSup);
		subEscritoButton.addActionListener(listenerSub);

		rightAction.addActionListener(listenerRight);
		centerAction.addActionListener(listenerCenter);
		leftAction.addActionListener(listenerLeft);
		JustifiedAction.addActionListener(listenerJustified);

		newButton.addActionListener(listenerNew);
		openButton.addActionListener(listenerOpen);
		saveAsButton.addActionListener(listenerSave);
		printButton.addActionListener(listenerPrint);
		exportPDFButton.addActionListener(listenerPdfExport);

		materia.addActionListener(listenerConsult);
		
		autoSubButton.setPreferredSize(new Dimension(24, 24));
		
		// Adicionando itens para os itens da barra de Menus
		fileMenu.add(newAction);
		fileMenu.add(openAction);
		fileMenu.add(saveAction);
		fileMenu.add(printAction);
		fileMenu.addSeparator();
		fileMenu.add(exportPDFAction);
		fileMenu.addSeparator();
		fileMenu.add(exitAction);

		editMenu.add(boldAction);
		editMenu.add(ItalicAction);
		editMenu.add(UnderlineAction);
		editMenu.add(SuperescriptAction);

		alignementMenu.add(rightAction);
		alignementMenu.add(centerAction);
		alignementMenu.add(leftAction);
		alignementMenu.add(JustifiedAction);

		consultMenu.add(materia);
		
		// Setando layout do JFrame
		getContentPane().add(panelBorder);

		// Adicionando botÃµes
		panel0.add(newButton, gbc);
		panel0.add(openButton, gbc);
		panel0.add(saveAsButton, gbc);
		panel0.add(printButton, gbc);
		panel0.add(exportPDFButton, gbc);

		panel1.add(typeFont, gbc);
		panel1.add(boxFamilyFont, gbc);
		panel1.add(boxSizeFont, gbc);

		panel2.add(autoSubButton);
		panel2.add(negritoButton);
		panel2.add(sublinhadoButton);
		panel2.add(italicoButton);
		panel2.add(superEscritoButton);
		panel2.add(subEscritoButton);

		panel3.add(leftButton, gbc);
		panel3.add(centerButton, gbc);
		panel3.add(rightButton, gbc);
		panel3.add(justifiedButton, gbc);

		panel0.setBorder(javax.swing.BorderFactory.createTitledBorder("Arquivo"));

		panel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo e Altura Fonte"));

		panel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Estilo"));

		panel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Alinhamento"));

		panelGrid.add(panel0);
		panelGrid.add(panel1);
		panelGrid.add(panel2);
		panelGrid.add(panel3);

		// panelGrid.setPreferredSize(new Dimension(1000, 75));

		add(jScroll, BorderLayout.CENTER);

		// Adicionando layout superior
		add(panelGrid, BorderLayout.NORTH);
		
		
		// Adicionando TextPane no centro do Layout
		contentPane.add(pagina.get(0).getTextPane());

		// Insere no tamanho atual e na fonte atual------------
		StyleConstants.setFontFamily(familyFont, (boxFamilyFont.getSelectedItem().toString()));
		StyleConstants.setFontSize(sizeFont, Integer.parseInt(boxSizeFont.getSelectedItem().toString()));

		pagina.get(0).getDoc().setCharacterAttributes(0, 1, familyFont, false);
		pagina.get(0).getDoc().setCharacterAttributes(0, 1, sizeFont, false);

		Font font = new Font("Times New Roman", 12, 12);

		pagina.get(0).getTextPane().setFont(font);
		// ----------------------------------------------------

		// Definindo propriedades do componente principal

		setSize(new Dimension(1024, 720));

		setLocationRelativeTo(null);

		setVisible(true);
		this.setExtendedState(MAXIMIZED_BOTH);

		Tutorial tutorial = new Tutorial();

		// setResizable(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Adicionando descrição nos botoes
		newButton.setToolTipText("Novo");
		openButton.setToolTipText("Abrir");
		saveAsButton.setToolTipText("Salvar Como");
		printButton.setToolTipText("Imprimir");
		exportPDFButton.setToolTipText("Exportar para PDF");

		autoSubButton.setToolTipText("Auto subscrito");
		negritoButton.setToolTipText("Negrito");
		italicoButton.setToolTipText("Italico");
		sublinhadoButton.setToolTipText("Sublinhado");
		subEscritoButton.setToolTipText("Subescrito");
		superEscritoButton.setToolTipText("Superescrito");
		
		this.run();

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	// FUNÇÕES I/O
	// ARQUIVOS----------------------------------------------------------------------
	// Função que salva arquivos
	void salvarArquivo() {

		// seta para selecionar apenas arquivos
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// desabilita todos os tipos de arquivos
		jFileChooser.setAcceptAllFileFilterUsed(false);

		jFileChooser.setFileFilter(new ExtensionFileFilter("e-Report", "esr"));

		// mostra janela para salvar
		int acao = jFileChooser.showSaveDialog(null);

		// executa acao conforme opção selecionada
		if (acao == JFileChooser.APPROVE_OPTION) {

			String nomeArq;

			// Escolheu arquivo
			System.out.println(jFileChooser.getSelectedFile().getAbsolutePath());
			try {

				// Verificando se o arquivo termina em txt
				if (jFileChooser.getSelectedFile().getAbsolutePath().endsWith(".esr")) {

					// Definindo nome do arquivo
					nomeArq = (jFileChooser.getSelectedFile().getAbsolutePath());

					// Verificando se o arquivo ja existe
					verificaExistencia(nomeArq);

				} else {

					// Definindo nome do arquivo
					nomeArq = (jFileChooser.getSelectedFile().getAbsolutePath() + ".esr");

					// Verificando se o arquivo ja existe
					verificaExistencia(nomeArq);

				}

				// Se existir
				if (existencia) {

					intEdicao = JOptionPane.showConfirmDialog(null, "Este arquivo ja existe, deseja substitui-lo?");
					if (intEdicao == JOptionPane.YES_OPTION)
						criarArquivo(nomeArq);
					else if (intEdicao == JOptionPane.NO_OPTION)
						salvarArquivo();
				} else {
					criarArquivo(nomeArq);
				}

			} catch (Exception erro) {
				JOptionPane.showMessageDialog(null, "Arquivo nao pode ser gerado!", "Erro", 0);
			}
		} else if (acao == JFileChooser.CANCEL_OPTION) {
			// apertou botao cancelar
		} else if (acao == JFileChooser.ERROR_OPTION) {
			// outra opcao
		}
	}

	// Função que imprime o arquivo
	void imprimirArquivo() throws PrinterException {

		PrinterJob job = PrinterJob.getPrinterJob();

		PrintableClass pg = new PrintableClass((int) centimetersToPostscript(29.7), (int) centimetersToPostscript(21),
				pagina);

		System.out.println(centimetersToPostscript(29.7) + " " + centimetersToPostscript(21));

		job.setPrintable(pg);
		job.setPageable(pg);

		boolean ok = job.printDialog();
		if (ok) {
			for (int i = 0; i < pagina.size(); i++) {
				pagina.get(i).saveHighlighters();
				pagina.get(i).removeSelections();
			}

			pagina.get(0).setPaintRectTextPane(false);
			try {
				job.print();
			} catch (PrinterException ex) {
				/* The job did not successfully complete */
			}
			pagina.get(0).setPaintRectTextPane(true);
			for (int i = 0; i < pagina.size(); i++) {
				pagina.get(i).loadHighlighters();
				pagina.get(i).getTextPane().getCaret().setVisible(true);
			}
		}
		// textPane.print();
	}

	// Função que verifica se o arquivo ja existe
	void verificaExistencia(String path) {
		try {
			BufferedReader buffRead = new BufferedReader(new FileReader(path));
			existencia = true;
		} catch (IOException e) {
			existencia = false;
		}
	}

	// Função que carrega um arquivo
	void carregaArquivo() throws IOException, ClassNotFoundException {
		// seta para selecionar apenas arquivos
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// desabilita todos os tipos de arquivos
		jFileChooser.setAcceptAllFileFilterUsed(false);

		jFileChooser.setFileFilter(new ExtensionFileFilter("e-Report", "esr"));

		// mostra janela para salvar
		int acao = jFileChooser.showOpenDialog(null);

		// executa acao conforme opção selecionada
		if (acao == JFileChooser.APPROVE_OPTION) {
			// escolheu arquivo

			try {

				// deserializando objeto

				FileInputStream novo = new FileInputStream(jFileChooser.getSelectedFile().getAbsolutePath());
				ObjectInputStream obj = new ObjectInputStream(novo);

				Object texto = obj.readObject();
				ArrayList<ClassToSave> auxDoc = (ArrayList<ClassToSave>) texto;

				// Criando novo arquivo

				for (int i = 0; i < pagina.size(); i++) {
					pagina.get(0).decressCount();
					contentPane.remove(0);
					pagina.remove(0);
					docCheck.remove(0);

				}

				// Definindo textPane
				for (int i = 0; i < auxDoc.size(); i++) {
					pagina.add(new Pagina());
					pagina.get(i).setDoc(auxDoc.get(i).getDoc());

					pagina.get(i).setImagens(auxDoc.get(i).getImagens());
					pagina.get(i).getTextPane().setImagens(pagina.get(i).getImagens());

					for (int j = 0; j < pagina.get(i).getImagens().size(); j++) {
						pagina.get(i).getImagens().get(j).createImage();
						pagina.get(i).getTextPane().repaint();

					}

					contentPane.add(pagina.get(i).getTextPane(), gbcToPages);

					// Adicionando variavel de verificação para alteraÃ§Ãµes
					// no documento
					dsdAux.setDocumentProperties(pagina.get(i).getDoc().getDocumentProperties());
					docCheck.add(dsdAux);
				}

				int paginas = (pagina.size() - 1);

				pagina.get(paginas).getTextPane().setCaretPosition(pagina.get(paginas).getDoc().getLength());

				jScroll.revalidate();
				jScroll.repaint();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Esse arquivo nao existe");
			}
		} else if (acao == JFileChooser.CANCEL_OPTION) {
			// apertou botao cancelar
		} else if (acao == JFileChooser.ERROR_OPTION) {
			// outra opcao
		}

	}

	// Função que cria arquivos
	void criarArquivo(String path) throws IOException {

		ArrayList<ClassToSave> classToSave = createArrayOfDocAndImages();

		// Serializando objeto
		FileOutputStream fs = new FileOutputStream(path);
		ObjectOutputStream os = new ObjectOutputStream(fs);

		os.writeObject(classToSave);
		os.close();
		fs.close();

	}
	
	int centimetersToPixels(double cm) {

		double inchAux;

		inchAux = cm / INCH;
		return (int) (pixelPerInch * inchAux);

	}

	float centimetersToPostscript(double cm) {

		double inchAux;

		inchAux = cm / INCH;
		return (float) (inchAux * 72);

	}

	// Função que exporta para PDF
	void exportarPDF() {

		// seta para selecionar apenas arquivos
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// desabilita todos os tipos de arquivos
		jFileChooser.setAcceptAllFileFilterUsed(false);

		jFileChooser.setFileFilter(new ExtensionFileFilter("PDF", "pdf"));

		// mostra janela para salvar
		int acao = jFileChooser.showSaveDialog(null);

		// executa acao conforme opção selecionada
		if (acao == JFileChooser.APPROVE_OPTION) {
			// escolheu arquivo
			System.out.println(jFileChooser.getSelectedFile().getAbsolutePath());
			try {

				// Verificando se o arquivo termina em pdf
				if (jFileChooser.getSelectedFile().getAbsolutePath().endsWith(".pdf")) {
					String nomeArq;

					// Definindo nome do arquivo
					nomeArq = (jFileChooser.getSelectedFile().getAbsolutePath());

					// Verificando se o arquivo ja existe
					verificaExistencia(nomeArq);

				} else {

					String nomeArq;

					// Definindo nome do arquivo
					nomeArq = (jFileChooser.getSelectedFile().getAbsolutePath() + ".pdf");

					// Verificando se o arquivo ja existe
					verificaExistencia(nomeArq);

				}

				// Se existir
				if (existencia) {
					intEdicao = JOptionPane.showConfirmDialog(null, "Este arquivo ja existe, deseja substitui-lo?");
					if (intEdicao == JOptionPane.YES_OPTION)
						criarArquivoPDF();
					else if (intEdicao == JOptionPane.NO_OPTION)
						salvarArquivo();
				} else {
					System.out.println("Aqui");
					criarArquivoPDF();
				}

			} catch (Exception erro) {
				JOptionPane.showMessageDialog(null, "Arquivo nao pode ser gerado!", "Erro", 0);
			}
		} else if (acao == JFileChooser.CANCEL_OPTION) {
			// apertou botao cancelar
		} else if (acao == JFileChooser.ERROR_OPTION) {
			// outra opcao
		}
	}

	// Criando arquivo em PDF
	void criarArquivoPDF() throws IOException, DocumentException {

		pagina.get(0).setPaintRectTextPane(false);

		// Removendo seleções do textPane
		for (Pagina pagina : pagina) {
			pagina.removeSelections();
			pagina.saveHighlighters();
		}

		// Serializando objeto

		Pagina.MyTextPane textPaneAux;

		Document document = new Document();
		FileOutputStream fs;

		if (jFileChooser.getSelectedFile().getAbsolutePath().endsWith(".pdf"))
			fs = new FileOutputStream(jFileChooser.getSelectedFile().getAbsolutePath());
		else
			fs = new FileOutputStream(jFileChooser.getSelectedFile().getAbsolutePath() + ".pdf");

		PdfWriter writer = PdfWriter.getInstance(document, fs);

		document.setPageSize(PageSize.A4);

		document.open();

		PdfContentByte cb = writer.getDirectContent();

		DefaultFontMapper mapper = new DefaultFontMapper();
		mapper.insertDirectory("c:/windows/fonts");

		for (int i = 0; i < pagina.size(); i++) {

			cb.saveState();
			cb.concatCTM(1, 0, 0, 1, 0, 0);

			document.setPageSize(PageSize.A4);

			textPaneAux = pagina.get(i).getTextPane();

			Graphics2D g2 = cb.createGraphics(centimetersToPostscript(21), centimetersToPostscript(29.7), mapper, true, .95f);
			
			AffineTransform at = new AffineTransform();
		    at.scale(pixelToPoint, pixelToPoint);

			g2.transform(at);
		    
			textPaneAux.paint(g2);
			
			g2.dispose();

			cb.restoreState();
			System.out.println(";-;");

			if (i != pagina.size() - 1) {
				document.newPage();
			}
		}

		// cb.restoreState();

		document.close();
		fs.flush();
		fs.close();

		pagina.get(0).setPaintRectTextPane(true);

		JOptionPane.showMessageDialog(null, "PDF gerado com sucesso!");

		for (int i = 0; i < pagina.size(); i++) {
			pagina.get(i).loadHighlighters();
			pagina.get(i).getTextPane().getCaret().setVisible(true);
		}

	}

	public float convertToPoints(int pixels) {
		return (float) (pixels * pixelToPoint);
	}

	public float convertToPixels(int points) {
		return (float) (points / pixelToPoint);
	}

	protected Rectangle getVisibleEditorRect(JTextPane ta) {
		Rectangle alloc = ta.getBounds();
		if ((alloc.width > 0) && (alloc.height > 0)) {
			alloc.x = alloc.y = 0;
			Insets insets = ta.getInsets();
			alloc.x += insets.left;
			alloc.y += insets.top;
			alloc.width -= insets.left + insets.right;
			alloc.height -= insets.top + insets.bottom;
			return alloc;
		}
		return null;
	}

	// Função que cria nova pagina
	private synchronized void newPage() {

		contentPane.add(Box.createRigidArea(new Dimension(this.getSize().width,50)));
		
		paginaAux = new Pagina();

		synchronized (paginaAux) {
			paginaAux.notify();
		}
		
		pagina.add(paginaAux);

		// Adicionando variavel de verificação para alterações no documento
		docCheck.add(new DefaultStyledDocument());

		dsdAux.setDocumentProperties(pagina.get(pagina.size() - 1).getDoc().getDocumentProperties());
		docCheck.add(dsdAux);

		contentPane.setPreferredSize(
				new Dimension(1024, ((centimetersToPixels(29.7) * pagina.size()) + ((60) * pagina.size()) + 10)));

		// Pintando background
		contentPane.add(pagina.get(pagina.size() - 1).getTextPane());

		// transferindo foco para proximo textPane
		pagina.get(pagina.size() - 1).getTextPane().grabFocus();
		pagina.get(pagina.size() - 1).getTextPane().setFocusable(true);

		jScroll.revalidate();
		jScroll.repaint();

		contentPane.repaint();

	}

	void novoArquivo() {

		for (int i = 1; i < pagina.size(); i++)
			pagina.remove(1);

		pagina.get(0).getTextPane().setText("");
		pagina.get(0).setImagens(new ArrayList<Imagem>());

		pagina.get(0).getTextPane().setImagens(new ArrayList<Imagem>());

	}


	private ArrayList<ClassToSave> createArrayOfDocAndImages() {
		ArrayList<ClassToSave> classToSave = new ArrayList<ClassToSave>();

		pagina.get(0).setPaintRectTextPane(false);

		for (int i = 0; i < pagina.size(); i++) {
			classToSave.add(new ClassToSave());
			classToSave.get(i).addDoc(pagina.get(i).getDoc());
			classToSave.get(i).addImagens(pagina.get(i).getImagens());
		}

		pagina.get(0).setPaintRectTextPane(true);

		return classToSave;
	}

	private class NewContentPane extends JPanel {

		protected void paintComponent(final Graphics g) {

			/*AffineTransform at = new AffineTransform();
			at.scale(1.3, 1.3);
			at.translate(-150, 0);
	
			g2d.transform(at);
*/
			Graphics2D g2d = (Graphics2D) g;

			super.paintComponent(g2d);

			// Definindo ponto de desenho do background
			/*
			 * int backgroundX = pagina.size() - 1; int backgroundY =
			 * pagina.size() - 1;
			 * 
			 * int width = (int)
			 * jScroll.getViewport().getVisibleRect().getWidth(); int height =
			 * (int) jScroll.getViewport().getVisibleRect().getHeight();
			 * 
			 * System.out.println(backgroundX + " " + backgroundY);
			 * 
			 * g.setColor(new Color(193, 245, 255)); g.fillRect(0, (int)
			 * (jScroll.getViewport().getViewPosition().getY()), width, height +
			 * 100);
			 */

			g2d.drawImage(bImage, 0, (int) ((jScroll.getViewport().getViewPosition().getY())),
					(int) (jScroll.getViewport().getVisibleRect().getWidth()),
					(int) (jScroll.getViewport().getVisibleRect().getHeight() ), this);

		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

			}

			int sizePagina = pagina.size();
			boolean generatePage = false;
			boolean nextPageExists = false;

			if(Pagina.selectAllPages){
				for(Pagina pagina: pagina){
					pagina.getTextPane().setSelectionStart(0);
					pagina.getTextPane().setSelectionEnd(pagina.getDoc().getLength());
				}
			}
			
			for (int i = 0; i < sizePagina; i++) {

				if (pagina.get(i).getNewPage()) {
					System.out.println("Pagina criada");

					newPage();
					synchronized (paginaAux) {
						paginaAux.notify();
					}

					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {

					}
					jScroll.getVerticalScrollBar().setValue(
							((centimetersToPixels(29.7) * (pagina.size() - 1)) + ((60) * (pagina.size() - 1)) - 80));

				}

				if (pagina.get(i).getTextForNextPage()) {

					try {
						pagina.get(i + 1);
						nextPageExists = true;
					} catch (IndexOutOfBoundsException e) {
						newPage();
						nextPageExists = false;

						synchronized (paginaAux) {
							paginaAux.notify();
						}

					}

					pagina.get(i + 1).getTextPane().grabFocus();
					pagina.get(i + 1).getTextPane().setCaretPosition(0);

					// Adicionando texto a proxima pagina
					addTextToNextPage(i);

					System.out.println(pagina.get(i + 1).getDoc().getLength() - 1);

					if (!nextPageExists)
						if (pagina.get(i + 1).verifyTextLocation(pagina.get(i + 1).getDoc().getLength() - 1)) {
							sizePagina++;
						}

					pagina.get(i).setTextForNextPage(false);

				}

				if (!generatePage)
					if (pagina.get(i).getToNextPage()) {

						System.out.println("TROLLADo");
						newPage();
						pagina.get(i).setToNextPage(false);

					}

				pagina.get(i).setNewPage(false);
				generatePage = false;

				if (pagina.get(i).isDeletePage()) {
					copyPostRemovePage(i);
					contentPane.remove(i*2);
					contentPane.remove(pagina.get(i).getTextPane());
					pagina.remove(i);
					
					
					// Corrigindo Index das Paginas
					for (int j = 0; j < pagina.size(); j++){
						pagina.get(j).setIndex(j);
					System.out.println("j:" + j);
					}
						
					pagina.get(i - 1).verifyTextLocation(pagina.get(i - 1).getDoc().getLength() - 1);
					
					i--;
					sizePagina--;
					


					if (i == pagina.size() - 1) {
						// Redesenhando janela
						contentPane.setPreferredSize(new Dimension(1024,
								((centimetersToPixels(29.7) * pagina.size()) + ((325) * pagina.size()))));
						
						jScroll.getVerticalScrollBar().setValue(centimetersToPixels(29.7) * i + ((60) * i) - 80);
						jScroll.revalidate();
						jScroll.repaint();
						this.repaint();
					}
				}
			}
			contentPane.repaint();

		}
	}

	public synchronized void addTextToNextPage(int i) {
		
		ArrayList<Element> elementos;

		try {
			elementos = pagina.get(i).saveElementsToNextPage();

			String text = "";
			try {
				text = pagina.get(i).saveTextForNextPage();
			} catch (BadLocationException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			for (int k = 0; k < elementos.size(); k++) {

				String aux = Character.toString(text.charAt(k));

				try {
					pagina.get(i + 1).getDoc().insertString(k, aux, elementos.get(k).getAttributes());
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					try {
						pagina.get(i + 1).getDoc().insertString(k, aux, null);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					e.printStackTrace();
				}

			}
			pagina.get(i + 1).setABNT(0, pagina.get(i + 1).getDoc().getLength());

		} catch (BadLocationException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

	}

	public synchronized void copyPostRemovePage(int i) {

		ArrayList<Element> elements = new ArrayList<Element>();
		DefaultStyledDocument docOfPage = pagina.get(i).getDoc();
		DefaultStyledDocument docOfPageToIns = pagina.get(i - 1).getDoc();

		String text = pagina.get(i).getTextPane().getText();

		for (int k = 0; k < text.length(); k++) {

			elements.add(docOfPage.getCharacterElement(k));

			try {
				docOfPageToIns.insertString(docOfPageToIns.getLength(), Character.toString(text.charAt(k)),
						elements.get(k).getAttributes());
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				try {
					docOfPageToIns.insertString(docOfPageToIns.getLength(), Character.toString(text.charAt(k)), null);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}
		
		pagina.get(i-1).getTextPane().setCaretPosition(text.length());

	}

}
