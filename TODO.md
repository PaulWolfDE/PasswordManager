# TODOs

~~-Every file is saved in another app data file during first open. One can choose recently opened files later in a combo box at startup of the password manager. Separate file to `.pmrc`. On every startup, files that don't exist anymore are being removed from the file and not loaded.
    - File name _could_ be something like `.pmro` (Password manager recently opened)~~

(Including option to perform a one-time pad on database)

Pack database before encryption (only suitable for large databases)

Advanced password generation

Adding API request to find out whether older versions still supports a newer database file. If signature is not supported by the old version, a second check should confirm if this version still can open this database file. 

Saving IV and Salt in hexadecimal dorm like the database hash value. 

~~Add possibility to play Tetris~~
