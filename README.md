# PasswordManager
This Java password manager is an open source software to encrypt confidential log-in data on the local hard disk. It uses modern cryptographic techniques to store the information as securely as possible. Encrypted information is combined into a file that is written to the hard disk. Said file can later be opened again by the password manager by entering a master password in order to view the data in a table view.

## Installation
Installation can be done by either downloading one of the release JAR files or by compiling the project yourself. Note that the most recent release does not have to reflect the most recent source files.

Release files compiled to Java 1.8 language standard can be found at https://github.com/PaulWolfDE/PasswordManager/releases.

For compiling the project, a Java development kit (JDK) is required. To create a JAR file, one must run the `compile.sh` script with a UNIX-like shell found in the folder `PasswordManager`. This will create a runnable JAR file in the same folder. 

```sh
$ ./compile.sh
```

On Windows, a Linux subsystem can be downloaded for running said file.

## Execution
The execution requires a Java runtime environment (JRE) at least at version `1.8` for running release files or the version compiled with.

```sh
$ java -jar PasswordManager.jar
```

## Usage
For tutorials on using the program, check the documentation.

## Credits
[<img alt="https://www.gnu.org/software/gnu-crypto/" src="https://img.shields.io/badge/Library-GNU Crypto-blue.svg">](https://www.gnu.org/software/gnu-crypto/)

Library containing various cryptographic methods.

[<img alt="https://github.com/stleary/JSON-java" src="https://img.shields.io/badge/Library-JSON Java-blue.svg">](https://github.com/stleary/JSON-java)

Library for JSON en- and decoding to check compatibilities.

[<img alt="https://github.com/nulab/zxcvbn4j" src="https://img.shields.io/badge/Library-zxcvbn4j-blue.svg">](https://github.com/nulab/zxcvbn4j)

Java implementation of the zxcvbn password checker.

[<img alt="http://www.jcraft.com/jsch/" src="https://img.shields.io/badge/Library-Jsch-blue.svg">](http://www.jcraft.com/jsch/)

Library for secure tunnel connection to perform SFTP.

[<img alt="https://github.com/JFormDesigner/FlatLaf" src="https://img.shields.io/badge/Library-FlatLaf-blue.svg">](https://github.com/JFormDesigner/FlatLaf)

Modern Look and Feel for the Java Swing user interface.

[<img alt="https://github.com/one-dark/jetbrains-one-dark-theme" src="https://img.shields.io/badge/Theme-OneDark-tan.svg">](https://github.com/one-dark/jetbrains-one-dark-theme) 
[<img alt="https://github.com/nordtheme/jetbrains" src="https://img.shields.io/badge/Theme-Nord-tan.svg">](https://github.com/nordtheme/jetbrains)
[<img alt="https://github.com/OlyaB/DarkPurpleTheme" src="https://img.shields.io/badge/Theme-DarkPurple-tan.svg">](https://github.com/OlyaB/DarkPurpleTheme)

Flatlaf IntelliJ themes.

[<img alt="https://www.jetbrains.com/lp/mono/" src="https://img.shields.io/badge/Font-JetBrainsMono-yellow.svg">](https://www.jetbrains.com/lp/mono/)

Free, open-source monospace font family.

[<img alt="https://www.fontspace.com/pixeloid-font-f69232" src="https://img.shields.io/badge/Font-Pixeloid-yellow.svg">](https://www.fontspace.com/pixeloid-font-f69232)

Free old school font used for Tetris

[<img alt="https://keepass.info/" src="https://img.shields.io/badge/Inspiration-KeePass-green.svg">](https://keepass.info/)

Open-source password manager.

## License
[<img alt="https://www.gnu.org/licenses/gpl-3.0.html" src="https://img.shields.io/badge/License-GPLv3-important.svg">](https://www.gnu.org/licenses/gpl-3.0.html)