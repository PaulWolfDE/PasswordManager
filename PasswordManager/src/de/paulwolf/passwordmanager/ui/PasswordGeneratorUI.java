package de.paulwolf.passwordmanager.ui;

import com.nulabinc.zxcvbn.Zxcvbn;
import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.wizards.ConversionWizard;
import de.paulwolf.passwordmanager.wizards.PasswordWizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class PasswordGeneratorUI extends JFrame {

    private static final String[] CHARSETS = {
            "abcdefghijklmnopqrstuvwxyz",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "0123456789",
            "+$&*'?|@!-%",
            "'(),./:;<=>{}[]_~\\\""
    };

    private static final PasswordGeneratorConfiguration[] CONFIGURATIONS = {
            new PasswordGeneratorConfiguration("Standard", 16, 0b01111, ""),
            new PasswordGeneratorConfiguration("Letters & Digits", 20, 0b00111, ""),
            new PasswordGeneratorConfiguration("Paul's config", 20, 0b01111, ".,/:;=_{}[]")
    };

    final JPanel wrapper = new JPanel();

    final JLabel lengthLabel = new JLabel("Password length:");
    final JTextField lengthField;

    final JLabel charsetsLabel = new JLabel("Charsets:");
    final JCheckBox lowercaseBox = new JCheckBox("Lowercase (abc...)");
    final JCheckBox uppercaseBox = new JCheckBox("Uppercase (ABC...)");
    final JCheckBox digitsBox = new JCheckBox("Digits (012...)");
    final JCheckBox specialsBox = new JCheckBox("Special characters");
    final JCheckBox commonSpecialsBox = new JCheckBox("Common (+$&*#?|@!-%)");
    final JCheckBox advancedSpecialsBox = new JCheckBox("Advanced ('(),./:;<=>{}[]_~\\\")");
    final JLabel additionalCharactersLabel = new JLabel("Include additional characters:");
    final JTextField additionalCharactersField = new JTextField(20);

    final JLabel configurationLabel = new JLabel("Configurations:");
    final JComboBox<PasswordGeneratorConfiguration> configurationBox = new JComboBox<>(CONFIGURATIONS);

    final JLabel asciiPasswordLabel = new JLabel("ASCII:");
    final JTextField asciiPasswordField = new JTextField(20);

    final JLabel hexPasswordLabel = new JLabel("Hexadecimal:");
    final JTextField hexPasswordField = new JTextField(20);

    final JLabel base64PasswordLabel = new JLabel("Base64:");
    final JTextField base64PasswordField = new JTextField(20);

    final JLabel entropyLabel = new JLabel("Password entropy:");
    final JLabel entropyDisplay = new JLabel("0 Bits");

    final JButton generatePassword = new JButton("Generate Password");
    final JButton acceptPassword = new JButton("Accept Password");
    final JButton closeGenerator = new JButton("Cancel");

    final PasswordAcceptingUI ui;

    public PasswordGeneratorUI(PasswordAcceptingUI ui, String password) {

        this.ui = ui;

        if (password != null) {
            this.asciiPasswordField.setText(password);
            this.hexPasswordField.setText(ConversionWizard.bytesToHex(password.getBytes()));
            this.base64PasswordField.setText(new String(Objects.requireNonNull(ConversionWizard.bytesToBase64(password.getBytes()))));
        }

        // Letting the length field only accept numbers
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.setGroupingUsed(false);
        lengthField = new JFormattedTextField(decimalFormat);
        lengthField.setColumns(20);
        lengthField.setText("20");

        lengthField.setPreferredSize(new Dimension(200, 26));
        additionalCharactersField.setPreferredSize(new Dimension(200, 26));
        asciiPasswordField.setPreferredSize(new Dimension(200, 26));
        hexPasswordField.setPreferredSize(new Dimension(200, 26));
        base64PasswordField.setPreferredSize(new Dimension(200, 26));

        generatePassword.setPreferredSize(new Dimension(200, 26));
        acceptPassword.setPreferredSize(new Dimension(200, 26));
        closeGenerator.setPreferredSize(new Dimension(200, 26));

        asciiPasswordField.setFont(new Font("Consolas", Font.PLAIN, 14));
        hexPasswordField.setFont(new Font("Consolas", Font.PLAIN, 14));
        base64PasswordField.setFont(new Font("Consolas", Font.PLAIN, 14));

        lengthLabel.setFont(lengthLabel.getFont().deriveFont(14.0f));
        lengthField.setFont(lengthField.getFont().deriveFont(14.0f));
        charsetsLabel.setFont(charsetsLabel.getFont().deriveFont(14.0f));
        lowercaseBox.setFont(lowercaseBox.getFont().deriveFont(14.0f));
        uppercaseBox.setFont(uppercaseBox.getFont().deriveFont(14.0f));
        digitsBox.setFont(digitsBox.getFont().deriveFont(14.0f));
        specialsBox.setFont(specialsBox.getFont().deriveFont(14.0f));
        commonSpecialsBox.setFont(commonSpecialsBox.getFont().deriveFont(14.0f));
        advancedSpecialsBox.setFont(advancedSpecialsBox.getFont().deriveFont(14.0f));
        additionalCharactersLabel.setFont(additionalCharactersLabel.getFont().deriveFont(14.0f));
        additionalCharactersField.setFont(additionalCharactersField.getFont().deriveFont(14.0f));
        configurationLabel.setFont(configurationLabel.getFont().deriveFont(14.0f));
        configurationBox.setFont(configurationBox.getFont().deriveFont(14.0f));
        asciiPasswordLabel.setFont(asciiPasswordLabel.getFont().deriveFont(14.0f));
        asciiPasswordField.setFont(asciiPasswordField.getFont().deriveFont(14.0f));
        hexPasswordLabel.setFont(hexPasswordLabel.getFont().deriveFont(14.0f));
        hexPasswordField.setFont(hexPasswordField.getFont().deriveFont(14.0f));
        base64PasswordLabel.setFont(base64PasswordLabel.getFont().deriveFont(14.0f));
        base64PasswordField.setFont(base64PasswordField.getFont().deriveFont(14.0f));
        entropyLabel.setFont(entropyDisplay.getFont().deriveFont(14.0f));
        entropyDisplay.setFont(entropyDisplay.getFont().deriveFont(14.0f));
        generatePassword.setFont(generatePassword.getFont().deriveFont(14.0f));
        acceptPassword.setFont(acceptPassword.getFont().deriveFont(14.0f));
        closeGenerator.setFont(closeGenerator.getFont().deriveFont(14.0f));

        wrapper.setLayout(new GridBagLayout());

        wrapper.add(lengthLabel, UIUtils.createGBC(0, 0, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(lengthField, UIUtils.createGBC(1, 0, GridBagConstraints.HORIZONTAL, 5, 1));

        wrapper.add(charsetsLabel, UIUtils.createGBC(0, 1, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(lowercaseBox, UIUtils.createGBC(0, 2, GridBagConstraints.HORIZONTAL, 3, 1, 1.0));
        wrapper.add(uppercaseBox, UIUtils.createGBC(3, 2, GridBagConstraints.HORIZONTAL, 3, 1, 1.0));
        wrapper.add(digitsBox, UIUtils.createGBC(0, 3, GridBagConstraints.HORIZONTAL, 3, 1, 1.0));
        wrapper.add(specialsBox, UIUtils.createGBC(3, 3, GridBagConstraints.HORIZONTAL, 3, 1, 1.0));
        wrapper.add(commonSpecialsBox, UIUtils.createGBC(0, 4, GridBagConstraints.HORIZONTAL, 3, 1, 1.0));
        wrapper.add(advancedSpecialsBox, UIUtils.createGBC(3, 4, GridBagConstraints.HORIZONTAL, 3, 1, 1.0));

        wrapper.add(additionalCharactersLabel, UIUtils.createGBC(0, 5, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(additionalCharactersField, UIUtils.createGBC(1, 5, GridBagConstraints.HORIZONTAL, 5, 1));

        wrapper.add(configurationLabel, UIUtils.createGBC(0, 6, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(configurationBox, UIUtils.createGBC(1, 6, GridBagConstraints.HORIZONTAL, 5, 1));

        wrapper.add(asciiPasswordLabel, UIUtils.createGBC(0, 7, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(asciiPasswordField, UIUtils.createGBC(1, 7, GridBagConstraints.HORIZONTAL, 5, 1));
        wrapper.add(hexPasswordLabel, UIUtils.createGBC(0, 8, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(hexPasswordField, UIUtils.createGBC(1, 8, GridBagConstraints.HORIZONTAL, 5, 1));
        wrapper.add(base64PasswordLabel, UIUtils.createGBC(0, 9, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(base64PasswordField, UIUtils.createGBC(1, 9, GridBagConstraints.HORIZONTAL, 5, 1));

        wrapper.add(entropyLabel, UIUtils.createGBC(0, 10, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(entropyDisplay, UIUtils.createGBC(1, 10, GridBagConstraints.HORIZONTAL, 5, 1));

        wrapper.add(generatePassword, UIUtils.createGBC(0, 11, GridBagConstraints.HORIZONTAL, 2, 1, 1.0));
        wrapper.add(acceptPassword, UIUtils.createGBC(2, 11, GridBagConstraints.HORIZONTAL, 2, 1, 1.0));
        wrapper.add(closeGenerator, UIUtils.createGBC(4, 11, GridBagConstraints.HORIZONTAL, 2, 1, 1.0));

        this.add(wrapper);
        this.setIconImage(Main.IMAGE);
        this.setTitle("Random Password Generator");
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        setConfiguration(CONFIGURATIONS[0]);
        updateEntropy();

        generatePassword.addActionListener(e -> {

            byte[] charset = selectCharset();

            hexPasswordField.setForeground(Color.BLACK);
            base64PasswordField.setForeground(Color.BLACK);

            if (charset.length > 0) {

                byte[] pw = PasswordWizard.generatePassword(Integer.parseInt(lengthField.getText()), charset);

                asciiPasswordField.setText(new String(pw));
                hexPasswordField.setText(ConversionWizard.bytesToHex(pw));
                base64PasswordField.setText(new String(Objects.requireNonNull(ConversionWizard.bytesToBase64(pw))));
            }

            updateEntropy();
        });

        acceptPassword.addActionListener(e -> {

            ui.setPassword(asciiPasswordField.getText());
            this.setVisible(false);
        });

        closeGenerator.addActionListener(e -> {

            this.setVisible(false);
        });

        configurationBox.addActionListener(e -> setConfiguration((PasswordGeneratorConfiguration) Objects.requireNonNull(configurationBox.getSelectedItem())));

        specialsBox.addActionListener(e -> {
            commonSpecialsBox.setEnabled(specialsBox.isSelected());
            advancedSpecialsBox.setEnabled(specialsBox.isSelected());
        });

        asciiPasswordField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                updateEntropy();
                hexPasswordField.setText(ConversionWizard.bytesToHex(asciiPasswordField.getText().getBytes()));
                base64PasswordField.setText(new String(Objects.requireNonNull(ConversionWizard.bytesToBase64(asciiPasswordField.getText().getBytes()))));
            }
        });
        hexPasswordField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                // Check whether hex string is valid
                String hexPassword = hexPasswordField.getText();
                if (hexPassword.matches("-?[0-9a-fA-F]+")) {
                    if (hexPassword.length() % 2 == 0) {
                        asciiPasswordField.setText(new String(ConversionWizard.hexToBytes(hexPasswordField.getText())));
                        base64PasswordField.setText(new String(Objects.requireNonNull(ConversionWizard.bytesToBase64(asciiPasswordField.getText().getBytes()))));
                        updateEntropy();
                        hexPasswordField.setForeground(Color.BLACK);
                    }
                } else {
                    hexPasswordField.setForeground(Color.RED);
                    asciiPasswordField.setText("");
                    base64PasswordField.setText("");
                }
            }
        });
        base64PasswordField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                String base64Password = base64PasswordField.getText();

                if (ConversionWizard.base64ToBytes(base64Password.getBytes()) != null) {
                    asciiPasswordField.setText(new String(Objects.requireNonNull(ConversionWizard.base64ToBytes(base64Password.getBytes()))));
                    hexPasswordField.setText(ConversionWizard.bytesToHex(asciiPasswordField.getText().getBytes()));
                    updateEntropy();
                    base64PasswordField.setForeground(Color.BLACK);
                } else {
                    base64PasswordField.setForeground(Color.RED);
                    asciiPasswordField.setText("");
                    hexPasswordField.setText("");
                }
            }
        });
    }
    private byte[] selectCharset() {

        StringBuilder charset = new StringBuilder();

        if (lowercaseBox.isSelected())
            charset.append(CHARSETS[0]);
        if (uppercaseBox.isSelected())
            charset.append(CHARSETS[1]);
        if (digitsBox.isSelected())
            charset.append(CHARSETS[2]);
        if (specialsBox.isSelected()) {
            if (commonSpecialsBox.isSelected())
                charset.append(CHARSETS[3]);
            if (advancedSpecialsBox.isSelected())
                charset.append(CHARSETS[4]);
        }

        String userChars = additionalCharactersField.getText();

        for (int i = 0; i < userChars.length(); i++) {
            if (!charset.toString().contains(String.valueOf(userChars.charAt(i))))
                charset.append(userChars.charAt(i));
        }

        return charset.toString().getBytes();
    }

    private byte[] getCharset(String password) {

       StringBuilder charset = new StringBuilder();

        for (String s : CHARSETS) {
            for (int j = 0; j < s.length(); j++) {
                if (password.contains(String.valueOf(s.charAt(j)))) {
                    charset.append(s);
                    break;
                }
            }
        }
        return charset.toString().getBytes();
    }

    private void setConfiguration(PasswordGeneratorConfiguration configuration) {

        lengthField.setText(String.valueOf(configuration.getPasswordLength()));
        int selectedCharsets = configuration.getSelectedCharsets();

        lowercaseBox.setSelected((selectedCharsets & (1 << 0)) != 0);
        uppercaseBox.setSelected((selectedCharsets & (1 << 1)) != 0);
        digitsBox.setSelected((selectedCharsets & (1 << 2)) != 0);
        commonSpecialsBox.setSelected((selectedCharsets & (1 << 3)) != 0);
        advancedSpecialsBox.setSelected((selectedCharsets & (1 << 4)) != 0);

        specialsBox.setSelected(commonSpecialsBox.isSelected() || advancedSpecialsBox.isSelected());

        additionalCharactersField.setText(configuration.getAdditionalCharacters());

        commonSpecialsBox.setEnabled(specialsBox.isSelected());
        advancedSpecialsBox.setEnabled(specialsBox.isSelected());
    }

    private void updateEntropy() {

        Zxcvbn zxcvbn = new Zxcvbn();
        int passwordScore = zxcvbn.measure(asciiPasswordField.getText()).getScore();
        int charsetLength = getCharset(asciiPasswordField.getText()).length;
        double passwordEntropy = 0;
        if (charsetLength != 0)
            passwordEntropy = Math.log(Math.pow(charsetLength, asciiPasswordField.getText().length())) / Math.log(2);

        String passwordStrength;

        if (passwordScore == 0)
            passwordStrength = "Weak";
        else if (passwordScore == 1)
            passwordStrength = "Fair";
        else if (passwordScore == 2)
            passwordStrength = "Good";
        else if (passwordScore == 3)
            passwordStrength = "Strong";
        else
            passwordStrength = "Very strong";

        entropyDisplay.setText(String.format("%.2f Bits (Score: %s)", passwordEntropy, passwordStrength));
    }
}

class PasswordGeneratorConfiguration {

    private final String configurationName;
    private final int passwordLength;
    private final int selectedCharsets;
    private final String additionalCharacters;

    PasswordGeneratorConfiguration(String configurationName, int passwordLength, int selectedCharsets, String additionalCharacters) {

        this.configurationName = configurationName;
        this.passwordLength = passwordLength;
        this.selectedCharsets = selectedCharsets;
        this.additionalCharacters = additionalCharacters;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public int getSelectedCharsets() {
        return selectedCharsets;
    }

    public String getAdditionalCharacters() {
        return additionalCharacters;
    }

    @Override
    public String toString() {
        return this.configurationName;
    }
}