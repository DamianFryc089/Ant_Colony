import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Display extends JPanel {

	ArrayList<Object> objects;
	Display(String[] args, ArrayList<Object> objects)
	{
		this.objects = objects;
		// Tworzenie okna
		JFrame frame = new JFrame("Ant Colony");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		if(args.length != 2) {
			// Okno w trybie okienkowym
			int width = 500;
			int height = 450;
			frame.setSize(new Dimension(width + 16, height + 39)); // args -> [int, int] // board size + borders?
			frame.setVisible(true);
			frame.getContentPane().add(this);
			frame.setVisible(true);
		}
		else {
			// Okno w trybie pełnoekranowym
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
			frame.setVisible(true);
			frame.getContentPane().add(this);
			frame.setVisible(true);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		for (int i = 0; i < objects.size(); i++) {
			g.setColor(objects.get(i).getColor());
			g.fillRect(objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getSize(), objects.get(i).getSize());
		}
	}
}
