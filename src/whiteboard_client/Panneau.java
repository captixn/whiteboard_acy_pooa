package whiteboard_client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Panneau extends JPanel {

	public Image image; // the image to draw on 
	public Graphics2D graphics2D; // the graphics2D that will allow us to draw
	public int currentX, currentY, oldX, oldY, Long, Larg, x1_toSend, y1_toSend; // the coordinates
	public double rayon; // the radius of the circle to draw
	public static int index_client = 0; // the index of the client is 0 at the beginning

	/*
	* The constructor of the panel
	*/
	public Panneau() {
		System.out.println("Pannel created");

		setDoubleBuffered(false); // we don't use a buffer to paint

		addMouseListener(new MyMouseAdapter(this)); // we add the mouse listener, for pressing mouse
		// and releasing mouse
		addMouseMotionListener(new MyMouseMotionAdapter(this)); // we add Mouse motion listener for 
		//dragging mouse

		// wait 2 seconds ( or more, depending on the machine ) so that all objects are ready
		historyUpdate();

	}

	/*
	* A function that will use a thread to wait 2 seconds before getting history from the server
	* The history is all the previous forms that have been drawn before the conenction of the client
	* so that he will be up to date
	*/
	private void historyUpdate() {
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1); // creating a schedulor
		exec.schedule(new Runnable() {
			@Override
			public void run() {
				getHistory(); // getting all the previous commands
			}
		}, 2, TimeUnit.SECONDS); // time set to 2 seconds, we can add more time if necessary,
		//depending on the machine we use
	}

	/*
	* A method that will send a request to the server in order to get all the previous command
	*/
	public static void getHistory() {
		if (index_client < Main.index_actu) { // if the index of the client is smaller than
			//the current drawing index
			try {
				sendCmd("GH000000000000"); // GH Stands for Get History
				//Main.output.writeUTF("GH000000000000");
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			index_client = Main.index_actu; // updating the client index
		}
	}


	/*
	* A method that will write a given string in a given coordinates in the panel
	*/
	public void drawText(int x, int y, String texte, String color) {
		setColor(color);     // setting the color of the drawing depending on the last color button pressed
		graphics2D.drawString(texte, x, y);
		Main.panel.repaint();

	}

	/*
	* A method that will send a string command to the server
	*/
	public static void sendCmd(String command) throws IOException {
		Main.output.writeUTF(command);
	}

	@Override
	/*
	* (non-Javadoc)
	* @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	* This is the user Interface paint method that will allow us to paint
	*/
	public void paintComponent(Graphics g) {
		if (image == null) {
			// image to draw null ==> we create
			image = createImage(getSize().width, getSize().height);

			graphics2D = (Graphics2D) image.getGraphics();
			// enable antialiasing
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// clear draw area
			clear();

		}
		g.drawImage(image, 0, 0, null);
	}

	/*
	 * A method that will allow us to clear the whiteboard
	 */
	public void clear() {
		graphics2D.setColor(Color.white);
		// draw white on entire draw area to clear
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setColor(Color.black);
		repaint();
	}

	/*
	* given length, width, coordinates and color, this method will draw a rectangle
	*/
	public void drawRect(int x1, int x2, int l, int w, String color) {

		setColor(color); // we set the color depending on the last color button pressed
		graphics2D.drawRect(x1, x2, w, l);
		repaint();

	}

	/*
	* given coordinates, color and radius, this method will draw a circle
	*/
	public void drawOval(int x1, int x2, int r, int R, String color) {

		int diametre = r * 2;
		setColor(color); // we set the color depending on the last color button pressed
		graphics2D.drawOval(x1 - r, x2 - r, diametre, diametre); // a little adjustment 
		//	to draw the circle in the correct position
		repaint();
	}

	/*
	* Given the first point, the second one and the color, this method will draw a line
	*/
	public void drawLine(int x1, int x2, int l, int w, String color) {

		setColor(color); // we set the color depending on the last color button pressed
		graphics2D.drawLine(x1, x2, l, w);
		repaint();
	}

	/*
	* This function will erase the drawing by dragging a white filled circle
	*/
	public void deleteForm(int x1, int x2, int r, int R) {
		graphics2D.setColor(Color.WHITE);
		graphics2D.fillOval(x1, x2, 20, 20);
		//g3.fillOval(x1, x2, r, R); // if we want to change the dimensions of the eraser
		repaint();
	}

	/*
	 * Given coordinates, radius and color, this method will draw a filled circle
	 */
	public void fillOval(int x1, int x2, int r, int r2, String color) {

		int diametre = r * 2;
		setColor(color);
		graphics2D.fillOval(x1 - r, x2 - r, diametre, diametre);
		repaint();
	}

	/*
	 * Given coordinates, length, width and color, the method will draw a filled rectangle 
	 */
	public void fillRect(int x1, int x2, int l, int w, String color) {
		setColor(color);
		graphics2D.fillRect(x1, x2, w, l);
		repaint();

	}


	/*
	* Every color is coded by a char ( 0 1 2 3 4 ), we set the graphics color depending on the char value
	*/
	private void setColor(String color) {
		switch (color) {

			case "0":
				graphics2D.setColor(Color.BLACK);
				break;
			case "1":
				graphics2D.setColor(Color.WHITE);
				break;
			case "2":
				graphics2D.setColor(Color.RED);
				break;
			case "3":
				graphics2D.setColor(Color.GREEN);
				break;
			case "4":
				graphics2D.setColor(Color.BLUE);
				break;
			default:
				graphics2D.setColor(Color.BLACK);

		}
	}

	/*
	 * A method that will properly close the window andinterrupt the thread to avoid exceptions
	 */
	public static void close() {
		Main.serverListener.interrupt();
		System.exit(0);
	}

	/*
	* Since in our protocol every coordinate should be coded by 3 digits, this method will take the coordinates
	* as integer, and convert them the a proper String : XYZ,0XY,00X.
	*/
	public static String remiseEnForme(int entier) {
		if (entier > 99 && entier <= 999) {
			return String.valueOf(entier);
		}
		if (entier > 999) {
			return String.valueOf(999);
		}
		if (entier <= 99 && entier > 9) {
			return "0" + String.valueOf(entier);
		}
		if (entier > 0 == entier <= 9) {
			return "00" + String.valueOf(entier);
		}
		if (entier < 0) {
			return "000";
		} else {
			return "000";
		}
	}

}
