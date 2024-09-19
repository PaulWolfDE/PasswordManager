package de.paulwolf.passwordmanager.ui.tetris;

import de.paulwolf.passwordmanager.TetrisMain;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Block {

    private final Field square0 = new Field();
    private final Field square1 = new Field();
    private final Field square2 = new Field();
    private final Field square3 = new Field();
    Random random = new Random();
    private int rotation = 0;
    private char shape;
    private boolean exists = true;

    public void setShape(char shape) {
        this.shape = shape;
    }

    Stack<Character> shapeStack = new Stack<>();

    public char getRandomShape() {

        if (shapeStack.isEmpty()) {
            List<Character> l = Arrays.asList('I', 'J', 'L', 'O', 'S', 'T', 'Z');
            Collections.shuffle(l);
            shapeStack.addAll(l);
        }

        return shapeStack.pop();
    }

    public Field getSquare(int s) {

        switch (s) {
            case 0:
                return square0;
            case 1:
                return square1;
            case 2:
                return square2;
            default:
                return square3;
        }
    }

    public void moveDown() {
        square0.setY(square0.getY() + 1);
        square1.setY(square1.getY() + 1);
        square2.setY(square2.getY() + 1);
        square3.setY(square3.getY() + 1);
        if (exists)
            updateDownGui();
    }

    public void moveRight() {
        square0.setX(square0.getX() + 1);
        square1.setX(square1.getX() + 1);
        square2.setX(square2.getX() + 1);
        square3.setX(square3.getX() + 1);
        updateRightGui();
    }

    public void moveLeft() {
        square0.setX(square0.getX() - 1);
        square1.setX(square1.getX() - 1);
        square2.setX(square2.getX() - 1);
        square3.setX(square3.getX() - 1);
        updateLeftGui();
    }

    public void updateGui() {

        setFieldsToShape();
    }

    public void updateRightGui() {

        TetrisMain.fields[square0.getX() - 1][square0.getY()].setColor(Color.WHITE);
        TetrisMain.fields[square1.getX() - 1][square1.getY()].setColor(Color.WHITE);
        TetrisMain.fields[square2.getX() - 1][square2.getY()].setColor(Color.WHITE);
        TetrisMain.fields[square3.getX() - 1][square3.getY()].setColor(Color.WHITE);

        setFieldsToShape();
    }

    private void setFieldsToShape() {
        TetrisMain.fields[square0.getX()][square0.getY()].setColor(shape);
        TetrisMain.fields[square1.getX()][square1.getY()].setColor(shape);
        TetrisMain.fields[square2.getX()][square2.getY()].setColor(shape);
        TetrisMain.fields[square3.getX()][square3.getY()].setColor(shape);

        TetrisMain.fieldLabel.repaint();
    }

    public void updateLeftGui() {

        TetrisMain.fields[square0.getX() + 1][square0.getY()].setColor(Color.WHITE);
        TetrisMain.fields[square1.getX() + 1][square1.getY()].setColor(Color.WHITE);
        TetrisMain.fields[square2.getX() + 1][square2.getY()].setColor(Color.WHITE);
        TetrisMain.fields[square3.getX() + 1][square3.getY()].setColor(Color.WHITE);

        setFieldsToShape();

    }

    public void updateDownGui() {

        TetrisMain.fields[square0.getX()][square0.getY() - 1].setColor(Color.WHITE);
        TetrisMain.fields[square1.getX()][square1.getY() - 1].setColor(Color.WHITE);
        TetrisMain.fields[square2.getX()][square2.getY() - 1].setColor(Color.WHITE);
        TetrisMain.fields[square3.getX()][square3.getY() - 1].setColor(Color.WHITE);

        setFieldsToShape();

    }

    public boolean appear() {

        if (shape == 'I' && !(TetrisMain.fields[3][0].isOccupied() || TetrisMain.fields[4][0].isOccupied() || TetrisMain.fields[5][0].isOccupied()
                || TetrisMain.fields[6][0].isOccupied())) {
            square0.setLocation(3, 0);
            square1.setLocation(4, 0);
            square2.setLocation(5, 0);
            square3.setLocation(6, 0);
            return true;

        } else if (shape == 'J' && !(TetrisMain.fields[4][0].isOccupied() || TetrisMain.fields[4][1].isOccupied() || TetrisMain.fields[5][1].isOccupied()
                || TetrisMain.fields[6][1].isOccupied())) {
            square0.setLocation(4, 0);
            square1.setLocation(4, 1);
            square2.setLocation(5, 1);
            square3.setLocation(6, 1);
            return true;

        } else if (shape == 'L' && !(TetrisMain.fields[6][0].isOccupied() || TetrisMain.fields[6][1].isOccupied() || TetrisMain.fields[5][1].isOccupied()
                || TetrisMain.fields[4][1].isOccupied())) {
            square0.setLocation(6, 0);
            square1.setLocation(4, 1);
            square2.setLocation(5, 1);
            square3.setLocation(6, 1);
            return true;

        } else if (shape == 'O' && !(TetrisMain.fields[4][0].isOccupied() || TetrisMain.fields[5][0].isOccupied() || TetrisMain.fields[4][1].isOccupied()
                || TetrisMain.fields[5][1].isOccupied())) {
            square0.setLocation(4, 0);
            square1.setLocation(5, 0);
            square2.setLocation(4, 1);
            square3.setLocation(5, 1);
            return true;

        } else if (shape == 'S' && !(TetrisMain.fields[4][1].isOccupied() || TetrisMain.fields[5][1].isOccupied() || TetrisMain.fields[5][0].isOccupied()
                || TetrisMain.fields[6][0].isOccupied())) {
            square0.setLocation(5, 0);
            square1.setLocation(6, 0);
            square2.setLocation(4, 1);
            square3.setLocation(5, 1);
            return true;

        } else if (shape == 'T' && !(TetrisMain.fields[4][1].isOccupied() || TetrisMain.fields[5][1].isOccupied() || TetrisMain.fields[6][1].isOccupied()
                || TetrisMain.fields[5][0].isOccupied())) {
            square0.setLocation(5, 0);
            square1.setLocation(4, 1);
            square2.setLocation(5, 1);
            square3.setLocation(6, 1);
            return true;

        } else if (shape == 'Z' && !(TetrisMain.fields[4][0].isOccupied() || TetrisMain.fields[5][0].isOccupied() || TetrisMain.fields[5][1].isOccupied()
                || TetrisMain.fields[6][1].isOccupied())) {
            square0.setLocation(4, 0);
            square1.setLocation(5, 0);
            square2.setLocation(5, 1);
            square3.setLocation(6, 1);
            return true;
        }
        exists = false;
        return false;
    }

    public void rotate() {

        if (Movement.lockRotate)
            return;

        if (shape == 'I') {
            if (rotation == 0 || rotation == 2) {
                if (square0.getX() + 1 <= 9 && square0.getY() - 1 >= 0
                        && square2.getX() - 1 >= 0 && square2.getY() + 1 <= 19
                        && square3.getX() - 2 >= 0 && square3.getY() + 2 <= 19) {
                    if (!(TetrisMain.fields[square0.getX() + 1][square0.getY() - 1].isOccupied()
                            || TetrisMain.fields[square2.getX() - 1][square2.getY() + 1].isOccupied()
                            || TetrisMain.fields[square3.getX() - 2][square3.getY() + 2].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() + 1, square0.getY() - 1);
                        square2.setLocation(square2.getX() - 1, square2.getY() + 1);
                        square3.setLocation(square3.getX() - 2, square3.getY() + 2);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 1 || rotation == 3) {
                if (square0.getX() - 1 >= 0 && square0.getY() + 1 <= 19
                        && square2.getX() + 1 <= 19 && square2.getY() - 1 <= 9
                        && square3.getY() - 2 >= 0 && square3.getX() + 2 <= 9) {
                    if (!(TetrisMain.fields[square0.getX() - 1][square0.getY() + 1].isOccupied()
                            || TetrisMain.fields[square2.getX() + 1][square2.getY() - 1].isOccupied()
                            || TetrisMain.fields[square3.getX() + 2][square3.getY() - 2].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() - 1, square0.getY() + 1);
                        square2.setLocation(square2.getX() + 1, square2.getY() - 1);
                        square3.setLocation(square3.getX() + 2, square3.getY() - 2);
                        setColor(2);
                        raiseRotation();
                    }
                }
            }
        } else if (shape == 'J') {
            if (rotation == 1) {
                if (square0.getX() - 2 >= 0 && square0.getY() + 1 <= 19
                        && square1.getY() - 1 >= 0) {
                    if (!(TetrisMain.fields[square0.getX() - 2][square0.getY() + 1].isOccupied()
                            || TetrisMain.fields[square1.getX()][square1.getY() - 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() - 2, square0.getY() + 1);
                        square1.setLocation(square1.getX(), square1.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 2) {
                if (square0.getY() - 1 >= 0
                        && square1.getX() - 1 >= 0
                        && square2.getX() - 2 >= 0
                        && square3.getX() - 1 >= 0 && square3.getY() - 1 >= 0) {
                    if (!(TetrisMain.fields[square0.getX()][square0.getY() - 1].isOccupied()
                            || TetrisMain.fields[square1.getX() - 1][square1.getY()].isOccupied()
                            || TetrisMain.fields[square2.getX() - 2][square2.getY()].isOccupied()
                            || TetrisMain.fields[square3.getX() - 1][square3.getY() - 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY() - 1);
                        square1.setLocation(square1.getX() - 1, square1.getY());
                        square2.setLocation(square2.getX() - 2, square2.getY());
                        square3.setLocation(square3.getX() - 1, square3.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 3) {
                if (square2.getX() + 1 <= 9 && square2.getY() - 1 >= 0
                        && square3.getX() + 1 <= 9 && square3.getY() + 1 <= 19) {
                    if (!(TetrisMain.fields[square2.getX() + 1][square2.getY() - 1].isOccupied()
                            || TetrisMain.fields[square3.getX() + 1][square3.getY() + 1].isOccupied())) {
                        setColor(1);
                        square2.setLocation(square2.getX() + 1, square2.getY() - 1);
                        square3.setLocation(square3.getX() + 1, square3.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 0) {
                if (square0.getX() + 2 <= 9
                        && square1.getX() + 1 <= 9 && square1.getY() + 1 <= 19
                        && square2.getX() + 1 <= 9 && square2.getY() + 1 <= 19) {
                    if (!(TetrisMain.fields[square0.getX() + 2][square0.getY()].isOccupied()
                            || TetrisMain.fields[square1.getX() + 1][square1.getY() + 1].isOccupied()
                            || TetrisMain.fields[square2.getX() + 1][square2.getY() + 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() + 2, square0.getY());
                        square1.setLocation(square1.getX() + 1, square1.getY() + 1);
                        square2.setLocation(square2.getX() + 1, square2.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            }
        } else if (shape == 'L') {
            if (rotation == 1) {
                if (square2.getX() - 2 >= 0 && square2.getY() - 1 >= 0
                        && square3.getX() - 2 >= 0 && square3.getY() - 1 >= 0) {
                    if (!(TetrisMain.fields[square2.getX() - 2][square2.getY() - 1].isOccupied()
                            || TetrisMain.fields[square3.getX() - 2][square3.getY() - 1].isOccupied())) {
                        setColor(1);
                        square2.setLocation(square2.getX() - 2, square2.getY() - 1);
                        square3.setLocation(square3.getX() - 2, square3.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 2) {
                if (square0.getX() - 1 >= 0 && square0.getY() + 2 <= 19
                        && square1.getX() - 1 >= 0 && square1.getY() + 2 <= 19) {
                    if (!(TetrisMain.fields[square0.getX() - 1][square0.getY() + 2].isOccupied()
                            || TetrisMain.fields[square1.getX() - 1][square1.getY() + 2].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() - 1, square0.getY() + 2);
                        square1.setLocation(square1.getX() - 1, square1.getY() + 2);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 3) {
                if (square0.getX() + 1 <= 9 && square0.getY() - 2 >= 0
                        && square1.getY() - 1 >= 0
                        && square2.getX() + 1 <= 9
                        && square3.getX() + 2 <= 9 && square3.getY() + 1 <= 19) {
                    if (!(TetrisMain.fields[square0.getX() + 1][square0.getY() - 2].isOccupied()
                            || TetrisMain.fields[square1.getX()][square1.getY() - 1].isOccupied()
                            || TetrisMain.fields[square2.getX() + 1][square2.getY()].isOccupied()
                            || TetrisMain.fields[square3.getX() + 2][square3.getY() + 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() + 1, square0.getY() - 2);
                        square1.setLocation(square1.getX(), square1.getY() - 1);
                        square2.setLocation(square2.getX() + 1, square2.getY());
                        square3.setLocation(square3.getX() + 2, square3.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 0) {
                if (square1.getX() + 1 <= 9 && square1.getY() - 1 >= 0
                        && square2.getY() + 1 <= 19 && square2.getX() + 1 <= 9) {
                    if (!(TetrisMain.fields[square1.getX() + 1][square1.getY() - 1].isOccupied()
                            || TetrisMain.fields[square2.getX() + 1][square2.getY() + 1].isOccupied())) {
                        setColor(1);
                        square1.setLocation(square1.getX() + 1, square1.getY() - 1);
                        square2.setLocation(square2.getX() + 1, square2.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            }
        } else if (shape == 'S') {
            if (rotation == 1 || rotation == 3) {
                if (square1.getY() - 1 >= 0
                        && square2.getX() - 2 >= 0 && square2.getY() - 1 >= 0) {
                    if (!(TetrisMain.fields[square1.getX()][square1.getY() - 1].isOccupied()
                            || TetrisMain.fields[square2.getX() - 2][square2.getY() - 1].isOccupied())) {
                        setColor(1);
                        square1.setLocation(square1.getX(), square1.getY() - 1);
                        square2.setLocation(square2.getX() - 2, square2.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 0 || rotation == 2) {
                if (square1.getY() + 1 <= 19
                        || square2.getX() + 2 <= 9 && square2.getY() + 1 <= 19) {
                    if (!(TetrisMain.fields[square1.getX()][square1.getY() + 1].isOccupied()
                            || TetrisMain.fields[square2.getX() + 2][square2.getY() + 1].isOccupied())) {
                        setColor(1);
                        square1.setLocation(square1.getX(), square1.getY() + 1);
                        square2.setLocation(square2.getX() + 2, square2.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            }
        } else if (shape == 'T') {
            if (rotation == 1) {
                if (square0.getX() + 1 <= 9 && square0.getY() + 1 <= 19) {
                    if (!(TetrisMain.fields[square0.getX() + 1][square0.getY() + 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() + 1, square0.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 2) {
                if (square1.getX() + 1 <= 9 && square1.getY() - 1 >= 0) {
                    if (!(TetrisMain.fields[square1.getX() + 1][square1.getY() - 1].isOccupied())) {
                        setColor(1);
                        square1.setLocation(square1.getX() + 1, square1.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 3) {
                if (square0.getX() - 1 >= 0 && square0.getY() - 1 >= 0
                        && square1.getX() - 1 >= 0 && square1.getY() + 1 <= 19
                        && square3.getX() + 1 <= 9 && square3.getY() - 1 >= 0) {
                    if (!(TetrisMain.fields[square0.getX() - 1][square0.getY() - 1].isOccupied()
                            || TetrisMain.fields[square1.getX() - 1][square1.getY() + 1].isOccupied()
                            || TetrisMain.fields[square3.getX() + 1][square3.getY() - 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() - 1, square0.getY() - 1);
                        square1.setLocation(square1.getX() - 1, square1.getY() + 1);
                        square3.setLocation(square3.getX() + 1, square3.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 0) {
                if (square3.getX() - 1 >= 0 && square3.getY() + 1 <= 19) {
                    if (!(TetrisMain.fields[square3.getX() - 1][square3.getY() + 1].isOccupied())) {
                        setColor(1);
                        square3.setLocation(square3.getX() - 1, square3.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            }
        } else if (shape == 'Z') {
            if (rotation == 1 || rotation == 3) {
                if (square0.getY() - 1 >= 0
                        && square3.getX() + 2 <= 9 && square3.getY() - 1 >= 0) {
                    if (!(TetrisMain.fields[square0.getX()][square0.getY() - 1].isOccupied()
                            || TetrisMain.fields[square3.getX() + 2][square3.getY() - 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY() - 1);
                        square3.setLocation(square3.getX() + 2, square3.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            } else if (rotation == 0 || rotation == 2) {
                if (square0.getY() + 1 <= 19
                        && square3.getX() - 2 >= 0 && square3.getY() + 1 <= 19) {
                    if (!(TetrisMain.fields[square0.getX()][square0.getY() + 1].isOccupied()
                            || TetrisMain.fields[square3.getX() - 2][square3.getY() + 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY() + 1);
                        square3.setLocation(square3.getX() - 2, square3.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                }
            }
        }
    }

    private void setColor(int a) {

        if (a == 1) {
            TetrisMain.fields[square0.getX()][square0.getY()].setColor(Color.WHITE);
            TetrisMain.fields[square1.getX()][square1.getY()].setColor(Color.WHITE);
            TetrisMain.fields[square2.getX()][square2.getY()].setColor(Color.WHITE);
            TetrisMain.fields[square3.getX()][square3.getY()].setColor(Color.WHITE);
        } else {
            TetrisMain.fields[square0.getX()][square0.getY()].setColor(shape);
            TetrisMain.fields[square1.getX()][square1.getY()].setColor(shape);
            TetrisMain.fields[square2.getX()][square2.getY()].setColor(shape);
            TetrisMain.fields[square3.getX()][square3.getY()].setColor(shape);
        }
        TetrisMain.fieldLabel.repaint();
    }

    private void raiseRotation() {
        if (rotation != 3) {
            rotation++;
        } else {
            rotation = 0;
        }
    }

    public boolean exists() {
        return exists;
    }
}
