package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Set;

public class Entity {
	final byte id;

	Vector location = new Vector(0, 0);
	int radius;
	Vector velocity = new Vector(0, 0);

	public Entity(byte id, int radius) {
		this.id = id;
		this.radius = radius;
	}

	public Entity(byte id, int radius, int x, int y) {
		this.id = id;
		this.radius = radius;

		location = new Vector(x, y);
	}

	public void notify(Set<InetAddress> addresses, int port) throws IOException {

		byte[] buf = ByteBuffer.allocate(9).put(id)
				.putInt(location.getXAsInt()).putInt(location.getYAsInt())
				.array();

		sendBuffer(addresses, port, buf);
	}

	protected void sendBuffer(Set<InetAddress> addresses, int port, byte[] buf)
			throws IOException {

		DatagramSocket socket = new DatagramSocket();
		for (InetAddress address : addresses) {

			DatagramPacket p = new DatagramPacket(buf, buf.length, address,
					port);

			socket.send(p);

		}
		socket.close();
	}

	public Vector collidesWith(Entity that) {
		Vector vec = Vector.sub(this.location, that.location);

		float distance = Math.abs(vec.magintude());

		if (distance > this.radius + that.radius) {
			return null;
		}

		return vec;
	}
}
