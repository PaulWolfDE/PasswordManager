# TO-DOs

## Ability to import/export databases <img src="https://img.shields.io/badge/Priority-Medium-yellow.svg">
There should be an option to export data, for example to csv.
It should also be possible to import data from file formats like csv, for example to merge databases.

## Remote database on user-defined server <img src="https://img.shields.io/badge/Priority-Low-yellowgreen.svg">
Users should be able to define a remote server to store their database on. Then, the application should be able to 
connect to this server and download the database without needing to have the latest version of the database file locally 
but only one static configuration that does not change with every database update.

# DONEs

## Saving recently opened files <img src="https://img.shields.io/badge/Priority-Implemented (1.3.5)-blue.svg">
Files opened in the database should be saved in order to display them at startup.
- File in Appdata (Windows) or Userdir (Linux)
- Only existing and compatible files will be displayed for opening

## Adding Tetris <img src="https://img.shields.io/badge/Priority-Implemented (1.3.7)-blue.svg">
Adding Tetris game into password manager.

Implementation comments:
- See documentation for how to start Tetris

## Check compatibility for newer versions <img src="https://img.shields.io/badge/Priority-Implemented (1.4.3)-blue.svg">
Old version should be able to check if newer database versions are stil compatible.
- HTTP request to get data from remote server
- JSON encoding for stored data

## Saving IV and salt in hexadecimal encoding <img src="https://img.shields.io/badge/Priority-Implemented (1.4.4)-blue.svg">
The IV and salt should be stored encoded into hexadecimal in the database file header like the database body hash.

## Advanced password generation <img src="https://img.shields.io/badge/Priority-Implemented (1.4.8)-blue.svg">
The default password generator should allow the user to define a few properties for themselves.
- Variable password length can be chosen
- Charsets for the password can be selected and unselected

Implementation comments:
- Also added variable encoding to ASCII, hexadecimal and base64 to every password field and the generator

## Universal Encoding <img src="https://img.shields.io/badge/Priority-Implemented (2.0.0)-informational.svg">
All encoding of strings like passwords should be in `UTF-8` format.
- Every string-byte conversion must comply with this encoding
- `ASCII` format for hex and base64 strings

## Security improvements <img src="https://img.shields.io/badge/Priority-Implemented (2.0.0)-informational.svg">
A few improvements at security to protect passwords to be leaked by core dumps and attacks on memory.
- Don't store the master password in memory after the database was opened
  - Store PBKDF2 hash of it instead
  - Use the database salt or generate new one at each runtime
- Consistently use Java's `SecretKey` class for storing passwords

Implementation comments:
- SecretKey specification for entries with AES encoding
- Switched to byte arrays instead of string as often as possible

## Compress database before encryption <img src="https://img.shields.io/badge/Priority-Not necessary-inactive.svg">
Compress the database before it is encrypted to prevent too large files.
- Only suitable for large database

Why not necessary:
- Databases will never grow so large that compression would have a real effect on file size

## Make Tetris fully resizable <img src="https://img.shields.io/badge/Priority-Not necessary-inactive.svg">
The Tetris window should be resizable resulting in responsive block size and text margin.

Why not necessary:
- Java applications can already be run with `java -Dsun.java2d.dpiaware=false -jar PasswordManager.jar` to dynamically scale a Swing GUI


# Labels

<img src="https://img.shields.io/badge/Priority-Very High-critical.svg">
<img src="https://img.shields.io/badge/Priority-High-important.svg">
<img src="https://img.shields.io/badge/Priority-Medium-yellow.svg">
<img src="https://img.shields.io/badge/Priority-Low-yellowgreen.svg">
<img src="https://img.shields.io/badge/Priority-Very Low-Green.svg">
<img src="https://img.shields.io/badge/Priority-Implemented-informational.svg">
<img src="https://img.shields.io/badge/Priority-Not necessary-inactive.svg">
