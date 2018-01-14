package whiteboard_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/*
* Server class, extending Thread 
* Opens a server socket and create a thread to process new connection
* Keep track of threads created by reference 
* Supply general communication methods
* Store stack of commands 
* 
* @extends Thread 
*/
public class Server extends Thread {

	/*
	* public variable that keeps a reference for the active server socket 
	*/
	public ServerSocket socket;
	
	/*
	* arraylist of active sockets 
	*/
	public static ArrayList<Socket> client_sockets = new  ArrayList<Socket>() ; 
	
	/*
	* arraylist of saved commands sent to the server and broadcasted 
	*/
	public static ArrayList<String> history = new ArrayList<String>() ; 
	
	/*
	* boolean that defines the state { the program is confirming that clients all confirm clearing of commands | or not }  
	*/
	public static Boolean repaintConfirming = false; 
		
	/*
	* arraylist that keeps a reference to all active client processor threads 
	*/
	public static ArrayList<ProcessorThread> processor_threads = new ArrayList<ProcessorThread>();

	/* 
	* arraylist that keeps a copy of all clients still required to confirm in state repaintConfirming = true
	*/
	public static ArrayList<ProcessorThread> clients_to_confirm ;

	/*
	* boolean that keeps the while loop iterating while it stays true - the while loop accepts new client 
	*/
	private boolean process;
	
	/*
	* array of all commands accepted (for which processing exists) (strictly speaking, subcommand) 
	*/
	public static final String[] CMD = new String[] {"DR","HL","DL","OT", "QT", "DC", "GH","DE","RD","RC", "FC","FR","DT"};
	
	/*
	* array of all commands that don't need to pass all protocol checks 
	*/	
	public static final String[] CMDwhite = new String[] {"HL","QT","GH","RD","RC","RP","DT"};

	/*
	* HL : Hello - on connection 
	* DR: Draw Rectangle 
	* DL : Draw Line 
	* DC : Draw Circle
	* DT : Draw Text
	* GH : Get History
	* DE : Draw Erase (erase) 
	* FR : Fill Rectangle (draw a full rectangle) 
	* RD : Repaint Demand 
	* RC : Repaint Confirm  
	* FC : Fill Circle (draw a disk)
	* QT : Quit 
	*/

	/*
	* Returns history member 
	* @return ArrayList<String> history Stack of commands stored on server 
	*/
	public static ArrayList<String> getHistory() {
		return history;
	}

	/*
	* Construsts the server on port passed in parameter, initializes boolean process as true 
	* @param int port Port on which to listen 
	* @throws IOException
	*/
	public Server(int port) throws IOException {
		socket = new ServerSocket(port);
		System.out.println("Server open on port " + port);
		process = true;
	}

	/*
	* Stop listening, close connections 
	* @throws IOException
	*/
	public void stoplistening() throws IOException {
		process = false;
		if (!socket.isClosed()) {
			socket.close();
		}

	}

	/*
	* return index : size of the array of commands, to indicate how many commands should a client receive 
	* @return int index Currnet last index of command : size of array of commands 
	*/
	public static int getIndex() {
		if (history.isEmpty()) {
			return 0;
		} else {
			return history.size();
		}
	}

	/*
	* check that command passed in parameter conforms to the protocol 
	* @param String command 
	* @return Boolean check
	*/
	public static Boolean checkCmd(String command) {
		Boolean check = true;

		// check that command have 15 chars : 2 chars for cmd and 4x3 for coordinates, and a last one to code the color 
		if (command.length() != 15 && ! command.substring(0, 2).equals("DT")) {
			check = false;
		}
		// if command is not in array of known commands, check didn't pass 
		if (!Arrays.asList(CMD).contains(command.substring(0, 2))) {
			check = false;
		}
		
		// if command is DRAW something, expects 4x3 for 4 coordinates
		ArrayList<Integer> coordinates = new ArrayList<>() ; 
		if(command.substring(0,1).equals("D") ){
			coordinates.add(extractInt(command,2,5));
			coordinates.add(extractInt(command,5,8)); 
			coordinates.add(extractInt(command,8,11));
			coordinates.add(extractInt(command,11,14));
			for(int i=0;i<coordinates.size();i++){
				Integer coord = coordinates.get(i);
				// current whiteboard dimensions : 999x700 : hence all cood must be between 0 and 999
				if (coord < 0 || coord > 999) {
					check = false;
				}
			}
		}

		return check;
	}
	
	/*
	* extract int from a string : get the substring between two index, and convert to int 
	* @param String command 
	* @param Integer start_int Index to start substring  
	* @param Integer end_int Index at which ends the substring 
	* @return Integer 
	*/
	public static Integer extractInt(String command, Integer start_int, Integer end_int) {
		return Integer.parseInt(command.substring(start_int, end_int));
	}
	
	/*
	* Instructions to run when server starts 
	* Accept new clients while process is true 
	*/
	public void run() {
		while (process) {
			try {
				System.out.println("Waiting for client on port " + socket.getLocalPort() + "...");
				// Accept new client 
				Socket server = socket.accept();
				// Save reference 
				client_sockets.add(server);
				// Save reference 
				ProcessorThread th = new ProcessorThread(server);
				processor_threads.add(th);
				// Start a thread with the accepted connection socket 
				th.start();
			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (SocketException s) {
				s.printStackTrace();
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	/*
	* save command to membe history
	* @public
	* @synchronized
	* @static
	* @param String command 
	* @return void 
	*/
	public synchronized static void saveCmd(String command) {
		history.add(command);
	}

	/*
	* broadcast command to all clients
	* iterate using an iterator on the arraylist of active client threads 
	* @param String command 
	*/
	public static void broadcast(String command) {
		Iterator<ProcessorThread> iterator = processor_threads.iterator();
		System.out.println("[SERVER] Command emitted : " + command);
		while (iterator.hasNext()) {
			ProcessorThread cThread = iterator.next();
			try {
				cThread.sendCmd(command);
			} catch (IOException e) {
				System.out.println();
				e.printStackTrace();
			}
		}
	}

	/*
	* clear history: empty stack fo commands  
	*/
	public static void resetHistory() {
		history.clear();
	}

	/* 
	* parse an int to a strign 
	* @param String subcommand 
	* @return Integer int 
	*/
	public static Integer parseStringToIndex(String subcommand) {
		return Integer.parseInt(subcommand);
	}

	/*
	* called when a client has resent RD when being called - client has confirmed 
	* therefore remove it from the list of clients to confirm 
	* and check if all clients have confirmed now (call ifConfirmThenSend() of aThis thread)
	* (this methof is to be called from a processorThread by static reference)
	* @param ProcessorThread aThis Reference to the processorThread object that calls this method. Expose members of said object.
	*/
	static void clientHasConfirmed(ProcessorThread aThis) {
		Server.clients_to_confirm.remove(aThis);
		aThis.ifConfirmThenSend(); 
	}

	
}
