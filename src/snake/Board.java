package snake;

import interfaceSnake.IBoard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener, IBoard {
	//toplevel variabele
	private final int GAME_SPEED_MILISECONDS = 500;
	private final int BOARD_SIZE = 500;
	private final int BOX_SIZE = 50;
	private final int REMAINING_SIZE = BOARD_SIZE - BOX_SIZE;
	private final int TOTAL_BOXES = (BOARD_SIZE / BOX_SIZE)*(BOARD_SIZE / BOX_SIZE);

	private boolean isGameOver;
	private Direction direction = Direction.RIGHT;
	private Timer timer;
	private int bodySnakeX[] = new int[TOTAL_BOXES];
	private int bodySnakeY[] = new int[TOTAL_BOXES];
	private int bodyLength;
	private int baitX;
	private int baitY;

	public Board() {
		timer = new Timer(GAME_SPEED_MILISECONDS,this);
		//zorgen voor dat keys gebruikt kunnen worden
		KeyListener keyListener = new MyKeyListener();
		addKeyListener(keyListener);
		setFocusable(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(BOARD_SIZE,BOARD_SIZE));
		initGame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//aangeroepen door timer veld wordt wit gemaakt en dan als de slang verplaatst is wordt er gekeken of hij doorgaat
		Graphics graphics = getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(X, X,BOARD_SIZE,BOARD_SIZE);
		graphics.setColor(Color.BLACK);
		moveSnake();
		checkCollisionBait();
		checkCollisionTail();
		draw(graphics);
	}

	@Override
	public void initGame() {
		//beginwaarde van de game instellen
		isGameOver = false;
		direction = Direction.RIGHT;
		bodyLength = 3;
		int x = 200;
		int y = 200;
		for(int length = X; length<bodyLength; length++) {
			bodySnakeX[length] = x;
			bodySnakeY[length] = y;
			x -= BOX_SIZE;
		}
		newBait(this);
		timer.start();
	}

	public static void newBait(Board board) {
		//nieuw punt kan niet op slang
		boolean noNewBait = true;
		while(noNewBait) {
			noNewBait = false;
			//berekening punt
			int x = (int) (board.REMAINING_SIZE * Math.random());
			int y = (int) (board.REMAINING_SIZE * Math.random());
			int resx = board.BOX_SIZE - x% board.BOX_SIZE;
			int resy = board.BOX_SIZE - y% board.BOX_SIZE;
			board.baitX = x + resx;
			board.baitY = y + resy;
			for(int length = X; length< board.bodyLength; length++) {
				//gaat door als het punt ook een punt van de slang is
				if(board.bodySnakeX[length] == board.baitX && board.bodySnakeY[length] == board.baitY) {
					noNewBait = true;
				}
			}
		}
	}

	@Override
	public void checkCollisionBait() {
		//als hij nieuw punt bereikt wordt die toegevoegd en nieuwe aangemaakt
		if(bodySnakeX[X]== baitX && bodySnakeY[X]== baitY) {
			bodyLength++;
			newBait(this);
		}
	}

	@Override
	public void checkCollisionTail() {
		//als hij zijn staart aanraakt is hij game over
		for(int i = 1; i<bodyLength; i++) {
			if(bodySnakeX[X]== bodySnakeX[i] && bodySnakeY[X]==bodySnakeY[i]) {
				isGameOver = true;
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		//als hij niet game over is
		if(!isGameOver) {
			drawCheckBoard(CheckBoard.createCheckBoard(g));
			drawSnake(g);
			drawBait(g);
		}
		//als hij game over is
		else {
			gameOver(g);
		}
	}

	@Override
	public void drawCheckBoard(CheckBoard checkBoard) {
		//grid wordt getekend
		int x = X;
		int y = X;
		for(int boardX = X; boardX<TOTAL_BOXES/2; boardX++) {
			for(int boardY = X; boardY<TOTAL_BOXES/2; boardY++) {
				checkBoard.getG().drawRect(x,y,BOX_SIZE,BOX_SIZE);
				x+= BOX_SIZE;
			}
			x= X;
			y+=BOX_SIZE;
		}
	}

	@Override
	public void drawSnake(Graphics g) {
		//voor elk punt in de lijst wordt er een cirkel getekend
		g.setColor(Color.GREEN);
		g.fillOval(bodySnakeX[X], bodySnakeY[X],BOX_SIZE,BOX_SIZE);
		g.setColor(Color.BLACK);
		for(int length=1; length<bodyLength;length++) {
			g.fillOval(bodySnakeX[length],bodySnakeY[length],BOX_SIZE,BOX_SIZE);
		}
	}

	@Override
	public void drawBait(Graphics g) {
		//nieuw punt getekend
		g.setColor(Color.RED);
		g.fillOval(baitX, baitY,BOX_SIZE,BOX_SIZE);
	}

	@Override
	public void moveSnake() {
		//elk punt neemt de positie van zijn voorganger over
		for(int length = bodyLength; length> X; length--) {
			bodySnakeX[length] = bodySnakeX[length-1];
			bodySnakeY[length] = bodySnakeY[length-1];
		}
		//positie voorste is een nieuw punt en als hij aan het eind van de frame is dan gaat hij verder aan de andere kant
		switch (direction) {
			case RIGHT:
				moveRight();
				break;
			case LEFT:
				moveLeft();
				break;
			case UP:
				moveUp();
				break;
			case DOWN:
				moveDown();
				break;
		}
	}

	private void moveDown() {
		if (bodySnakeY[X] == REMAINING_SIZE) {
            bodySnakeY[X] = X;
        } else {
            bodySnakeY[X] += BOX_SIZE;
        }
	}

	private void moveUp() {
		if (bodySnakeY[X] == X) {
            bodySnakeY[X] = REMAINING_SIZE;
        } else {
            bodySnakeY[X] -= BOX_SIZE;
        }
	}

	private void moveLeft() {
		if (bodySnakeX[X] == X) {
            bodySnakeX[X] = REMAINING_SIZE;
        } else {
            bodySnakeX[X] -= BOX_SIZE;
        }
	}

	private void moveRight() {
		if (bodySnakeX[X] == REMAINING_SIZE) {
            bodySnakeX[X] = X;
        } else {
            bodySnakeX[X] += BOX_SIZE;
        }
	}

	@Override
	public void gameOver(Graphics g) {
		new GameOver(g).invoke();

	}

	public class MyKeyListener implements KeyListener {
		//kijkt welke key er ingevoerd wordt en veranderd de waarde
		@Override
		public void keyTyped(KeyEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {
			//rechts
			if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
				direction = Direction.RIGHT;
			}
			//links
			else if(e.getKeyCode()== KeyEvent.VK_LEFT) {
				direction = Direction.LEFT;
			}
			//onder
			else if(e.getKeyCode()== KeyEvent.VK_DOWN) {
				direction = Direction.DOWN;
			}
			//boven
			else if(e.getKeyCode()== KeyEvent.VK_UP) {
				direction = Direction.UP;
			}
			//y
			else if(e.getKeyCode()== KeyEvent.VK_Y && isGameOver) {
				initGame();
			}
			//n
			else if(e.getKeyCode()== KeyEvent.VK_N && isGameOver) {
				System.exit(X);
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {}
	}

	private class GameOver {
		private Graphics g;

		public GameOver(Graphics g) {
			this.g = g;
		}

		public void invoke() {
			//uitgevoerd als game over
			timer.stop();
			g.setColor(Color.WHITE);
			g.fillRect(X, X,BOARD_SIZE,BOARD_SIZE);
			g.setColor(Color.BLACK);
			g.drawString("GAME OVER!!!! \n  \n  \n ",100,100);
			g.drawString("Om een nieuw spel te starten klik Y",100,120);
			g.drawString("Om het spel te stoppen klik N",100,140);
			g.drawString("Je hebt een lengte bereikt van: "+bodyLength,100,200);
		}
	}
}