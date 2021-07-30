package com.bobo_chicke.apparelinventorymanager.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static  String  getResult(String inputStr) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(inputStr.getBytes("UTF-8"));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }
}