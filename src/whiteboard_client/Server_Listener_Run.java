package whiteboard_client;

import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

/*
 * listen for commands coming from the server and execute it on the whiteboard
 */
public class Server_Listener_Run extends Panneau implements Runnable {

	public static Graphics2D g; 	// shapes to be drawn on the whiteboard
	public boolean process = true; 	// loop variable
	DataInputStream in; 			// entering stream of data 
	Panneau panelRef; 				// drawing panel

	public Server_Listener_Run(Panneau panel){
		this.panelRef = panel;
	}

	public void run() {

		while (process) {
			String received = "";
			try {
				if (Main.input.available() > 0) { 
					received = Main.input.readUTF();	// read the command received 
					processReceived(received);			
				}

			} catch (IOException e) {
				System.out.println("interrupted connection");
				break;

			}

		}
	}

	/*
	 *process the command and do the appropriate action on the panel
	 *@argument: the received string (command), sent by the server
	 */
	private void processReceived(String received) {

		switch (received.substring(0, 2)) {
		
		case "DC":{ //Draw Circle
			int x = Integer.parseInt(received.substring(2, 5));
			int y = Integer.parseInt(received.substring(5, 8));
			int R = Integer.parseInt(received.substring(8, 11));
			int r = Integer.parseInt(received.substring(11, 14));
			String color = received.substring(14, 15);

			panelRef.drawOval(x, y, R, r,color);
			break;
		}

		case "DR":{ // Draw Rectangle
			int x = Integer.parseInt(received.substring(2, 5));
			int y = Integer.parseInt(received.substring(5, 8));
			int L = Integer.parseInt(received.substring(8, 11));
			int l = Integer.parseInt(received.substring(11, 14));
			String color = received.substring(14, 15);
			panelRef.drawRect(x, y, L, l,color);
			break;
		}
		
		case "DL":{ // Draw Line
			int x1 = Integer.parseInt(received.substring(2, 5));
			int y1 = Integer.parseInt(received.substring(5, 8));
			int x2 = Integer.parseInt(received.substring(8, 11));
			int y2 = Integer.parseInt(received.substring(11, 14));
			String color = received.substring(14, 15);
			panelRef.drawLine(x1, y1, x2, y2,color);
			break;
		}
		
		case "DE":{ // Draw Erase (eraser)
			int x1 = Integer.parseInt(received.substring(2, 5));
			int y1 = Integer.parseInt(received.substring(5, 8));
			int x2 = Integer.parseInt(received.substring(8, 11));
			int y2 = Integer.parseInt(received.substring(11, 14));

			panelRef.deleteForm(x1, y1, x2, y2);
			break;
		}
		
		case "RD":{ //Repaint Request: The client send a request to the server to clear the panel

			int choice = JOptionPane.showConfirmDialog(panelRef,
					"Allow panel wide clearing ? \n");
			if (choice == 0) {
				try {
					Panneau.sendCmd("RD000000000000");
				} catch (IOException ex) {
					System.out.println("connection interrupted");
				}
			}
			break;
		}
		
		case "RC":{ //Repaint Confirmed: The server has confirmed the clearing of the panel
			System.out.println("clear all !!!");
			panelRef.clear();
			break;
		}
		
		case "FC":{ // draw a Filled Circle
			int x = Integer.parseInt(received.substring(2, 5));
			int y = Integer.parseInt(received.substring(5, 8));
			int R = Integer.parseInt(received.substring(8, 11));
			int r = Integer.parseInt(received.substring(11, 14));
			String color = received.substring(14, 15);

			panelRef.fillOval(x, y, R, r,color);
			break;
		}

		case "FR": { // draw a filled Rectangle
			int x = Integer.parseInt(received.substring(2, 5));
			int y = Integer.parseInt(received.substring(5, 8));
			int L = Integer.parseInt(received.substring(8, 11));
			int l = Integer.parseInt(received.substring(11, 14));
			String color = received.substring(14, 15);

			panelRef.fillRect(x, y, L, l,color);
			break;
		}


		case "DT":{ // Draw Text: write text
			int x = Integer.parseInt(received.substring(2, 5));
			int y = Integer.parseInt(received.substring(5, 8));
			String texte = received.substring(14,received.length()-1);
			String color = received.substring(received.length()-1,received.length());
			panelRef.drawText(x, y,texte,color);
			break;
		}


		}



	}

}
