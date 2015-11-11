import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.text.DefaultStyledDocument;

public class ClassToSave implements Serializable{

	DefaultStyledDocument doc = new DefaultStyledDocument();
	ArrayList<Imagem> imagens = new ArrayList<Imagem>();
	
	public ClassToSave(){
		
	}
	
	void addDoc(DefaultStyledDocument doc){
		this.doc = doc;
	}
	
	void addImagens(ArrayList<Imagem> imagens){
		this.imagens = imagens;
	}

	public DefaultStyledDocument getDoc() {
		return doc;
	}

	public ArrayList<Imagem> getImagens() {
		return imagens;
	}
	
	
	
}
