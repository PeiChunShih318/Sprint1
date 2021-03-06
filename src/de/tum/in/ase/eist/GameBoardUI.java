package de.tum.in.ase.eist;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class implements the user interface for steering the player car. The
 * user interface is implemented as a Thread that is started by clicking the
 * start button on the tool bar and stops by the stop button.
 *
 */
public class GameBoardUI extends Canvas implements Runnable {
	private static Color backgroundColor = Color.WHITE;
	private static int SLEEP_TIME = 1000 / 25; // this gives us 25fps
	private static Dimension2D DEFAULT_SIZE = new Dimension2D(500, 300);
	// attribute inherited by the JavaFX Canvas class
	GraphicsContext graphicsContext = this.getGraphicsContext2D();

	// thread responsible for starting game
	private Thread theThread;

	// user interface objects
	private GameBoard gameBoard;
	private Dimension2D size;
	private Toolbar toolBar;

	/**
	 * Sets up all attributes, starts the mouse steering and sets up all graphics
	 * @param toolBar used to start and stop the game
	 */
	public GameBoardUI(Toolbar toolBar) {
		this.toolBar = toolBar;
		this.size = getPreferredSize();
		this.gameBoard = new GameBoard(this.size);
		this.widthProperty().set(this.size.getWidth());
		this.heightProperty().set(this.size.getHeight());
		this.size = new Dimension2D(getWidth(), getHeight());
		gameSetup();
	}

	/**
	 * Called after starting the game thread
	 * Constantly updates the game board and renders graphics
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (this.gameBoard.isRunning()) {
			// updates car positions and re-renders graphics
			this.gameBoard.update();
			paint(this.graphicsContext);
			try {
				Thread.sleep(SLEEP_TIME); // milliseconds to sleep
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @return current gameBoard
	 */
	public GameBoard getGameBoard() {
		return this.gameBoard;
	}

	/**
	 * @return preferred gameBoard size
	 */
	public static Dimension2D getPreferredSize() {
		return DEFAULT_SIZE;
	}

	/**
	 * Removes all existing cars from the game board and re-adds them. Status bar is
	 * set to default value. Player car is reset to default starting position.
	 * Renders graphics.
	 */
	public void gameSetup() {
		this.gameBoard.resetCars();
		paint(this.graphicsContext);
		this.toolBar.resetToolBarButtonStatus(false);
	}


	/**
	 * Starts the GameBoardUI Thread, if it wasn't running. Starts the game board,
	 * which causes the cars to change their positions (i.e. move). Renders graphics
	 * and updates tool bar status.
	 */
	public void startGame() {
		if (!this.gameBoard.isRunning()) {
			this.gameBoard.startGame();
			this.theThread = new Thread(this);
			this.theThread.start();
			paint(this.graphicsContext);
			this.toolBar.resetToolBarButtonStatus(true);
		}
	}

	/**
	 * Render the graphics of the whole game by iterating through the cars of the
	 * game board at render each of them individually.
	 * @param graphics used to draw changes
	 */
	private void paint(GraphicsContext graphics) {
		graphics.setFill(backgroundColor);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		for (Car car : this.gameBoard.getCars()) {
			paintCar(car, graphics);
		}
		// render player car
		paintCar(this.gameBoard.getPlayerCar(), graphics);
	}

	/**
	 * Show image of a car at the current position of the car.
	 * @param car to be drawn
	 * @param graphics used to draw changes
	 */
	private void paintCar(Car car, GraphicsContext graphics) {
		Point2D carPosition = car.getPosition();
		Point2D canvasPosition = convertPosition(carPosition);

		graphics.drawImage(car.getIcon(), canvasPosition.getX(), canvasPosition.getY(), 
				car.getSize().getWidth(), car.getSize().getHeight());
	}

	/**
	 * Converts position of car to position on the canvas
	 * @param toConvert the point to be converted
	 */
	public Point2D convertPosition(Point2D toConvert) {
		return new Point2D(toConvert.getX(), getHeight() - toConvert.getY());
	}

	/**
	 * Stops the game board and set the tool bar to default values.
	 */
	public void stopGame() {
		if (this.gameBoard.isRunning()) {
			this.gameBoard.stopGame();
			this.toolBar.resetToolBarButtonStatus(false);
		}
	}
}
