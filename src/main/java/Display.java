import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;

public class Display extends JPanel implements KeyListener {

	AntColony simulation;
	Display(String[] args, AntColony simulation)
	{
		this.simulation = simulation;


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
//			frame.add(this);
			frame.setVisible(true);
		}
		else {
			// Okno w trybie pełnoekranowym
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
			frame.setVisible(true);
			frame.getContentPane().add(this);
//			frame.add(this);
			frame.setVisible(true);
		}

		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(simulation.gameMap.getBackgroundImage(), 0, 0, null);
		g.drawImage(simulation.gameMap.getScentImage(),0,0, null);
		g.drawImage(simulation.gameMap.getObjectsImage(),0,0,null);

			// Rysowanie liczby ticków
		g.drawChars(("Tick: " + simulation.tick).toCharArray(), 0,("Tick: " + simulation.tick).length(),10,15);
			// Rysowanie liczby mrówek
		g.drawChars(("Ants: " + Ant.antCounter).toCharArray(), 0,("Ants: " + Ant.antCounter).length(),10,30);
			// Rysowanie liczby pozostałego jedzenia na planszy
		g.drawChars(("Food left: " + Food.foodCounter).toCharArray(), 0,("Food left: " + Food.foodCounter).length(),10,45);
			// Rysowanie liczby fps
		g.drawChars(("FPS: "+(int)simulation.targetFPS).toCharArray(), 0,("FPS: "+(int)simulation.targetFPS).length(),getWidth()-32- 7*(""+(int)simulation.targetFPS).length(),10);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_SPACE)
			simulation.isPaused = !simulation.isPaused;

		if (e.getKeyChar() == 'x' || e.getKeyChar() == KeyEvent.VK_ESCAPE)
			System.exit(0);

		if (e.getKeyChar() == 's')
			SaveHandler.save(simulation);
		if (e.getKeyChar() == 'l') {
			simulation.isPaused = true;
			SaveHandler.load(simulation);
			repaint();
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			simulation.targetFPS = Math.min(1000, simulation.targetFPS*11/10);
			simulation.updateFPS();
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			simulation.targetFPS = Math.max(1, simulation.targetFPS*9/10);
			simulation.updateFPS();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
