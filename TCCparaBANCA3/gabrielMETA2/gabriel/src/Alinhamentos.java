import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Alinhamentos {

	// Alinhamento de texto
	SimpleAttributeSet estiloCentro = new SimpleAttributeSet();
	SimpleAttributeSet estiloDireita = new SimpleAttributeSet();
	SimpleAttributeSet estiloEsquerda = new SimpleAttributeSet();
	SimpleAttributeSet estiloJustificado = new SimpleAttributeSet();
	SimpleAttributeSet espacamento = new SimpleAttributeSet();
	

	Alinhamentos() {

		// Estilos de alinhamento
		StyleConstants.setAlignment(estiloCentro, StyleConstants.ALIGN_CENTER);
		StyleConstants.setAlignment(estiloDireita, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setAlignment(estiloEsquerda, StyleConstants.ALIGN_LEFT);
		StyleConstants.setAlignment(estiloJustificado, StyleConstants.ALIGN_JUSTIFIED);
		StyleConstants.setLineSpacing(espacamento, (float)1.5);

	}

	public SimpleAttributeSet getEstiloCentro() {
		return estiloCentro;
	}

	public SimpleAttributeSet getEstiloDireita() {
		return estiloDireita;
	}

	public SimpleAttributeSet getEstiloEsquerda() {
		return estiloEsquerda;
	}

	public SimpleAttributeSet getEstiloJustificado() {
		return estiloJustificado;
	}

	public SimpleAttributeSet getEspacamento() {
		return espacamento;
	}
	

}
