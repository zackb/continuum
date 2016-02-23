package com.dlvr.continuum.io.file;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Because the name "File" is already taken
 */
public interface Reference extends Serializable {
    String virtualPath();
    InputStream inputStream() throws IOException;
    OutputStream outputStream() throws IOException;
    long write(byte[] bytes) throws IOException;
    long write(InputStream ins) throws IOException;
    long write(InputStream ins, long size) throws IOException;
    void delete() throws IOException;
    long getLastModified() throws IOException;
    boolean exists() throws IOException;
    Reference[] list() throws IOException;
}
