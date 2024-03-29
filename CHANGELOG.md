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

## Release 1.4.4
- Saving IV and Salt in hexadecimal form

## Release 1.4.5
- Added automatic update indication

## Release 1.4.6
- Added password strength checkers to editable password fields

## Release 1.4.7
- Added automatic backup to remote server via SFTP
  - Explanations can be found in the documentation

## Release 1.4.8
- Added advanced password generated
- Substituted password fields for fields with variable encoding

## Release 2.0.0
- Switched to universal `UTF-8` charset
- Implemented various improvements in the security of storing passwords in memory

## Release 2.0.1
- Implemented unique font for Tetris
- Developed resolution-independent user interface for both password manager and Tetris

## Release 2.0.2
- Implemented GitHub API request to determine the latest version
- Fixed a bug resulting the program to crash with empty .pmrc
- Added a timeout for SFTP backup to prevent the program from crashing because of e.g. firewall-blocked port 22
- Minimal graphics improvements
- Added compilation script for Java 8

## Release 2.0.3
- Added a system to prevent closing unsaved databases

## Release 2.0.4
- Added a new logo icon as svg and high-resolution png render
- Redacts information in the table view in database when window is not focussed

## Release 2.0.5
- Added custom Swing Look and Feel with the FlatLaf library
- Minor bug-fixes

## Release 2.0.6
- Addition of third-party IntelliJ UI themes
- Various UI fixes
- Moved compatibility checking file to GitHub