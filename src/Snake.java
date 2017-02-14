import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Snake extends JFrame {

	public Snake() {
		add(new Board());
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] arg) {
		JFrame ex = new Snake();
		ex.setVisible(true);
	}
}