package com.tiranaporcelain.admin.utils;

/**
 * Created by mphj on 1/14/18.
 */

public class NumUtils {

    public static String intToString(int number, int minLength) {
        String rawNumber = String.valueOf(number);
        int digitsCount = rawNumber.length();
        if (digitsCount >= minLength)
            return rawNumber;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < minLength - digitsCount; i++) {
            stringBuilder.append(0);
        }
        return stringBuilder.append(rawNumber).toString();
    }

}
