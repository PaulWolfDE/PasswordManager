package de.paulwolf.passwordmanager.ui.tetris;

import de.paulwolf.passwordmanager.Main;

import java.awt.*;
import java.util.Random;

public class Block {

    private final Field square0 = new Field();
    private final Field square1 = new Field();
    private final Field square2 = new Field();
    private final Field square3 = new Field();
    Random random = new Random();
    private int rotation = 0;
    private char shape;

    public void setShape(char shape) {
        this.shape = shape;
    }

    public char getRandomShape() {
        int t = random.nextInt(7);

        switch (t) {
            case 0:
                return 'I';
            case 1:
                return 'J';
            case 2:
                return 'L';
            case 3:
                return 'O';
            case 4:
                return 'S';
            case 5:
                return 'T';
            default:
                return 'Z';
        }
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

        Main.fields[square0.getX()][square0.getY()].setColor(shape);
        Main.fields[square1.getX()][square1.getY()].setColor(shape);
        Main.fields[square2.getX()][square2.getY()].setColor(shape);
        Main.fields[square3.getX()][square3.getY()].setColor(shape);

        Main.fieldLabel.repaint();
    }

    public void updateRightGui() {

        Main.fields[square0.getX() - 1][square0.getY()].setColor(Color.WHITE);
        Main.fields[square1.getX() - 1][square1.getY()].setColor(Color.WHITE);
        Main.fields[square2.getX() - 1][square2.getY()].setColor(Color.WHITE);
        Main.fields[square3.getX() - 1][square3.getY()].setColor(Color.WHITE);

        Main.fields[square0.getX()][square0.getY()].setColor(shape);
        Main.fields[square1.getX()][square1.getY()].setColor(shape);
        Main.fields[square2.getX()][square2.getY()].setColor(shape);
        Main.fields[square3.getX()][square3.getY()].setColor(shape);

        Main.fieldLabel.repaint();

    }

    public void updateLeftGui() {

        Main.fields[square0.getX() + 1][square0.getY()].setColor(Color.WHITE);
        Main.fields[square1.getX() + 1][square1.getY()].setColor(Color.WHITE);
        Main.fields[square2.getX() + 1][square2.getY()].setColor(Color.WHITE);
        Main.fields[square3.getX() + 1][square3.getY()].setColor(Color.WHITE);

        Main.fields[square0.getX()][square0.getY()].setColor(shape);
        Main.fields[square1.getX()][square1.getY()].setColor(shape);
        Main.fields[square2.getX()][square2.getY()].setColor(shape);
        Main.fields[square3.getX()][square3.getY()].setColor(shape);

        Main.fieldLabel.repaint();

    }

    public void updateDownGui() {

        Main.fields[square0.getX()][square0.getY() - 1].setColor(Color.WHITE);
        Main.fields[square1.getX()][square1.getY() - 1].setColor(Color.WHITE);
        Main.fields[square2.getX()][square2.getY() - 1].setColor(Color.WHITE);
        Main.fields[square3.getX()][square3.getY() - 1].setColor(Color.WHITE);

        Main.fields[square0.getX()][square0.getY()].setColor(shape);
        Main.fields[square1.getX()][square1.getY()].setColor(shape);
        Main.fields[square2.getX()][square2.getY()].setColor(shape);
        Main.fields[square3.getX()][square3.getY()].setColor(shape);

        Main.fieldLabel.repaint();

    }

    public void appear() {

        if (shape == 'I') {
            if (!(Main.fields[3][0].isOccupied() || Main.fields[4][0].isOccupied() || Main.fields[5][0].isOccupied()
                    || Main.fields[6][0].isOccupied())) {
                square0.setLocation(3, 0);
                square1.setLocation(4, 0);
                square2.setLocation(5, 0);
                square3.setLocation(6, 0);
            } else {
                Movement.lose = true;
                Movement.timer.cancel();
            }

        } else if (shape == 'J') {
            if (!(Main.fields[4][0].isOccupied() || Main.fields[4][1].isOccupied() || Main.fields[5][1].isOccupied()
                    || Main.fields[6][1].isOccupied())) {
                square0.setLocation(4, 0);
                square1.setLocation(4, 1);
                square2.setLocation(5, 1);
                square3.setLocation(6, 1);
            } else {
                Movement.lose = true;
                Movement.timer.cancel();
            }

        } else if (shape == 'L') {
            if (!(Main.fields[6][0].isOccupied() || Main.fields[6][1].isOccupied() || Main.fields[5][1].isOccupied()
                    || Main.fields[4][1].isOccupied())) {
                square0.setLocation(6, 0);
                square1.setLocation(4, 1);
                square2.setLocation(5, 1);
                square3.setLocation(6, 1);
            } else {
                Movement.lose = true;
                Movement.timer.cancel();
            }

        } else if (shape == 'O') {
            if (!(Main.fields[4][0].isOccupied() || Main.fields[5][0].isOccupied() || Main.fields[4][1].isOccupied()
                    || Main.fields[5][1].isOccupied())) {
                square0.setLocation(4, 0);
                square1.setLocation(5, 0);
                square2.setLocation(4, 1);
                square3.setLocation(5, 1);
            } else {
                Movement.lose = true;
                Movement.timer.cancel();
            }

        } else if (shape == 'S') {
            if (!(Main.fields[4][1].isOccupied() || Main.fields[5][1].isOccupied() || Main.fields[5][0].isOccupied()
                    || Main.fields[6][0].isOccupied())) {
                square0.setLocation(5, 0);
                square1.setLocation(6, 0);
                square2.setLocation(4, 1);
                square3.setLocation(5, 1);
            } else {
                Movement.lose = true;
                Movement.timer.cancel();
            }

        } else if (shape == 'T') {
            if (!(Main.fields[4][1].isOccupied() || Main.fields[5][1].isOccupied() || Main.fields[6][1].isOccupied()
                    || Main.fields[5][0].isOccupied())) {
                square0.setLocation(5, 0);
                square1.setLocation(4, 1);
                square2.setLocation(5, 1);
                square3.setLocation(6, 1);
            } else {
                Movement.lose = true;
                Movement.timer.cancel();
            }

        } else if (shape == 'Z') {
            if (!(Main.fields[4][0].isOccupied() || Main.fields[5][0].isOccupied() || Main.fields[5][1].isOccupied()
                    || Main.fields[6][1].isOccupied())) {
                square0.setLocation(4, 0);
                square1.setLocation(5, 0);
                square2.setLocation(5, 1);
                square3.setLocation(6, 1);
            } else {
                Movement.lose = true;
                Movement.timer.cancel();
            }
        }
    }

    public void rotate() {

        if (shape == 'I') {
            if (rotation == 0 || rotation == 2) {
                if (square0.getX() + 1 <= 9 && square0.getY() - 1 >= 0 && square2.getX() - 1 >= 0
                        && square2.getY() + 1 <= 19 && square3.getX() - 2 >= 0 && square3.getY() + 2 <= 19) {
                    if (!(Main.fields[square0.getX() + 1][square0.getY() - 1].isOccupied()
                            || Main.fields[square1.getX()][square1.getY()].isOccupied()
                            || Main.fields[square2.getX() - 1][square2.getY() + 1].isOccupied()
                            || Main.fields[square3.getX() - 2][square3.getY() + 2].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() + 1, square0.getY() - 1);
                        square1.setLocation(square1.getX(), square1.getY());
                        square2.setLocation(square2.getX() - 1, square2.getY() + 1);
                        square3.setLocation(square3.getX() - 2, square3.getY() + 2);
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 1 || rotation == 3) {
                if (square0.getY() - 1 >= 0 && square0.getX() + 1 <= 9 && square1.getY() - 1 >= 0
                        && square1.getX() + 1 <= 9 && square3.getY() - 2 >= 0 && square3.getX() + 2 <= 9) {
                    if (!(Main.fields[square0.getX() - 1][square0.getY() + 1].isOccupied()
                            || Main.fields[square1.getX()][square1.getY()].isOccupied()
                            || Main.fields[square2.getX() + 1][square2.getY() - 1].isOccupied()
                            || Main.fields[square3.getX() + 2][square3.getY() - 2].isOccupied())) {

                        setColor(1);
                        square0.setLocation(square0.getX() - 1, square0.getY() + 1);
                        square1.setLocation(square1.getX(), square1.getY());
                        square2.setLocation(square2.getX() + 1, square2.getY() - 1);
                        square3.setLocation(square3.getX() + 2, square3.getY() - 2);
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            }
        } else if (shape == 'J') {
            if (rotation == 1) {
                if (square0.getX() - 2 >= 0 && square0.getY() + 1 <= 19 && square1.getY() - 1 >= 0) {
                    if (!(Main.fields[square0.getX() - 2][square0.getY() + 1].isOccupied()
                            || Main.fields[square1.getX()][square1.getY() - 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() - 2, square0.getY() + 1);
                        square1.setLocation(square1.getX(), square1.getY() - 1);
                        square2.setLocation(square2.getX(), square2.getY());
                        square3.setLocation(square3.getX(), square3.getY());
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 2) {
                if (square0.getY() - 1 >= 0 && square1.getX() - 1 >= 0 && square2.getX() - 2 >= 0
                        && square3.getX() - 1 >= 0 && square3.getY() - 1 >= 0) {
                    if (!(Main.fields[square0.getX()][square0.getY() - 1].isOccupied()
                            || Main.fields[square1.getX() - 1][square1.getY()].isOccupied()
                            || Main.fields[square2.getX() - 2][square2.getY()].isOccupied()
                            || Main.fields[square3.getX() - 1][square3.getY() - 1].isOccupied())) {

                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY() - 1);
                        square1.setLocation(square1.getX() - 1, square1.getY());
                        square2.setLocation(square2.getX() - 2, square2.getY());
                        square3.setLocation(square3.getX() - 1, square3.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 3) {
                if (square2.getX() + 1 <= 9 && square2.getY() - 1 >= 0 && square3.getX() + 1 <= 9
                        && square3.getY() + 1 <= 19) {
                    if (!(Main.fields[square2.getX() + 1][square2.getY() - 1].isOccupied()
                            || Main.fields[square3.getX() + 1][square3.getY() + 1].isOccupied())) {

                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY());
                        square1.setLocation(square1.getX(), square1.getY());
                        square2.setLocation(square2.getX() + 1, square2.getY() - 1);
                        square3.setLocation(square3.getX() + 1, square3.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 0) {
                if (square0.getX() + 2 <= 9 && square1.getY() + 1 <= 19 && square1.getX() + 1 <= 9
                        && square2.getY() + 1 <= 19 && square2.getX() + 1 <= 9) {
                    if (!(Main.fields[square0.getX() + 2][square0.getY()].isOccupied()
                            || Main.fields[square1.getX() + 1][square1.getY() + 1].isOccupied()
                            || Main.fields[square2.getX() + 1][square2.getY() + 1].isOccupied()
                            || Main.fields[square3.getX()][square3.getY()].isOccupied())) {

                        setColor(1);
                        square0.setLocation(square0.getX() + 2, square0.getY());
                        square1.setLocation(square1.getX() + 1, square1.getY() + 1);
                        square2.setLocation(square2.getX() + 1, square2.getY() + 1);
                        square3.setLocation(square3.getX(), square3.getY());
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            }

        } else if (shape == 'L') {
            if (rotation == 1) {
                if (square2.getX() - 2 >= 0 && square2.getY() - 1 >= 0 && square3.getY() - 1 >= 0
                        && square3.getX() - 2 >= 0) {
                    if (!(Main.fields[square2.getX() - 2][square2.getY() - 1].isOccupied()
                            || Main.fields[square3.getX() - 2][square3.getY() - 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY());
                        square1.setLocation(square1.getX(), square1.getY());
                        square2.setLocation(square2.getX() - 2, square2.getY() - 1);
                        square3.setLocation(square3.getX() - 2, square3.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 2) {
                if (square0.getX() - 1 >= 0 && square1.getY() + 2 <= 19 && square1.getX() - 1 >= 0
                        && square1.getY() + 2 <= 19) {
                    if (!(Main.fields[square0.getX() - 1][square0.getY() + 2].isOccupied()
                            || Main.fields[square1.getX() - 1][square1.getY() + 2].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX() - 1, square0.getY() + 2);
                        square1.setLocation(square1.getX() - 1, square1.getY() + 2);
                        square2.setLocation(square2.getX(), square2.getY());
                        square3.setLocation(square3.getX(), square3.getY());
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 3) {
                if (square0.getX() + 1 <= 9 && square0.getY() - 2 >= 0 && square1.getY() - 1 >= 0
                        && square2.getX() + 1 <= 9 && square3.getX() + 2 <= 9 && square3.getY() + 1 <= 19) {
                    if (!(Main.fields[square0.getX() + 1][square0.getY() - 2].isOccupied()
                            || Main.fields[square1.getX()][square1.getY() - 1].isOccupied()
                            || Main.fields[square2.getX() + 1][square2.getY()].isOccupied()
                            || Main.fields[square3.getX() + 2][square3.getY() + 1].isOccupied())) {

                        setColor(1);
                        square0.setLocation(square0.getX() + 1, square0.getY() - 2);
                        square1.setLocation(square1.getX(), square1.getY() - 1);
                        square2.setLocation(square2.getX() + 1, square2.getY());
                        square3.setLocation(square3.getX() + 2, square3.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 0) {
                if (square1.getX() + 1 <= 9 && square1.getY() - 1 >= 0 && square2.getY() + 1 <= 19
                        && square2.getX() + 1 <= 9) {
                    if (!(Main.fields[square1.getX() + 1][square1.getY() - 1].isOccupied()
                            || Main.fields[square2.getX() + 1][square2.getY() + 1].isOccupied())) {

                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY());
                        square1.setLocation(square1.getX() + 1, square1.getY() - 1);
                        square2.setLocation(square2.getX() + 1, square2.getY() + 1);
                        square3.setLocation(square3.getX(), square3.getY());
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            }

        } else if (shape == 'S') {
            if (rotation == 1 || rotation == 3) {
                if (square2.getX() - 2 >= 0 && square2.getY() - 1 >= 0 && square1.getY() - 1 >= 0) {
                    if (!(Main.fields[square2.getX() - 2][square2.getY() - 1].isOccupied()
                            || Main.fields[square1.getX()][square1.getY() - 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY());
                        square1.setLocation(square1.getX(), square1.getY() - 1);
                        square2.setLocation(square2.getX() - 2, square2.getY() - 1);
                        square3.setLocation(square3.getX(), square3.getY());
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 0 || rotation == 2) {
                if (square2.getX() + 2 <= 9 && square2.getY() + 1 <= 19 && square1.getY() + 1 <= 19) {
                    if (!(Main.fields[square2.getX() + 2][square2.getY() + 1].isOccupied()
                            || Main.fields[square1.getX()][square1.getY() + 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY());
                        square1.setLocation(square1.getX(), square1.getY() + 1);
                        square2.setLocation(square2.getX() + 2, square2.getY() + 1);
                        square3.setLocation(square3.getX(), square3.getY());
                        setColor(2);
                        raiseRotation();
                    }
                } else System.out.println("out of bounds");
            }

        } else if (shape == 'T') {
            if (rotation == 1) {
                if (square0.getX() + 1 <= 9 && square0.getY() + 1 <= 19) {
                    if (!(Main.fields[square0.getX() + 1][square0.getY() + 1].isOccupied())) {
                        System.out.println(1);
                        setColor(1);
                        square0.setLocation(square0.getX() + 1, square0.getY() + 1);
                        square1.setLocation(square1.getX(), square1.getY());
                        square2.setLocation(square2.getX(), square2.getY());
                        square3.setLocation(square3.getX(), square3.getY());
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 2) {
                if (square1.getX() + 1 <= 9 && square1.getY() + 1 <= 19) {
                    if (!(Main.fields[square1.getX() + 1][square1.getY() + 1].isOccupied())) {
                        System.out.println(2);
                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY());
                        square1.setLocation(square1.getX() + 1, square1.getY() - 1);
                        square2.setLocation(square2.getX(), square2.getY());
                        square3.setLocation(square3.getX(), square3.getY());
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 3) {
                if (square0.getX() - 1 >= 0 && square0.getY() - 1 >= 0 && square1.getX() - 1 >= 0
                        && square1.getY() - 1 >= 0 && square3.getX() + 1 <= 9 && square3.getY() - 1 >= 0) {
                    if (!(Main.fields[square0.getX() - 1][square0.getY() - 1].isOccupied()
                            || Main.fields[square1.getX() - 1][square1.getY() - 1].isOccupied()
                            || Main.fields[square3.getX() + 1][square3.getY() - 1].isOccupied())) {
                        System.out.println(3);
                        setColor(1);
                        square0.setLocation(square0.getX() - 1, square0.getY() - 1);
                        square1.setLocation(square1.getX() - 1, square1.getY() + 1);
                        square2.setLocation(square2.getX(), square2.getY());
                        square3.setLocation(square3.getX() + 1, square3.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 0) {
                if (square0.getY() + 1 <= 19 && square3.getX() - 2 >= 0 && square3.getY() + 1 <= 19) {
                    if (!(Main.fields[square3.getX() - 2][square3.getY() + 1].isOccupied()
                            || Main.fields[square0.getX()][square0.getY() + 1].isOccupied())) {
                        System.out.println(0);
                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY());
                        square1.setLocation(square1.getX(), square1.getY());
                        square2.setLocation(square2.getX(), square2.getY());
                        square3.setLocation(square3.getX() - 1, square3.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            }
        } else if (shape == 'Z') {
            if (rotation == 1 || rotation == 3) {
                if (square3.getX() + 2 <= 9 && square3.getY() - 1 >= 0 && square0.getY() - 1 >= 0) {
                    if (!(Main.fields[square3.getX() + 2][square3.getY() - 1].isOccupied()
                            || Main.fields[square0.getX()][square0.getY() - 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY() - 1);
                        square1.setLocation(square1.getX(), square1.getY());
                        square2.setLocation(square2.getX(), square2.getY());
                        square3.setLocation(square3.getX() + 2, square3.getY() - 1);
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            } else if (rotation == 0 || rotation == 2) {
                if (square3.getX() - 2 >= 0 && square3.getY() + 1 <= 19 && square0.getY() + 1 <= 19) {
                    if (!(Main.fields[square3.getX() - 2][square3.getY() + 1].isOccupied()
                            || Main.fields[square0.getX()][square0.getY() + 1].isOccupied())) {
                        setColor(1);
                        square0.setLocation(square0.getX(), square0.getY() + 1);
                        square1.setLocation(square1.getX(), square1.getY());
                        square2.setLocation(square2.getX(), square2.getY());
                        square3.setLocation(square3.getX() - 2, square3.getY() + 1);
                        setColor(2);
                        raiseRotation();
                    }
                } else {
                    System.out.println("out of bounds");
                }
            }
        }
    }

    private void setColor(int a) {

        if (a == 1) {
            Main.fields[square0.getX()][square0.getY()].setColor(Color.WHITE);
            Main.fields[square1.getX()][square1.getY()].setColor(Color.WHITE);
            Main.fields[square2.getX()][square2.getY()].setColor(Color.WHITE);
            Main.fields[square3.getX()][square3.getY()].setColor(Color.WHITE);
        } else {
            Main.fields[square0.getX()][square0.getY()].setColor(shape);
            Main.fields[square1.getX()][square1.getY()].setColor(shape);
            Main.fields[square2.getX()][square2.getY()].setColor(shape);
            Main.fields[square3.getX()][square3.getY()].setColor(shape);
        }
        Main.fieldLabel.repaint();
    }

    private void raiseRotation() {
        if (rotation != 3) {
            rotation++;
        } else {
            rotation = 0;
        }
    }
}
