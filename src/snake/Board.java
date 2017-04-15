package snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
	//toplevel variabele
	private static final int GAME_SPEED_MILISECONDS = 500;
	private static final int BOARD_SIZE = 500;
	private static final int BOX_SIZE = 50;
	private static final int REMAINING_SIZE = BOARD_SIZE - BOX_SIZE;
	private static final int TOTAL_BOXES = (BOARD_SIZE / BOX_SIZE)*(BOARD_SIZE / BOX_SIZE);

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

	public void actionPerformed(ActionEvent e) {
		//aangeroepen door timer veld wordt wit gemaakt en dan als de slang verplaatst is wordt er gekeken of hij doorgaat
		Graphics graphics = getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0,0,BOARD_SIZE,BOARD_SIZE);
		graphics.setColor(Color.BLACK);
		moveSnake();
		checkCollisionBait();
		checkCollisionTail();
		draw(graphics);
	}

	public void initGame() {
		//beginwaarde van de game instellen
		isGameOver = false;
		direction = Direction.RIGHT;
		bodyLength = 3;
		int x = 200;
		int y = 200;
		for(int length = 0; length<bodyLength; length++) {
			bodySnakeX[length] = x;
			bodySnakeY[length] = y;
			x -= BOX_SIZE;
		}
		newBait();
		timer.start();
	}

	public void newBait() {
		//nieuw punt kan niet op slang
		boolean noNewBait = true;
		while(noNewBait) {
			noNewBait = false;
			//berekening punt
			int x = getRandom(REMAINING_SIZE);
			int y = getRandom(REMAINING_SIZE);
			int resx = BOX_SIZE - x%BOX_SIZE;
			int resy = BOX_SIZE - y%BOX_SIZE;
			baitX = x + resx;
			baitY = y + resy;
			for(int length = 0; length<bodyLength;length++) {
				//gaat door als het punt ook een punt van de slang is
				if(bodySnakeX[length] == baitX && bodySnakeY[length] == baitY) {
					noNewBait = true;
				}
			}
		}
	}

	public void checkCollisionBait() {
		//als hij nieuw punt bereikt wordt die toegevoegd en nieuwe aangemaakt
		if(bodySnakeX[0]== baitX && bodySnakeY[0]== baitY) {
			bodyLength++;
			newBait();
		}
	}

	public void checkCollisionTail() {
		//als hij zijn staart aanraakt is hij game over
		for(int i = 1; i<bodyLength; i++) {
			if(bodySnakeX[0]== bodySnakeX[i] && bodySnakeY[0]==bodySnakeY[i]) {
				isGameOver = true;
			}
		}
	}

	public void draw(Graphics g) {
		//als hij niet game over is
		if(!isGameOver) {
			drawCheckBoard(g);
			drawSnake(g);
			drawBait(g);
		}
		//als hij game over is
		else {
			gameOver(g);
		}
	}

	public void drawCheckBoard(Graphics g) {
		//grid wordt getekend
		int x = 0;
		int y = 0;
		for(int boardX = 0; boardX<TOTAL_BOXES/2; boardX++) {
			for(int boardY = 0; boardY<TOTAL_BOXES/2; boardY++) {
				g.drawRect(x,y,BOX_SIZE,BOX_SIZE);
				x+= BOX_SIZE;
			}
			x=0;
			y+=BOX_SIZE;
		}
	}

	public void drawSnake(Graphics g) {
		//voor elk punt in de lijst wordt er een cirkel getekend
		g.setColor(Color.GREEN);
		g.fillOval(bodySnakeX[0], bodySnakeY[0],BOX_SIZE,BOX_SIZE);
		g.setColor(Color.BLACK);
		for(int length=1; length<bodyLength;length++) {
			g.fillOval(bodySnakeX[length],bodySnakeY[length],BOX_SIZE,BOX_SIZE);
		}
	}

	public void drawBait(Graphics g) {
		//nieuw punt getekend
		g.setColor(Color.RED);
		g.fillOval(baitX, baitY,BOX_SIZE,BOX_SIZE);
	}

	public void moveSnake() {
		//elk punt neemt de positie van zijn voorganger over
		for(int length = bodyLength; length>0; length--) {
			bodySnakeX[length] = bodySnakeX[length-1];
			bodySnakeY[length] = bodySnakeY[length-1];
		}
		//positie voorste is een nieuw punt en als hij aan het eind van de frame is dan gaat hij verder aan de andere kant
		switch (direction) {
			case RIGHT:
				if (bodySnakeX[0] == REMAINING_SIZE) {
					bodySnakeX[0] = 0;
				} else {
					bodySnakeX[0] += BOX_SIZE;
				}
				break;
			case LEFT:
				if (bodySnakeX[0] == 0) {
					bodySnakeX[0] = REMAINING_SIZE;
				} else {
					bodySnakeX[0] -= BOX_SIZE;
				}
				break;
			case UP:
				if (bodySnakeY[0] == 0) {
					bodySnakeY[0] = REMAINING_SIZE;
				} else {
					bodySnakeY[0] -= BOX_SIZE;
				}
				break;
			case DOWN:
				if (bodySnakeY[0] == REMAINING_SIZE) {
					bodySnakeY[0] = 0;
				} else {
					bodySnakeY[0] += BOX_SIZE;
				}
				break;
		}
	}

	public void gameOver(Graphics g) {
		//uitgevoerd als game over
		timer.stop();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,BOARD_SIZE,BOARD_SIZE);
		g.setColor(Color.BLACK);
		g.drawString("GAME OVER!!!! \n  \n  \n ",100,100);
		g.drawString("Om een nieuw spel te starten klik Y",100,120);
		g.drawString("Om het spel te stoppen klik N",100,140);
		g.drawString("Je hebt een lengte bereikt van: "+bodyLength,100,200);
	}

	private int getRandom(int i) {
		return Random.nextInt(i);
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
				System.exit(0);
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {}
	}
}