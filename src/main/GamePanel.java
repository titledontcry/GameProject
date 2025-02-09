package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;
import entity.Player;
import tile.TileManager;
	public class GamePanel extends JPanel implements Runnable{
		//SCREEN settings
		final int originalTileSize = 16; // 32 pixels
		final int scale = 3;
		
		public final int tileSize = originalTileSize*scale; //48x48 pixels
		public final int maxScreenCol = 16;
		public final int maxScreenRow = 12;
		public final int screenWidth = maxScreenCol*tileSize;
		public final int screenHeight = maxScreenRow*tileSize;
		
		//WORLD PARAMETERS
		public final int maxWorldCol = 50;
		public final int maxWorldRow = 50;
		public final int worldWidth = tileSize * maxWorldCol;
		public final int worldHeight = tileSize * maxWorldRow;
		
		int FPS = 60;
		
		TileManager tileM = new TileManager(this);
		KeyHandler keyH = new KeyHandler();
		Thread gameThread;
		public CollisionChecker cChecker = new CollisionChecker(this);
		public Player player = new Player(this,keyH);
		
		public GamePanel(){
			this.setPreferredSize(new Dimension(screenWidth, screenHeight));
			this.setBackground(Color.black);
			this.setDoubleBuffered(true);
			this.addKeyListener(keyH);
			this.setFocusable(true);
		}
		public void startGameThread() {
			gameThread = new Thread(this);
			gameThread.start(); // call run method
		}
/*		public void run() {
			double drawInterval = 1000000000/FPS; //0.01666 second
			double nextDrawTime = System.nanoTime() +  drawInterval;
			while(gameThread != null) {
				
				// 1 UPDATE update information such as character position;
				update();
				// 2 DRAW draw the screen with the updated information
				repaint();
				
				try {
					double remainingTime = nextDrawTime - System.nanoTime();
					remainingTime = remainingTime/1000000;
					
					if(remainingTime < 0) {
						remainingTime = 0;
					}
					Thread.sleep((long) remainingTime);
					
					nextDrawTime += drawInterval;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		*/
		public void run() {
			double drawInterval = 1000000000/FPS;
			double delta = 0;
			long lastTime = System.nanoTime();
			long currentTime;
			long timer = 0;
			int drawCount = 0;
			while(gameThread != null) {
				currentTime = System.nanoTime();
				
				delta += (currentTime - lastTime)/drawInterval;
				timer += (currentTime - lastTime);
				lastTime = currentTime;
				if(delta >= 1) {
					update();
					repaint();
					delta--;
					drawCount++;
				}
				if(timer >= 1000000000) {
					System.out.println("FPS: "+drawCount);
					drawCount = 0;
					timer = 0;
				}
			}
		}
		public void update() {
			player.update();
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D)g; // has a bit more function
			tileM.draw(g2);//called draw inside tileManagher
			player.draw(g2);
			g2.dispose(); //save some memory
	}
}
