package de.paulwolf.passwordmanager.ui.tetris;

import de.paulwolf.passwordmanager.Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Painting extends JLabel {

	private static final long serialVersionUID = 1L;

	BufferedImage field;
	BufferedImage grid;

	protected void paintComponent(Graphics g) {

		try {
			field = ImageIO.read(Objects.requireNonNull(getClass().getResource("/empty_field.png")));
			grid = ImageIO.read(Objects.requireNonNull(getClass().getResource("/grid.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Graphics2D graphics = field.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for(int x = 0; x < 10; x++) {
			for (int y = 0; y < 20; y++) {

				graphics.setPaint(Main.fields[x][y].getColor());
				graphics.fillRect(0, 0, 32, 32);

				g.drawImage(field, x*32, y*32, 32, 32, null);
				g.drawImage(grid, x*32, y*32, 32, 32, null);

			}
		}
		g.setFont(new Font("Verdana", Font.PLAIN, 30));
		g.drawString("Score:", 350, 50);
		g.drawString(String.valueOf(Movement.score), 350, 90);
		g.drawString("Level:", 350, 150);
		g.drawString(String.valueOf(Movement.level), 350, 190);
		g.drawString("Next:", 350, 380);

		g.setColor(Field.getColor(Movement.next));

		switch (Movement.next) {
			case 'I':
				g.fillRect(350, 416, 32, 32);
				g.fillRect(350 + 32, 416, 32, 32);
				g.fillRect(350 + 32*2, 416, 32, 32);
				g.fillRect(350 + 32*3, 416, 32, 32);
				break;
			case 'J':
				g.fillRect(350, 416, 32, 32);
				g.fillRect(350, 416 + 32, 32, 32);
				g.fillRect(350 + 32, 416 + 32, 32, 32);
				g.fillRect(350 + 32*2, 416 + 32, 32, 32);
				break;
			case 'L':
				g.fillRect(350, 416+32, 32, 32);
				g.fillRect(350 + 32, 416+32, 32, 32);
				g.fillRect(350 + 32*2, 416+32, 32, 32);
				g.fillRect(350 + 32*2, 416, 32, 32);
				break;
			case 'O':
				g.fillRect(350, 416, 32, 32);
				g.fillRect(350 + 32, 416, 32, 32);
				g.fillRect(350, 416 + 32, 32, 32);
				g.fillRect(350 + 32, 416 + 32, 32, 32);
				break;
			case 'S':
				g.fillRect(350, 416 + 32, 32, 32);
				g.fillRect(350 + 32, 416 + 32, 32, 32);
				g.fillRect(350 + 32, 416, 32, 32);
				g.fillRect(350 + 32 * 2, 416, 32, 32);
				break;
			case 'T':
				g.fillRect(350, 416+32, 32, 32);
				g.fillRect(350 + 32, 416+32, 32, 32);
				g.fillRect(350+32, 416, 32, 32);
				g.fillRect(350 + 32*2, 416+32, 32, 32);
				break;
			case 'Z':
				g.fillRect(350, 416, 32, 32);
				g.fillRect(350 + 32, 416, 32, 32);
				g.fillRect(350+32, 416+32, 32, 32);
				g.fillRect(350 + 32*2, 416+32, 32, 32);
				break;
		}
	}
}