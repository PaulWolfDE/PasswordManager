package de.paulwolf.passwordmanager.ui.tetris;

import java.awt.*;

public class Field {

    private boolean occupied;
    private Color color;
    private int x, y;

    public static Color getColor(char shape) {
        switch (shape) {
            case 'I':
                return Color.CYAN;
            case 'J':
                return Color.BLUE;
            case 'L':
                return Color.ORANGE;
            case 'O':
                return Color.YELLOW;
            case 'S':
                return Color.GREEN;
            case 'T':
                return Color.MAGENTA;
            default:
                return Color.RED;
        }
    }

    public boolean isOccupied() {
        if (x < 0 || y < 0 || x > 9 || y > 19) {
            return true;
        } else {
            return occupied;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(char shape) {
        color = getColor(shape);
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

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
