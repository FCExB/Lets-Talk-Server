package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;

public class Informer {

	private static int PORT = 4445;

	private final Set<Entity> entities;
	private final Set<InetAddress> addresses;

	public Informer(Set<Entity> entities, Set<InetAddress> addresses) {
		this.entities = entities;
		this.addresses = addresses;
        System.out.println("Informer started");
	}

	public void inform() {


			for (Entity entity : entities) {
				try {
					if (entity != null) {
						entity.notify(addresses, PORT);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


	}
}
