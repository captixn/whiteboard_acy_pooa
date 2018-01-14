/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whiteboard_client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author acvkp
 */
public class Window extends JFrame {

	public Object lastButtonPressed;			// contains 

	public static JTextField textField; 		// field which allow the user to type text

	// tables of buttons with handles
	public Hashtable<String, JButton> buttons ; 
	public Hashtable<String, JButton> colorButtons ;

	public Window(Panneau panel) throws IOException, URISyntaxException {

		this.setTitle("Collaborative whiteboard");
		this.setSize(999, 700); 				// set the size of the window

		Container contentPane = getContentPane();
		JPanel buttonPanel = new JPanel(); 		// instantiate the drawing panel
		JPanel colorButtonPanel = new JPanel();	// instantiate the color selection buttons panel 

		this.buttons = new Hashtable<>();
		this.colorButtons = new Hashtable<>();

		BufferedImage	circleIcon, rectangleIcon, fillCircleIcon,
						fillRectangleIcon,lineIcon, eraseIcon, closeIcon, clearIcon,
						redIcon, blueIcon,greenIcon,blackIcon,whiteIcon;
		
		// set the buttons' icons
		circleIcon = ImageIO.read(
				getClass().getResourceAsStream("/whiteboard_client/icones/circle.png"));
		rectangleIcon = ImageIO.read(
				getClass().getResource("icones/rectangle.png"));
		fillCircleIcon = ImageIO.read(
				getClass().getResource("icones/fillCircle.png"));
		fillRectangleIcon =ImageIO.read(
				getClass().getResource("icones/fillRectangle.png"));
		lineIcon = ImageIO.read(
				getClass().getResource("icones/line.png"));
		eraseIcon = ImageIO.read(
				getClass().getResource("icones/eraser.png"));
		closeIcon = ImageIO.read(
				getClass().getResource("icones/close.png"));
		clearIcon = ImageIO.read(
				getClass().getResource("icones/clear.png"));
		redIcon = ImageIO.read(
				getClass().getResource("icones/red.png"));
		blueIcon = ImageIO.read(
				getClass().getResource("icones/blue.png"));
		greenIcon = ImageIO.read(
				getClass().getResource("icones/green.png"));
		whiteIcon = ImageIO.read(
				getClass().getResource("icones/white.png"));
		blackIcon = ImageIO.read(
				getClass().getResource("icones/black.png"));
		
		// resize each icon
		Image resizedCircleIcon = circleIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedRectangleIcon = rectangleIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedFillCircleIcon = fillCircleIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedFillRectangleIcon = fillRectangleIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedLineIcon = lineIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedEraseIcon = eraseIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedCloseIcon = closeIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedClearIcon = clearIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedRedIcon = redIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedBlueIcon = blueIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedGreenIcon = greenIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedWhiteIcon = whiteIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		Image resizedBlackIcon = blackIcon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);

		
		textField = new JTextField("entrez carracteres");
		buttonPanel.add(textField);	// add the textField object to the buttons panel
		JButton textButton=new JButton("OK");

		// create the buttons and link them to their corresponding icons
		JButton circleButton = new JButton(new ImageIcon(resizedCircleIcon));
		JButton rectButton = new JButton(new ImageIcon(resizedRectangleIcon));
		rectButton.setPreferredSize(new Dimension(33, 33));
		JButton fillCircleButton = new JButton(new ImageIcon(resizedFillCircleIcon));
		fillCircleButton.setPreferredSize(new Dimension(33, 33));
		JButton fillRectButton = new JButton(new ImageIcon(resizedFillRectangleIcon));
		fillRectButton.setPreferredSize(new Dimension(33, 33));
		JButton lineButton = new JButton(new ImageIcon(resizedLineIcon));
		lineButton.setPreferredSize(new Dimension(33, 33));
		JButton eraserButton = new JButton(new ImageIcon(resizedEraseIcon));
		eraserButton.setPreferredSize(new Dimension(33, 33));
		JButton closeButton = new JButton(new ImageIcon(resizedCloseIcon));
		closeButton.setPreferredSize(new Dimension(33, 33));
		JButton repaintButton = new JButton(new ImageIcon(resizedClearIcon));
		repaintButton.setPreferredSize(new Dimension(33, 33));

		JButton redButton = new JButton(new ImageIcon(resizedRedIcon));
		redButton.setPreferredSize(new Dimension(33, 33));
		JButton blueButton = new JButton(new ImageIcon(resizedBlueIcon));
		blueButton.setPreferredSize(new Dimension(33, 33));
		JButton greenButton = new JButton(new ImageIcon(resizedGreenIcon));
		greenButton.setPreferredSize(new Dimension(33, 33));
		JButton whiteButton = new JButton(new ImageIcon(resizedWhiteIcon));
		blueButton.setPreferredSize(new Dimension(33, 33));
		JButton blackButton = new JButton(new ImageIcon(resizedBlackIcon));
		greenButton.setPreferredSize(new Dimension(33, 33));

		/* Store events in hashTable */
		buttons.put("circle", circleButton);
		buttons.put("rect", rectButton);
		buttons.put("fillCircle", fillCircleButton);
		buttons.put("fillRect", fillRectButton);
		buttons.put("line", lineButton);
		buttons.put("eraser", eraserButton);
		buttons.put("repaint", repaintButton);
		buttons.put("close", closeButton); 
		buttons.put("text", textButton); 

		colorButtons.put("black", blackButton); 
		colorButtons.put("white", whiteButton); 
		colorButtons.put("blue", blueButton); 
		colorButtons.put("red", redButton); 
		colorButtons.put("green", greenButton); 

		/* Declare Action Listener */
		final ActionListener buttonPressed;
		buttonPressed = (ActionListener) new  WindowActionListener(this);
		
		/* Iterate through buttons */
		Set<String> keys = buttons.keySet();
		keys.stream().forEach((key) -> {
			/* Add button to buttons panel */
			/* Add the Action Listener for each button */
			buttons.get(key).addActionListener(buttonPressed);
		});

		keys = colorButtons.keySet();
		keys.stream().forEach((key) -> {
			/* Add the Action Listener for each button */
			colorButtons.get(key).addActionListener(buttonPressed);
		});

		/* Defining buttons panel */
		buttonPanel.add(circleButton);
		buttonPanel.add(rectButton);
		buttonPanel.add(fillCircleButton);
		buttonPanel.add(fillRectButton);
		buttonPanel.add(lineButton);
		buttonPanel.add(eraserButton);
		buttonPanel.add(repaintButton);
		buttonPanel.add(textField);
		buttonPanel.add(textButton);
		buttonPanel.add(closeButton);
		
		colorButtonPanel.add(greenButton);
		colorButtonPanel.add(redButton);
		colorButtonPanel.add(blueButton);
		colorButtonPanel.add(blackButton);
		colorButtonPanel.add(whiteButton);

		/* Add  both panels of buttons and graphics panel to window*/
		contentPane.add(buttonPanel, BorderLayout.NORTH);
		contentPane.add(panel, BorderLayout.CENTER);
		contentPane.add(colorButtonPanel, BorderLayout.SOUTH);

		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(contentPane);
		this.setVisible(true);

	}

}
