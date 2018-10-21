package com.riverinnovations.saltui.model.gpg;

import org.c02e.jpgpj.Decryptor;
import org.c02e.jpgpj.Key;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import javax.validation.constraints.Null;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GpgEncryptorTest {

    private @Nullable String decrypt(Decryptor decryptor, @Nullable String in) throws Exception {
        if (in != null) {
            InputStream istr = new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream ostr = new ByteArrayOutputStream();
            decryptor.decrypt(istr, ostr);
            ostr.flush();
            return new String(ostr.toByteArray(), StandardCharsets.UTF_8);
        }
        else {
            return null;
        }
    }

    @Test
    public void testEncrypt() throws Exception {
        try {
            String originalText = "foobarbazbucket";
            GpgEncryptor encryptor = new GpgEncryptor(Paths.get("src/test/resources/gpg/pubring.gpg"));
            @Nullable String encValue = encryptor.encrypt(originalText);
            System.err.println(encValue);

            Key secKey = new Key((Paths.get("src/test/resources/gpg/secring.gpg").toFile()));
            secKey.setNoPassphrase(true);
            Decryptor decryptor = new Decryptor(secKey);
            decryptor.setVerificationRequired(false);
            @Nullable String decryptedText = this.decrypt(decryptor, encValue);
            if (decryptedText != null) {
                assertEquals(originalText, decryptedText);
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
            if (e.getCause() != null) {
                System.err.println(e.getCause());
            }
            fail("Exception thrown in GPG encryption/decryption");
        }
    }
}
