package ea.demo.net.snake;

import ea.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServerState extends GameState implements VerbindungHergestelltReagierbar, Empfaenger {
	private ParticipantManager participantManager;
	private Map<NetzwerkVerbindung, int[]> players;
	private Text countText;
	private Server server;
	private Main main;
	private ServerPlayState next;

	public ServerState (Main main) {
		this.players = new LinkedHashMap<>();
		this.participantManager = new ParticipantManager();
		this.server = new Server(1337);
		this.server.netzwerkSichtbarkeit(true);
		this.server.setVerbindungHergestelltReagierbar(this);
		this.server.globalenEmpfaengerSetzen(this);
		this.server.setBroadcast(false);
		this.main = main;

		Figur loader = new Figur(20, 275, "res/loader.eaf");
		loader.faktorSetzen(1);

		Text loadText = new Text("waiting for clients...", 40, 272, "Wellbutrin", 14);
		loadText.setzeFarbe("hellgrau");

		Text hostText = new Text("press space to search for games", 200, 130, "Wellbutrin", 20);
		hostText.setAnker(Text.Anker.MITTE);
		hostText.setzeFarbe("hellgrau");

		countText = new Text("0 participants", 200, 160, "Wellbutrin", 16);
		countText.setAnker(Text.Anker.MITTE);
		countText.setzeFarbe("hellgrau");

		this.add(loader, loadText, hostText, countText);
	}

	@Override
	public void onKeyDown (int code) {
		int count = participantManager.count();

		if (count > 0 && code == Taste.ENTER && next == null) {
			server.netzwerkSichtbarkeit(false);
			next = new ServerPlayState(main, server);
		}

		if (code == Taste.LEERTASTE) {
			server.beendeVerbindung();
			server.netzwerkSichtbarkeit(false);
			main.setState(new ClientState(main));
		}
	}

	@Override
	public void verbindungHergestellt (String s) {
		participantManager.add(new Participant(s, participantManager));
		countText.inhaltSetzen(participantManager.count() + " participants");
	}

	@Override
	public void empfangeString (String s) {
		if (s.equals("ready")) {
			int x = (int) (Math.random() * 30) + 5;
			int y = (int) (Math.random() * 20) + 5;
			int color = players.size() * 60 % 255;
			NetzwerkVerbindung verbindung = server.naechsteVerbindungAusgeben();
			players.put(verbindung, new int[] {x, y, color});
			verbindung.sendeString("initial_info:" + color);

			if (next != null) {
				main.setState(next);
				next.init(players);
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
