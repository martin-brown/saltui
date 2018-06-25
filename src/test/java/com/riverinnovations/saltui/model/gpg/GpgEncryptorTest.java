package com.riverinnovations.saltui.model.gpg;

import org.c02e.jpgpj.Decryptor;
import org.c02e.jpgpj.Key;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GpgEncryptorTest {

    private String decrypt(Decryptor decryptor, String in) throws Exception {
        InputStream istr = new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream ostr = new ByteArrayOutputStream();
        decryptor.decrypt(istr, ostr);
        ostr.flush();
        return new String(ostr.toByteArray(), StandardCharsets.UTF_8);
    }

    @Test
    public void testEncrypt() throws Exception {
        try {
            String originalText = "foobarbazbucket";
            GpgEncryptor encryptor = new GpgEncryptor(Paths.get("src/test/resources/gpg/pubring.gpg"));
            String encValue = encryptor.encrypt(originalText);
            System.err.println(encValue);

            Key secKey = new Key((Paths.get("src/test/resources/gpg/secring.gpg").toFile()));
            secKey.setNoPassphrase(true);
            Decryptor decryptor = new Decryptor(secKey);
            decryptor.setVerificationRequired(false);
            assertEquals(originalText, this.decrypt(decryptor, encValue));
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
