package com.example.kyrsovay.controller.utils;

import java.security.SecureRandom;

public class Randomizer {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String LOWER_CASE = "qwertyuiopasdfghjklzxcvbnm";
    private static final String UPPER_CASE = LOWER_CASE.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String COMMON_SCROLL = LOWER_CASE + UPPER_CASE + NUMBER;
    private static final int DEFAULT_LENGTH_STRING = 10;

    public static String getRandomString(int countCharacters) {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < countCharacters; i++) {
            int randomIndex = RANDOM.nextInt(COMMON_SCROLL.length());
            string.append(COMMON_SCROLL.charAt(randomIndex));
        }
        return string.toString();
    }

    public static String getRandomString() {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < DEFAULT_LENGTH_STRING; i++) {
            int randomIndex = RANDOM.nextInt(COMMON_SCROLL.length());
            string.append(COMMON_SCROLL.charAt(randomIndex));
        }
        return string.toString();
    }
}
