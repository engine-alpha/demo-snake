package ea.demo.net.snake;

import ea.Taste;
import ea.Text;

public class ErrorState extends GameState {
    private Main main;

    public ErrorState(Main main, String message) {
        this.main = main;

        Text text = new Text(message, 200, 130, "Wellbutrin", 22);
        text.setzeFarbe("rot");
        text.setAnker(Text.Anker.MITTE);

        Text info = new Text("press enter to continue", 200, 180, "Wellbutrin", 16);
        info.setzeFarbe("grau");
        info.setAnker(Text.Anker.MITTE);

        this.add(text, info);
    }

    @Override
    public void onKeyDown(int code) {
        if (code == Taste.ENTER) {
            main.setState(new ClientState(main));
        }
    }
}
