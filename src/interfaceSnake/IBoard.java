package interfaceSnake;

import snake.CheckBoard;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stefan on 19-3-2017.
 */
public interface IBoard {
    int X = 0;

    void actionPerformed(ActionEvent e);

    void initGame();

    void checkCollisionBait();

    void checkCollisionTail();

    void draw(Graphics g);

    void drawCheckBoard(CheckBoard checkBoard);

    void drawSnake(Graphics g);

    void drawBait(Graphics g);

    void moveSnake();

    void gameOver(Graphics g);
}
