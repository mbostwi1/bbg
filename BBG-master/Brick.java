import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Brick extends GameObject{
	
	private int pink, black, blue;
	
	public Brick(int x, int y, int w, int h){
		super(x, y, w, h);
		Random gen = new Random();

	}

	public void paint(Graphics page)
	{
		page.setColor(new Color(black));
		page.fillRect(x, y, width, height);
	}
}