package de.paulwolf.passwordmanager.ui.windows;

import com.nulabinc.zxcvbn.Zxcvbn;
import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.ui.UIUtils;
import de.paulwolf.passwordmanager.ui.components.*;
import de.paulwolf.passwordmanager.wizards.EncodingWizard;
import de.paulwolf.passwordmanager.wizards.PasswordWizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.charset.StandardCharsets;
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

    private static final String PRINTABLE_ASCII_CHARACTERS = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    private static final PasswordGeneratorConfiguration[] CONFIGURATIONS = {
            new PasswordGeneratorConfiguration("Standard", 16, 0b01111, ""),
            new PasswordGeneratorConfiguration("Letters & Digits", 20, 0b00111, ""),
            new PasswordGeneratorConfiguration("Paul's config", 20, 0b01111, ".,/:;=_{}[]"),
            new PasswordGeneratorConfiguration("All printable ASCII (including space)", 16,0, PRINTABLE_ASCII_CHARACTERS)
    };

    final JPanel wrapper = new JPanel();

    final ScaledLabel lengthLabel = new ScaledLabel("Password length:");
    final ScaledFormattedTextField lengthField;

    final ScaledLabel charsetsLabel = new ScaledLabel("Charsets:");
    final ScaledCheckBox lowercaseBox = new ScaledCheckBox("Lowercase (abc...)");
    final ScaledCheckBox uppercaseBox = new ScaledCheckBox("Uppercase (ABC...)");
    final ScaledCheckBox digitsBox = new ScaledCheckBox("Digits (012...)");
    final ScaledCheckBox specialsBox = new ScaledCheckBox("Special characters");
    final ScaledCheckBox commonSpecialsBox = new ScaledCheckBox("Common (+$&*#?|@!-%)");
    final ScaledCheckBox advancedSpecialsBox = new ScaledCheckBox("Advanced ('(),./:;<=>{}[]_~\\\")");
    final ScaledLabel additionalCharactersLabel = new ScaledLabel("Include additional characters:");
    final ScaledTextField additionalCharactersField = new ScaledTextField();

    final ScaledLabel configurationLabel = new ScaledLabel("Configurations:");
    final ScaledComboBox<PasswordGeneratorConfiguration> configurationBox = new ScaledComboBox<>(CONFIGURATIONS);

    final ScaledLabel utf8PasswordLabel = new ScaledLabel("UTF-8:");
    final ScaledTextField utf8PasswordField = new ScaledTextField();

    final ScaledLabel hexPasswordLabel = new ScaledLabel("Hexadecimal:");
    final ScaledTextField hexPasswordField = new ScaledTextField();

    final ScaledLabel base64PasswordLabel = new ScaledLabel("Base64:");
    final ScaledTextField base64PasswordField = new ScaledTextField();

    final ScaledLabel entropyLabel = new ScaledLabel("Password entropy:");
    final ScaledLabel entropyDisplay = new ScaledLabel("0.00 Bits (Weak)");

    final ScaledButton generatePassword = new ScaledButton("Generate Password");
    final ScaledButton acceptPassword = new ScaledButton("Accept Password");
    final ScaledButton closeGenerator = new ScaledButton("Cancel");

    final PasswordAcceptingUI ui;

    public PasswordGeneratorUI(PasswordAcceptingUI ui, String password, int encoding) {

        this.ui = ui;

        additionalCharactersField.setFont(Configuration.MONOSPACE_FONT);
        utf8PasswordField.setFont(Configuration.MONOSPACE_FONT);
        hexPasswordField.setFont(Configuration.MONOSPACE_FONT);
        base64PasswordField.setFont(Configuration.MONOSPACE_FONT);

        if (password != null) {
            if (encoding == 0) {
                this.utf8PasswordField.setText(password);
                this.hexPasswordField.setText(EncodingWizard.bytesToHex(password.getBytes(Configuration.STANDARD_CHARSET)));
                this.base64PasswordField.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(password.getBytes(Configuration.STANDARD_CHARSET))), StandardCharsets.US_ASCII));
            } else if (encoding == 1) {
                this.hexPasswordField.setText(password);
                this.utf8PasswordField.setText(new String(EncodingWizard.hexToBytes(password), Configuration.STANDARD_CHARSET));
                this.base64PasswordField.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(utf8PasswordField.getText().getBytes(Configuration.STANDARD_CHARSET))), StandardCharsets.US_ASCII));
            } else {
                this.base64PasswordField.setText(password);
                this.utf8PasswordField.setText(new String(Objects.requireNonNull(EncodingWizard.base64ToBytes(password.getBytes(StandardCharsets.US_ASCII))), Configuration.STANDARD_CHARSET));
                this.hexPasswordField.setText(EncodingWizard.bytesToHex(utf8PasswordField.getText().getBytes(Configuration.STANDARD_CHARSET)));
            }
        }

        // Letting the length field only accept numbers
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.setGroupingUsed(false);
        lengthField = new ScaledFormattedTextField(decimalFormat);
        lengthField.setText("20");

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

        wrapper.add(utf8PasswordLabel, UIUtils.createGBC(0, 7, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(utf8PasswordField, UIUtils.createGBC(1, 7, GridBagConstraints.HORIZONTAL, 5, 1));
        wrapper.add(hexPasswordLabel, UIUtils.createGBC(0, 8, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(hexPasswordField, UIUtils.createGBC(1, 8, GridBagConstraints.HORIZONTAL, 5, 1));
        wrapper.add(base64PasswordLabel, UIUtils.createGBC(0, 9, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(base64PasswordField, UIUtils.createGBC(1, 9, GridBagConstraints.HORIZONTAL, 5, 1));

        wrapper.add(entropyLabel, UIUtils.createGBC(0, 10, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(entropyDisplay, UIUtils.createGBC(1, 10, GridBagConstraints.HORIZONTAL, 5, 1));

        if (this.ui == null) {
            wrapper.add(generatePassword, UIUtils.createGBC(0, 11, GridBagConstraints.HORIZONTAL, 3, 1, 1.0));
            wrapper.add(closeGenerator, UIUtils.createGBC(3, 11, GridBagConstraints.HORIZONTAL, 3, 1, 1.0));
        } else {
            wrapper.add(generatePassword, UIUtils.createGBC(0, 11, GridBagConstraints.HORIZONTAL, 2, 1, 1.0));
            wrapper.add(acceptPassword, UIUtils.createGBC(2, 11, GridBagConstraints.HORIZONTAL, 2, 1, 1.0));
            wrapper.add(closeGenerator, UIUtils.createGBC(4, 11, GridBagConstraints.HORIZONTAL, 2, 1, 1.0));
        }

        setConfiguration(CONFIGURATIONS[0]);
        updateEntropy();

        this.add(wrapper);
        this.setIconImage(Configuration.IMAGE);
        this.setTitle("Random Password Generator");
        this.setLocationRelativeTo((Component) ui);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setVisible(true);

        generatePassword.addActionListener(e -> {

            byte[] charset = selectCharset();

            hexPasswordField.setForeground(Color.BLACK);
            base64PasswordField.setForeground(Color.BLACK);

            if (charset.length > 0) {

                byte[] pw = PasswordWizard.generatePassword(Integer.parseInt(lengthField.getText()), charset);

                utf8PasswordField.setText(new String(pw, Configuration.STANDARD_CHARSET));
                hexPasswordField.setText(EncodingWizard.bytesToHex(pw));
                base64PasswordField.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(pw)), StandardCharsets.US_ASCII));
            }

            updateEntropy();
        });

        acceptPassword.addActionListener(e -> {

            assert ui != null;
            ui.setPassword(utf8PasswordField.getText());
            this.setVisible(false);
        });

        closeGenerator.addActionListener(e -> this.setVisible(false));

        configurationBox.addActionListener(e -> setConfiguration((PasswordGeneratorConfiguration) Objects.requireNonNull(configurationBox.getSelectedItem())));

        specialsBox.addActionListener(e -> {
            commonSpecialsBox.setEnabled(specialsBox.isSelected());
            advancedSpecialsBox.setEnabled(specialsBox.isSelected());
        });

        utf8PasswordField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                updateEntropy();
                hexPasswordField.setText(EncodingWizard.bytesToHex(utf8PasswordField.getText().getBytes(Configuration.STANDARD_CHARSET)));
                base64PasswordField.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(utf8PasswordField.getText().getBytes(Configuration.STANDARD_CHARSET))), StandardCharsets.US_ASCII));
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
                if (hexPassword.matches("-?[\\da-fA-F]+")) {
                    if (hexPassword.length() % 2 == 0) {
                        utf8PasswordField.setText(new String(EncodingWizard.hexToBytes(hexPasswordField.getText()), Configuration.STANDARD_CHARSET));
                        base64PasswordField.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(utf8PasswordField.getText().getBytes(Configuration.STANDARD_CHARSET))), StandardCharsets.US_ASCII));
                        updateEntropy();
                        hexPasswordField.setForeground(Color.BLACK);
                    }
                } else {
                    hexPasswordField.setForeground(Color.RED);
                    utf8PasswordField.setText("");
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

                if (EncodingWizard.base64ToBytes(base64Password.getBytes(StandardCharsets.US_ASCII)) != null) {
                    utf8PasswordField.setText(new String(Objects.requireNonNull(EncodingWizard.base64ToBytes(base64Password.getBytes(StandardCharsets.US_ASCII)))));
                    hexPasswordField.setText(EncodingWizard.bytesToHex(utf8PasswordField.getText().getBytes(Configuration.STANDARD_CHARSET)));
                    updateEntropy();
                    base64PasswordField.setForeground(Color.BLACK);
                } else {
                    base64PasswordField.setForeground(Color.RED);
                    utf8PasswordField.setText("");
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

        return charset.toString().getBytes(Configuration.STANDARD_CHARSET);
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
        return charset.toString().getBytes(Configuration.STANDARD_CHARSET);
    }

    private int getCharsetLength(String password) {

        int charsetLength = getCharset(password).length;

        character:
        for (int i = 0; i < password.length(); i++) {
            for (String charset : CHARSETS) {
                if (charset.contains(String.valueOf(password.charAt(i))))
                    continue character;
            }
            if (PRINTABLE_ASCII_CHARACTERS.contains(String.valueOf(password.charAt(i)))) {
                charsetLength = PRINTABLE_ASCII_CHARACTERS.length();
                continue;
            }
            return 128;
        }
        return charsetLength;
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
        int passwordScore = zxcvbn.measure(utf8PasswordField.getText()).getScore();
        int charsetLength = getCharsetLength(utf8PasswordField.getText());

        double passwordEntropy = Math.log(Math.pow(charsetLength, utf8PasswordField.getText().length())) / Math.log(2);

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