package de.paulwolf.passwordmanager.ui.tetris;

import java.awt.Color;

public class Field {

	private boolean occupied;
	private Color color;
	private int x, y;

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public boolean isOccupied() {
		if (x < 0 || y < 0 || x > 9 || y > 19) {
			return true;
		} else {
			return occupied;
		}
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setColor(char shape) {
		color = getColor(shape);
	}

	public static Color getColor(char shape) {
		return switch(shape){
			case 'I' -> Color.CYAN;
			case 'J' -> Color.BLUE;
			case 'L' -> Color.ORANGE;
			case 'O' -> Color.YELLOW;
			case 'S' -> Color.GREEN;
			case 'T' -> Color.MAGENTA;
			default -> Color.RED;
		};
	}

	public Color getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean getOccupied() {
		return occupied;
	}
}
