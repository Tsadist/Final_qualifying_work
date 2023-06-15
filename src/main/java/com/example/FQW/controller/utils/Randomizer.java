package com.example.FQW.controller.utils;

import com.example.FQW.models.enums.CleaningType;
import com.example.FQW.models.enums.RoomType;

import java.security.SecureRandom;
import java.sql.Date;

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

    public static Short getShot() {
        return Short.parseShort(String.valueOf(RANDOM.nextInt(8, 24)));
    }

    public static Date getDate() {
        Date date = new Date(System.currentTimeMillis());
        long timeStart = date.getTime();
        long timeEnd = timeStart + 1000L * 60 * 60 * 24 * 30 * 12 * 50;
        return new Date(RANDOM.nextLong(timeStart, timeEnd));
    }

    public static CleaningType getCleaningType() {
        CleaningType[] cleaningTypes = CleaningType.values();
        int length = cleaningTypes.length;
        return cleaningTypes[RANDOM.nextInt(length)];
    }

    public static RoomType getRoomType() {
        RoomType[] roomTypes = RoomType.values();
        int length = roomTypes.length;
        return roomTypes[RANDOM.nextInt(length)];
    }

    public static Float getRandomFloat() {
        return RANDOM.nextFloat(10, Float.MAX_VALUE);
    }
}
