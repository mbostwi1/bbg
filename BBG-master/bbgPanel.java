import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class bbgPanel extends JPanel{
	
	private GameScreen gameScreen;
	
	public bbgPanel(){
		JLabel scoreLabel = new JLabel("SCORE: 0");
		gameScreen = new GameScreen(scoreLabel);
		
		this.setLayout(new BorderLayout());
		this.add(scoreLabel, BorderLayout.NORTH);
		this.add(gameScreen, BorderLayout.CENTER);
	}
	
	public GameScreen getGamePanel(){
		return gameScreen;
	}
}
