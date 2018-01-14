	package whiteboard_client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MyMouseAdapter extends MouseAdapter  {


	private Panneau panel;
	
	public MyMouseAdapter(Panneau panel) {
		this.panel = panel;
		
	}
	
	public void mousePressed(MouseEvent e) { // Si on clique ...
		panel.oldX = e.getX(); // on enregistre les cordonnees
		panel.oldY = e.getY();
	}

	public void mouseReleased(MouseEvent e) { // Si on declique...
		if (Main.select.equals("circle"))
				sendCircleToServer(e);

		else if(Main.select.equals("text"))
				sendTextToServer(e);

		else if (Main.select.equals("rectangle"))
				sendRectangleToServer(e);

		else if (Main.select.equals("fillCircle"))
				sendFillCircleToServer(e);
		
		else if (Main.select.equals("fillRectangle"))
				sendFillRectangleToServer(e);
		
		//else System.err.println("Select not recognised: "+Main.select);

	}

	private void sendFillRectangleToServer(MouseEvent e) {
		
		
		
		panel.currentX = e.getX(); // Si on d�clique du gauche ( normal )
		panel.currentY = e.getY();
		panel.Long = Math.abs(panel.currentX - panel.oldX); // On r�cup�re les coordonn�es,
		panel.Larg = Math.abs(panel.currentY - panel.oldY); // on calcule la longueur et la largeur en cons�quence

		if ((panel.currentX > panel.oldX) && (panel.currentY > panel.oldY)) {
			panel.x1_toSend = panel.oldX;
			panel.y1_toSend = panel.oldY;
		}
		else if ((panel.currentX > panel.oldX) && (panel.currentY < panel.oldY)) {
			panel.x1_toSend = panel.oldX;
			panel.y1_toSend = panel.oldY - panel.Larg;
		}
		else if ((panel.currentX < panel.oldX) && (panel.currentY > panel.oldY)) {
			panel.x1_toSend = panel.oldX - panel.Long;
			panel.y1_toSend = panel.oldY;
		}
		else if ((panel.currentX < panel.oldX) && (panel.currentY < panel.oldY)) {
			panel.x1_toSend = panel.oldX - panel.Long;
			panel.y1_toSend = panel.oldY - panel.Larg;
		}

		if (panel.graphics2D != null) {

			String x1_toSend_str = Panneau.remiseEnForme(panel.x1_toSend);
			String y1_toSend_str = Panneau.remiseEnForme(panel.y1_toSend);
			String Larg_str = Panneau.remiseEnForme(panel.Larg);
			String Long_str = Panneau.remiseEnForme(panel.Long);
			System.out.println("FR" + x1_toSend_str + y1_toSend_str + Larg_str + Long_str+getColorId());
			try {
				Main.output.writeUTF("FR" + x1_toSend_str + y1_toSend_str + Larg_str + Long_str+ getColorId());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	

	private void sendFillCircleToServer(MouseEvent e) {
		
		panel.currentX = e.getX();
		panel.currentY = e.getY();

		

		if (panel.graphics2D != null) {
			// draw line if g2 context not null
			int castRayon = (int) getRadius();

			String oldX_str = Panneau.remiseEnForme(panel.oldX); // Chaque entier doit �tre cod� sur 3

			String oldY_str = Panneau.remiseEnForme(panel.oldY); // car c'est le protocole qu'on a d�fini

			String castRayon_str = Panneau.remiseEnForme(castRayon);
			System.out.println("FC" + oldX_str + oldY_str + castRayon_str + castRayon_str+ getColorId());
			try {
				Main.output.writeUTF("FC" + oldX_str + oldY_str + castRayon_str + castRayon_str+ getColorId());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// refresh draw area to repaint
			this.panel.repaint();
			// store current coords x,y as olds x,y
			panel.oldX = panel.currentX;
			panel.oldY = panel.currentY;
		}
	}

	private void sendRectangleToServer(MouseEvent e) {
		panel.currentX = e.getX(); // Si on d�clique du gauche ( normal )
		panel.currentY = e.getY();
		panel.Long = Math.abs(panel.currentX - panel.oldX); // On r�cup�re les coordonn�es,
		panel.Larg = Math.abs(panel.currentY - panel.oldY); // on calcule la longueur et la largeur en cons�quence

		if ((panel.currentX > panel.oldX) && (panel.currentY > panel.oldY)) {
			panel.x1_toSend = panel.oldX;
			panel.y1_toSend = panel.oldY;
		}
		if ((panel.currentX > panel.oldX) && (panel.currentY < panel.oldY)) {
			panel.x1_toSend =panel.oldX;
			panel.y1_toSend = panel.oldY - panel.Larg;
		}
		if ((panel.currentX < panel.oldX) && (panel.currentY > panel.oldY)) {
			panel.x1_toSend = panel.oldX - panel.Long;
			panel.y1_toSend = panel.oldY;
		}
		if ((panel.currentX < panel.oldX) && (panel.currentY < panel.oldY)) {
			panel.x1_toSend = panel.oldX - panel.Long;
			panel.y1_toSend = panel.oldY - panel.Larg;
		}

		if (panel.graphics2D != null) {

			String x1_toSend_str = Panneau.remiseEnForme(panel.x1_toSend);
			String y1_toSend_str = Panneau.remiseEnForme(panel.y1_toSend);
			String Larg_str = Panneau.remiseEnForme(panel.Larg);
			String Long_str = Panneau.remiseEnForme(panel.Long);
			System.out.println("DR" + x1_toSend_str + y1_toSend_str + Larg_str + Long_str+ getColorId());
			 
			
			try {
				Main.output.writeUTF("DR" + x1_toSend_str + y1_toSend_str + Larg_str + Long_str+ getColorId());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		
	}

	private void sendTextToServer(MouseEvent e) {
		//TODO envoi du protocole !!!
		//panel.drawText(e.getX(),e.getY());	
		
		
		try {
			Main.output.writeUTF("DT" + Panneau.remiseEnForme(e.getX()) +Panneau.remiseEnForme(e.getY())+ "000000" + Window.textField.getText() + getColorId());
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

	private void sendCircleToServer(MouseEvent e) {
		
		panel.currentX = e.getX();
		panel.currentY = e.getY();
			
		if (panel.graphics2D != null) {
			// draw line if g2 context not null
			int castRayon = (int) getRadius();

			String oldX_str = Panneau.remiseEnForme(panel.oldX); // Chaque entier doit �tre cod� sur 3

			String oldY_str = Panneau.remiseEnForme(panel.oldY); // car c'est le protocole qu'on a d�fini

			String castRayon_str = Panneau.remiseEnForme(castRayon);
			System.out.println("DC" + oldX_str + oldY_str + castRayon_str + castRayon_str+ getColorId());
			
			
			try {
				Main.output.writeUTF("DC" + oldX_str + oldY_str + castRayon_str + castRayon_str+ getColorId());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			/* 
			// refresh draw area to repaint
			panel.repaint();
			// store current coords x,y as olds x,y
			panel.oldX = panel.currentX;
			panel.oldY = panel.currentY;*/
		}
		
	}

	private double getRadius() {
		return Math.sqrt(Math.pow(panel.currentX - panel.oldX, 2) + Math.pow(panel.currentY - panel.oldY, 2));
	}
	
	public static char getColorId() {
		switch (Main.color) {
        case "black": return '0';
        case "white": return '1';
        case "red": return '2';
        case "green": return '3';
        case "blue": return '4';
        default: return '0';
		}
	}
	
}
