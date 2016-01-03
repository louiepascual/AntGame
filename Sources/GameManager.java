/*
	GameManager.java
	1. Holds all the control for the game
	2. Holds all the GUI for the game
	3. Holds all the dialog boxes too actually
*/
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class GameManager extends JFrame {
	// Main components
	private Ant ant;
	private Board board;

	// UI components
	JPanel statusbar;
	JLabel gameStatus;
	JLabel coinStatus;

	// Other game needs
	int toWinVal;
	int bSize;

	public GameManager() {
		// Opens NewGameDialog to get BoardSize
		NewGameDialog ng = new NewGameDialog(this);
		ng.setModal(true);
		ng.setVisible(true);
		initUI();
	}

	public final void initUI() {
		// Setup Menubar 
		JMenuBar menubar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem closeGame = new JMenuItem("Close Game");
		JMenuItem exitGame = new JMenuItem("Exit Game");
		JMenu helpMenu = new JMenu("Help");
		JMenuItem howTo = new JMenuItem("HowTo");
		JMenuItem about = new JMenuItem("About");

		// Setups listeners in MenuItems
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				GameManager ex = new GameManager();
				ex.setVisible(true);
			}
		});

		closeGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.getPanel().setVisible(false);
				gameStatus.setText("Hey, let's play a new game!");
				coinStatus.setText("");
			}
		});

		exitGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		howTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HowToDialog ht = new HowToDialog(GameManager.this);
				ht.setVisible(true);
			}
		});

		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog a = new AboutDialog(GameManager.this);
				a.setVisible(true);
			}
		});

		gameMenu.add(newGame);
		gameMenu.add(closeGame);
		gameMenu.addSeparator();
		gameMenu.add(exitGame);
		helpMenu.add(howTo);
		helpMenu.add(about);
		menubar.add(gameMenu);
		menubar.add(helpMenu);

		setJMenuBar(menubar);


		// initialize game requirements
		ant = new Ant();
		board = new Board(bSize);
		add(board.getPanel(),BorderLayout.CENTER);
		board.getTile(0,0).setIcon(new ImageIcon(ant.getImage()));
		toWinVal = (board.getBoardSize()*board.getBoardSize())/4;

		// Add ArrowKey Listener to board
		board.getPanel().addKeyListener(new KeyInputCheck());
		board.getPanel().setFocusable(true);


		// Status bar
		statusbar = new JPanel();
		statusbar.setPreferredSize(new Dimension(-1,22));
		gameStatus = new JLabel("Normal Tile");
		statusbar.add(gameStatus);
		coinStatus = new JLabel("Coins: 0/" + Integer.toString(toWinVal));
		statusbar.add(coinStatus);
		statusbar.setBorder(LineBorder.createGrayLineBorder());
		add(statusbar,BorderLayout.SOUTH);
		

		// Set Window Attributes
		setTitle("AntGame");
		setSize(500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	/** Checks keyboard inputs **/
	private class KeyInputCheck extends KeyAdapter {
	
		public void keyReleased(KeyEvent e) {
			ant.keyReleased(e);
			if(checkMovement() == true) {
				ant.move();
				checkTile(ant.getX(),ant.getY());
				checkAntLife();
			} 	
		}
	}

	/** Checks if move is possible **/
	public boolean checkMovement() {
		int x = ant.getDX();
		int y = ant.getDY();
		int n = board.getBoardSize();

		// Checks if the given movement is beyond the borders
		if((x < 0) || (x >= n) || (y < 0) || (y >= n)) {
			// resets the value of dx/dy to coordinates
			ant.reset();

			// warn user of illegal move
			gameStatus.setText("Move not possible!");
			return false;
		}
		else {
			int cx = ant.getX();
			int cy = ant.getY();
			
			// Set departure tile to clear ant image
			board.getTile(cy,cx).setIcon(null);
			board.getTile(cy,cx).setBackground(Color.WHITE);

			// Set arrival tile to new ant image
			board.getTile(y,x).setIcon(new ImageIcon(ant.getImage()));
			
			return true;
		}
	}

	/** Checls and updates tile and ant coins **/
	public void checkTile(int x, int y) {
		// Get the tileEffect value
		int val = board.getTile(y,x).tileEffect();

		// Check the tileEffect value for the tile color change
		if(val == 1) {
			board.getTile(y,x).setBackground(Color.yellow);
			gameStatus.setText("Sweet! You got a coin!");
		}
		else if(val == -999) {
			board.getTile(y,x).setBackground(Color.blue);
			gameStatus.setText("Awww snap X_X Water Trap!");
		}
		else if(val == -1) {
			board.getTile(y,x).setBackground(Color.red);
			gameStatus.setText("The Hill Trap stole 1 coin!!");
		}
		else if(val == -3) {
			board.getTile(y,x).setBackground(Color.red);
			gameStatus.setText("The Mud Trap stole 3 coins!!");	
		}
		else if(val == 0) {
			gameStatus.setText("Nothing in here, move on.");
		}
			
		board.getTile(y,x).setOpaque(true); // to make color visible

		// Adjust ant coin value depending on tileEffect Value
		ant.setCoinEffect(val);

		// Adjust statusbar values
		String c = Integer.toString(ant.getCoins());
		coinStatus.setText("Coins: " + c + "/" + Integer.toString(toWinVal));
	}

	/** Shows Dialog depending on number of coins **/
	public void checkAntLife() {
		int c = ant.getCoins();
				
		if(c < 0) {
			new LostDialog(this);
		}
		else if(c >= toWinVal) {
			// victory
			gameStatus.setText("VICTORY!!");
			new VictoryDialog(this);
			
		}
	}

	/** JDialog notification when ant wins **/
	public class VictoryDialog extends JDialog {
		public VictoryDialog(Frame parent) {
			super(parent);
			vdUI();
		}

		private void vdUI() {
			JLabel ic = new JLabel(new ImageIcon("victory.png"));
			ic.setSize(5,5);
			JPanel centerPanel = new JPanel(new GridLayout(4,0));
			JLabel victoryLabel = new JLabel("You are Victorious!",
				SwingConstants.CENTER);
			JButton newGameBtn = new JButton("New Game");
			JButton closeGameBtn = new JButton("Close Game");

			centerPanel.add(ic);
			centerPanel.add(victoryLabel);
			centerPanel.add(newGameBtn);
			centerPanel.add(closeGameBtn);
			
			newGameBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GameManager ex = new GameManager();
					setVisible(false);
					dispose();
					ex.setVisible(true);
				}
			});

			closeGameBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});

			centerPanel.setBorder(new EmptyBorder(new Insets(40,60,40,60)));
			add(centerPanel);
			
			pack();
			setTitle("Victory!");
			setLocationRelativeTo(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);

		}
	}

	/** JDialog notification when ant dies **/
	public class LostDialog extends JDialog {
		public LostDialog(Frame parent) {
			super(parent);
			ldUI();
		}

		private void ldUI() {
			JLabel ic = new JLabel(new ImageIcon("rip.gif"));
			ic.setSize(5,5);
			JPanel centerPanel = new JPanel(new GridLayout(4,0));
			JLabel deadLabel = new JLabel("Your ant is dead!",
				SwingConstants.CENTER);
			JButton newGameBtn = new JButton("New Game");
			JButton closeGameBtn = new JButton("Close Game");

			centerPanel.add(ic);
			centerPanel.add(deadLabel);
			centerPanel.add(newGameBtn);
			centerPanel.add(closeGameBtn);
			
			newGameBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GameManager ex = new GameManager();
					setVisible(false);
					dispose();
					ex.setVisible(true);
				}
			});

			closeGameBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});

			centerPanel.setBorder(new EmptyBorder(new Insets(40,60,40,60)));
			add(centerPanel);
			
			pack();
			setTitle("X_X!");
			setLocationRelativeTo(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);

		}
	}

	/** JDialog notification for new Game **/
	public class NewGameDialog extends JDialog {
		public NewGameDialog(Frame parent) {
			super(parent);
			ngUI();
		}

		private void ngUI() {
			JLabel ic = new JLabel(new ImageIcon("new.gif"));
			ic.setSize(5,5);
			JPanel centerPanel = new JPanel(new GridLayout(4,0));
			JLabel inputLabel = new JLabel("Input Board Size: ",
				SwingConstants.CENTER);
			final JTextArea inputArea = new JTextArea();
			JButton startGameBtn = new JButton("Start Game!");
			
			centerPanel.add(ic);
			centerPanel.add(inputLabel);
			centerPanel.add(inputArea);
			centerPanel.add(startGameBtn);
			
			startGameBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					bSize = Integer.parseInt(inputArea.getText());

					// Catch invalid inputs
					if(bSize <= 0)
						bSize = 9;

					setVisible(false);
					dispose();
					
				}
			});

			centerPanel.setBorder(new EmptyBorder(new Insets(40,60,40,60)));
			add(centerPanel);
			
			pack();
			setTitle("New Game!");
			setLocationRelativeTo(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
	}

	/** JDialog for How To Play **/
	public class HowToDialog extends JDialog {
		public HowToDialog(Frame parent) {
			super(parent);
			htUI();
		}

		private void htUI() {
			JPanel centerPanel = new JPanel(new GridLayout(5,2));

			JLabel howtoIcon = new JLabel(new ImageIcon("howto.png"));
			JLabel howtoLabel = new JLabel("How to Play",
				SwingConstants.CENTER);
			JLabel arrowIcon = new JLabel(new ImageIcon("arrow.gif"));
			JLabel arrowLabel = new JLabel("Use arrow keys to move" +
				" the ant.");

			JLabel yellowIcon = new JLabel(" ");
			yellowIcon.setBackground(Color.yellow);
			yellowIcon.setOpaque(true);
			JLabel yellowLabel = new JLabel("Yellow tiles gives 1 coin");

			JLabel redIcon = new JLabel(" ");
			redIcon.setBackground(Color.red);
			redIcon.setOpaque(true);
			JLabel redLabel = new JLabel("Red tiles removes 1 (HillTrap)" +
			 " or 3 (MudTrap) coins");

			JLabel blueIcon = new JLabel(" ");
			blueIcon.setBackground(Color.blue);
			blueIcon.setOpaque(true);
			JLabel blueLabel = new JLabel("Blue tiles (WaterTrap) kills" +
				" the ant");

			JButton okBtn = new JButton("Ok!");	

			centerPanel.add(howtoIcon);
			centerPanel.add(howtoLabel);
			centerPanel.add(arrowIcon);
			centerPanel.add(arrowLabel);
			centerPanel.add(yellowIcon);
			centerPanel.add(yellowLabel);
			centerPanel.add(redIcon);
			centerPanel.add(redLabel);
			centerPanel.add(blueIcon);
			centerPanel.add(blueLabel);
			
			okBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					dispose();
					
				}
			});

			centerPanel.setBorder(new EmptyBorder(new Insets(40,60,40,60)));
			add(centerPanel);
			
			pack();
			setTitle("How To?");
			setLocationRelativeTo(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);	
		}
	}

	/** JDialog notification for new Game **/
	public class AboutDialog extends JDialog {
		public AboutDialog(Frame parent) {
			super(parent);
			abUI();
		}

		private void abUI() {

			JPanel centerPanel = new JPanel(new GridLayout(3,0));
			JLabel aLabel = new JLabel("Game coded in 2 days by ",
				SwingConstants.CENTER);
			JLabel bLabel = new JLabel("Louie Pascual, BS CS 2",
				SwingConstants.CENTER);
			JButton alrytBtn = new JButton("Alright!");
			
			centerPanel.add(aLabel);
			centerPanel.add(bLabel);
			centerPanel.add(alrytBtn);
			
			alrytBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					dispose();
					
				}
			});

			centerPanel.setBorder(new EmptyBorder(new Insets(40,60,40,60)));
			add(centerPanel);
			
			pack();
			setTitle("About");
			setLocationRelativeTo(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
	}	

}