package com.dlvr.continuum.util;

import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.datum.impl.NDatum;
import com.dlvr.continuum.series.db.DatumID;
import com.dlvr.continuum.series.db.ID;
import com.dlvr.continuum.series.db.impl.NDatumID;
import com.dlvr.util.BSON;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

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

    public static DatumID DatumID(byte[] bytes) {
        return new NDatumID(bytes);
    }

    public static byte[] bytes(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double Double(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    public static byte[] bytes(long value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putLong(value);
        return bytes;
    }

    public static long Long(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    public static byte[] bytes(Datum datum) {
        return BSON.encode(datum);
    }

    public static Datum Datum(byte[] bytes) {
        return BSON.decodeObject(bytes, NDatum.class);
    }

    private static final Charset utf8 = Charset.forName("UTF-8");

    public static byte[] bytes(String value) {
        return value.getBytes(utf8);
    }

    public static String String(byte[] bytes) {
        return new String(bytes);
    }

    public static byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static byte[] concat(byte[] a, byte b) {
        byte[] c = new byte[a.length + 1];
        System.arraycopy(a, 0, c, 0, a.length);
        c[c.length - 1] = b;
        return c;
    }

    public static byte[] append(byte[] target, int pos, byte[] addition) {
        return append(target, pos, addition, addition.length);
    }

    public static byte[] append(byte[] target, int pos, byte[] addition, int count) {
        for (int i = 0; i < count; i++) {
            target[pos++] = addition[i];
        }
        return target;
    }

    public static byte[] append(byte[] target, int pos, byte addition) {
        target[pos] = addition;
        return target;
    }

    public static byte[][] split(byte[] target, byte delimit) {
        int size = 0;
        for (byte b : target)
            if (b == delimit) size++;

        byte[][] result = new byte[size+1][];
        int resPos = 0;

        byte[] buffer = new byte[1024];
        int curPos = 0;
        for (int i = 0; i < target.length; i++) {
            byte b = target[i];
            if (b == delimit) {
                byte[] e = new byte[curPos];
                append(e, 0, buffer, curPos);
                result[resPos++] = e;
                curPos = 0;
            } else {
                buffer[curPos++] = b;
            }
        }
        if (curPos > 0) {
            byte[] e = new byte[curPos];
            append(e, 0, buffer, curPos);
            result[resPos++] = e;
        }

        return result;
    }

    public static int lastIndexOf(byte[] target, byte b) {
        for (int i = target.length; i < 0; i++) {
            if (target[i] == b) return i;
        }
        return -1;
    }

    public static byte[] range(byte[] target, int start) {
        return range(target, start, target.length);
    }

    public static byte[] range(byte[] target, int start, int end) {
        return Arrays.copyOfRange(target, start, end);
    }

    public static final byte SEP = 0x00;
}
