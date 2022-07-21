// Main Menu Class: used as the launcher for the game, that launches a new window of the game each time the button is pressed
// William Lin and Matthew Liu
// Jan 21, 2022
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu implements ActionListener {
	JFrame frame = new JFrame();
	JPanel title_panel = new JPanel();
	JButton start = new JButton("Start a New Game");
	JLabel textfield = new JLabel();
	ButtonGrid game;
	
	//constructor method for the Main Menu class.
	public MainMenu() {
		//sets up the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 350);
		frame.getContentPane().setBackground(new Color(211, 211, 211));
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		
		//sets up the text title
		textfield.setBackground(new Color(211, 211, 211));
		textfield.setForeground(new Color(10, 10, 10));
		textfield.setFont(new Font("Ink Free", Font.BOLD, 55));
		textfield.setHorizontalAlignment(JLabel.CENTER);
		textfield.setText("Minesweeper");
		textfield.setOpaque(true);

		// creates the title panel for the textfield, and then adds it
		title_panel.setLayout(new BorderLayout());
		title_panel.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2));
		title_panel.setBounds(0, 0, 500, 100);
		title_panel.add(textfield);

		// adds the title panel to the frame
		frame.add(title_panel, BorderLayout.NORTH);
		frame.setLayout(null);

		// initializes the start button
		start.setSize(350, 75);
		start.setLocation(75, 137);
		start.setBackground(new Color(200, 200, 200));
		start.setForeground(new Color(34, 139, 34));
		start.setFont(new Font("Courier New", Font.BOLD, 25));
		start.setFocusable(false);
		start.addActionListener(this);
		start.setMargin(new Insets(0, 0, 0, 0));
		start.setBackground(new Color(211, 211, 211));
		start.setBorder(BorderFactory.createBevelBorder(0));

		// adds the start button to the frame
		frame.add(start);
	}

  // What to do when button is clicked
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start) {
			if (game != null) {// closes the previous rendition of the game, if it exists
				game.close();
			}
			game = new ButtonGrid();// starts a new game
		}
	}
}
