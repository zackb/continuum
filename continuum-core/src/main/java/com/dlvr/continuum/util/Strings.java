package com.dlvr.continuum.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utils
 * Created by zack on 3/11/16.
 */
public class Strings {

    /**
     * Removes a chracter or sting from both the right and left side of a string
     * @param s string to trim
     * @param delimiter character or string to remove
     * @return trimmed string
     */
    public static String trim(String s, char delimiter) {
        return rtrim(ltrim(s, delimiter), delimiter);
    }

    /**
     * Removes a chracter or sting the right side of a string
     * @param s string to trim
     * @param delimiter character or string to remove
     * @return trimmed string
     */
    public static String rtrim(String s, char delimiter) {
        int i = s.length() -1;
        while (i >= 0 && delimiter == s.charAt(i))
            i--;
        return s.substring(0, i + 1);
    }

    /**
     * Removes a chracter or sting the left side of a string
     * @param s string to trim
     * @param delimiter character or string to remove
     * @return trimmed string
     */
    public static String ltrim(String s, char delimiter) {
        int i = 0;
        while (i < s.length() && delimiter == s.charAt(i))
            i++;
        return s.substring(i);
    }

    public static String rpad(String s, int length, char character) {
        if (s.length() >= length) {
            return s;
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append(s);
        for (int i = s.length(); i < length; i++) {
            sb.append(character);
        }
        return sb.toString();
    }

    public static String lpad(String s, int length, char character) {
        if (s.length() >= length) {
            return s;
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = s.length(); i < length; i++) {
            sb.append(character);
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * Replaces the last occurrence of a string with a different string.
     * @param subject which contains the string to replace
     * @param find string to replace
     * @param replacement string to replace with
     * @return string with the replacement
     */
    public static String replaceLast(String subject, String find, String replacement) {
        int ind = subject.lastIndexOf(find);
        if (ind < 0) {
            return subject;
        }
        return new StringBuilder(subject).replace(ind, ind + 1, replacement).toString();
    }

    /**
     * SHA1 hash
     * @param subject to sha1
     * @return subject hashed as sha1
     */
    public static String sha1(String subject) {
        return hash(subject, "SHA-1");
    }

    /**
     * SHA256 hash
     * @param subject to sha256
     * @return subject hashed as sha256
     */
    public static String sha256(String subject) {
        return hash(subject, "SHA-256");
    }

    /**
     * MD5 hash
     * @param subject to md5
     * @return subject hashed as md5
     */
    public static String md5(String subject) {
        return hash(subject, "MD5");
    }

    private static String hash(String subject, String algo) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algo);
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            byte[] bytes = md.digest(subject.getBytes("UTF-8"));
            return bytesToHex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Translate an array of bytes to their hexadecimal form.
     * @param b bytes to transform
     * @return string representation of the hexadecimal form
     */
    public static String bytesToHex(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * Utility to perform a regular expression on a string and return the first match.
     * @param subject to regex
     * @param regex pattern
     * @return the match of regex on the subject
     */
    public static String regex(String subject, String regex) {
        return regexp(subject, Pattern.compile(regex));
    }

    /**
     * Utility to perform a regular expression on a string and return the first match.
     * @param subject to regex
     * @param pattern pattern
     * @return the match of regex on the subject
     */
    public static String regexp(String subject, Pattern pattern) {
        if (empty(subject)) {
            return subject;
        }
        String result = null;
        Matcher m = pattern.matcher(subject);
        if (m.find()) {
            if (m.groupCount() > 0) {
                result = m.group(1);
            } else {
                result = m.group(0);
            }
        }
        return result;
    }

    public static boolean empty(String s) {
        return s == null || s.equals("");
    }

    /**
     * Sprintf to a string.
     *
     * @param format a format string
     * @param values values referenced by the format specifiers in the format string.
     * @return the resulting formatted string
     */
    public static String sprintf(String format, Object[] values) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        out.printf(format, values);
        return outputStream.toString();
    }

    public static String[] range(String[] source, int start, int end) {
        String[] result = new String[end - start];
        for (int i = 0, pos = start; pos < end; i++, pos++) {
            result[i] = source[pos];
        }
        return result;
    }
}
