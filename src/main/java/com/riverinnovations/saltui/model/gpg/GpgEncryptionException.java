package com.riverinnovations.saltui.model.gpg;

import com.riverinnovations.saltui.model.ModelException;

public class GpgEncryptionException extends ModelException {
    public GpgEncryptionException(String s) {
        super(s);
    }

    public GpgEncryptionException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
