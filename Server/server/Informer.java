package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;

public class Informer extends Thread {

	private static int PORT = 4445;

	private final Set<Entity> entities;
	private final Set<InetAddress> addresses;

	public Informer(Set<Entity> entities, Set<InetAddress> addresses) {
		this.entities = entities;
		this.addresses = addresses;
	}

	@Override
	public void run() {
		while (true) {

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
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
