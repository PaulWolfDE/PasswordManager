# Password Manager - Documentation

Password Manager is an open source software to encrypt confidential log-in data on the local hard disk. It uses modern cryptographic techniques to store the information as securely as possible. Encrypted information is combined into a file that is written to the hard disk. Said file can later be opened again by the password manager by entering a master password in order to view the data.

## How to use the password manager

This chapter shows how to work with the password manager as a user.

### Creating a database

To create a database, the lower button must be pressed in the main window. After that, you will get to a window where you have to specify the master password and the file location, and also get the possibility to change used algorithms. Explanations can be found in the "Cryptographic Means" chapter. After creating the database, one gets to a table view about this database. It is important to save these after each correct change, otherwise they will be lost.

### Opening a database

To open a database, it is necessary to either specify the path to it in the main window by typing it in relative or absolute, or navigate to it using the "Browse" button. The user will then be prompted to enter their main password, which will decrypt and display the database if entered correctly.

### Actions with entries

In order for an entry to be created, the corresponding button must be pressed in the window with the database unlocked. In the window that pops up, the information can now be stored and then added to the database.
To delete or edit an entry, this must be done by right-clicking on it. After each of these operations, the database should be saved again.

Copying email addresses, usernames and passwords is easily done by right-clicking on the corresponding entry. Notes can be viewed when the entry is edited.

### Changing settings

Settings over the entire database can be changed by clicking on "Settings" in the database window. After entering the master password again, you can now freely select this and the algorithms used again. Also after this action the database should be saved.

### Running Tetris

To play Tetris, the user must enter `tetris:NN` in the filter field in the database. `NN` is then to be replaced with the desired level, where the level must be ≥ 0 and ≤ 29. For numbers with one digit, a `0` must also be added at the beginning, so that the level always represents 2 characters (`8` --> `08`).

## Password manager in the background

This chapter shows how the password manager works in the background, for example when a button is pressed to encrypt.

### Cipher processes

During encryption, the entire database is first combined into a plaintext string. This string is then hashed and stored for the file. Then an initialization vector for encryption and salt for key derivation are generated pseudo-randomly. The assigned master password is now derived into a key in 310,000 iterations by `PBKDF2`. This key and the initialization vector are used to encrypt the database. The metadata for the header (see cipher file) is combined with the cipher text and stored.

During decryption, the file is read first and the individual information in the header is separated. The password entered by the user is derived with the stored salt and used with the initialization vector to decrypt the ciphertext. Afterwards, a hash is again formed over the plaintext and this is compared with the hash already stored. If this matches, the entered password is correct. The advantage of forming the hash using the plaintext instead of the password is that vulnerability to rainbow tables is eliminated and brute force attacks are slowed down even further.

### Cipher file
The cipher file is stored on the hard disk. It contains all encrypted information as well as important metadata. What a cipher file might look like now follows.
```
PasswordManager<1.4.4>  | Version signature
c93a91e148ae8a27e4d...  | Hash of the plaintext
Serpent/CBC/ISO10126    | Encryption algorithm
SHA-256                 | Hash algorithm
92b07c7d54031c0e514...  | Initialization vector
8c23a17448f473ef99b...  | KDF Salt


d4051db02d30ca57293...  | Ciphertext
```

### Start variables
The canonical paths of open databases are stored in a file named `.pmrc` in `%user.home%/PasswordManager/` (Linux) or `%Appdata%/PasswordManager/` (Windows). Only existing stored files are shown in the selection box when starting the password manager to allow quick opening.


## Cryptographic means

### Encryption algorithms

There are four encryption algorithms to choose from in Password Manager. These are compared in the following table to help you choose between them.

|Criteria       |AES/Rijndael                   |Serpent            |Twofish 			|Blowfish 			|
|---------------|:-----------------------------:|:-----------------:|:-----------------:|:-----------------:|
|Security       |Very good                      |Excellent          |Very good          |Good 				|
|Efficiency     |Very good                      |Good               |Very good          |Very good			|
|Architecture   |SP network                     |SP network         |Feistel network    |Feistel network	|
|Publication    |1998 (Standardization 2000)    |1998               |1998               |1993 				|
|Block size     |128 bit                        |128 bit            |128 bit            |64 bit 			|
|Key length     |128, 160, 192, 224, 256 bits   |128, 192, 256 bits |128, 192, 256 bits |32-448 bits    	|
|Rounds         |10 - 14                        |32                 |16                 |16     			|

### Operation modes

The encryption algorithms are assigned an operation mode that determines how the encryption of individual blocks is applied to the whole text.

|Criteria                       |GCM        |CTR        |CBC        |ECB    |
|-------------------------------|:---------:|:---------:|:---------:|:-----:|
|Security                       |Very good  |Very good  |Very good  |Good   |
|Message authentication         |Yes        |No         |No         |No     |
|initialization vector / nonce  |Yes        |Yes        |Yes        |No     |
|Parallelizability - Encryption |Yes        |Yes        |No         |Yes    |
|Parallelizability - Decryption |Yes        |Yes        |Yes        |Yes    |

### Hashalgorithms

In the password manager you can choose between the two hash algorithms 'SHA-256' and 'MD5'. The former is strongly recommended. The high efficiency of `MD5` makes it very vulnerable to brute force attacks.

|Criteria       |SHA-256    |MD5        |
|---------------|:---------:|:---------:|
|Publication    |2001       |1992       |
|Safety         |Very good  |Moderate   |
|Efficiency     |Very good  |Too good   |

### Message padding

Before encrypting messages with a block cipher, messages are brought to the required block size by message padding. In this case, no padding needs to be added for stream-like operation modes such as Counter or Galois/Counter. Paddings add n bytes to the plaintext at the end until a multiple of the block size is reached. The padding is removed after a decryption by a reference to the length of this. Padding algorithms used are `PKCS#7 padding` or `PKCS#5 padding` and `ISO10126 padding`.

#### PKCS#7/#5 padding
In `PKCS#7/#5-Padding` each byte to be padded represents the number of bytes added so that they can be easily removed.
```
[53, 65, 63, 72, 65, 74, 20, 74, 65, 78, 74, 2e] (padding to 16 bytes)
[53, 65, 63, 72, 65, 74, 20, 74, 65, 78, 74, 2e, 04, 04, 04, 04]
```

#### ISO10126 padding
In `ISO10126 padding`, the last byte represents the number of bytes padded. The remaining added bytes are filled by a random number generator.
```
[53, 65, 63, 72, 65, 74, 20, 74, 65, 78, 74, 2e] (padding to 16 bytes).
[53, 65, 63, 72, 65, 74, 20, 74, 65, 78, 74, 2e, 5a, 2c, 12, 04]
```

### Key derivation
Key derivation refers to a technique in which a cryptographic key is derived from a string of characters, usually a password entered by the user. In the password manager, the PBKDF2 algorithm is used. By deriving the key, it is very easy to bring it to the required key length of the encryption algorithm. Similarly, many brute force attacks are made more difficult because the key is derived from the basic hash function in 310,000 iterations. The length and diffusion in the derived key makes brute force attacks on it virtually impossible. An example of how PBKDF2 works is attached.

![Password-Based Key Derivation Function 2](http://paulwolf.de/crypto/passwordmanager/doucmentation/pbkdf2.png)

## References

[<img src="https://img.shields.io/badge/License-GPL 3-important.svg">](https://www.gnu.org/licenses/gpl-3.0.html)
[<img src="https://img.shields.io/badge/Library-GNU Crypto-blue.svg">](https://www.gnu.org/software/gnu-crypto/)
[<img src="https://img.shields.io/badge/Inspiration-KeePass-green.svg">](https://keepass.info/)
