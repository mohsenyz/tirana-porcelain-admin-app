package com.tiranaporcelain.admin.utils;

/**
 * Created by mphj on 10/20/2017.
 */

public class ArrayUtils {

    public static boolean contains(int[] array, int value){
        for (int val : array){
            if (value == val)
                return true;
        }
        return false;
    }

}
