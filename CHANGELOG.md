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
- Added saving of previously opended database as run command
    - `.pmrc` in `%Appdata%/PasswordManager/`
    - Changing of the database to open in password input

## Release 1.3.6
- All recently opened files are stored in the `.pmrc` file
    - At startup, no file is directly opened, but a combo box will let one select a file from rc
