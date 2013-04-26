package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Set;

class Slime extends Entity {

	boolean jumping = false;

	public Slime(byte id) throws SocketException {
		super(id, 20);

		location.x = -200 + 400 * (id % 2);
	}

	public void notifyRemoval(Set<InetAddress> addresses, int port)
			throws IOException {
		byte[] buf = new byte[] { id };
		sendBuffer(addresses, port, buf);
	}

	public void jumpRight() {
		velocity.x = 3.5f;
		jumpUp();

	}

	public void jumpLeft() {
		velocity.x = -3.5f;
		jumpUp();
	}

	public void jumpUp() {
		jumping = true;
		velocity.y = 3.5f;
		location.y = 1;
	}
}
