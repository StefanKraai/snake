import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
	//toplevel variabele
	private boolean notGO;
	private boolean right = true;
	private boolean left = false;
	private boolean down = false;
	private boolean up = false;
	private Timer t;
	private int aantalx[] = new int[100];
	private int aantaly[] = new int[100];
	private int aantal;
	private int xAdd;
	private int yAdd;

	public Board() {
		t = new Timer(500,this);
		//zorgen voor dat keys gebruikt kunnen worden
		KeyListener listener = new MyKeyListener();
		addKeyListener(listener);
		setFocusable(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(501,501));
		initGame();
	}

	public void actionPerformed(ActionEvent e) {
		//aangeroepen door timer veld wordt wit gemaakt en dan als de slang verplaatst is wordt er gekeken of hij doorgaat
		Graphics g = getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,501,501);
		g.setColor(Color.BLACK);
		moveSnake();
		checkCollisionAdd();
		checkCollisionTail();
		draw(g);
	}

	public void initGame() {
		//beginwaarde van de game instellen
		notGO = true;
		right = true;
		left = false;
		down = false;
		up = false;
		aantal = 3;
		int x = 200;
		int y = 200;
		for(int i = 0; i<aantal; i++) {
			aantalx[i] = x;
			aantaly[i] = y;
			x -= 50;
		}
		newAdd();
		t.start();
	}

	public void newAdd() {
		//nieuw punt kan niet op slang
		boolean test = true;
		while(test) {
			test = false;
			//berekening punt
			int x = getRandom(450);
			int y = getRandom(450);
			int resx = 50 - x%50;
			int resy = 50 - y%50;
			xAdd = x + resx;
			yAdd = y + resy;
			for(int i = 0; i<aantal;i++) {
				//gaat door als het punt ook een punt van de slang is
				if(aantalx[i] == xAdd && aantaly[i] == yAdd) {
					test = true;
				}
			}
		}
	}

	public void checkCollisionAdd() {
		//als hij nieuw punt bereikt wordt die toegevoegd en nieuwe aangemaakt
		if(aantalx[0]==xAdd && aantaly[0]==yAdd) {
			aantal++;
			newAdd();
		}
	}

	public void checkCollisionTail() {
		//als hij zijn staart aanraakt is hij game over
		for(int i = 1; i<aantal; i++) {
			if(aantalx[0]==aantalx[i] && aantaly[0]==aantaly[i]) {
				notGO = false;
			}
		}
	}

	public void draw(Graphics g) {
		//als hij niet game over is
		if(notGO) {
			drawCheckBoard(g);
			drawSnake(g);
			drawAdd(g);
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
		for(int i = 0; i<10; i++) {
			for(int j = 0; j<10; j++) {
				g.drawRect(x,y,50,50);
				x+= 50;
			}
			x=0;
			y+=50;
		}
	}

	public void drawSnake(Graphics g) {
		//voor elk punt in de lijst wordt er een cirkel getekend
		g.setColor(Color.GREEN);
		g.fillOval(aantalx[0], aantaly[0],50,50);
		g.setColor(Color.BLACK);
		for(int i=1; i<aantal;i++) {
			g.fillOval(aantalx[i],aantaly[i],50,50);
		}
	}

	public void drawAdd(Graphics g) {
		//nieuw punt getekend
		g.setColor(Color.RED);
		g.fillOval(xAdd,yAdd,50,50);
	}

	public void moveSnake() {
		//elk punt neemt de positie van zijn voorganger over
		for(int i = aantal; i>0; i--) {
			aantalx[i] = aantalx[i-1];
			aantaly[i] = aantaly[i-1];
		}
		//positie voorste is een nieuw punt en als hij aan het eind van de frame is dan gaat hij verder aan de andere kant
		if(right) {
			if(aantalx[0] == 450) {
				aantalx[0] = 0;
			}
			else {
				aantalx[0] += 50;
			}
		}
		else if(left) {
			if(aantalx[0] == 0) {
				aantalx[0] = 450;
			}
			else {
				aantalx[0] -= 50;
			}
		}
		else if(up) {
			if(aantaly[0] == 0) {
				aantaly[0] = 450;
			}
			else {
				aantaly[0] -= 50;
			}
		}
		else if(down) {
			if(aantaly[0] == 450) {
				aantaly[0] = 0;
			}
			else {
				aantaly[0] += 50;
			}
		}
	}

	public void gameOver(Graphics g) {
		//uitgevoerd als game over
		t.stop();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,501,501);
		g.setColor(Color.BLACK);
		g.drawString("GAME OVER!!!! \n  \n  \n ",100,100);
		g.drawString("Om een nieuw spel te starten klik Y",100,120);
		g.drawString("Om het spel te stoppen klik N",100,140);
		g.drawString("Je hebt een lengte bereikt van: "+aantal,100,200);
	}

	private int getRandom(int i) {
		return (int)(i * Math.random());
	}

	public class MyKeyListener implements KeyListener {
		//kijkt welke key er ingevoerd wordt en veranderd de waarde
		@Override
		public void keyTyped(KeyEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {
			//rechts
			if(e.getKeyCode()==39) {
				left = false;
				up = false;
				down = false;
				right =true;
			}
			//links
			else if(e.getKeyCode()==37) {
				up = false;
				down = false;
				right = false;
				left =true;
			}
			//onder
			else if(e.getKeyCode()==40) {
				up = false;
				right = false;
				left = false;
				down = true;
			}
			//boven
			else if(e.getKeyCode()==38) {
				right = false;
				left = false;
				down = false;
				up = true;
			}
			//y
			else if(e.getKeyCode()==89 && !notGO) {
				initGame();
			}
			//n
			else if(e.getKeyCode()==78 && !notGO) {
				System.exit(0);
			}
			//System.out.println("keyPressed="+e.getKeyCode());
		}
		@Override
		public void keyReleased(KeyEvent e) {}
	}
}