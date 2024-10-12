import javax.swing.JFrame;

public class GameFrame extends JFrame{
	
	GameFrame(){
		this.add(new GamePanel());
		this.setTitle("Snake!!");
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
}
