package io.mkrzywanski.gpn.scrapper.domain.post;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Hash {

    private static final String SHA_256 = "SHA256";

    private static final MessageDigest MESSAGE_DIGEST;
    private final byte[] hash;

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance(SHA_256);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private Hash(final byte[] hash) {
        this.hash = hash;
    }

    public static Hash compute(final String value) {
        return new Hash(MESSAGE_DIGEST.digest(value.getBytes(StandardCharsets.UTF_8)));
    }

    public static Hash fromString(final String value) {
        return new Hash(Base64.getDecoder().decode(value));
    }

    public String asString() {
        return Base64.getEncoder().encodeToString(hash);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Hash hash1 = (Hash) o;
        return Arrays.equals(hash, hash1.hash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }

    @Override
    public String toString() {
        return asString();
    }
}
