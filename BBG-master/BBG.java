import javax.swing.JFrame;

public class BBG {
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("BBG");
		int width = 410;
		int height = 610;
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		bbgPanel bbgPanel = new bbgPanel();
		frame.add(bbgPanel);
		frame.setVisible(true);
		bbgPanel.getGamePanel().startGame();
	}
}
