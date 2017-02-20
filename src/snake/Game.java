package snake;

import javax.swing.*;

class Game extends JFrame {

	public Game() {
		add(new Board());
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] arg) {
		JFrame ex = new Game();
		ex.setVisible(true);
	}
}