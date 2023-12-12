package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Font;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel boardGame;

	private final int X = 4;
	private final int Y = 3;
	private Cell[][] cellMatrix;

	private Cell firstCell;
	private int score;
	private int lives;
	private JLabel lblScore;
	private JLabel lblLives;
	private JLabel lblGameResult;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Juego de Memoria");
		setBounds(100, 100, 1031, 680);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		boardGame = new JPanel();
		
		
		lblScore = new JLabel("Puntaje: " + score);
		lblScore.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblScore.setBounds(820, 43, 108, 30);
		contentPane.add(lblScore);
		
		lblLives = new JLabel("Intentos: " + lives);
		lblLives.setForeground(new Color(255, 99, 71));
		lblLives.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblLives.setBounds(820, 143, 108, 30);
		contentPane.add(lblLives);
		
		lblGameResult = new JLabel();
		lblGameResult.setFont(new Font("Tahoma", Font.BOLD, 38));
		lblGameResult.setBounds(820, 251, 124, 124);
		lblGameResult.setVisible(false);
		contentPane.add(lblGameResult);
		
		JButton btnRestart = new JButton("Restart");
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame();
				boardGame.repaint();
				boardGame.setVisible(false);
				boardGame.setVisible(true);
			}
		});
		btnRestart.setForeground(UIManager.getColor("Button.foreground"));
		btnRestart.setBackground(UIManager.getColor("Button.background"));
		btnRestart.setBounds(828, 588, 89, 23);
		contentPane.add(btnRestart);
		
		startGame();

	}
	
	public void startGame() {
		initMatrix();
		loadMatrix();
		initBoardGame();
		loadBoardGame();
		firstCell = null;
		score = 0;
		lives = 5;
		lblScore.setText("Puntaje: " + score);
		lblLives.setText("Intentos: " + lives);
		
	}
	
	public void initMatrix() {
		this.cellMatrix = new Cell[X][Y];
	}
	
	public void initBoardGame() {
		boardGame.removeAll();
		boardGame.repaint();
		
		boardGame.setVisible(false);
		boardGame.setVisible(true);
		
		boardGame.setBorder(new LineBorder(new Color(0, 0, 0)));
		boardGame.setBackground(new Color(255, 255, 255));
		boardGame.setBounds(10, 11, 800, 600);
		contentPane.add(boardGame);
		boardGame.setLayout(new GridLayout(3, 4, 0, 0));
	}
	
	public void loadBoardGame() {
		for (Cell[] cellArr : cellMatrix) {
			for (Cell cell : cellArr) {
				boardGame.add(cell);
			}
		}
	}
	
	public void gameOver() {
		for (Cell[] cellArr : cellMatrix) {
			for (Cell cell : cellArr) {
				cell.cleanListener();
			}
		}
	}

	public void loadMatrix() {
		ClassLoader classLoader = Main.class.getClassLoader();

		//Para correr en Eclipse
//		URL questionMarkURL =  classLoader.getResource("images/Question_Mark.jpg");
		//Para correr como .jar en Windows
		URL questionMarkURL =  classLoader.getResource("resources/images/Question_Mark.jpg");
		BufferedImage imgQuestionMark = null;
		
		try {
			imgQuestionMark = ImageIO.read(questionMarkURL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<BufferedImage> images = getGameImages(classLoader);

		Random rand = new Random();
		for (int i = 0; i < 6; i++) {

			for (int j = 0; j < 2; j++) {
				Cell cell = new Cell(i, images.get(i), imgQuestionMark);

				cell.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent event) {
						cell.reveal();

						if (firstCell == null) {
							firstCell = cell;

						} else {
							if (compareImages(cell.getId())) {
								score++;
								lblScore.setText("Puntaje: " + score);
								
								if(score >= 6) {
									lblGameResult.setText(
										"<html>" 
										+ "You Won!"
										+ "</html>"	
										);
									lblGameResult.setForeground(new Color(0, 190, 0));
									lblGameResult.setVisible(true);
									gameOver();
								}
								
							} else {
								lives--;
								lblLives.setText("Intentos: " + lives);
								SwingUtilities.invokeLater(() -> {
									setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
									MyThread thread = new MyThread();
									thread.start();
									try {
										thread.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									firstCell.hide();
									cell.hide();
									setCursor(Cursor.getDefaultCursor());
								});
								
								if(lives < 1) {
									lblGameResult.setText(
											"<html>" 
											+ "You Lost!"
											+ "</html>"	
											);
									lblGameResult.setForeground(new Color(190, 0, 0));
									lblGameResult.setVisible(true);
									gameOver();
								}
							}
							
							SwingUtilities.invokeLater(() -> {
								firstCell = null;
							});
						}
					}
				});

				List<Point> emptySpaces = getEmptySpaces(cellMatrix);
				Point cellPos = emptySpaces.get(rand.nextInt(emptySpaces.size()));
				cellMatrix[cellPos.x][cellPos.y] = cell;
			}
		}
	}

	public boolean compareImages(int idSecond) {
		if (firstCell.getId() == idSecond) {
			return true;
		}

		return false;
	}

	public List<BufferedImage> getGameImages(ClassLoader classLoader) {
		List<BufferedImage> images = new ArrayList<BufferedImage>();

//		ClassLoader classLoader = Main.class.getClassLoader();
		
		String[] imagesName = new String[] {
			"Architect.png"	,
			"Armoudillo.png",
			"Eagle_Eye.png",
			"Pinchfist.png",
			"Seep.png",
			"Warthog.png"
		};
		
		for(String imgName : imagesName) {
			//Para correr en Eclipse
//			URL imageURL = classLoader.getResource("images/forts_character/" + imgName);

			//Para correr como .jar en Windows
			URL imageURL = classLoader.getResource("resources/images/forts_character/" + imgName);
			try {
				BufferedImage image = ImageIO.read(imageURL);
				images.add(image);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return images;
	}

	public List<Point> getEmptySpaces(Cell[][] matrix) {
		List<Point> emptySpaces = new ArrayList<Point>();
		for (int x = 0; x < X; x++) {
			for (int y = 0; y < Y; y++) {
				if (matrix[x][y] == null) {
					emptySpaces.add(new Point(x, y));
				}
			}
		}

		return emptySpaces;
	}
}
