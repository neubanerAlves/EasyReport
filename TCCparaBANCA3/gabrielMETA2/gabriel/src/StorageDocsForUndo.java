import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

public class StorageDocsForUndo {
	Estilos estilos = new Estilos();

	ArrayList<StyledDocument> document = new ArrayList<StyledDocument>();
	int position = 0;

	public void addDoc(StyledDocument document, int aux) {

		//Para saber se vai criar nova posicao ou sobrescrever
		position = position+aux;
		
		//Se a posicao nao existir cria uma nova e se existir apaga
		
		if (position == this.document.size())
			this.document.add(new DefaultStyledDocument(estilos.getSC()));
		else
			try {
				this.document.get(position).remove(0, this.document.get(position).getLength());
			} catch (BadLocationException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		
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
				this.document.get(position).insertString(i, Character.toString(text.charAt(i)),
						element.getAttributes());
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(aux==0)
			position++;
	}

	public Document returnAction() {
		if (position > 0)
			return this.document.get(--position);
		else
			Toolkit.getDefaultToolkit().beep();

		System.out.println(position);

		return this.document.get(position);
	}

	public Document remakeAction() {
		System.out.println(this.document.size() - 1);
		if (position < this.document.size() - 1)
			return this.document.get(++position);
		else
			Toolkit.getDefaultToolkit().beep();

		return this.document.get(position);
	}
}
