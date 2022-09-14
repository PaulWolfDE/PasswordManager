package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;
import java.awt.*;

public class ScaledFileChooser extends JFileChooser {

    public ScaledFileChooser() {

        setFileChooserFont(this.getComponents());
        this.setPreferredSize(Configuration.SCALED_FILE_CHOOSER_SIZE);
    }

    private void setFileChooserFont(Component[] comp) {
        for (Component component : comp) {
            if (component instanceof Container)
                setFileChooserFont(((Container) component).getComponents());
            if (component instanceof JScrollPane) {
                ((JScrollPane) component).getVerticalScrollBar().setPreferredSize(new Dimension(Configuration.SCALED_SCROLL_BAR_THICKNESS, 0));
                ((JScrollPane) component).getHorizontalScrollBar().setPreferredSize(new Dimension(0, Configuration.SCALED_SCROLL_BAR_THICKNESS));
            }
            try {
                component.setFont(Configuration.STANDARD_FONT);
            } catch (Exception ignored) {}
        }
    }
}
