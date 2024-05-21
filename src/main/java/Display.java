import javax.swing.*;
import java.awt.*;

public class Display extends JPanel {

	AntColony game;
	Display(String[] args, AntColony game)
	{
		this.game = game;

			// Tworzenie okna
		JFrame frame = new JFrame("Ant Colony");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

			// Znalezienie wartości wymiarów okna z argumentów
		int[] size = {1000, 600};
		if (args.length == 2 ) {
			try{
				size[0] = Integer.parseInt(args[0]);
				size[1] = Integer.parseInt(args[1]);
			} catch (NumberFormatException ignored) {}
		}

		if(true){//size[0] > 250 && size[1] > 250) {
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
		g.drawImage(game.gameMap.getBackgroundImage(), 0, 0, null);
		g.drawImage(game.gameMap.getScentImage(),0,0, null);
		g.drawImage(game.gameMap.getObjectsImage(),0,0,null);

			// Rysowanie liczby ticków
		g.drawChars(("Tick: " + game.tick).toCharArray(), 0,("Tick: " + game.tick).length(),10,15);
			// Rysowanie liczby mrówek
		g.drawChars(("Ants: " + Ant.antCounter).toCharArray(), 0,("Ants: " + Ant.antCounter).length(),10,30);
			// Rysowanie liczby pozostałego jedzenia na planszy
		g.drawChars(("Food left: " + Food.foodCounter).toCharArray(), 0,("Food left: " + Food.foodCounter).length(),10,45);
	}
}
