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
