# Changelog - PasswordManager

## Release 1.3.2
- Initial commit

## Release 1.3.3
- Added notes to database entries

## Release 1.3.4
- Added key derivation instead of password hashing for encryption
    - PBKDF2 algorithm
    - 310,000 iterations
    - Randomly salted
    - SHA-256 HMAC algorithm
- Allowing empty entries

## Release 1.3.5
- Added saving of previously opened database as run command
    - `.pmrc` in `%Appdata%/PasswordManager/`
    - Changing of the database to open in password input

## Release 1.3.6
- All recently opened files are stored in the `.pmrc` file
    - At startup, no file is directly opened, but a combo box will let one select a file from rc

## Release 1.3.7
- Added little secret game to password manager

## Release 1.3.8
- Tetris updates
- Switched back to Java 8 language standard

## Release 1.3.9
- `.pmrc` 
  - in user home -> Linux
  - hiding invalid files instead of deleting them
- Tetris block update
- Tetris access update
- default combo box index as path

## Release 1.4.0
- Major Tetris graphics update

## Release 1.4.1
- "Tetris bugfix release"
  - Block rotation
  - Visibility of non-existent blocks
  - Moving blocks within animations

## Release 1.4.2
- Added compilation script
- Added Tetris pause
- Improved README

## Release 1.4.3
- Added remote compatibility JSON
  - Checking whether newer versions still are available
  - Remote script getting updated
  - Old versions staying the same
  - Works only with internet connection!