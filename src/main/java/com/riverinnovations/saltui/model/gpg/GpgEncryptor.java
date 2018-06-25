package com.riverinnovations.saltui.model.gpg;

import org.bouncycastle.openpgp.PGPException;
import org.c02e.jpgpj.Encryptor;
import org.c02e.jpgpj.HashingAlgorithm;
import org.c02e.jpgpj.Key;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * Class to perform GPG encryption of sensitive data such as passwords.
 * Single threaded.
 */
public class GpgEncryptor {

    /** The encryptor for hiding data */
    private final Encryptor encryptor;

    /**
     * Constructos the Encryptor.
     * @param gpgKeyPath The path to the key that we will use for the encryption.
     */
    public GpgEncryptor(Path gpgKeyPath) throws GpgEncryptionException {
        try {
            this.encryptor = new Encryptor(new Key(gpgKeyPath.toFile()));
            this.encryptor.setAsciiArmored(true);
            this.encryptor.setSigningAlgorithm(HashingAlgorithm.Unsigned);
        }
        catch (IOException | PGPException e) {
            throw new GpgEncryptionException("Error creating encryptor from key " + gpgKeyPath, e);
        }
    }

    /**
     * Utility method to encrypt the parameter.
     * If the parameter is null then returns null.
     */
    public @Nullable String encrypt(@Nullable String plain) throws GpgEncryptionException {
        if (plain == null) {
            return null;
        }
        else {
            try {
                InputStream istr = new ByteArrayInputStream(plain.getBytes(StandardCharsets.UTF_8));
                ByteArrayOutputStream ostr = new ByteArrayOutputStream();
                encryptor.encrypt(istr, ostr);
                ostr.flush();
                return new String(ostr.toByteArray(), StandardCharsets.UTF_8);
            }
            catch (IOException | PGPException e) {
                throw new GpgEncryptionException("Error encrypting data: " + e.getMessage(), e);
            }
        }
    }

}
