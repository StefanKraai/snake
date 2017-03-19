package snake;

import java.awt.*;

public class CheckBoard {
    private final Graphics g;

    private CheckBoard(Graphics g) {
        this.g = g;
    }

    public static CheckBoard createCheckBoard(Graphics g) {
        return new CheckBoard(g);
    }

    public Graphics getG() {
        return g;
    }
}
