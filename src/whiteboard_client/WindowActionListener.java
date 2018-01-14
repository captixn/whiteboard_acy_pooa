/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whiteboard_client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * add a listener on the buttons
 */
public class WindowActionListener implements ActionListener {

	private Window window;

	public WindowActionListener(Window window) {
		this.window = window;
	}

	@Override	
	/*
	 *Allow to set what shape will be drawn at the next click on the panel 
	 */
	public void actionPerformed(ActionEvent e) {

		Object refButton = window.lastButtonPressed = e.getSource(); // store the click event

		// Set select variable according to the event received
		if (refButton == window.buttons.get("circle")) {
			Main.select = "circle";
		} else if (refButton == window.buttons.get("rect")) {
			Main.select = "rectangle";
		} else if (refButton == window.buttons.get("line")) {
			Main.select = "line";
		}else if (refButton == window.buttons.get("fillCircle")) {
			Main.select = "fillCircle";
		} else if (refButton == window.buttons.get("fillRect")) {
			Main.select = "fillRectangle";
		} else if (refButton == window.buttons.get("eraser")) {
			Main.select = "eraser";
		} else if (refButton == window.buttons.get("repaint")) {
			try {
				Panneau.sendCmd("RD000000000000"); // send the command to clear the screen
			} catch (IOException ex) {
				System.err.println("clear command has failed");
			}
		} else if (refButton == window.buttons.get("close")) {
			try {
				Panneau.sendCmd("QT000000000000"); // send the command to close the program
			} catch (IOException ex) {
				System.err.println("close command has failed");
			}

			Panneau.close();
			try {
				Main.output.close(); // close output stream
				Main.input.close(); // close input stream
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (refButton == window.buttons.get("text")) {
			Main.select = "text";
		}


		// set color variable according to the wanted color
		if (refButton == window.colorButtons.get("black")) {
			Main.color = "black";
		} else if (refButton == window.colorButtons.get("white")) {
			Main.color = "white";
		} else if (refButton == window.colorButtons.get("red")) {
			Main.color = "red";
		} else if (refButton == window.colorButtons.get("blue")) {
			Main.color = "blue";
		} else if (refButton == window.colorButtons.get("green")) {
			Main.color = "green";
		}

	}

}
