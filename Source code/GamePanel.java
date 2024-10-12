import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	static final int delay = 80;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int appleEaten = 0;//score
	int luck = 4;//high is good [Max = 100]
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	boolean colorFul = false;
	boolean rare = false;
	boolean firstTimeScreen = true;
	Timer timer;
	Random random;
	JButton restartButton;
	JButton startButton;
	JButton exitButton; 
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.DARK_GRAY);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		
		//restart Button Setting
		restartButton = new JButton("Restart");
		restartButton.addActionListener(new RestartButtonListener());
		
		restartButton.setVisible(false);
		restartButton.setFont(new Font("Sans serif",Font.ROMAN_BASELINE,25));
		restartButton.setBorderPainted(false);
		restartButton.setBackground(new Color(89,89,89, 100));
		restartButton.setFocusPainted(false);
		restartButton.setBorderPainted(false);
		this.add(restartButton);
		
		//start button Settings
		startButton = new JButton("Start");
		startButton.addActionListener(new StartButtonListener());
		
		startButton.setVisible(false);
		startButton.setFont(new Font("Sans serif",Font.ROMAN_BASELINE,25));
		startButton.setBorderPainted(false);
		startButton.setBackground(new Color(89,89,89, 100));
		startButton.setFocusPainted(false);
		startButton.setBorderPainted(false);
		this.add(startButton);
		
		//defining exit button
		exitButton = new JButton("Exit!!");
		exitButton.addActionListener(new exitButtonListener());
		
		exitButton.setVisible(false);
		exitButton.setFont(new Font("Sans serif",Font.ROMAN_BASELINE,25));
		exitButton.setBorderPainted(false);
		exitButton.setBackground(new Color(89,89,89, 100));
		exitButton.setFocusPainted(false);
		exitButton.setBorderPainted(false);
		this.add(exitButton);

		//startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer= new Timer(delay,this);
		timer.start();
	}
	public void StartScreen(Graphics g){
		g.setColor(Color.red);
		g.setFont(new Font("Sans serif",Font.BOLD,70));
		FontMetrics metricsStart = getFontMetrics(g.getFont());
		int startX = (SCREEN_WIDTH - metricsStart.stringWidth("Snake!!"))/2;
		int startY = SCREEN_HEIGHT/2;
		g.drawString("Snake!!", startX, startY);
		
		//defining start button location
		startButton.setVisible(true);
		int startButtonX = (SCREEN_WIDTH - startButton.getWidth()) / 2;
		int startButtonY = startX + metricsStart.getHeight() + 80;
		startButton.setBounds(startButtonX, startButtonY, startButton.getWidth(), startButton.getHeight());
		
		//location of exit button relative to "Snake!!"
		exitButton.setVisible(true);
		int exitButtonX = (SCREEN_WIDTH - startButton.getWidth()) / 2;
		int exitButtonY = startX + metricsStart.getHeight() + 160;
		exitButton.setBounds(exitButtonX, exitButtonY, exitButton.getWidth(), exitButton.getHeight());
		

	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if(firstTimeScreen) {
			StartScreen(g);
		}
		else {
			if(running) {
				//To show grid lines(for testing)
				/*
					//grid
					for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
						g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
						g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
					}
				
				*/
				
				if(rare) {
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
				}else {
					g.setColor(new Color(210, 115, 90));
				}
			    
				g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
				g.setColor(Color.white);
				g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
			
				for(int i=1; i<bodyParts; i++) {	
					if(colorFul) {
						g.setColor(new Color(0,random.nextInt(255),0));
					}else {
						g.setColor(new Color(40, 200, 150));
					}
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				//score display
				g.setColor(Color.white);
				g.setFont(new Font("Sans serif",Font.ROMAN_BASELINE,30));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("Score: "+appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Score"+appleEaten))/2, g.getFont().getSize());

			}
			else {
				//restartGame();
				gameOver(g);
			}
		}
		
		
	}
	public void newApple() {

		 boolean appleOnSnake = true;
		    while (appleOnSnake) {
		        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
		        
		        // Check if the apple position overlaps with any snake body segment
		        appleOnSnake = false;
		        for (int i = 0; i < bodyParts; i++) {
		            if (appleX == x[i] && appleY == y[i]) {
		                appleOnSnake = true;
		                break;
		            }
		        }
		    }
		
		if(random.nextInt(100)<(100-luck)) {
			rare=false;
		}else {
			rare=true;
			
		}
	}
	public void checkApple() {
		if(x[0] == appleX && y[0] == appleY) {
			bodyParts++;
			appleEaten++;
			if(rare) {
				luck = luck+2;
				if(colorFul) {
					colorFul=false;
				}else {
					colorFul = true;
				}
			}else {
				luck = luck + 1;
			}
			newApple();
		}
		
		
	}
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	public void checkCollision() {
		for(int i=bodyParts; i>0;i--) {
			//check if collides with body
			if(!colorFul) {
				if(x[0]==x[i] && y[0]==y[i]) {
					running = false;
				}
			}

			//check if head touches border
			if(x[0]<0 || x[0] >SCREEN_WIDTH || y[0]<0 || y[0] > SCREEN_HEIGHT){
				running =false;
			}
			
			/*it does not stop the timer so that when colorFul is true "Game Over" can change its color
			if(!running) {
				timer.stop();
			}
			*/
		}
		
	}
	private void restartGame() {
		//reseting every values
	    bodyParts = 6;
	    appleEaten = 0;
	    direction = 'R';
	    running = false;
	    colorFul = false;
	    rare = false;
	    
	    //Reset snake's position to initial state
	    for (int i = 0; i < bodyParts; i++) {
	        x[i] = 0;
	        y[i] = 0;
	    }

		
        timer.stop();
        startGame();
        restartButton.setVisible(false);//hiding button after 
        exitButton.setVisible(false);//hiding exit button after
        
	}	
	public void gameOver(Graphics g) {
		//score display
		g.setColor(Color.white);
		g.setFont(new Font("Sans serif",Font.ROMAN_BASELINE,30));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score"+appleEaten))/2, g.getFont().getSize());

		//game over display
		if(colorFul) {
			g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		}else {
			g.setColor(Color.red);
		}
		g.setFont(new Font("Sans serif",Font.BOLD,70));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		int gameOverX = (SCREEN_WIDTH - metrics2.stringWidth("GameOver"))/2;
		int gameOverY = SCREEN_HEIGHT/2;
		g.drawString("Game Over", gameOverX, gameOverY);
		
		//setting location of restart Button
		restartButton.setVisible(true);
		int restartButtonX = (SCREEN_WIDTH - restartButton.getWidth()) / 2;
		int restartButtonY = gameOverY + metrics2.getHeight() + 20;
		restartButton.setBounds(restartButtonX, restartButtonY, restartButton.getWidth(), restartButton.getHeight());
		
		//setting exit button here 
		exitButton.setVisible(true);
		int exitButtonX = (SCREEN_WIDTH - startButton.getWidth()) / 2;
		int exitButtonY = gameOverY + metrics2.getHeight() + 80;
		exitButton.setBounds(exitButtonX, exitButtonY, exitButton.getWidth(), exitButton.getHeight());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollision();
		}
		repaint();
	}
	
    class RestartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            restartGame(); //Call restartGame method when restart button is clicked
            luck = 4;
            requestFocusInWindow();
        }
    }

    class StartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	firstTimeScreen = false;
        	startButton.setVisible(false);
        	exitButton.setVisible(false);
        	startGame(); //call start to start the game dum!!
            requestFocusInWindow();
        }
    }
    
    class exitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	exitButton.setVisible(false);
            //Close the window here 
        	System.exit(0);
        	
            requestFocusInWindow();
        }
    }
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			if(firstTimeScreen == false) {//checking this so that can not change snake facing direction before the game starts.
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if(direction != 'R') {
						direction = 'L';
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(direction != 'L') {
						direction = 'R';
					}
					break;
				case KeyEvent.VK_UP:
					if(direction != 'D') {
						direction = 'U';
					}
					break;
				case KeyEvent.VK_DOWN:
					if(direction != 'U') {
						direction = 'D';
					}
					break;
				}
			}
		}
	}
 }