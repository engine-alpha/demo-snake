package ea.demo.net.snake;

import ea.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientState extends GameState implements ServerGefundenReagierbar {
	private List<Text> servers;
	private Map<String, Long> serverList;
	private int selection;
	private Main main;

	public ClientState (Main main) {
		this.servers = new ArrayList<>();
		this.serverList = new HashMap<>();
		this.main = main;

		Figur loader = new Figur(20, 275, "res/loader.eaf");
		loader.faktorSetzen(1);

		Text loadText = new Text("searching for servers...", 40, 272, "Wellbutrin", 14);
		loadText.setzeFarbe("hellgrau");

		Text hostText = new Text("press space to host game", 200, 40, "Wellbutrin", 20);
		hostText.setAnker(Text.Anker.MITTE);
		hostText.setzeFarbe("hellgrau");

		this.add(loader, loadText, hostText);
		ServerSuche.start(this);
	}

	@Override
	public void onKeyDown (int code) {
		int count = servers.size();

		if (count > 0) {
			if (code == Taste.OBEN || code == Taste.UNTEN) {
				servers.get(selection).setzeFarbe("grau");
				selection = code == Taste.OBEN ? --selection : ++selection % count;
				selection = selection < 0 ? count - 1 : selection;
				servers.get(selection).setzeFarbe("weiss");
			} else if(code == Taste.ENTER) {
				main.setState(new PlayState(main, servers.get(selection).gibInhalt()));
			}
		}

		if (code == Taste.LEERTASTE) {
			ServerSuche.stop();
			main.setState(new ServerState(main));
		}
	}

	@Override
	public void serverGefunden (String s) {
		if (serverList.containsKey(s)) {
			serverList.put(s, System.currentTimeMillis());
		} else {
			serverList.put(s, System.currentTimeMillis());
			Text text = new Text(s, 200, 100 + servers.size() * 20, "Wellbutrin", 16);
			text.setAnker(Text.Anker.MITTE);
			text.setzeFarbe(serverList.size() == 1 ? "weiss" : "grau");
			servers.add(text);
			this.add(text);
		}
	}
}
