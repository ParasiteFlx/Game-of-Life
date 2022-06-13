import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.colorchooser.*;


//import java.util.Timer;
import java.util.TimerTask;
import java.awt.Insets;
import java.awt.Component;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 * The main frame with the matrix;
 *
 */
public class GameOfLIfe extends JFrame{
	Color c=BLUE;
    
	private JPanel contentPane;

	final static int GRID_PANEL_BORDER_WIDTH = 5, N = 11, CELLSIZE = 100;

	// static final JLayeredPane layer = new JLayeredPane();
	static JPanel panel = new JPanel();
	static final int SM_CELL_BORDER_WIDTH = 1;
	static LineBorder SMcellBorder = new LineBorder(BLACK, SM_CELL_BORDER_WIDTH);

	static JTextField[][] cells = new JTextField[N][N];
	
	//timer
	Timer timer;
	
	 /**
	   * The original matrix that will be used at the first step
	   */
	  int[][] generatiaAnterioara = new int[11][11];
	 private CustomJPanel pnlGrid;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameOfLIfe frame = new GameOfLIfe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public GameOfLIfe() {
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				int W = 4;
				int H = 3;
				Rectangle b = arg0.getComponent().getBounds();
				arg0.getComponent().setBounds(b.x, b.y, b.width, b.width * H / W);
				// arg0.getComponent().setBounds(b.x, b.y, b.height, b.height*H/W);
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 718, 781);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnChangeColour = new JButton("Change Colour");
		btnChangeColour.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				JColorChooser palette = new JColorChooser();
				Color c2 = palette.showDialog(null, "Please choose a new color", Color.RED);
			   c=c2;
			   
			}
		});
		menuBar.add(btnChangeColour);
		makeGrid();
	}

	private void makeGrid() {
		pnlGrid = new CustomJPanel();
		pnlGrid.setLayout(new GridLayout(N, N));
		pnlGrid.setBackground(Color.DARK_GRAY);
		pnlGrid.setBorder(BorderFactory.createLineBorder(Color.darkGray, GRID_PANEL_BORDER_WIDTH));
		pnlGrid.setLayout(new GridLayout(N, N));

		MouseListener mouseHandler = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				doMousePressed(e);
			}
		};

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				cells[i][j] = new JTextField() {
					@Override
					public Dimension getPreferredSize() {
						return new Dimension(20, 20);
					}
				};
				cells[i][j].setText(" ");
				cells[i][j].setPreferredSize(new Dimension(CELLSIZE, CELLSIZE));
				cells[i][j].setHorizontalAlignment(JTextField.CENTER);
				cells[i][j].setFocusTraversalKeysEnabled(false);
				cells[i][j].setBorder(SMcellBorder);
				cells[i][j].setEditable(false);
				cells[i][j].addMouseListener(mouseHandler);
				pnlGrid.add(cells[i][j]);
			}
		}
		panel = new JPanel();
		panel.setBackground(new Color(240, 240, 240));
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {

				int key = arg0.getKeyCode();

				if (key == KeyEvent.VK_ENTER) {
					complete();
				}

			}
		});
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(pnlGrid);
		panel.setVisible(true);
		
		getContentPane().setPreferredSize(new Dimension(700,600));
		getContentPane().add(panel);
		
		JPanel interiorJpanel = new JPanel();
		interiorJpanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		interiorJpanel.setPreferredSize(new Dimension(100,100));
		panel.add(interiorJpanel);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(13, 5, 73, 25);
		btnStart.setPreferredSize(new Dimension(73, 25));
		btnStart.setActionCommand("Start ");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				complete();
			}
		});
		interiorJpanel.setLayout(null);
		interiorJpanel.add(btnStart);
		
			
			JButton btnPause = new JButton("Pause");
			btnPause.setBounds(13, 35, 73, 25);
			btnPause.setPreferredSize(new Dimension(73, 25));
			btnPause.setActionCommand("Pause");
			btnPause.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					timer.stop();
				}
			});
			interiorJpanel.add(btnPause);
		
		JButton btnRestart = new JButton("Clear");
		btnRestart.setBounds(13, 65, 73, 23);
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			  
			for (int i1 = 0; i1 < 11; i1++) {
					for (int j1 = 0; j1 < 11; j1++) {
                        
						generatiaAnterioara[i1][j1]=0;
						cells[i1][j1].setBackground(panel.getBackground());

					}
				}

				
			}
		});
		interiorJpanel.add(btnRestart);
		
//		JButton btnStart = new JButton("Start");
//		panel.add(btnStart);
		 pack();
		
		setVisible(true);
	}
	
//	private JPanel getLoginScreen() {
//		
//		JPanel panel = new JPanel();
//		panel.setBackground(BLUE);
//		
//		return panel;
//	}
	

	public void doMousePressed(MouseEvent e) {
		Point p = e.getPoint();
		System.out.println("Source point = " + p + " within " + e.getComponent());
		e.getComponent().setBackground(c);
		for(int contor1=0; contor1< 11; contor1++) {
			for(int contor2 =0; contor2 < 11; contor2++) {
				if(cells[contor1][contor2].equals(e.getComponent())) {
					System.out.println("x si y"+ contor1+ " " + contor2);
					generatiaAnterioara[contor1][contor2] = 1;
				}
			}
		}
		p = SwingUtilities.convertPoint(e.getComponent(), p, e.getComponent().getParent());
		System.out.println("Converted point = " + p + " within " + e.getComponent().getParent());
	}
	
	
		  public void  calcul() {
			 
			  
			  /**
			   * The matrix that will be used to generate the next version of generatiaAnterioara;
			   */
				int[][] generatiaUrmatoare = new int[11][11]; 
				
				ActionListener taskPerformer = new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		                executeStep(generatiaAnterioara, generatiaUrmatoare);
		            }
		        };
		        timer = new Timer(500 ,taskPerformer);
		        timer.setRepeats(true);
		        timer.start();
				
				
	      }
		  
		  
		  public void executeStep(int[][] generatiaAnterioara, int[][] generatiaUrmatoare) {
				for (int i = 0; i < 11; i++) {
					for (int j = 0; j < 11; j++) {
						if (j == 0 && i == 0) {
							int numarVecini = 0;
							if (generatiaAnterioara[i][j + 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j + 1] == 1) {
								numarVecini++;
							}
							if (numarVecini < 2) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini > 3) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini == 3 && generatiaAnterioara[i][j] == 0) {
								generatiaUrmatoare[i][j] = 1;
							} else if ((numarVecini == 2 || numarVecini == 3) && generatiaAnterioara[i][j] == 1) {
								generatiaUrmatoare[i][j] = 1;
							}
							
						} else if (j == 10 && i == 0) {
							int numarVecini = 0;
							if (generatiaAnterioara[i][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j - 1] == 1) {
								numarVecini++;
							}

							if (numarVecini < 2) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini > 3) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini == 3 && generatiaAnterioara[i][j] == 0) {
								generatiaUrmatoare[i][j] = 1;
							} else if ((numarVecini == 2 || numarVecini == 3) && generatiaAnterioara[i][j] == 1) {
								generatiaUrmatoare[i][j] = 1;
							}

							// System.out.println("Elementul matrice[" + i + "][" + j + "]" + "are " +
							// numarVecini + " vecini");

						} else if (j == 0 && i == 10) {
							int numarVecini = 0;
							if (generatiaAnterioara[i - 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i][j + 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j + 1] == 1) {
								numarVecini++;
							}

							if (numarVecini < 2) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini > 3) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini == 3 && generatiaAnterioara[i][j] == 0) {
								generatiaUrmatoare[i][j] = 1;
							} else if ((numarVecini == 2 || numarVecini == 3) && generatiaAnterioara[i][j] == 1) {
								generatiaUrmatoare[i][j] = 1;
							}

							// System.out.println("Elementul matrice[" + i + "][" + j + "]" + "are " +
							// numarVecini + " vecini");
						} else if (j == 10 && i == 10) {
							int numarVecini = 0;
							if (generatiaAnterioara[i - 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j - 1] == 1) {
								numarVecini++;
							}

							if (numarVecini < 2) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini > 3) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini == 3 && generatiaAnterioara[i][j] == 0) {
								generatiaUrmatoare[i][j] = 1;
							} else if ((numarVecini == 2 || numarVecini == 3) && generatiaAnterioara[i][j] == 1) {
								generatiaUrmatoare[i][j] = 1;
							}

							// System.out.println("Elementul matrice[" + i + "][" + j + "]" + "are " +
							// numarVecini+ " vecini");
						}

						else if (j == 0) {
							int numarVecini = 0;
							if (generatiaAnterioara[i][j + 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j + 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j + 1] == 1) {
								numarVecini++;
							}

							if (numarVecini < 2) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini > 3) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini == 3 && generatiaAnterioara[i][j] == 0) {
								generatiaUrmatoare[i][j] = 1;
							} else if ((numarVecini == 2 || numarVecini == 3) && generatiaAnterioara[i][j] == 1) {
								generatiaUrmatoare[i][j] = 1;
							}

							// System.out.println("Elementul matrice[" + i + "][" + j + "]" + "are " +
							// numarVecini+ " vecini");

						} else if (i == 0) {
							int numarVecini = 0;
							if (generatiaAnterioara[i][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i][j + 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j + 1] == 1) {
								numarVecini++;
							}

							if (numarVecini < 2) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini > 3) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini == 3 && generatiaAnterioara[i][j] == 0) {
								generatiaUrmatoare[i][j] = 1;
							} else if ((numarVecini == 2 || numarVecini == 3) && generatiaAnterioara[i][j] == 1) {
								generatiaUrmatoare[i][j] = 1;
							}
							// System.out.println("Elementul matrice[" + i + "][" + j + "]" + "are " +
							// numarVecini+ " vecini");

						} else if (j == 10) {
							int numarVecini = 0;
							if (generatiaAnterioara[i - 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j - 1] == 1) {
								numarVecini++;
							}

							if (numarVecini < 2)
								if (numarVecini < 2) {
									generatiaUrmatoare[i][j] = 0;
								} else if (numarVecini > 3) {
									generatiaUrmatoare[i][j] = 0;
								} else if (numarVecini == 3 && generatiaAnterioara[i][j] == 0) {
									generatiaUrmatoare[i][j] = 1;
								} else if ((numarVecini == 2 || numarVecini == 3) && generatiaAnterioara[i][j] == 1) {
									generatiaUrmatoare[i][j] = 1;
								}

							// System.out.println("Elementul matrice[" + i + "][" + j + "]" + "are " +
							// numarVecini+ " vecini");

						} else if (i == 10) {
							int numarVecini = 0;
							if (generatiaAnterioara[i][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i][j + 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j + 1] == 1) {
								numarVecini++;
							}

							if (numarVecini < 2) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini > 3) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini == 3 && generatiaAnterioara[i][j] == 0) {
								generatiaUrmatoare[i][j] = 1;
							} else if ((numarVecini == 2 || numarVecini == 3) && generatiaAnterioara[i][j] == 1) {
								generatiaUrmatoare[i][j] = 1;
							}

							// System.out.println("Elementul matrice[" + i + "][" + j + "]" + "are " +
							// numarVecini+ " vecini");

						} else {

							int numarVecini = 0;
							if (generatiaAnterioara[i][j + 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j + 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i - 1][j - 1] == 1) {
								numarVecini++;
							}
							if (generatiaAnterioara[i + 1][j + 1] == 1) {
								numarVecini++;
							}

							if (numarVecini < 2) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini > 3) {
								generatiaUrmatoare[i][j] = 0;
							} else if (numarVecini == 3 && generatiaAnterioara[i][j] == 0) {
								generatiaUrmatoare[i][j] = 1;
							} else if ((numarVecini == 2 || numarVecini == 3) && generatiaAnterioara[i][j] == 1) {
								generatiaUrmatoare[i][j] = 1;
							}

							// System.out.println("Elementul matrice[" + i + "][" + j + "]" + "are " +
							// numarVecini+ " vecini");

						}
					}
				}

				// if (j == 10)
				// System.out.println();
				//
				for (int i1 = 0; i1 < 11; i1++) {
					for (int j1 = 0; j1 < 11; j1++) {

						cells[i1][j1].setBackground(panel.getBackground());

					}
				}

				for (int i1 = 0; i1 < 11; i1++) {
					for (int j1 = 0; j1 < 11; j1++) {
						if (generatiaUrmatoare[i1][j1] == 1)
							cells[i1][j1].setBackground(c);
					}
				}

				// System.out.print(generatiaUrmatoare[i][j]+" ");

			//	System.out.println("Test " + ok);
				
			//	cells[1][ok].setText("Test");

				for (int i = 0; i < 11; i++) {
					for (int j = 0; j < 11; j++) {
						generatiaAnterioara[i][j] = generatiaUrmatoare[i][j];
					}
				}

				for (int i = 0; i < 11; i++) {
					for (int j = 0; j < 11; j++) {
						generatiaUrmatoare[i][j] = 0;
					}
				}
				
		  }
	
	
	public void complete() {
		
		
		calcul();
		
		// generatiaAnterioara[5][3] = 1;
		// generatiaAnterioara[5][4] = 1;

		// cells[3][3].setBackground(Color.BLUE);
		// cells[4][4].setBackground(Color.BLUE);
		// cells[4][4].setBackground(panel.getBackground());
/*
		System.out.println("Matricea intiala : ");
		System.out.println();

		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				if (j == 10)
					System.out.println();
				else
					System.out.print(generatiaAnterioara[i][j] + " ");
			}

		}

		System.out.println();

		int ok = 0;

		while (ok != 10) {
			
		

		}*/

		// System.arraycopy(generatiaAnterioara, 0, urmatoareaGeneratie, 0,
		// generatiaAnterioara.length);
		// Arrays.fill(urmatoareaGeneratie, 0);
		
		
	     
		
	}	public Color getPnlGridBackground() {
		return pnlGrid.getBackground();
	}
	public void setPnlGridBackground(Color background) {
		pnlGrid.setBackground(background);
	}
	public CustomJPanel getPnlGrid() {
		return pnlGrid;
	}
}
