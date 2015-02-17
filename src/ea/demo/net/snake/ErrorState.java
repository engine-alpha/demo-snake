package ea.demo.net.snake;

import ea.Taste;
import ea.Text;

public class ErrorState extends GameState {
	private Main main;

	public ErrorState (Main main, String message) {
		this.main = main;

		Text text = new Text(message, 200, 130, "Wellbutrin", 22);
		text.setzeFarbe("rot");
		text.setAnker(Text.Anker.MITTE);

		this.add(text);
	}

	@Override
	public void onKeyDown (int code) {
		if (code == Taste.ENTER) {
			main.setState(new ClientState(main));
		}
	}
}
