package de.paulwolf.passwordmanager.ui.tetris;

import de.paulwolf.passwordmanager.Main;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class Movement {
	
	public static long delay = 500;
	public static Block block;
	public static Timer timer = new Timer();
	public static boolean lose = false;
	public static boolean temp = false;
	public static int[] lines = new int[20];

	public static void startMovement() {

		System.out.println("started new movement");

		timer.cancel();

		block = new Block();
		block.setRandomShape();
		block.appear();
		timer = new Timer();
		if (lose) {
			System.out.println("u lose");
			timer.cancel();
			return;
		}
		block.updateGui();
		KeyHandler.b = false;

		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (lose) {
					timer.cancel();
					return;
				}
				moveDown();
			}
		}, delay, delay);

	}

	public static void moveRight() {
		if (!(block.getSquare(0).getX() >= 9 || block.getSquare(1).getX() >= 9 || block.getSquare(2).getX() >= 9
				|| block.getSquare(3).getX() >= 9)) {

			if (!Main.fields[block.getSquare(0).getX() + 1][block.getSquare(0).getY()].isOccupied()
					&& !Main.fields[block.getSquare(1).getX() + 1][block.getSquare(1).getY()].isOccupied()
					&& !Main.fields[block.getSquare(2).getX() + 1][block.getSquare(2).getY()].isOccupied()
					&& !Main.fields[block.getSquare(3).getX() + 1][block.getSquare(3).getY()].isOccupied()) {

				block.moveRight();
			}
		}
	}

	public static void moveLeft() {
		if (!(block.getSquare(0).getX() <= 0 || block.getSquare(1).getX() <= 0 || block.getSquare(2).getX() <= 0
				|| block.getSquare(3).getX() <= 0)) {

			if (!Main.fields[block.getSquare(0).getX() - 1][block.getSquare(0).getY()].isOccupied()
					&& !Main.fields[block.getSquare(1).getX() - 1][block.getSquare(1).getY()].isOccupied()
					&& !Main.fields[block.getSquare(2).getX() - 1][block.getSquare(2).getY()].isOccupied()
					&& !Main.fields[block.getSquare(3).getX() - 1][block.getSquare(3).getY()].isOccupied()) {

				block.moveLeft();
			}
		}
	}

	public static void moveDown() {

		if (block.getSquare(0).getY() + 1 < 20 && block.getSquare(1).getY() + 1 < 20
				&& block.getSquare(2).getY() + 1 < 20 && block.getSquare(3).getY() + 1 < 20) {
			
			if (temp && !(!Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY() + 1].isOccupied()
					&& !Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY() + 1].isOccupied()
					&& !Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY() + 1].isOccupied()
					&& !Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY() + 1].isOccupied())) {
				temp = false;
				timer.cancel();
				Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY()].setOccupied(true);
				Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY()].setOccupied(true);
				Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY()].setOccupied(true);
				Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY()].setOccupied(true);
				for (int i = 0; i < 4; i++) {
					lines[block.getSquare(i).getY()]++;
				}
				checkLines(/*lines, block.getSquare(0).getY(), block.getSquare(1).getY(), block.getSquare(2).getY(),
						block.getSquare(3).getY()*/);
				Main.fieldLabel.repaint();
				System.out.println("test");
				startMovement();
			}
		}

		if (block.getSquare(0).getY() == 19 || block.getSquare(1).getY() == 19 || block.getSquare(2).getY() == 19
				|| block.getSquare(3).getY() == 19) {
			for (int i = 0; i < 4; i++) {
				Main.fields[block.getSquare(i).getX()][block.getSquare(i).getY()].setOccupied(true);
				lines[block.getSquare(i).getY()]++;
			}
			checkLines();
			startMovement();

		} else {
			if (!Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY() + 1].isOccupied()
					&& !Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY() + 1].isOccupied()
					&& !Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY() + 1].isOccupied()
					&& !Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY() + 1].isOccupied()) {
				block.moveDown();
			} else {
				temp = true;
			}			
		}
	}

	public static void skipDown() {

		timer.cancel();

		Thread t1 = new Thread(() -> {

			while (true) {
				if (!Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY() + 1].isOccupied()
						&& !Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY() + 1].isOccupied()
						&& !Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY() + 1].isOccupied()
						&& !Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY() + 1].isOccupied()) {

					if (block.getSquare(0).getY() >= 18 || block.getSquare(1).getY() >= 18
							|| block.getSquare(2).getY() >= 18 || block.getSquare(3).getY() >= 18) {
						timer.cancel();
						block.moveDown();
						Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY()].setOccupied(true);
						Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY()].setOccupied(true);
						Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY()].setOccupied(true);
						Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY()].setOccupied(true);
						for (int i = 0; i < 4; i++) {
							lines[block.getSquare(i).getY()]++;
						}
						checkLines(/*lines, block.getSquare(0).getY(), block.getSquare(1).getY(),
								block.getSquare(2).getY(), block.getSquare(3).getY()*/);
						startMovement();
						return;
					} else {
						block.moveDown();
					}

				} else {
					timer.cancel();
					Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY()].setOccupied(true);
					Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY()].setOccupied(true);
					Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY()].setOccupied(true);
					Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY()].setOccupied(true);
					for (int i = 0; i < 4; i++) {
						lines[block.getSquare(i).getY()]++;
					}
					checkLines(/*lines, block.getSquare(0).getY(), block.getSquare(1).getY(),
							block.getSquare(2).getY(), block.getSquare(3).getY()*/);
					startMovement();
					return;
				}
			}
		});
		t1.start();
	}

	public static void checkLines(/*int[] line, int heightS1, int heightS2, int heightS3, int heightS4*/) {

		displayOccupiedFields();

		for (int i = 19; i >= 0; i--) {
			if (lines[i] == 10) {
				cleanLines(i);
			}
		}

		for (int i = 19; i >= 0; i--) {
			if (lines[i] == 10) {
				checkLines();
				break;
			}
		}
	}

	public static void cleanLines(int height) {

		displayOccupiedFields();
		for (int x = 0; x < 10; x++) {
			Main.fields[x][height].setOccupied(false);
			Main.fields[x][height].setColor(Color.WHITE);
			System.out.println(lines[height] + " lines " + height);
			Main.fieldLabel.repaint();
		}
		lines[height] = 0;

		for (int i = height-1; i >= 0; i--) {
			lineDown(i);
			Main.fieldLabel.repaint();
		}
	}

	public static void lineDown(int height) {

		for (int i = 0; i < 10; i++) {
			if (Main.fields[i][height].isOccupied()) {
				lines[height]--;
				lines[height+1]++;
			}
			Main.fields[i][height+1].setColor(Main.fields[i][height].getColor());
			Main.fields[i][height+1].setOccupied(Main.fields[i][height].getOccupied());
			Main.fields[i][height].setOccupied(false);
			Main.fields[i][height].setColor(Color.WHITE);
			displayOccupiedFields();
		}
	}

	// !DEBUG
	private static void displayOccupiedFields() {

		for (int y = 0; y < 20; y++) {

			for (int x = 0; x < 10; x++) {

				System.out.print(Main.fields[x][y].isOccupied() ? "1 " : "0 ");
			}
			System.out.printf(" : %d\n", lines[y]);
		}
		System.out.println();

	}
}