package ea.demo.net.snake;

import ea.*;

import java.util.HashMap;
import java.util.Map;

public class ServerPlayState extends PlayState implements Ticker {
	private Server server;
	private Map<NetzwerkVerbindung, Snake> connections;
	private int alive;
	private int step;

	public ServerPlayState (Main main, Server server, Map<NetzwerkVerbindung, int[]> players) {
		super(main, "127.0.0.1");
		this.server = server;
		this.server.setBroadcast(false);
		this.server.globalenEmpfaengerSetzen(new ServerHandler());
		this.connections = new HashMap<>();

		String payload = "start:" + players.size();

		for (NetzwerkVerbindung conn : players.keySet()) {
			int[] info = players.get(conn);
			connections.put(conn, new Snake(info[0], info[1], info[2]));
			payload += ":" + info[0] + ":" + info[1];
		}

		server.sendeString(payload);

		alive = players.size();
		main.manager.anmelden(ServerPlayState.this, 100);
		step = 0;
	}

	@Override
	public void tick () {
		boolean add = step++ % 30 == 0;
		String payload = (add ? "supdate:" : "update:") + connections.size();

		for (NetzwerkVerbindung conn : connections.keySet()) {
			Snake snake = connections.get(conn);

			if (snake.isAlive()) {
				snake.next(add);
			}

			Rechteck head = snake.getHead();
			int x = (int) (head.getX() / 10);
			int y = (int) (head.getY() / 10);

			if (snake.isAlive()) {
				for (Snake s : connections.values()) {
					if (x < 0 || x >= 40 || y < 0 || y >= 30 || (snake == s ? head.schneidet(s.getNonHead()) : head.schneidet(s))) {
						snake.setAlive(false);
						alive--;

						if (alive == 0) {
							server.sendeString("end:" + snake.getHue());
							main.manager.abmelden(this);
							server.beendeVerbindung();
						}

						break;
					}
				}
			}

			payload += ":" + x + ":" + y + ":" + snake.isAlive();
		}

		server.sendeString(payload);
	}

	private class ServerHandler implements Empfaenger {
		@Override
		public void empfangeString (String s) {
			String[] info = s.split(":", 2);

			if (info[0].equals("dir")) {
				String[] args = info[1].split(":");

				int hue = Integer.parseInt(args[1]);

				for (NetzwerkVerbindung conn : connections.keySet()) {
					Snake snake = connections.get(conn);

					if (snake.getHue() == hue) {
						snake.setDirection(Integer.parseInt(args[0]));
					}
				}
			}
		}

		@Override
		public void empfangeInt (int i) {

		}

		@Override
		public void empfangeByte (byte b) {

		}

		@Override
		public void empfangeDouble (double v) {

		}

		@Override
		public void empfangeChar (char c) {

		}

		@Override
		public void empfangeBoolean (boolean b) {

		}

		@Override
		public void verbindungBeendet () {

		}
	}
}
