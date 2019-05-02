package de.tum.in.ase.eist;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Dimension2D;


/**
 * Creates all car objects, detects collisions, updates car positions, notifies
 * player about victory or defeat
 *
 */
public class GameBoard {

	// list of all active cars, does not contain player car
	private List<Car> cars = new ArrayList<>();
	// the player object with player car object
	private Player player;
	private AudioPlayer audioPlayer;
	private Dimension2D size;
	// list of all loser cars (needed for testing, DO NOT DELETE THIS)
	private List<Car> loserCars = new ArrayList<>();
	// true if game is running, false if game is stopped
	private boolean isRunning;
	// used for testing, DO NOT DELETE THIS
	public String result;
	public boolean printed;

	//constants
	public static int NUMBER_OF_SLOW_CARS = 5;

	/**
	 * Constructor, creates the gameboard based on size 
	 * @param size of the gameboard
	 */
	public GameBoard(Dimension2D size) {
		Car playerCar = new FastCar(250, 30);
		this.player = new Player(playerCar);
		this.audioPlayer = new AudioPlayer();
		this.size = size;
		this.result = "undefined";
		this.addCars();
	}

	/**
	 * Adds specified number of cars to the cars list, creates new object for each car
	 */
	public void addCars() {
		for (int i = 0; i < NUMBER_OF_SLOW_CARS; i++) {
			this.cars.add(new SlowCar((int)this.size.getWidth(), (int) this.size.getHeight()));
		}
	}

	/**
	 * Removes all existing cars from the car list, resets the position of the
	 * player car Invokes the creation of new car objects by calling addCars()
	 */
	public void resetCars() {
		this.player.getCar().reset((int) this.size.getHeight());
		this.cars.clear();
		addCars();
	}

	/**
	 * Checks if game is currently running by checking if the thread is running
	 * @return boolean isRunning
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * Used for testing only
	 */
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	/**
	 * @return list of cars
	 */
	public List<Car> getCars() {
		return this.cars;
	}

	/**
	 * @return the player's car
	 */
	public Car getPlayerCar() {
		return this.player.getCar();
	}
	
	/**
	 * @return the gameboard's instance of AudioPlayer
	 */
	public AudioPlayer getAudioPlayer() {
		return this.audioPlayer;
	}

	/**
	 * Updates the position of each car
	 */
	public void update() {
		moveCars();
	}

	/**
	 * Starts the game. Cars start to move and background music starts to play.
	 */
	public void startGame() {
		// TODO Call the method playMusic() and start the game by setting
		// isRunning to true
		playMusic();
		this.isRunning = true;
	}

	/**
	 * Stops the game. Cars stop moving and background music stops playing.
	 */
	public void stopGame() {
		// TODO Call the method stopMusic() and stop the game by setting
		// isRunning to false
		stopMusic();
		this.isRunning = false;
	}

	/**
	 * Starts the background music
	 */
	public void playMusic() {
		// TODO Call the method playBackgroundMusic on audioPlayer
		this.audioPlayer.playBackgroundMusic();
	}

	/**
	 * Stops the background music
	 */
	public void stopMusic() {
		this.audioPlayer.stopBackgroundMusic();// TODO Call the method stopBackgroundMusic on audioPlayer
	}

	/**
	 * @return list of loser cars
	 */
	public List<Car> getLoserCars() {
		return this.loserCars;
	}

	/**
	 * Iterate through list of cars (without the player car) and update each car's position 
	 * Update player car afterwards separately
	 */
	public void moveCars() {

		List<Car> cars = getCars();

		// maximum x and y values a car can have depending on the size of the game board
		int maxX = (int)this.size.getWidth();
		int maxY = (int)this.size.getHeight();

		// update the positions of the player car and the autonomous cars
		for (Car car : cars) {
			car.updatePosition(maxX, maxY);
		}

		this.player.getCar().updatePosition(maxX, maxY);
	}
}
