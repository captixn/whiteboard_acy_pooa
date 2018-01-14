package whiteboard_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* Thread class to process an individual client 
* Object is constructed on a new connection and takes the socket and streams as its private members 
* Basically receive command sent and do the corresponding processing : broadcast, send history, send index, close connection, etc...
*/
public class ProcessorThread extends Thread {

	/* 
	* InputStream used for input of the connection socket 
	* Supply readUTF() method 
	*/
	private DataInputStream input;

	/* 
	* OutputStream used for output of the connection socket 
	* Supply writeUTF() method 
	*/
	private DataOutputStream output;

	/*
	* Connection socket initialized upon construction of the thread in Server class, onAccept() 
	*/
	private Socket socket;

	/*
	* Boolean that stays true as long as the client has not sent "QT" for quit nor it has been disconnected 
	* Controls the while loop in which commands are processed continuously 
	*/
	public Boolean process = true;

	/* 
	* ArrayList of ProcessorThread that keeps a local copy of list of clients at the time it has been created 
	* The copy is made from the list of the central class server 
	* It is actually used to implement the clearing mechanism
	* When a client asks for a clearing, a list of all clients are made. Everytime a client leaves or confirms, the client is removed from the list
	* When the list becomes empty, it means all clients have confirmed. Then the board is cleared. 
	*/
	public static ArrayList<ProcessorThread> own_list_client ;

	/*
	* Constructor
	* Hydrate members of the class: streams and socket 
	* @param Socket socket Connection socket 
	* @throws IOException 
	*/
	public ProcessorThread(Socket socket) throws IOException {

		if (socket == null) {
			throw new NullPointerException("the socket cannot be null");
		}
		this.socket = socket;
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
	}

	/*
	* Close connection cleanly : close inputStream, outputStream, socket and remove this thread and this socket from the server's list of active thread and sockets 
	* @throws IOException 
	*/
	private void close() throws IOException{
		// remove client from confirming list 	
		if(Server.repaintConfirming)
				Server.clientHasConfirmed(this); 

		input.close();
		output.close();
		socket.close();
		
		// remove socket & thread from ArrayList
		Server.processor_threads.remove(this);
		Server.client_sockets.remove(socket);
	}
	
	/*
	* On error, close connection 
	* If connection can't also be closed, warn with console 
	* @param String message Error message 
	*/
	private void error(String message) {
		System.out.println(message);
		try {
			close();
		} catch (Exception e) {
			System.out.println("Error while closing socket");
		}
	}

	/*
	* Try reading last string received from inputStream 
	* Blocking call, which is why each client runs on a dedicated thread 
	* @return String command Command received on inputStream
	*/
	private String getCommandUtf8() {
		try {
			return input.readUTF();
		} catch (IOException io) {
			error("echec de reception");
			return null;
		}
	}

	/*
	* Run method of the thread 
	* Check that first command ever received is HL, returns index 
	* Then process all following commands in processCommand()
	*
	*/
	public void run() {
		System.out.println("Just connected to remote socket address " + socket.getRemoteSocketAddress());
		// implementation du protocole
		String command = getCommandUtf8();
		String subcommand = command.substring(0, 2);
		if (!subcommand.equals("HL")) {
			error("erreur de protocole: communication must start by hello");
			System.out.println(subcommand);
			return;
		}
		try {
			System.out.println("Writing index following HL request");
			int index = Server.getIndex();
			this.sendCmd(Integer.toString(index));
			// now send all history from index received (send all commands whose index between the index of the client and the current index of the server for synchronization) 
			sendHistoryFromIndex(index);
		} catch (IOException io) {
			io.printStackTrace();
			error("echec d'envoi");
			return;
		}
		while (process) {
			try {
				command = getCommandUtf8();
				if (command != null) {
					processCommand(command);
				} else {
					// a client has probably been disconnected
					break;
				}
			} catch (IOException io) {
				io.printStackTrace();
				error("echec d'envoi");
			}
		}
		// il faut tout fermer
		try {
			System.out.println("Closing connection");
			close();
		} catch (IOException e) {
			System.out.print("Error while closing connection");
		}
	}

	/*
	* Process command received (passed as argument)
	* Executes different actions depending on the command received (first two chars) and its parameters (what follows)
	* @param String command Command and its parameters (expected: 15 chars, except that command DT that Draws Text) 
	* @return void Broadcast command in most of cases 
	*/
	public void processCommand(String command) throws IOException {
		String subcommand = command.substring(0, 2);
		// special cases first : if command is QT, quit 
		if (subcommand.equals("QT")) {
			System.out.println("closing connection");
			this.process = false;
			// don't broadcast : so return before broadcast method is hit 
			return;
		}
		// if command is GH, send history (don't broadcast)
		if (subcommand.equals("GH")) {
			try {
				// avoid overlap of commands 
				// and call of GH when client isn't all ready 
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			subcommand = command.substring(2, 14);
			sendHistoryFromIndex(Server.parseStringToIndex(subcommand));
			// don't broadcast 
			return;
		}
		// RD: Repaint Demand : if confirmed by all clients, reset history and broadcast signal RC - RepaintConfirm 
		if (subcommand.equals("RD")) {
			try {
				System.out.println("Repaint demand received"); 
				repaintConfirm();
				return ; 
			} catch (InterruptedException ex) {
				Logger.getLogger(ProcessorThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		// Check that command is known, otherwise reject it 
		if (!Arrays.asList(Server.CMD).contains(subcommand)) {
			error("Protocol error: command unknown");
			return;
			}
		// Broadcast
		System.out.println("[SERVER] Réception de " + command);
		try {
			// Just check that command is really valid: undergo a protocol check using Server.checkCmd(String command)
			if (Server.checkCmd(command)) {
				// It is correct, so save it in Server stack - the all whole stack of command is sent upon connection for synchronisation of client disconnected or reopenning the application 
				Server.saveCmd(command);
				System.out.println("[SERVER] ATTEMPTING TO BROADCAST " + command);
				// Send to all cients in Server.processor_threads the command 
				Server.broadcast(command);
			} else {
				throw new IOException("Invalid command");
			}
		} catch (Exception e) {
			error("Communication error");
			e.printStackTrace();
			// Break the while loop
			process = false;
			return;
		}
	}

	/*
	* sendCommand to connected client 
	* @param String command Command to save 
	*/
	public void sendCmd(String command) throws IOException {
		output.writeUTF(command);
		System.out.println("[SERVER] >>>>>" + command + " [CLIENT] ");
	}

	/*
	* send History of commands from index
	* @param int index Index from which sends the history of commands 
	* @return void send all commands returned
	*/
	public void sendHistoryFromIndex(Integer index) throws IOException {
		ArrayList<String> history = Server.getHistory();
		System.out.println("[SERVER] Sending History from Index " + index);
		if (index < history.size()) {
			for (int i = index; i < history.size(); i++) {
				this.sendCmd(history.get(i));
				System.out.println("************************************");
			}
		}
	}

	/*
	* %ethod called when server receives RD - Repain demand 
	* Confirm repainting i Server.repaintConfirming (=state of repaint process) is true (i.e. less than 5 seconds ago a first demander has requested a clearing
	* Or start a repainting process - withiin five seconds, clients must click on "Confirm" so that they sent "RD" that would confirm they accept clearing 
	* @throws InterruptedException 
	*/
	private void repaintConfirm() throws InterruptedException {
		if(!Server.repaintConfirming){
			Server.repaintConfirming = true; 
			// time to wonder to request all clients confirm clearing 
			// hold a copy a list of all client threads 
			Server.clients_to_confirm = new ArrayList<ProcessorThread>(Server.processor_threads) ; 
				
			// broadcast command to all clients 
			Server.broadcast("RD000000000000");

			// 5 seconds later, reset state
			// Create a thread that runs five seconds in a scheduler 
			ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
			exec.schedule(new Runnable() {
				@Override
				public void run() {
					Server.repaintConfirming = false;
				}
			}, 5, TimeUnit.SECONDS);
		}else{
			// this client has confirmed while a request has already been sent
			// remove it from the list of clients to confirm 
			Server.clientHasConfirmed(this);
		}
	}

	/*
	* called in server function clientHasConfirmed
	* if all clients have confirmed then send repaint confirmed signal 
	* and update state 
	*/
	public void ifConfirmThenSend(){
		if(Server.clients_to_confirm.isEmpty()){
			//all client have confirmed
			repaintConfirmed(); 
			Server.repaintConfirming = false; 
		}else{
			// not all clients have reconfirmed, never mind. 
			Server.repaintConfirming = true; 
		}	
	}

	/* 
	* Reset history (empty stack of commands) 
	* Broadcast repaint confirmed 
	*/
	private void repaintConfirmed() {
		Server.resetHistory();
		Server.broadcast("RC000000000000");
	}

	

}
