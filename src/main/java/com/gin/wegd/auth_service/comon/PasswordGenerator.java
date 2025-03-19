package com.gin.wegd.auth_service.comon;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordGenerator {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = "!@#$%^&*()-_=+<>?";
    private static final String ALL_CHARS = UPPER + LOWER + DIGITS + SPECIALS;
    private static final SecureRandom random = new SecureRandom();

    public String generatePassword(int length) {
        if (length < 6) {
            throw new IllegalArgumentException("Password length should be at least 6 characters");
        }

        StringBuilder password = new StringBuilder();
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIALS.charAt(random.nextInt(SPECIALS.length())));

        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        return shuffleString(password.toString());
    }

    public String shuffleString(String input) {
        char[] array = input.toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
        return new String(array);
    }
}
