package de.paulwolf.passwordmanager.ui;

import java.awt.*;

public class UIUtils {

    private static final int INGS_GAP = 10;

    public static GridBagConstraints createGBC(int x, int y, int fill, int width, int height) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.fill = fill;

        gbc.weightx = x == 0 ? 0.0 : 1.0;
        gbc.weighty = 0.0;

        gbc.insets = new Insets(INGS_GAP, INGS_GAP, INGS_GAP, INGS_GAP);
        return gbc;
    }

    public static GridBagConstraints createGBC(int x, int y, int fill, int width, int height, double weightx) {

        GridBagConstraints gbc = createGBC(x, y, fill, width, height);
        gbc.weightx = weightx;
        return gbc;
    }

}
