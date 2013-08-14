package server;

import java.net.SocketException;

public class Server {

    public static final boolean DEBUG_MODE = true;


	public static void main(String[] args) throws SocketException {

		Simulator sim = new Simulator();
        sim.start();

		new Listener(sim).start();
        
	}
}
