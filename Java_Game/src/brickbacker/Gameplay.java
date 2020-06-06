package brickbacker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import javax.swing.JPanel;
import javax.swing.Timer;


public class Gameplay extends JPanel implements KeyListener, ActionListener
{
	private boolean play = false;
	private int score = 0;
	private int totalbricks = 48;
	private int delay = 15;
	
	private Timer timer;
	private int playerx = 310;
	private int ballpostx = 120;
	private int ballposty = 350;
	private int ballxdir = -1;
	private int ballydir = -2;
	
	private MapGenerator map;
	
	
	public Gameplay() {

		//
		map = new MapGenerator(4, 12);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
		
	}
	
	public void paint(Graphics g)
	{
		
		//background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//drawing map
		map.draw((Graphics2D)g);
		
		
		//border
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(0, 0, 3, 592);
		
		//scores
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" + score, 590, 30);
		
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerx, 550, 100, 8);
		
		//ball
		g.setColor(Color.yellow);
		g.fillOval(ballpostx, ballposty, 20, 20);
		
		// when you won the game
		if(totalbricks <= 0) {
			play = false;
			ballxdir = 0;
			ballydir = 0;
			
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You won:", 260, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		// when you lose the game
		if(ballposty > 570) {
			play = false;
			ballxdir = 0;
			ballydir = 0;
			
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, score :" + score, 190, 300);
			
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		
		g.dispose();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(play)
		{
			if(new Rectangle(ballpostx, ballposty, 20, 20).intersects(new Rectangle(playerx, 550, 30, 8))){
			
				ballydir = -ballydir;
				ballxdir = -2;
			}
			else if(new Rectangle(ballpostx, ballposty, 20, 20).intersects(new Rectangle(playerx + 70, 550, 30, 8)))
			{
				ballydir = -ballydir;
				ballxdir = ballxdir + 1;
			}
			else if(new Rectangle(ballposty, ballposty, 20, 20).intersects(new Rectangle(playerx + 30, 550, 40, 8)))
			{
				ballydir = -ballydir;
			}
			
			// check map collision with the ball
			A: for(int i = 0; i<map.map.length; i++)
			{
				for(int j = 0; j<map.map[0].length; j++)
				{
					if(map.map[i][j] >0) {
					int bricksX = j*map.brickwidth +80;
					int bricksY = j * map.brickheight +50;
					int brickwidth = map.brickwidth;
					int brickheight = map.brickheight;
					
					Rectangle rect = new Rectangle(bricksX, bricksY, brickwidth, brickheight);
					Rectangle ballrect = new Rectangle(ballpostx, ballposty, 20, 20);
					Rectangle brickrect = rect;
					
					if(ballrect.intersects(brickrect))
					{
						map.setbrickvalue(0, i, j);
						totalbricks--;
						score += 5;
						
						// when ball hit right or left of brick
						
						if (ballpostx + 19 <= brickrect.x || ballpostx + 1 >= brickrect.x + brickrect.width)
						{
							ballxdir = -ballxdir;
						}
						// when ball hits top or bottom of brick
						else {
							ballydir = -ballydir;
						}
						break A;
					}
					
				}
			}
			
			
			
			
			
			ballpostx += ballxdir;
			ballposty += ballydir;
			if(ballpostx < 0){
	        ballxdir = -ballxdir;
				
			}
			if(ballposty < 0){
			ballydir = -ballydir;
			}
			if(ballpostx > 670){
			ballxdir = -ballxdir;	
			}
		 }
			repaint();
		
	  }
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if(playerx >= 600)
			{
				playerx = 600;
		}else {
			moveRight();
		}
		
	}
	if(e.getKeyCode() == KeyEvent.VK_LEFT)
	{
		if(playerx <10) {
			playerx = 10;
			
		}else {
			moveLeft();
		}
	}
	
	if(e.getKeyCode() == KeyEvent.VK_ENTER)
	{
		if(!play) {
			play = true;
			ballpostx = 120;
			ballposty = 350;
			ballxdir = -1;
			ballydir = -2;
			playerx = 310;
			score = 0;
			totalbricks= 21;
			
			map = new MapGenerator(3, 7);
			repaint();
		}
	}
	}

	public void moveRight()
	{
		play = true;
		playerx+=20;
	}
	
	public void moveLeft()
	{
		play = true;
		playerx-=20;
	}

	

}
