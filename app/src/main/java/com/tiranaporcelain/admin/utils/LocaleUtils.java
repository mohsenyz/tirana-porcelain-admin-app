package com.tiranaporcelain.admin.utils;

/**
 * Created by mphj on 10/22/2017.
 */

public class LocaleUtils {
    public static String englishNumberToArabic(String numbers){
        char[] arabicChars = {'٠','١','٢','٣','۴','٥','٦','٧','٨','٩'};
        StringBuilder builder = new StringBuilder();
        for(int i =0;i<numbers.length();i++)
        {
            if(Character.isDigit(numbers.charAt(i)))
            {
                builder.append(arabicChars[(int)(numbers.charAt(i))-48]);
            }
            else
            {
                builder.append(numbers.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String e2f(String value) {
        return englishNumberToArabic(value);
    }
}
