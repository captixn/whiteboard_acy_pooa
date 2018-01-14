package whiteboard_client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class Main {

	public static Panneau panel;
	public static Thread serverListener;
	public static DataOutputStream output;
	public static DataInputStream input;
	public static int index_actu;
	public static String select = "line";
	public static String color = "black";
	
	public static void main(String[] args) {

		Socket client = null;
		System.out.println("Connecting to ALEX on port " + 11237);

		try {
			client = new Socket("localhost", 11237);
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		System.out.println("client just connected to " + client.getRemoteSocketAddress());

		try {
			output = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		try {
			input = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			output.writeUTF("HL000000000000");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			String index = input.readUTF();
			index_actu = Integer.parseInt(index);
			System.out.println("Index recu par ALEX : " + index);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		panel = new Panneau();
		
		try {
			new Window(panel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e){
			e.printStackTrace();
		}
		Main.serverListener = new Thread(new Server_Listener_Run(panel));
		Main.serverListener.start();
		
		try {
			Main.serverListener.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
	}
}
