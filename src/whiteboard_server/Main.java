package whiteboard_server;

/*
* Main part of the program
* Initialize the server on a given port - by default 11237 
* 
* Primay function of server is to broadcast commands sent from one client to all others 
* Commands include Draw Line, Draw Circle - etc 
* It also manages connections with clients and synchronisation 
* @param void 
* @return void 
*/
public class Main {

	public static int port;

	public static void main(String[] args) {

		Server server;

		try {
			server = new Server(11237);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
