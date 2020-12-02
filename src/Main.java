import gyrolike.model.*;
import gyrolike.view.*;

public class Main {
	public static void main(String... args) throws Exception {
		Game game = new Game();
		game.loadLevel("1");
		GameWindow win = new GameWindow(game);
		win.setVisible(true);
	}
}
