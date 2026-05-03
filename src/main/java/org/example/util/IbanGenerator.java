package org.example.util;

import java.util.Random;

public class IbanGenerator {

    private static final String IBAN_PREFIX = "IBAN";
    private static final int RANDOM_IBAN_PART_LENGTH = 28;
    private static final String POSSIBLE_IBAN_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    public static String generateIban() {
        StringBuilder randomPart = new StringBuilder(RANDOM_IBAN_PART_LENGTH);
        for (int i = 0; i < RANDOM_IBAN_PART_LENGTH; i++) {
            int index = RANDOM.nextInt(POSSIBLE_IBAN_CHARACTERS.length());
            randomPart.append(POSSIBLE_IBAN_CHARACTERS.charAt(index));
        }
        return IBAN_PREFIX + randomPart;
    }
}