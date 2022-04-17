package de.paulwolf.passwordmanager.ui.tetris;

import de.paulwolf.passwordmanager.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Painting extends JLabel {

    private static final long serialVersionUID = 1L;

    BufferedImage field;
    BufferedImage emptyField;
    BufferedImage grid;

    protected void paintComponent(Graphics g) {

        try {
            field = ImageIO.read(Objects.requireNonNull(getClass().getResource("/empty_field.png")));
            emptyField = ImageIO.read(Objects.requireNonNull(getClass().getResource("/empty_field.png")));
            grid = ImageIO.read(Objects.requireNonNull(getClass().getResource("/grid.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Graphics2D graphics = field.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {

                graphics.setPaint(Main.fields[x][y].getColor());
                if (Main.fields[x][y].getColor() != Color.WHITE) graphics.fillRect(0, 0, 32, 32);

                g.drawImage(field, x * 32, y * 32, 32, 32, null);
                if (Main.fields[x][y].getColor() == Color.WHITE) g.drawImage(emptyField, x * 32, y * 32, 32, 32, null);
                else g.drawImage(grid, x * 32, y * 32, 32, 32, null);

            }
        }
        int minReach = 20;
        int reach;

        for (int i = 0; i < 4; i++) {
            reach = Movement.block.getSquare(i).getY();
            while (reach + 1 < 20 && !Main.fields[Movement.block.getSquare(i).getX()][reach + 1].isOccupied()) reach++;
            if (reach - Movement.block.getSquare(i).getY() < minReach)
                minReach = reach - Movement.block.getSquare(i).getY();
        }
        g.setColor(new Color(Main.fields[Movement.block.getSquare(0).getX()][Movement.block.getSquare(0).getY()].getColor().getRed(), Main.fields[Movement.block.getSquare(0).getX()][Movement.block.getSquare(0).getY()].getColor().getGreen(), Main.fields[Movement.block.getSquare(0).getX()][Movement.block.getSquare(0).getY()].getColor().getBlue(), 50));
        for (int i = 0; i < 4; i++)
            g.fillRect(Movement.block.getSquare(i).getX() * 32, (minReach + Movement.block.getSquare(i).getY()) * 32, 32, 32);

        g.setColor(Color.WHITE);

        if (!Main.windows) g.setFont(new Font("Nimbus Mono PS", Font.BOLD, 40));

        g.drawString("SCORE:", 350, 50);
        g.drawString(String.valueOf(Movement.score), 350, 90);
        g.drawString("LINES:", 350, 150);
        g.drawString(String.valueOf(Movement.achievedLines), 350, 190);
        g.drawString("LEVEL:", 350, 250);
        g.drawString(String.valueOf(Movement.level), 350, 290);
        g.drawString("NEXT:", 350, 520);

        g.setColor(Field.getColor(Movement.next));

        switch (Movement.next) {
            case 'I':
                g.fillRect(350, 544, 32, 32);
                g.drawImage(grid, 350, 544, 32, 32, null);
                g.fillRect(350 + 32, 544, 32, 32);
                g.drawImage(grid, 350 + 32, 544, 32, 32, null);
                g.fillRect(350 + 32 * 2, 544, 32, 32);
                g.drawImage(grid, 350 + 32 * 2, 544, 32, 32, null);
                g.fillRect(350 + 32 * 3, 544, 32, 32);
                g.drawImage(grid, 350 + 32 * 3, 544, 32, 32, null);
                break;
            case 'J':
                g.fillRect(350, 544, 32, 32);
                g.drawImage(grid, 350, 544, 32, 32, null);
                g.fillRect(350, 544 + 32, 32, 32);
                g.drawImage(grid, 350, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32, 544 + 32, 32, 32);
                g.drawImage(grid, 350 + 32, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32 * 2, 544 + 32, 32, 32);
                g.drawImage(grid, 350 + 32 * 2, 544 + 32, 32, 32, null);
                break;
            case 'L':
                g.fillRect(350, 544 + 32, 32, 32);
                g.drawImage(grid, 350, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32, 544 + 32, 32, 32);
                g.drawImage(grid, 350 + 32, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32 * 2, 544 + 32, 32, 32);
                g.drawImage(grid, 350 + 32 * 2, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32 * 2, 544, 32, 32);
                g.drawImage(grid, 350 + 32 * 2, 544, 32, 32, null);
                break;
            case 'O':
                g.fillRect(350, 544, 32, 32);
                g.drawImage(grid, 350, 544, 32, 32, null);
                g.fillRect(350 + 32, 544, 32, 32);
                g.drawImage(grid, 350 + 32, 544, 32, 32, null);
                g.fillRect(350, 544 + 32, 32, 32);
                g.drawImage(grid, 350, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32, 544 + 32, 32, 32);
                g.drawImage(grid, 350 + 32, 544 + 32, 32, 32, null);
                break;
            case 'S':
                g.fillRect(350, 544 + 32, 32, 32);
                g.drawImage(grid, 350, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32, 544 + 32, 32, 32);
                g.drawImage(grid, 350 + 32, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32, 544, 32, 32);
                g.drawImage(grid, 350 + 32, 544, 32, 32, null);
                g.fillRect(350 + 32 * 2, 544, 32, 32);
                g.drawImage(grid, 350 + 32 * 2, 544, 32, 32, null);
                break;
            case 'T':
                g.fillRect(350, 544 + 32, 32, 32);
                g.drawImage(grid, 350, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32, 544 + 32, 32, 32);
                g.drawImage(grid, 350 + 32, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32, 544, 32, 32);
                g.drawImage(grid, 350 + 32, 544, 32, 32, null);
                g.fillRect(350 + 32 * 2, 544 + 32, 32, 32);
                g.drawImage(grid, 350 + 32 * 2, 544 + 32, 32, 32, null);
                break;
            case 'Z':
                g.fillRect(350, 544, 32, 32);
                g.drawImage(grid, 350, 544, 32, 32, null);
                g.fillRect(350 + 32, 544, 32, 32);
                g.drawImage(grid, 350 + 32, 544, 32, 32, null);
                g.fillRect(350 + 32, 544 + 32, 32, 32);
                g.drawImage(grid, 350 + 32, 544 + 32, 32, 32, null);
                g.fillRect(350 + 32 * 2, 544 + 32, 32, 32);
                g.drawImage(grid, 350 + 32 * 2, 544 + 32, 32, 32, null);
                break;
        }
    }
}