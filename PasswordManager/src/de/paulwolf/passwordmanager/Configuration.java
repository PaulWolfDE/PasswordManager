package de.paulwolf.passwordmanager;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import de.paulwolf.passwordmanager.wizards.FileWizard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Configuration {

    // Font size for 1920 px screen width
    private static final float STANDARD_FONTSIZE = 14f;
    private static final float STANDARD_TETRIS_FONTSIZE = 30f;

    // Text field size for 1920x1080 px resolution
    private static final Dimension STANDARD_TEXT_FIELD_SIZE = new Dimension(250, 26);
    public static Dimension SCALED_TEXT_FIELD_SIZE;
    public static Dimension SCALED_PASSWORD_FIELD_SIZE;

    // Button size for 1920x1080 px resolution
    private static final Dimension STANDARD_BUTTON_SIZE = new Dimension(170, 26);
    public static Dimension SCALED_BUTTON_SIZE;

    // File chooser size for 1920x1080 px resolution
    private static final Dimension STANDARD_FILE_CHOOSER_SIZE = new Dimension(600, 300);
    public static Dimension SCALED_FILE_CHOOSER_SIZE;

    // Row height of table for 1920x1080 px resolution
    private static final int STANDARD_TABLE_ROW_HEIGHT = 20;
    public static int SCALED_TABLE_ROW_HEIGHT;

    // Scroll bar width for 1920x1080 px resolution
    private static final int STANDARD_SCROLL_BAR_WIDTH = 15;
    public static int SCALED_SCROLL_BAR_THICKNESS;

    // Standard character encoding
    public static final Charset STANDARD_CHARSET = StandardCharsets.UTF_8;
    // Standard hash algorithm
    public static final String HASH_ALGORITHM = "SHA-256";
    // Hash algorithms available
    public static final String[] HASH_ALGORITHMS = {"SHA-256", "MD5"};
    // Standard encryption algorithm
    public static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    // Encryption algorithms available
    public static final String[] ENCRYPTION_ALGORITHMS = {"AES/GCM/NoPadding", "AES/CTR/NoPadding",
            "AES/CBC/PKCS5Padding", "AES/ECB/PKCS5Padding", "Serpent/CTR/NoPadding", "Serpent/CBC/ISO10126",
            "Serpent/ECB/ISO10126", "Twofish/CTR/NoPadding", "Twofish/CBC/ISO10126", "Twofish/ECB/ISO10126",
            "Blowfish/CTR/NoPadding", "Blowfish/CBC/PKCS5Padding", "Blowfish/ECB/PKCS5Padding"};
    // HMAC algorithm used by PBKDF2
    public static final String HMAC_ALGORITHM = "HMAC-SHA-256";
    // Number of PBKDF2 HMAC iterations
    public static final int ITERATIONS = 310000;
    // Entry date format
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd@HH:mm");
    // Version identifier
    public static final String VERSION_NUMBER = "2.0.6";
    // Compatible version identifiers
    public static final String[] COMPATIBLE_VERSIONS = {"2.0.6", "2.0.5", "2.0.4", "2.0.3", "2.0.2", "2.0.1", "2.0.0", "1.4.8", "1.4.7", "1.4.6", "1.4.5", "1.4.4", "1.4.3",
            "1.4.2", "1.4.1", "1.4.0", "1.3.9", "1.3.8", "1.3.7", "1.3.6", "1.3.5", "1.3.4", "1.3.3", "1.3.2"};
    // Password field echo char
    public static final char ECHO_CHAR = 0x2022;
    // Reserved name of backup entry
    public static final String BACKUP_TITLE = "sftp-automated-backup";

    // Themes available in FlatLaf
    public static final String[] FLATLAF_THEMES = {"LIGHT", "DARK", "INTELLIJ", "DARCULA", "ONE_DARK", "NORD", "DARK_PURPLE", "MACOS_LIGHT", "MACOS_DARK", "SYSTEM_DEFAULT", "SWING_DEFAULT"};
    // Standard FlatLaf theme
    public static final String FLATLAF_THEME = "DARK";
    // FlatLaf theme that is currently active
    public static String ACTIVE_THEME;

    // Window icon
    public static Image IMAGE;
    // Standard window font
    public static Font STANDARD_FONT;
    // Standard monospace font
    public static Font MONOSPACE_FONT;
    // Font used for Tetris
    public static Font TETRIS_FONT;

    private static final Dimension STANDARD_TETRIS_WINDOW_SIZE = new Dimension(600, 800);
    public static Dimension SCALED_TETRIS_WINDOW_SIZE;

    private static final Dimension STANDARD_TETRIS_LABEL_SIZE = new Dimension(520, 640);
    public static Dimension SCALED_TETRIS_LABEL_SIZE;

    // Tetris block width and height in px
    private static final int STANDARD_TETRIS_BLOCK_DIMENSION = 32;
    public static int SCALED_TETRIS_BLOCK_DIMENSION;

    private static final int STANDARD_TETRIS_WIDTH_MARGIN = 350;
    public static int SCALED_TETRIS_WIDTH_MARGIN;

    public static final int STANDARD_TETRIS_HEIGHT_MARGIN = 544;
    public static int SCALED_TETRIS_HEIGHT_MARGIN;

    private static final int STANDARD_TETRIS_ELEMENT_MARGIN = 50;
    public static int SCALED_TETRIS_ELEMENT_MARGIN;

    private static final int STANDARD_TETRIS_TEXT_MARGIN = 40;
    public static int SCALED_TETRIS_TEXT_MARGIN;

    private static final int STANDARD_TETRIS_BOTTOM_ELEMENT = 520;
    public static int SCALED_TETRIS_BOTTOM_ELEMENT;

    public static void loadResources() {

        initFlatlaf();

        try {
            IMAGE = ImageIO.read(Objects.requireNonNull(Main.class.getResource("/icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        InputStream is = Configuration.class.getResourceAsStream("/JetBrainsMono-Regular.ttf");
        InputStream is2 = Configuration.class.getResourceAsStream("/PixeloidSansBold-RpeJo.ttf");
        try {
            STANDARD_FONT = (new JLabel().getFont())
                    .deriveFont((float) (STANDARD_FONTSIZE / 1920 * screenSize.getWidth()));
            MONOSPACE_FONT = (Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(is)))
                    .deriveFont((float) (STANDARD_FONTSIZE / 1920 * screenSize.getWidth()));
            TETRIS_FONT = (Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(is2)))
                    .deriveFont((float) (STANDARD_TETRIS_FONTSIZE / 1920 * screenSize.getWidth()));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        SCALED_TEXT_FIELD_SIZE = new Dimension(
                (int) (STANDARD_TEXT_FIELD_SIZE.getWidth() / 1920 * screenSize.getWidth()),
                (int) (STANDARD_TEXT_FIELD_SIZE.getHeight() / 1080 * screenSize.getHeight())
        );

        SCALED_PASSWORD_FIELD_SIZE = new Dimension(
                (int) ((STANDARD_TEXT_FIELD_SIZE.getWidth() + 100) / 1920 * screenSize.getWidth()),
                (int) (SCALED_TEXT_FIELD_SIZE.getHeight())
        );

        SCALED_BUTTON_SIZE = new Dimension(
                (int) (STANDARD_BUTTON_SIZE.getWidth() / 1920 * screenSize.getWidth()),
                (int) (STANDARD_BUTTON_SIZE.getHeight() / 1080 * screenSize.getHeight())
        );

        SCALED_FILE_CHOOSER_SIZE = new Dimension(
                (int) (STANDARD_FILE_CHOOSER_SIZE.getWidth() / 1920 * screenSize.getWidth()),
                (int) (STANDARD_FILE_CHOOSER_SIZE.getHeight() / 1080 * screenSize.getHeight())
        );

        SCALED_TETRIS_WINDOW_SIZE = new Dimension(
                (int) (STANDARD_TETRIS_WINDOW_SIZE.getWidth() / 1920 * screenSize.getWidth()),
                (int) (STANDARD_TETRIS_WINDOW_SIZE.getHeight() / 1080 * screenSize.getHeight())
        );

        SCALED_TETRIS_LABEL_SIZE = new Dimension(
                (int) (STANDARD_TETRIS_LABEL_SIZE.getWidth() / 1920 * screenSize.getWidth()),
                (int) (STANDARD_TETRIS_LABEL_SIZE.getHeight() / 1080 * screenSize.getHeight())
        );

        SCALED_TABLE_ROW_HEIGHT = (int) ((double) STANDARD_TABLE_ROW_HEIGHT / 1080 * screenSize.getHeight());
        SCALED_SCROLL_BAR_THICKNESS = (int) ((double) STANDARD_SCROLL_BAR_WIDTH / 1920 * screenSize.getWidth());

        SCALED_TETRIS_BLOCK_DIMENSION = (int) ((double) STANDARD_TETRIS_BLOCK_DIMENSION / 1920 * screenSize.getWidth());
        SCALED_TETRIS_WIDTH_MARGIN = (int) ((double) STANDARD_TETRIS_WIDTH_MARGIN / 1920 * screenSize.getWidth());
        SCALED_TETRIS_HEIGHT_MARGIN = (int) ((double) STANDARD_TETRIS_HEIGHT_MARGIN / 1080 * screenSize.getHeight());
        SCALED_TETRIS_ELEMENT_MARGIN = (int) ((double) STANDARD_TETRIS_ELEMENT_MARGIN / 1080 * screenSize.getHeight());
        SCALED_TETRIS_TEXT_MARGIN = (int) ((double) STANDARD_TETRIS_TEXT_MARGIN / 1080 * screenSize.getHeight());
        SCALED_TETRIS_BOTTOM_ELEMENT = (int) ((double) STANDARD_TETRIS_BOTTOM_ELEMENT / 1080 * screenSize.getHeight());
    }

    public static void initFlatlaf() {

        String theme;
        try {
            theme = FileWizard.getSelectedTheme(false);
        } catch (IOException e) {
            theme = Configuration.FLATLAF_THEME;
        }

        try {
            setTheme(theme);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTheme(String theme) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        if (theme.equals(FLATLAF_THEMES[0]))
            FlatLightLaf.setup();
        else if (theme.equals(FLATLAF_THEMES[1]))
            FlatDarkLaf.setup();
        else if (theme.equals(FLATLAF_THEMES[2]))
            FlatIntelliJLaf.setup();
        else if (theme.equals(FLATLAF_THEMES[3]))
            FlatDarculaLaf.setup();
        else if (theme.equals(FLATLAF_THEMES[4]))
            IntelliJTheme.setup(Configuration.class.getResourceAsStream("/themes/one_dark.theme.json"));
        else if (theme.equals(FLATLAF_THEMES[5]))
            IntelliJTheme.setup(Configuration.class.getResourceAsStream("/themes/nord.theme.json"));
        else if (theme.equals(FLATLAF_THEMES[6]))
            IntelliJTheme.setup(Configuration.class.getResourceAsStream("/themes/DarkPurple.theme.json"));
        else if (theme.equals(FLATLAF_THEMES[7]))
            FlatMacLightLaf.setup();
        else if (theme.equals(FLATLAF_THEMES[8]))
            FlatMacDarkLaf.setup();
        else if (theme.equals(FLATLAF_THEMES[9]))
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        else if (theme.equals(FLATLAF_THEMES[10]))
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());


        ACTIVE_THEME = theme;
    }
}
