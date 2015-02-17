package ea.demo.net.snake;

import ea.*;

import java.awt.*;
import java.util.ArrayList;

public class PlayState extends GameState implements Empfaenger {
	protected Main main;
	private Client client;
	private Text waitingText;
	private java.util.List<Snake> players;
	private int hue;
	private boolean running;

	public PlayState (Main main, String ip) {
		this.main = main;
		this.client = new Client(ip, 1337);
		this.client.empfaengerHinzufuegen(this);
		this.client.sendeString("ready");
		this.waitingText = new Text("waiting for host to start game", 200, 130, "Wellbutrin", 22);
		this.waitingText.setAnker(Text.Anker.MITTE);
		this.waitingText.setzeFarbe("grau");
		this.add(waitingText);
		this.running = true;
	}

	@Override
	public void onKeyDown (int code) {
		if (running) {
			switch (code) {
				case Taste.OBEN:
					this.client.sendeString("dir:" + Snake.UP + ":" + hue);
					break;
				case Taste.RECHTS:
					this.client.sendeString("dir:" + Snake.RIGHT + ":" + hue);
					break;
				case Taste.UNTEN:
					this.client.sendeString("dir:" + Snake.DOWN + ":" + hue);
					break;
				case Taste.LINKS:
					this.client.sendeString("dir:" + Snake.LEFT + ":" + hue);
					break;
				default:
					break;
			}
		} else {
			if (code == Taste.ENTER) {
				main.setState(new ClientState(main));
			}
		}
	}

	@Override
	public void empfangeString (String s) {
		String[] data = s.split(":", 2);
		String[] args;
		int count;

		switch (data[0]) {
			case "initial_info":
				this.hue = Integer.parseInt(data[1]);
				this.waitingText.setzeFarbe(Color.getHSBColor(hue / 255f, .9f, .9f));

				Rechteck background = new Rechteck(0, 0, 400, 300);
				background.farbeSetzen(Color.getHSBColor(hue / 255f, .9f, .15f));
				background.zIndex(-1);
				add(background);

				break;
			case "start":
				this.entfernen(this.waitingText);
				this.players = new ArrayList<>();

				args = data[1].split(":");
				count = Integer.parseInt(args[0]);

				for (int i = 0; i < count; i++) {
					Snake snake = new Snake(Integer.parseInt(args[1 + 2 * i]) * 10, Integer.parseInt(args[2 + 2 * i]) * 10, 60 * i % 255);
					players.add(snake);
					this.add(snake);
				}
				break;
			case "update":
				args = data[1].split(":");
				count = Integer.parseInt(args[0]);

				for (int i = 0; i < count; i++) {
					if (Boolean.parseBoolean(args[3 + 3 * i])) {
						players.get(i).add(Integer.parseInt(args[1 + 3 * i]) * 10, Integer.parseInt(args[2 + 3 * i]) * 10, false);
					}
				}
				break;
			case "supdate":
				args = data[1].split(":");
				count = Integer.parseInt(args[0]);

				for (int i = 0; i < count; i++) {
					if (Boolean.parseBoolean(args[3 + 3 * i])) {
						players.get(i).add(Integer.parseInt(args[1 + 3 * i]) * 10, Integer.parseInt(args[2 + 3 * i]) * 10, true);
					}
				}
				break;
			case "end":
				int hue = Integer.parseInt(data[1]);

				Rechteck blend = new Rechteck(0, 0, 400, 300);
				blend.farbeSetzen(new Color(0, 0, 0, 200));

				Text text = new Text("you " + (this.hue == hue ? "won" : "lost"), 200, 130, "Wellbutrin", 22);
				text.setzeFarbe(this.hue == hue ? "gruen" : "rot");
				text.setAnker(Text.Anker.MITTE);

				add(blend, text);

				running = false;

				break;
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
		if (running) {
			main.setState(new ErrorState(main, "connection lost"));
		}
	}
}
