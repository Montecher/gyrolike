package gyrolike.view;

import java.util.Date;

import javax.swing.*;

import gyrolike.util.Functional;
import gyrolike.util.ResourceLoader;
import gyrolike.model.*;

@SuppressWarnings("serial")
public class GameWindow extends JFrame {
	public GameWindow(Game game) {
		super("Gyrolike");
		setIconImage(ResourceLoader.getImage("/icon.png"));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new CloseConfirm());

		Gamepad gamepad = new Gamepad();
		gamepad.addListener(game::setKeys);
		gamepad.addListener(Key.RED, () -> game.setSelectedColor(Tile.COLUMN_RED));
		gamepad.addListener(Key.BLUE, () -> game.setSelectedColor(Tile.COLUMN_BLUE));
		gamepad.addListener(Key.CUP, () -> game.setColumnDirection(Direction.UP));
		gamepad.addListener(Key.CDOWN, () -> game.setColumnDirection(Direction.DOWN));
		addKeyListener(gamepad);

		game.addTickListener(Functional.CodeBlock.toRunnable(() -> SwingUtilities.invokeAndWait(() -> {
			long timeBefore = new Date().getTime();
			this.repaint();
			long timeAfter = new Date().getTime();
			if(game.DEBUG) System.out.println("Frame: "+(timeAfter-timeBefore)+"ms");
		})));

		add(new GamePanel(game));
		pack();
		setMinimumSize(getSize());
		setMaximumSize(getSize());
	}
}
