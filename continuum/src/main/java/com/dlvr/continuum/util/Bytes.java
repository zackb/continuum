package com.dlvr.continuum.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Working with byte arrays
 */
public class Bytes {

    public static byte[] bytes(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value
        };
    }

    public static int Int(byte[] bytes) {
        return Int(bytes[0], bytes[1], bytes[2], bytes[3]);
    }

    public static int Int(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }

    public static byte[] bytes(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double Double(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    private static final Charset utf8 = Charset.forName("UTF-8");

    public static byte[] bytes(String value) {
        return value.getBytes(utf8);
    }

    public static String String(byte[] bytes) {
        return new String(bytes);
    }
}
