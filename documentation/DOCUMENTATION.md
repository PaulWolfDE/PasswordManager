# PasswordManager - Documentation
The PasswordManager is a Java software to store passwords encrypted locally. It offers many cryptographic techniques to keep the credentials safe. 

## Features & Concept

### Cryptographic Methods
Cipher Algorithms
- AES/Rijndael
- Serpent
- Twofish
- Blowfish

Cipher Modes Of Operation
- Galois/Counter Mode
- Counter Mode
- Cipher Block Chaining Mode
- Electronic Code Book Mode

Cryptographic Hash Functions
- Secure Hash Algorithm 256
- Message-Digest Algorithm 5

Padding Techniques
- PKCS#5/PKCS#7 Padding
- ISO10126 Padding

Key Derivation Functions
- PBKDF2 based on SHA-256 HMAC

### Password Generation
Passwords are being generated either with
- `javax.crypto.KeyGenerator` with `AES` algorithm
- `java.security.SecureRandom` at length 20 with alphabet `` 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#$%&'()*+,-./:;<=>?@[\]^_`{|}~" ``

### En-/Deciphering Process

#### Enciphering 
- Generation of a 16 bytes initialization vector with `java.security.SecureRandom`
- Creation of a plain text string from the database
- Derivation of a 32 bytes key by `PBKDF2` algorithm at 310,000 iterations
- Building a hash value over the plaintext string
- Encryption of the plaintext string with the chosen algorithm and mode of operation
- Concatenation of the database header and the cipher string
- Writing the cipher file

#### Deciphering
- Reading the cipher file
- Derivation of the user entered key
- Decryption of the ciphertext
- Building a hash value over the resulting plaintext
	- If the saved hash matches the generated, the password is correct
	- If not, the user will be asked to enter it again
- The plaintext database is displayed in the GUI

### Run Commands/Variables
Paths of recently opened files are saved in `%user.home%/PasswordManger/.pmrc` (Linux) or `%Appdata%/PasswordManager/.pmrc` (Windows).
If and only if a file exists and the database is compatible, it is displayed in a combo box during startup.
The run commands should never be edited by something other than the PasswordManager!

### Cipher File
```
Version signature | PasswordManager<1.3.5>
Plaintext hash value | 4b8570b813df ...
Encryption algorithm | Serpent/CTR/NoPadding
Hash algorithm | SHA-256
Initialization vector | -12,30,-46, ...
KDF salt | 49,98,-90, ...


Cipher string | e295688d2 ...
```

## Credits
- Gnu Crypto
	- Serpent
	- Twofish
	- PBKDF2
- Java Crypto 
	- AES/Rijndael
	- Blowfish
	- SHA-256
	- MD5
	- PKCS#5/#7 Padding
