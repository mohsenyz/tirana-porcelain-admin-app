package com.tiranaporcelain.admin.utils;

/**
 * Created by mphj on 10/22/2017.
 */

public class DoubleValidator {

    public static boolean isValid(String intString){
        try {
            Integer.parseInt(intString);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
