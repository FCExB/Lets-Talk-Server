package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class Simulator extends Thread {

	Set<Entity> entities = new HashSet<Entity>();

	Set<InetAddress> addresses = new HashSet<InetAddress>();

	int port = 4445;

	private static final int LENGTH_OF_ONE_TICK = 10;
	private static final float GRAVITY_EFFECT_PER_TICK = 0.1f;

	private long lastFrameTime;

	public Simulator() {

		new Informer(entities, addresses).start();
        System.out.println("Simulator started");
	}

	@Override
	public void run() {
		while (true) {

			long currentTime = System.nanoTime();
			long difference = currentTime - lastFrameTime;

			double delta = difference / 1000000.0;
			lastFrameTime = currentTime;

			simulate(delta);
		}
	}

	private void simulate(double delta) {

		double numOfTicks = delta / LENGTH_OF_ONE_TICK;

		Entity ball = null;
		for (Entity entity : entities) {

			if (entity instanceof Slime) {

				Slime slime = (Slime) entity;

				if (slime.location.y <= 0) {
					slime.jumping = false;
					slime.location.y = 0;
					slime.velocity.y = 0;
				}

				slime.location.x += slime.velocity.x * numOfTicks;
				slime.location.y += slime.velocity.y * numOfTicks;

				slime.velocity.y -= GRAVITY_EFFECT_PER_TICK * numOfTicks;

			} else {

				ball = entity;
			}
		}

		if (ball != null) {

			for (Entity e : entities) {
				if (e != ball) {
					Vector vec = ball.collidesWith(e);
					if (vec != null) {
						vec.normalise();

						float mag = ball.velocity.magintude();

						ball.velocity = new Vector(mag * vec.x, mag * vec.y);
					}
				}
			}

			if (ball.location.y <= 0) {
				ball.velocity.y = -ball.velocity.y;
			}
			if (ball.location.x <= -400 || ball.location.x >= 400) {
				ball.velocity.x = -ball.velocity.x;
			}

			ball.location.x += Math.round(ball.velocity.x * numOfTicks);
			ball.location.y += Math.round(ball.velocity.y * numOfTicks);

			ball.velocity.y -= 0.1 * numOfTicks;

		}
	}

	public void addPlayer(Slime slime) {
		entities.add(slime);
	}

	public void removePlayer(Slime slime) {
		entities.remove(slime);

		try {
			slime.notifyRemoval(addresses, port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addAddress(InetAddress address) {
		addresses.add(address);
	}

	public void removeAddress(InetAddress address) {
		addresses.remove(address);
	}

	public void addBall() {
		entities.add(new Entity((byte) 1, 10, -200, 300));
	}
}
