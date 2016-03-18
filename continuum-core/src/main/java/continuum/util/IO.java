package continuum.util;

import java.io.*;

/**
 * IO utils
 * Created by zack on 3/11/16.
 */
public class IO {

    public static long copyStream(final InputStream inputStream,
                                  final OutputStream outputStream) throws IOException {
        return copyStream(inputStream, outputStream, 16384 * 2);
    }

    public static long copyStream(final InputStream inputStream,
                                  final OutputStream outputStream,
                                  final int bufferSize) throws IOException {
        return copyStream(inputStream, outputStream, bufferSize, -1);
    }

    public static long copyStream(final InputStream inputStream,
                                  final OutputStream outputStream,
                                  final int bufferSize,
                                  final long maxBytes) throws IOException {

        byte[] buffer = new byte[bufferSize];
        int bytesRead = inputStream.read(buffer);
        long totalBytes = bytesRead == -1 ? 0 : bytesRead;
        while (bytesRead != -1) {
            outputStream.write(buffer, 0, bytesRead);
            if (maxBytes > 0 && totalBytes >= maxBytes) {
                break;
            }
            bytesRead = inputStream.read(buffer);
            if (bytesRead != -1)
                totalBytes += bytesRead;
        }
        return totalBytes;
    }

    public static String readString(InputStream ins) throws IOException {

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            copyStream(ins, bout);
            close(bout);
        } finally {
            close(bout);
            close(ins);
        }

        return Bytes.String(bout.toByteArray());
    }

    public static void close(OutputStream out) {
        try {
            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close(InputStream ins) {
        try {
            if (ins != null) {
                ins.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
