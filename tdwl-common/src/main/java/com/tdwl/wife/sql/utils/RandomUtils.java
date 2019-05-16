package com.tdwl.wife.sql.utils;


import java.util.Random;

public class RandomUtils {

    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    private static final String nums = "1234567890";



    public static String getRandomNums(int length){
        StringBuffer sb = new StringBuffer();
        Random rd = new Random();
        for(int i = 0; i < length; i++){
            sb.append(nums.charAt(rd.nextInt(nums.length())));
        }
        return sb.toString();
    }


    public static String createNoncestr(int length) {
        StringBuilder sb = new StringBuilder();
        Random rd = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rd.nextInt(chars.length())));
        }
        return sb.toString();
    }

}
