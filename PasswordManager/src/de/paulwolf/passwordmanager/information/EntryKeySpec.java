package de.paulwolf.passwordmanager.information;

import javax.crypto.spec.SecretKeySpec;

public class EntryKeySpec extends SecretKeySpec {

    public EntryKeySpec(byte[] key) {
        super(key, "AES");
    }
}
