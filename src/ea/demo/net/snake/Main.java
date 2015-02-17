package ea.demo.net.snake;

import ea.Game;

public class Main extends Game {
	public static void main (String[] args) {
		new Main();
	}

	private GameState state;

	public Main () {
		super(400, 300, "Snake — Engine Alpha", false);
		setState(new ClientState(this));
	}

	public void setState (GameState state) {
		this.state = state;
		this.wurzel.leeren();
		this.wurzel.add(state);
	}

	@Override
	public void tasteReagieren (int code) {
		if (this.state == null) {
			return;
		}

		this.state.onKeyDown(code);
	}
}
