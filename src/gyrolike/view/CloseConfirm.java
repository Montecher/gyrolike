package gyrolike.view;

import java.awt.event.*;
import javax.swing.JOptionPane;

public class CloseConfirm extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent e) {
		if(JOptionPane.showConfirmDialog(e.getWindow(), "Really close the game?") == JOptionPane.YES_OPTION) System.exit(0);
	}
}
