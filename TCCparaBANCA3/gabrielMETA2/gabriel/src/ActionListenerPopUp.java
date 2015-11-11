import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ActionListenerPopUp implements ActionListener {

	private ActionListener listener;
	private ArrayList<Pagina> paginas = new ArrayList<Pagina>();
	private final String attr;
	private int type;

	// type define qual tipo de listener sera criado
	ActionListenerPopUp(ArrayList<Pagina> paginas, String attr, int type) {

		this.paginas = paginas;
		this.attr = attr;
		this.type = type;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (type == 1)
			for (Pagina pagina : paginas) {
				pagina.setStyle(attr);
			}
		if(type == 2){
			for (Pagina pagina : paginas) {
				pagina.setAlignement(attr);
			}
		}
	}

}
