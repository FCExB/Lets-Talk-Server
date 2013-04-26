package server;

import java.net.SocketException;

public class Server {
	public static void main(String[] args) throws SocketException {

		Simulator sim = new Simulator();
		sim.start();

		Listener lis = new Listener(sim);
		lis.start();
	}
}
