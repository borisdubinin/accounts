package org.example.util;

import java.util.Random;

public class IbanGenerator {

    private static final String PREFIX = "IBAN";
    private static final int RANDOM_PART_LENGTH = 28;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    public static String generateIban() {
        StringBuilder randomPart = new StringBuilder(RANDOM_PART_LENGTH);
        for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            randomPart.append(CHARACTERS.charAt(index));
        }
        return PREFIX + randomPart;
    }
}