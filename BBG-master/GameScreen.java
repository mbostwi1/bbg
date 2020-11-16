import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
//import java.util.Random;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements ActionListener, KeyListener, MouseListener{
	
	//variables
	private int panelWidth;
	private int panelHeight;
	private JLabel scoreLabel;
	private boolean pause;
	private boolean pressed;
	private int keyCode;
	private Timer timer;
	private final int DELAY = 100;
	private int score;
	
	//paddle variables
	private Paddle paddle;
	private int paddleWidth;
	private int paddleHeight;
	private int paddleSpeed;
	private int paddleX;
	private int paddleY;
	
	//ball variables
	private Ball ball;
	private int ballRadius;
	private int ballDiameter;
	private int ballSpeed;
	private int ballX, ballY;
	private int diffX, diffY;
	private boolean stickToPaddle;
	
	//brick variables
	private ArrayList<Brick> bricks;
	private int brickHeight;
	private int brickWidth;
	private int brickX, brickY;
	
	//GameScreen constructor
	public GameScreen(JLabel sl)
	{
		this.setBackground(Color.WHITE);
		scoreLabel = sl;
		score = 0;
		
		//add listeners
		addKeyListener(this);
		addMouseListener(this);
		timer = new Timer(DELAY, this);
		setFocusable(true);
	}
	
	//start game
	public void startGame(){
		
		//screen
		panelWidth = this.getWidth();
		panelHeight = this.getHeight();
		System.out.println("Width: " + panelWidth);
		System.out.println("Height: " + panelHeight);
		pause = false;
		pressed = false;
		
		//paddle
		paddleX = 0;
		paddleY = panelHeight - 50;
		paddleWidth = 100;
		paddleHeight = 20;
		paddle = new Paddle(paddleX, paddleY, paddleWidth, paddleHeight);
		paddleSpeed = 10;
		
		//ball
		ballSpeed = 10;
		ballDiameter = 20;
		ballRadius = ballDiameter / 2;
		ballX = paddleX + paddleWidth / 2 - ballRadius;
		ballY = paddleY - ballDiameter;
		ball = new Ball(ballX, ballY, ballDiameter);
		stickToPaddle = true;
		diffX = 0;
		diffY = 0;
		
		//bricks
		brickX = 0;
		brickY = 40;
		brickWidth = 40;
		brickHeight = 20;
		bricks = new ArrayList<Brick>();
		int bricksCount = panelWidth / brickWidth;
		int rows = 3;
		
		//create bricks
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < bricksCount; j++)
			{
				bricks.add(new Brick(brickX, brickY, brickWidth, brickHeight));
				brickX += (brickWidth + 1);
			}
			brickX = 0;
			brickY += (brickHeight + 1);
		}
		

		timer.start();
	}
	
	//Color game objects
	public void paintComponent(Graphics screen)
	{
		super.paintComponent(screen);
		
		//paint paddle and ball
		paddle.paint(screen);
		ball.paint(screen);
		
		//paint all remaining bricks
		for (int i = 0; i < bricks.size(); i++)
			bricks.get(i).paint(screen);
		
	}
	
	
	//Key events

	@Override
	public void keyPressed(KeyEvent e) {
		pressed = true;
		keyCode = e.getKeyCode();
		
		//pause
		if (keyCode == KeyEvent.VK_P)
		{
			if (pause){
				pause = false;
				timer.start();
			}
			else{
				pause = true;
				timer.stop();
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		pressed = false;
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	
	//Mouse events

	@Override
	public void mouseClicked(MouseEvent event) {
		
		Point mouseClick = event.getPoint();
	    int clickX = (int)mouseClick.getX();
	    int clickY = (int)mouseClick.getY();
	    
//	    System.out.println(clickX + ", " + ballX);
	    
	    int xP = clickX - ballX;
	    int yP = ballY - clickY;
	    diffX = (int) (ballSpeed * (xP/Math.sqrt(Math.pow(xP, 2) + Math.pow(yP, 2))));
	    diffY = (int) (ballSpeed * (yP/Math.sqrt(Math.pow(xP, 2) + Math.pow(yP, 2))));
	    
	    ball.setVelocity(diffX, diffY);
	    
//	    System.out.println(diffX + ", " + diffY);
//	    System.out.println(clickX + ", " + clickY);
	    
	    stickToPaddle = false;
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	
	//Timer events

	@Override
	public void actionPerformed (ActionEvent event) {
		//long start = System.nanoTime();
		
		//move the paddle
		if (pressed){
			switch(keyCode)
			{
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					if (paddleX < (panelWidth - paddleWidth))
					{
						paddleX += paddleSpeed;
						paddle.setPos(paddleX, paddleY);
					}
					break;
				
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					if (paddleX > 0)
					{
						paddleX -= paddleSpeed;
						paddle.setPos(paddleX, paddleY);
					}
					break;
				default:
			}
		}
		
		//border collision logic
		if (ball.x >= (panelWidth - ballDiameter) ||
			ball.x <= 0){
			ball.diffX *= -1;
		}
		
		if (ball.y <= 0){
			ball.diffY *= -1;
		}
		
		else if (ball.y > (panelHeight - ballDiameter)){
			ball.diffY *= -1;
			timer.stop();
			JOptionPane.showMessageDialog(null, "YOU LOSE!");
		}
		
		//move ball
		if (!stickToPaddle){
			ball.move();
		}
		else{
			ballX = paddleX + paddleWidth / 2 - ballRadius;
			ball.setPos(ballX, ballY);
		}
		
		//paddle collision
		Point overlapP = ball.overlapPoint(paddle);
		if (overlapP != null && !stickToPaddle){
			ball.bounce(paddle, overlapP);
		}
		
		//brick collision
		for (int i = 0; i < bricks.size(); i++)
		{
			Point contactPoint = ball.overlapPoint(bricks.get(i));
			if (contactPoint != null){
				ball.bounce(bricks.get(i), contactPoint);
				bricks.remove(i);
				score++;
				scoreLabel.setText("SCORE: " + score);
				break;
			}
		}
		

				repaint();
		
		//Check win to see if win
		if (bricks.size() == 0){
			timer.stop();
			JOptionPane.showMessageDialog(null, "YOU WIN!");
		}
		
		//System.out.println(System.nanoTime() - start);
	}
}
