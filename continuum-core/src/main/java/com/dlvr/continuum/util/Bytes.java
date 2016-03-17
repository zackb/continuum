package com.dlvr.continuum.util;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.core.atom.KAtom;
import com.dlvr.continuum.core.atom.NAtom;
import com.dlvr.continuum.core.atom.SAtom;
import com.dlvr.continuum.core.atom.KAtomID;
import com.dlvr.continuum.atom.AtomID;
import com.dlvr.continuum.core.atom.SAtomID;
import com.dlvr.continuum.except.ZiggyStardustError;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Working with byte arrays
 */
public class Bytes {

    public static final byte B = 0x0;

    public static byte[] bytes(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value
        };
    }


    /**
     * Quark!
     * TODO: String values
     * Decode only the values of the atom. If the full body is not needed to be decoded.
     * @param bytes encoded with {#values(Atom)}
     * @return 64 bit min, max, count, sum, value from bytes
     */
    public static Values Values(byte[] bytes) {
        int pos = bytes.length;
        byte[] value = range(bytes, pos - 8, pos);
        pos -= 9; // null byte divider
        byte[] sum = range(bytes, pos - 8, pos);
        pos -= 9;
        byte[] count = range(bytes, pos - 8, pos);
        pos -= 9;
        byte[] min = range(bytes, pos - 8, pos);
        pos -= 9;
        byte[] max = range(bytes, pos - 8, pos);
        return Continuum.values()
                .min(Double(min))
                .max(Double(max))
                .count(Double(count))
                .sum(Double(sum))
                .value(Double(value))
                .build();
    }

    public static int Int(byte[] bytes) {
        return Int(bytes[0], bytes[1], bytes[2], bytes[3]);
    }

    public static int Int(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }

    public static AtomID SAtomID(byte[] bytes) {
        return new SAtomID(bytes);
    }

    public static AtomID KAtomID(byte[] bytes) {
        return new KAtomID(bytes);
    }

    public static byte[] bytes(AtomID id) {
        return id.bytes();
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

    /**
     * // TODO: Store values instead of values/avg?
     *      Quark! byte[bson(fields)] + 0x0 + byte[min] + 0x0 + byte[max] + 0x0 + byte[sum] + byte[count] + 0x0
     * // TOOD: String values support
     *
     * Encode atom as bytes[bson] + 0x0 + bytes[values]
     * @param atom to encode
     * @return values for slab storage
     */
    public static byte[] bytes(Atom atom) {
        return BSON.encode(atom);

        /*
        Values values = atom.values();
        natom.values = null;
        byte[] bson = BSON.encode(natom);
        natom.values = values;

        byte[] min = bytes(values.min());
        byte[] max = bytes(values.max());
        byte[] count = bytes(values.count());
        byte[] sum = bytes(values.sum());
        byte[] value = bytes(values.value());

        int valuesLen = min.length + 1 + max.length + 1 + count.length + 1 + sum.length + 1 + value.length;

        byte[] slice = new byte[bson.length + 1 + valuesLen];

        int pos = 0;

        append(slice, 0, bson);
        pos += bson.length;
        append(slice, pos, B);
        pos += 1;

        append(slice, pos, min);
        pos += min.length;
        append(slice, pos, B);
        pos += 1;

        append(slice, pos, max);
        pos += max.length;
        append(slice, pos, B);
        pos += 1;

        append(slice, pos, count);
        pos += count.length;
        append(slice, pos, B);
        pos += 1;

        append(slice, pos, sum);
        pos += sum.length;
        append(slice, pos, B);
        pos += 1;

        append(slice, pos, value);

        return slice;
*/

    }

    public static Atom SAtom(byte[] bytes) {
        return Atom(bytes, Continuum.Dimension.SPACE);
    }

    public static Atom KAtom(byte[] bytes) {
        return Atom(bytes, Continuum.Dimension.TIME);
    }

    public static Atom Atom(byte[] bytes, Continuum.Dimension dimension) {
        Class<? extends NAtom> clazz = null;
        if (dimension == Continuum.Dimension.SPACE)
            clazz = SAtom.class;
        else if (dimension == Continuum.Dimension.TIME)
            clazz = KAtom.class;
        else throw new ZiggyStardustError();
        NAtom atom = BSON.decodeObject(bytes, clazz);
        return atom;
    }

    private static final Charset utf8 = Charset.forName("UTF-8");

    public static byte[] bytes(String value) {
        return value.getBytes(utf8);
    }

    public static String String(byte[] bytes) {
        return new String(bytes);
    }

    public static byte[] copy(byte[] a) {
        byte[] c = new byte[a.length];
        System.arraycopy(a, 0, c, 0, a.length);
        return c;
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
