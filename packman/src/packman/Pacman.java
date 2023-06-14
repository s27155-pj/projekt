package packman;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Pacman extends JFrame {
	 public Pacman() {
		 add(new Main());
	 }
	public static void main(String[] args) {
		Pacman pacman = new Pacman();
		pacman.setVisible(true);
		pacman.setTitle("Pacman");
		pacman.setSize(380,420);
		pacman.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pacman.setLocationRelativeTo(null);
		ImageIcon obraz;
		obraz = new ImageIcon("logo.png");
		pacman.setIconImage(obraz.getImage());
		pacman.setResizable(false);
	}

}
