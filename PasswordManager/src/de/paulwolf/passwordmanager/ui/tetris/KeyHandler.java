package de.paulwolf.passwordmanager.ui.tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public static boolean b = false;

    @Override
    public void keyPressed(KeyEvent e) {

        if (!Movement.lose) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {

                Movement.moveDown();
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {

                Movement.moveLeft();
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {

                Movement.block.rotate();

            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

                Movement.moveRight();
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {

                if (!b) {
                    b = true;
                    Movement.skipDown();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
