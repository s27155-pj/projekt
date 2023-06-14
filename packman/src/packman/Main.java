package packman;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Main extends JPanel implements ActionListener{

		private Dimension d; 
		private final Font smallFont = new Font("Arial",Font.BOLD,12);
		private boolean inGame = false;
		private boolean dying = false;
		private final int blockSize = 24;
		private final int blocks = 15;
		private final int screenSize = blocks * blockSize;
		private final int ghosts = 4;
		private final int speed = 2;
		private int lives;
		private int score;
		private int [] dx, dy;
		private int[] ghostX,ghostY,ghostDX,ghostDY,ghostSpeed;
		private ImageIcon heart, ghost;
		private ImageIcon up,down,left,right;
		private Image heart1, ghost1;
		private Image up1,down1,left1,right1;
		private int pacmanX,pacmanY,pacmanDX,pacmanDY;
		private int reqDX,reqDY;
		
		private final short level[]= {
			0,19,26,26,26,18,26,18,26,18,26,26,26,22,0,
			19,28,0,0,0,21,0,21,0,21,0,0,0,25,22,
			21,0,0,0,19,28,0,21,0,25,22,0,0,0,21,
			17,26,26,26,20,0,0,21,0,0,17,26,26,26,20,
			21,0,0,0,17,26,18,24,18,26,20,0,0,0,21,
			21,0,0,0,21,0,21,0,21,0,21,0,0,0,21,
			21,0,19,26,20,0,29,0,29,0,17,26,22,0,21,
			21,0,21,0,21,0,0,0,0,0,21,0,21,0,21,
			17,26,28,0,25,26,18,26,18,26,28,0,25,26,20,
			21,0,0,0,0,0,21,0,21,0,0,0,0,0,21,
			17,26,22,0,19,26,28,0,25,26,22,0,19,26,20,
			21,0,21,0,21,0,0,0,0,0,21,0,21,0,21,
			21,0,25,26,16,26,26,18,26,26,16,26,28,0,21,
			25,22,0,0,21,0,0,29,0,0,21,0,0,19,28,
			0,25,26,26,28,0,0,0,0,0,25,26,26,28,0
			};
		private short [] screenData;
		private Timer timer;
		public Main() {
			loadImages();
			initVariables();
			addKeyListener(new TAdapter());
			setFocusable(true);
			initGame();
		}
		private void loadImages() {
			down = new ImageIcon("pacmanDown.png");
			up = new ImageIcon("pacmanUP.png");
			left = new ImageIcon("pacmanLeft.png");
			right = new ImageIcon("logo.png");
			ghost = new ImageIcon("duch.png");
			heart = new ImageIcon("serce.png");
			down1 =down.getImage();
			up1 = up.getImage();
			left1 = left.getImage();
			right1 = right.getImage();
			ghost1 = ghost.getImage();
			heart1 = heart.getImage();
		}
		private void initVariables() {
			screenData = new short[blocks*blocks];
			d = new Dimension(400,400);
			ghostX = new int [ghosts];
			ghostDX = new int [ghosts];
			ghostY = new int [ghosts];
			ghostDY = new int [ghosts];
			ghostSpeed = new int [ghosts];
			dx = new int [4];
			dy = new int [4];
			timer = new Timer(10,this);
			timer.start();
		}
		private void playGame(Graphics2D g2d) {
			if (dying) {
				death();
			}
			else { 
				move();
				draw(g2d);
				Ghosts(g2d);
				checkMaze();
				
			}
		}
		public void showIntroScreen(Graphics2D g2d){
			String Start = "Nacisnij Spacje by zaczac";
			g2d.setColor(Color.YELLOW);
			g2d.drawString(Start,screenSize/4,150);
		}
		public void drawScore(Graphics2D g2d) {
			g2d.setFont(smallFont);
			g2d.setColor(Color.YELLOW);
			String Score = "Score: "+score;
			g2d.drawString(Score,screenSize/2+96,screenSize+16);
			for(int i=0;i<lives;i++) {
				g2d.drawImage(heart1,i*28+8,screenSize+1,this);
			}
		}
		public void checkMaze() {
			int i =0;
			boolean finished = true;
			while(i<blocks*blocks&&finished) {
				if((screenData[i]&48)!=0) {
					finished = false;
				}
				i++;
			}
			if(finished) {
				score+=50;
			
			initLevel();
			}
		}
		private void death() {
			lives--;
			if(lives==0) {
				inGame = false;
			}
			continueLevel();
			}
		public void Ghosts(Graphics2D g2d) {
			int position;
			int licznik;
			for(int i=0;i<ghosts;i++) {
				if(ghostX[i]%blockSize==0&&ghostY[i]%blockSize==0) {
					position = ghostX[i]/blockSize+blocks*(int)(ghostY[i])/ blockSize;
					licznik= 0;
					if((screenData[position]&1)==0&&ghostDX[i]!=1) {
						dx[licznik]=-1;
						dy[licznik]=0;
						licznik++;
					}
					if((screenData[position]&2)==0&&ghostDY[i]!=1) {
						dx[licznik]=0;
						dy[licznik]=-1;
						licznik++;
					}
					if((screenData[position]&4)==0&&ghostDX[i]!=-1) {
						dx[licznik]=1;
						dy[licznik]=0;
						licznik++;
					}
					if((screenData[position]&8)==0&&ghostDY[i]!=-1) {
						dx[licznik]=0;
						dy[licznik]=1;
						licznik++;
					}
					if(licznik==0) {
						if((screenData[position]&15)==15) {
							ghostDY[i]=0;
							ghostDX[i]=0;
						}
						else {
							ghostDY[i]=-ghostDY[i];
							ghostDX[i]=-ghostDX[i];
						}
					}
					else {
						licznik = (int)(Math.random()*licznik);
						if(licznik>3) {
							licznik =3;
						}
						ghostDY[i]=dy[licznik];
						ghostDX[i]=dx[licznik];
					}
				}
				ghostX[i]=ghostX[i]+(ghostDX[i]*ghostSpeed[i]);
				ghostY[i]=ghostY[i]+(ghostDY[i]*ghostSpeed[i]);
				drawGhost(g2d,ghostX[i]+1,ghostY[i]+1);
			if(pacmanX>(ghostX[i]-12)&&pacmanX<(ghostX[i]+12)&&pacmanY>(ghostY[i]-12)&&pacmanY<(ghostY[i]+12)&&inGame) {
				dying=true;
			}
			}
		}
		public void drawGhost(Graphics2D g2d,int x,int y) {
			g2d.drawImage(ghost1, x, y, this);
		}

		public void move() {
			int position;
			short p;
			 if (pacmanX %blockSize == 0 && pacmanY % blockSize == 0) {
		            position = pacmanX / blockSize + blocks * (int) (pacmanY / blockSize);
		            p = screenData[position];

		            if ((p & 16) != 0) {
		                screenData[position] = (short) (p & 15);
		                score++;
		            }

		            if (reqDX != 0 || reqDY != 0) {
		                if (!((reqDX == -1 && reqDY == 0 && (p & 1) != 0)|| (reqDX == 1 && reqDY == 0 && (p & 4) != 0)|| (reqDX == 0 && reqDY == -1 && (p & 2) != 0)|| (reqDX == 0 && reqDY == 1 && (p & 8) != 0))) {
		                    pacmanDX = reqDX;
		                    pacmanDY = reqDY;
		                }
		            }
		            if ((pacmanDX == -1 && pacmanDY == 0 && (p & 1) != 0)|| (pacmanDX == 1 && pacmanDY == 0 && (p & 4) != 0)||(pacmanDX == 0 && pacmanDY == -1 && (p & 2) != 0)|| (pacmanDX == 0 && pacmanDY == 1 && (p & 8) != 0)) {
		                pacmanDX = 0;
		                pacmanDY = 0;
		            }
		        } 
		        pacmanX = pacmanX + speed * pacmanDX;
		        pacmanY = pacmanY + speed * pacmanDY;
		}
		public void draw(Graphics2D g2d) {
			if(reqDX==-1) {
				g2d.drawImage(left1, pacmanX+1, pacmanY+1, this); 
			}
			else
			if(reqDX==1) {
				g2d.drawImage(right1, pacmanX+1, pacmanY+1, this); 
			}
			else
			if(reqDY==-1) {
				g2d.drawImage(up1, pacmanX+1, pacmanY+1, this); 
			}
			else{
				g2d.drawImage(down1, pacmanX+1, pacmanY+1, this); 
			}
		}
		public void drawMaze(Graphics2D g2d) {
			short i =0;
		int x,y;
		for(y = 0;y<screenSize;y+=blockSize) {
			for(x=0;x<screenSize;x+=blockSize) {
				g2d.setColor(Color.BLUE);
				g2d.setStroke(new BasicStroke(5));
				if(level[i]==0) {
					g2d.fillRect(x, y, blockSize, blockSize);
				}
				if((screenData[i]&1)!=0) {
					g2d.drawLine(x, y,x,y+ blockSize -1);
				}
				if((screenData[i]&2)!=0) {
					g2d.drawLine(x, y,x+ blockSize-1,y);
				}
				if((screenData[i]&4)!=0) {
					g2d.drawLine(x+blockSize-1, y,x+blockSize-1 ,y+blockSize-1);
				}
				if((screenData[i]&8)!=0) {
					g2d.drawLine(x, y+ blockSize-1,x+blockSize-1,y+blockSize-1);
				}
				if((screenData[i]&16)!=0) {
					g2d.setColor(Color.YELLOW);
					g2d.fillOval(x+10, y+10, 6, 6);
				}
				i++;
			}
			
		}
		}
		public void initGame() {
			lives = 3;
			initLevel();
			score = 0;
			
		}
		private void initLevel() {
			int i;
			for(i =0;i<blocks*blocks;i++) {
				screenData[i] = level[i];
			}
			continueLevel();
		}
	
		
		
	
		
		private void continueLevel() {
			int dx = 1;
			
			for(int i =0;i<ghosts;i++) {
				ghostY[i] = 4* blockSize;
				ghostX[i] = 7* blockSize;
				ghostDY[i] =0;
				ghostDX[i] = dx;
				dx = -dx;
			ghostSpeed[i] = speed;
			}
			pacmanX = 7*blockSize;
			pacmanY = 13*blockSize;
			pacmanDX=0;
			pacmanDY=0;
			reqDX=0;
			reqDY =0;
			dying = false;
		}
	
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.black);
			g2d.fillRect(0, 0,d.width, d.height);
			drawMaze(g2d);
			drawScore(g2d);
			if(inGame) {
				playGame(g2d);
			}
			else {
				showIntroScreen(g2d);
			}
			Toolkit.getDefaultToolkit().sync();
			g2d.dispose();
		}
		class TAdapter extends KeyAdapter {

	        @Override
	        public void keyPressed(KeyEvent e) {

	            int key = e.getKeyCode();

	            if (inGame) {
	                if (key == KeyEvent.VK_LEFT) {
	                    reqDX = -1;
	                    reqDY = 0;
	                } else if (key == KeyEvent.VK_RIGHT) {
	                    reqDX = 1;
	                    reqDY = 0;
	                } else if (key == KeyEvent.VK_UP) {
	                    reqDX = 0;
	                    reqDY = -1;
	                } else if (key == KeyEvent.VK_DOWN) {
	                    reqDX = 0;
	                    reqDY = 1;
	                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
	                    inGame = false;
	                } 
	            } else {
	                if (key == KeyEvent.VK_SPACE) {
	                    inGame = true;
	                    initGame();
	                }
	            }
	        }
	}

		
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

}
