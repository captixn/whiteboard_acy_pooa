package whiteboard_client;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

public class MyMouseMotionAdapter extends MouseMotionAdapter {
	private Panneau panel;
	
	public MyMouseMotionAdapter(Panneau panel){
		this.panel=panel;
		
	}
	
	public void mouseDragged(MouseEvent e) { // tant que la souris est en mouvement et est cliqu�e

		if (Main.select.equals("line")) { // En clique droit
			// coord x,y when drag mouse
			panel.currentX = e.getX();
			panel.currentY = e.getY();

			if (panel.graphics2D != null) {
				// draw line if g2 context not null
				// graphics2D.drawLine(oldX, oldY, currentX, currentY); // Dessiner une ligne entre
				// chaque 2 points

				String oldX_str = Panneau.remiseEnForme(panel.oldX);
				String oldY_str = Panneau.remiseEnForme(panel.oldY);
				String currentX_str = Panneau.remiseEnForme(panel.currentX);
				String currentY_str = Panneau.remiseEnForme(panel.currentY);
				System.out.println("DL" + oldX_str + oldY_str + currentX_str + currentY_str+ MyMouseAdapter.getColorId());
				try {
					Main.output.writeUTF("DL" + oldX_str + oldY_str + currentX_str + currentY_str+ MyMouseAdapter.getColorId());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				panel.oldX = panel.currentX;
				panel.oldY = panel.currentY;
			}
		}

		if (Main.select.equals("eraser")) {
			// coord x,y when drag mouse
			panel.currentX = e.getX();
			panel.currentY = e.getY();

			if (panel.graphics2D != null) {

				String oldX_str = Panneau.remiseEnForme(panel.oldX);
				String oldY_str = Panneau.remiseEnForme(panel.oldY);
				String long_str = Panneau.remiseEnForme(panel.currentX);
				String larg_str = Panneau.remiseEnForme(panel.currentY);
				System.out.println("DE" + oldX_str + oldY_str + long_str + larg_str);
				try {
					Main.output.writeUTF("DE" + oldX_str + oldY_str + long_str + larg_str + '1');
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				panel.repaint(); // rafraichir pour redessiner
				panel.oldX = panel.currentX;
				panel.oldY = panel.currentY; // le dernier point dessin� devient notre point de d�part
			}
		}

		// get history if unsync
		Panneau.getHistory();
	}
	

}
