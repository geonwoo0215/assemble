package com.geonwoo.assemble.domain.invitation.model.algorithm;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sha256Algorithm {

    private static final Base64.Encoder urlEncoder = Base64.getUrlEncoder();
    private static final int LENGTH = 8;

    private static byte[] encrypt(String value) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String execute(String value) {
        Random random = new Random();
        byte[] encrypt = encrypt(value);
        String encode = urlEncoder.withoutPadding().encodeToString(encrypt);
        int start = random.nextInt(encrypt.length - LENGTH);
        return encode.substring(start, start + LENGTH);
    }
}
