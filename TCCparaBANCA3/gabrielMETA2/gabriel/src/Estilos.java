import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;



public class Estilos {
	
	private StyleContext sc = new StyleContext();
	
	// Estilos
	private final Style estiloSubscrito = sc.addStyle("autoSubscrito", null);
	private final Style estiloSubscritoOff = sc.addStyle("autoSubscritoOff", null);
	private final Style estiloNegrito = sc.addStyle("neg", null);
	private final Style estiloNegritoOff = sc.addStyle("negOff", null);
	private final Style estiloSublinhado = sc.addStyle("sub", null);
	private final Style estiloSublinhadoOff = sc.addStyle("subOff", null);
	private final Style estiloItalico = sc.addStyle("ita", null);
	private final Style estiloItalicoOff = sc.addStyle("itaOff", null);
	private final Style estiloSuperscrito = sc.addStyle("sup", null);
	private final Style estiloSuperscritoOff = sc.addStyle("supOff", null);
	

	
	Estilos(){
		
		// Criando estilos
				// Estilos de fonte
				estiloSubscrito.addAttribute(StyleConstants.Subscript, new Boolean(
						true));
				estiloSubscritoOff.addAttribute(StyleConstants.Subscript, new Boolean(
						false));
				estiloNegrito.addAttribute(StyleConstants.Bold, new Boolean(true));
				estiloNegritoOff.addAttribute(StyleConstants.Bold, new Boolean(false));
				estiloItalico.addAttribute(StyleConstants.Italic, new Boolean(true));
				estiloItalicoOff
						.addAttribute(StyleConstants.Italic, new Boolean(false));
				estiloSublinhado.addAttribute(StyleConstants.Underline, new Boolean(
						true));
				estiloSublinhadoOff.addAttribute(StyleConstants.Underline, new Boolean(
						false));
				estiloSuperscrito.addAttribute(StyleConstants.Superscript, new Boolean(
						true));
				estiloSuperscritoOff.addAttribute(StyleConstants.Superscript, new Boolean(
						false));
				
				System.out.println("Estilos criados");
		
	}


	public Style getEstiloSubscrito() {
		return estiloSubscrito;
	}

	

	public Style getEstiloSubscritoOff() {
		return estiloSubscritoOff;
	}


	public Style getEstiloNegrito() {
		return estiloNegrito;
	}


	public Style getEstiloNegritoOff() {
		return estiloNegritoOff;
	}


	public Style getEstiloSublinhado() {
		return estiloSublinhado;
	}


	public Style getEstiloSublinhadoOff() {
		return estiloSublinhadoOff;
	}


	public Style getEstiloItalico() {
		return estiloItalico;
	}


	public Style getEstiloItalicoOff() {
		return estiloItalicoOff;
	}
	
	public StyleContext getSC(){
		return sc;
	}


	public Style getEstiloSuperscrito() {
		return estiloSuperscrito;
	}


	public Style getEstiloSuperscritoOff() {
		return estiloSuperscritoOff;
	}
	
	
}
