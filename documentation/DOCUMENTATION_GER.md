# Passwortmanager - Dokumentation

Der Passwortmanager ist eine Open-Source-Software, um vertrauliche Log-in-Daten auf der lokalen Festplatte zu verschlüsseln. Es werden moderne kryptographische Techniken genutzt, um die Informationen so sicher wie möglich zu speichern. Verschlüsselte Informationen werden in einer Datei zusammengefasst, die auf die Festplatte geschrieben wird. Besagte Datei kann später wieder vom Passwortmanager unter Eingabe eines Master-Passworts geöffnet werden, um die Daten einsehen zu können.

## Umgang mit dem Passwortmanager

Dieses Kapitel zeigt, wie man mit dem Passwortmanager als Nutzer arbeitet.

### Erstellen einer Datenbank

Für das Erstellen einer Datenbank muss im Hauptfenster der untere Knopf gedrückt werden. Danach gelangt man in ein Fenster, in dem man das Master-Passwort und den Dateispeicherort angeben muss und ebenso die Möglichkeit erhält, verwendete Algorithmen zu ändern. Erklärungen dazu sind im Kapitel "Kryptographische Mittel" aufzufinden. Nach dem Erstellen der Datenbank gelangt man in eine Tabellenansicht über diese Datenbank. Es ist wichtig, diese nach jeder richtigen Änderung zu speichern, da diese sonst verloren gehen.

### Öffnen einer Datenbank

Um eine Datenbank zu öffnen, muss im Hauptfenster entweder der Pfad zu dieser durch Eintippen relativ oder absolut angegeben werden oder durch den Knopf "Browse" zu ihr navigiert werden. Anschließend wird der Nutzer aufgefordert, sein Hauptpasswort einzugeben, was die Datenbank bei richtiger Eingabe entschlüsselt und anzeigt.

### Aktionen mit Einträgen

Damit ein Beitrag erstellt werden kann, muss im Fenster mit entsperrter Datenbank auf den entsprechenden Knopf gedrückt werden. In dem aufploppenden Fenster können nun die Informationen hinterlegt werden und anschließend zur Datenbank hinzugefügt werden.
Um einen Eintrag zu löschen oder zu editieren, muss dies mit einem Rechtsklick auf diesen Eintrag durchgeführt werden. Nach jeder dieser Operationen sollte die Datenbank erneut gespeichert werden.

Das Kopieren von E-Mail-Adressen, Nutzernamen und Passwörtern ist einfach über einen Rechtsklick auf den entsprechenden Eintrag durchzuführen. Notizen können eingesehen werden, wenn der Eintrag editiert wird.

### Ändern von Einstellungen

Die Einstellungen über die gesamte Datenbank können durch einen Klick auf "Settings" im Datenbank-Fenster geändert werden. Nach einer erneuten Eingabe des Master-Passworts kann man jetzt dieses und die verwendeten Algorithmen erneut frei auswählen. Auch nach dieser Aktion sollte die Datenbank gespeichert werden.

### Einrichten eines automatischen SFTP-Backups

Der Passwortmanager stellt ein automatisches Backup zur Verfügung, das bei jeder Speicherung die neuste Version der Datenbank auf einem entfernten Server sicher per SFTP speichert. Zunächst muss ein Linux-Server eingerichtet werden, der auf Port 22 (SSH) lauscht, sodass eine SSH- bzw. SFTP-Verbindung hergestellt werden kann. Anschließend muss der im Passwortmanager reservierte Eintrag `sftp-automated-backup` bearbeitet werden. Im Feld `Username` ist der Nutzername auf dem entfernten Server einzutragen. Das Feld `Hostname` benötigt einen Namen für den Server wie eine IP-Adresse oder eine Domain. Das einzutragende Passwort stellt zuletzt das Passwort für die SFTP-Verbindung dar. Hat alles geklappt, wird bei jeder lokalen Speicherung entfernt eine Datei in `~/database_backup/` abgelegt, die durch ihren Namen eindeutig auf das Backup zurückführbar ist. Ein beispielhafter Name wäre `MyDatabase-2022-07-19@19:52.pmdtb`.

ACHTUNG: Der Hostname kann eine lokale IP-Adresse (z. B. `192.168.178.80`) oder öffentliche IP-Adresse (z. B. `52.149.246.39`) bzw. eine Domain (z. B. `example.com`) dazu beinhalten. Wichtig bei einem Server im lokalen Netz ist, dass der Port `22` beim Nutzen einer öffentlichen Möglichkeit außerhalb des LANs ansprechbar ist.

### Tetris spielen

Um Tetris zu spielen, muss der Nutzer `tetris:NN` in das Filterfeld in der Datenbank eingeben. `NN` ist dann mit dem gewünschten Level zu ersetzen, wobei gilt, dass das Level ≥ 0 und ≤ 29 sein muss. Bei Zahlen mit einer Stelle, muss ebenso eine `0` am Anfang hinzugefügt werden, sodass das Level immer 2 Zeichen darstellt (`8` --> `08`).  

## Passwortmanager im Hintergrund

Dieses Kapitel zeigt, wie der Passwortmanager im Hintergrund arbeitet, wenn zum Beispiel ein Knopf zum Verschlüsseln gedrückt wird.

### Chiffreprozesse
Bei der Verschlüsselung wird zunächst die gesamte Datenbank zu einem Klartext-String zusammengefasst. Dieser String wird dann gehasht und für die Datei abgespeichert. Anschließend wird ein Initialisierungsvektor für die Verschlüsselung und Salt für die Schlüsselableitung pseudozufällig generiert. Das vergebene Master-Passwort wird nun in 310,000 Iterationen durch `PBKDF2` zu einem Schlüssel abgeleitet. Mit diesem Schlüssel und dem Initialisierungsvektor wird die Datenbank verschlüsselt. Die Metadaten für den Header (siehe Chiffredatei) werden mit dem Chiffretext verbunden und abgespeichert.

Bei der Entschlüsselung wird zuerst die Datei eingelesen und die einzelnen Informationen im Header werden getrennt. Das vom Nutzer eingegebene Passwort wird mit dem hinterlegten Salt abgeleitet und mit dem Initialisierungsvektor genutzt, um den Chiffretext zu entschlüsseln. Danach wird wieder ein Hash über den Klartext gebildet und dieser mit dem bereits gespeicherten Hash verglichen. Wenn dieser übereinstimmt, ist das eingegebene Passwort korrekt. Der Hash über den Klartext statt über das Passwort zu bilden hat den Vorteil, dass Anfälligkeit gegen Rainbow-Tables entfällt und Brute-Force-Attacken nochmal verlangsamt werden.

### Chiffredatei
Die Chiffredatei wird auf der Festplatte gespeichert. Sie enthält alle verschlüsselten Informationen sowie wichtige Metadaten. Wie eine Chiffredatei aussehen könnte, folgt nun.
```
PasswordManager<1.4.4>  | Versionssignatur
c93a91e148ae8a27e4d...  | Hash des Klartexts
Serpent/CBC/ISO10126    | Verschlüsselungsalgorithmus
SHA-256                 | Hashalgorithmus
92b07c7d54031c0e514...  | Initialisierungsvektor
8c23a17448f473ef99b...  | KDF-Salt


d4051db02d30ca57293...  | Chiffretext
```

### Startvariablen
Die kanonischen Pfade von geöffneten Datenbanken werden in einer Datei namens `.pmrc` in `%user.home%/PasswordManager/` (Linux) oder `%Appdata%/PasswordManager/` (Windows). Nur existierende hinterlegte Dateien werden beim Start des Passwortmanagers in der Auswahlbox gezeigt, um ein schnelles Öffnen zu ermöglichen.

## Kryptographische Mittel

### Verschlüsselungsalgorithmen

Im Passwortmanager stehen vier Verschlüsselungsalgorithmen zur Auswahl. Diese werden in der folgenden Tabelle verglichen, um die Wahl zwischen ihnen zu erleichtern.

|Kriterium			|AES/Rijndael					|Serpent			|Twofish			|Blowfish			|
|-------------------|:-----------------------------:|:-----------------:|:-----------------:|:-----------------:|
|Sicherheit			|Sehr gut 						|Exzellent 			|Sehr gut 			|Gut 				|
|Effizienz			|Sehr gut 						|Gut 				|Sehr gut 			|Sehr gut 			|
|Architektur		|SP-Netzwerk					|SP-Netzwerk		|Feistel-Netzwerk	|Feistel-Netzwerk	|
|Veröffentlichung	|1998 (Standardisierung 2000)	|1998 				|1998 				|1993 				|
|Blockgröße			|128 Bit 						|128 Bit 			|128 Bit 			|64 Bit 			|
|Schlüssellänge		|128, 160, 192, 224, 256 Bit	|128, 192, 256 Bit 	|128, 192, 256 Bit 	|32-448 Bit 		|
|Runden				|10 - 14						|32 				|16 				|16 				|

### Operationsmodi

Den Verschlüsselungsalgorithmen wird ein Operationsmodus zugewiesen, der bestimmt, in welcher Weise die Verschlüsselung einzelner Blöcke auf den gesamten Text angewandt wird.

|Kriterium                              |GCM        |CTR        |CBC        |ECB    |
|---------------------------------------|:---------:|:---------:|:---------:|:-----:|
|Sicherheit                             |Sehr gut   |Sehr gut   |Sehr gut   |Gut    |
|Nachrichtenauthentifizierung           |Ja         |Nein       |Nein       |Nein   |
|Initialisierungsvektor / Nonce         |Ja         |Ja         |Ja         |Nein   |
|Parallelisierbarkeit - Verschlüsselung |Ja         |Ja         |Nein       |Ja     |
|Parallelisierbarkeit - Entschlüsselung |Ja         |Ja         |Ja         |Ja     |

### Hashalgorithmen

Im Passwortmanager kann zwischen den zwei Hashalgorithmen `SHA-256` und `MD5` entschieden wird. Ersteres wird dringend empfohlen. Die hohe Effizienz von `MD5` macht es sehr anfällig für Brute-Force-Attacken.

|Kriterium			|SHA-256	|MD5		|
|-------------------|:---------:|:---------:|
|Veröffentlichung	|2001		|1992		|
|Sicherheit 		|Sehr gut 	|Mäßig		|
|Effizienz			|Sehr gut 	|Zu hoch	|

### Nachrichtenpadding

Nachrichten werden vor der Verschlüsselung eines Blockchiffres durch ein Nachrichtenpadding auf die benötigte Blockgröße gebracht. Dabei muss bei Stream-ähnlichen Operationsmodi wie Counter oder Galois/Counter kein Padding hinzugefügt werden. Paddings fügen dem Klartext n Bytes am Ende hinzu, bis ein Vielfaches der Blockgröße erreicht ist. Das Padding wird nach einer Entschlüsselung durch eine Referenz auf die Länge dieses wieder entfernt. Verwendete Paddingalgorithmen sind `PKCS#7-Padding` bzw. `PKCS#5-Padding` sowie `ISO10126-Padding`.

#### PKCS#7/#5-Padding
Beim `PKCS#7/#5-Padding` stellt jedes aufzufüllendes Byte die Anzahl der hinzugefügten Bytes dar, damit diese leicht wieder entfernt werden können.
```
[53, 65, 63, 72, 65, 74, 20, 74, 65, 78, 74, 2e] (Padding auf 16 Bytes)
[53, 65, 63, 72, 65, 74, 20, 74, 65, 78, 74, 2e, 04, 04, 04, 04]
```

#### ISO10126-Padding
Beim `ISO10126-Padding` stellt das letzte Byte die Anzahl der gepaddeten Bytes dar. Die restlichen hinzugefügten Bytes werden durch einen Zufallszahlengenerator gefüllt.
```
[53, 65, 63, 72, 65, 74, 20, 74, 65, 78, 74, 2e] (Padding auf 16 Bytes)
[53, 65, 63, 72, 65, 74, 20, 74, 65, 78, 74, 2e, 5a, 2c, 12, 04]
```

### Schlüsselableitung
Die Schlüsselableitung bezeichnet eine Technik, bei der ein kryptographischer Schlüssel von einer Zeichenkette abgeleitet wird, die meist ein vom Nutzer eingegebenes Passwort darstellt. Im Passwortmanager wird der Algorithmus PBKDF2 verwendet. Durch die Ableitung des Schlüssels kann dieser sehr einfach auf die geforderte Schlüssellänge des Verschlüsselungsalgorithmus gebracht werden. Ebenso werden viele Brute-Force-Angriffe erschwert, da der Schlüssel in 310.000 Iterationen von der grundlegenden Hashfunktion abgeleitet wird. Die Länge und Diffusion im abgeleiteten Schlüssel macht Brute-Force-Attacken auf diesen praktisch unmöglich. Ein Beispiel zur Funktionsweise von PBKDF2 ist angefügt.

![Password-Based Key Derivation Function 2](http://paulwolf.de/crypto/passwordmanager/doucmentation/pbkdf2.png)

## Referenzen

[<img src="https://img.shields.io/badge/Lizenz-GPLv3-important.svg">](https://www.gnu.org/licenses/gpl-3.0.html)
[<img src="https://img.shields.io/badge/Bibliothek-GNU Crypto-blue.svg">](https://www.gnu.org/software/gnu-crypto/)
[<img src="https://img.shields.io/badge/Library-JSON Java-blue.svg">](https://github.com/stleary/JSON-java)
[<img src="https://img.shields.io/badge/Inspiration-KeePass-green.svg">](https://keepass.info/)