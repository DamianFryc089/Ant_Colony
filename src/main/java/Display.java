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

			// Znalezienie wartości wymiarów okna z argumentów
		int[] size = {0, 0};
		if (args.length == 2 ) {
			try{
				size[0] = Integer.parseInt(args[0]);
				size[1] = Integer.parseInt(args[1]);
			} catch (NumberFormatException ignored) {}
		}

		if(size[0] > 250 && size[1] > 250) {
			// Okno w trybie okienkowym
			frame.setSize(new Dimension(size[0] + 16, size[1] + 39));
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
