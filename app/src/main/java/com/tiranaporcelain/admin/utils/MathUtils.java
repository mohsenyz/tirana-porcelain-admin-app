package com.tiranaporcelain.admin.utils;

import java.util.Random;

/**
 * Created by mphj on 10/22/2017.
 */

public class MathUtils {
    public static int getRandomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }


    public static String randomIntString(int length) {
        Random rand = new Random();
        StringBuilder string = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            string.append("0123456789".charAt(rand.nextInt(10)));
        }
        return string.toString();
    }
}
