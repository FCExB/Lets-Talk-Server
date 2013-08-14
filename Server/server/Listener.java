package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Listener extends Thread {

	private static byte nextId = 2;

	private final Map<InetAddress, Slime> players = new HashMap<InetAddress, Slime>();
	private final DatagramSocket socket;

	private final Simulator sim;

	public Listener(Simulator sim) throws SocketException {
		socket = new DatagramSocket(4444);
		this.sim = sim;
	}

	public Map<InetAddress, Slime> getEntities() {
		return players;
	}

	@Override
	public void run() {
		byte[] buf = new byte[256];
		DatagramPacket p = new DatagramPacket(buf, buf.length);

		while (true) {

			try {
				socket.receive(p);
                if(Server.DEBUG_MODE) {
                    System.out.println("Update received from " + p.getAddress() 
                        + " size = " + p.getLength()); 
                }               

				if (p.getLength() == 1) {
					if (p.getData()[0] == 0) {
						if (!players.containsKey(p.getAddress())) {
							if (nextId == 2) {
								sim.addBall();
							}

							Slime player = new Slime(nextId);
	                        System.out.print("Player number " + nextId);
                            nextId++;

							players.put(p.getAddress(), player);

							sim.addPlayer(player);
							sim.addAddress(p.getAddress());
                            
                            if(Server.DEBUG_MODE) {
                                System.out.println(" added from " + p.getAddress());
						    }
                        }

					} else if (p.getData()[0] == 1) {
						Slime player = players.get(p.getAddress());

						players.remove(p.getAddress());

						sim.removePlayer(player);
						sim.removeAddress(p.getAddress());

                        if(Server.DEBUG_MODE) {
                            System.out.println("Player " + p.getAddress() 
                                                + " removed");      
	                    }
    				}
    
				} else if (p.getLength() == 8) {
					Slime player = players.get(p.getAddress());

					ByteBuffer bb = ByteBuffer.wrap(p.getData());

					int xChange = bb.getInt();
					int yChange = bb.getInt();

					if (!player.jumping) {
						player.velocity.x = xChange;
						if (yChange != 0) {
							player.velocity.y = yChange * 3;
							player.jumping = true;
							player.location.y = 3;
						}
					}

                    if(Server.DEBUG_MODE) {
                        System.out.println("Player " + p.getAddress() 
                                                + " updated");      
				    }
                }

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
