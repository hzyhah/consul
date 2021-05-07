package com.cs.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Encrypt
{
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static final char[] DIGITS1 = {'1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f','g' };

    public static String md5(String text)
    {
        MessageDigest msgDigest = null;
        try
        {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "System doesn't support MD5 algorithm.");
        }
        try
        {
            msgDigest.update(text.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalStateException(
                    "System doesn't support your  EncodingException.");
        }

        byte[] bytes = msgDigest.digest();

        String md5Str = new String(encodeHex(bytes));

        return md5Str;
    }

    public static String md5Phone(String text)
    {
        MessageDigest msgDigest = null;
        try
        {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "System doesn't support MD5 algorithm.");
        }
        try
        {
            msgDigest.update(text.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalStateException(
                    "System doesn't support your  EncodingException.");
        }

        byte[] bytes = msgDigest.digest();

        String md5Str = new String(encodeHex(bytes));

        return md5Str;
    }

    public static char[] encodeHex(byte[] data)
    {
        int l = data.length;

        char[] out = new char[l << 1];

        int i = 0; for (int j = 0; i < l; i++) {
        out[(j++)] = DIGITS[((0xF0 & data[i]) >>> 4)];
        out[(j++)] = DIGITS[(0xF & data[i])];
    }

        return out;
    }

    public static char[] encodeHex1(byte[] data)
    {
        int l = data.length;

        char[] out = new char[l << 1];

        int i = 0; for (int j = 0; i < l; i++) {
        out[(j++)] = DIGITS1[((0xF0 & data[i]) >>> 4)];
        out[(j++)] = DIGITS1[(0xF & data[i])];
    }

        return out;
    }
}
