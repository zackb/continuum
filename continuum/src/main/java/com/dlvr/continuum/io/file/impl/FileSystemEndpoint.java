package com.dlvr.continuum.io.file.impl;

import com.dlvr.continuum.io.file.Endpoint;
import com.dlvr.util.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Local file system implementation of Endpoint interface
 */
class FileSystemEndpoint implements Endpoint<FileSystemReference> {

    public static final String TMP_FILE_PREFIX = "continuum.fsendpoint.work.";

    private final String base;

    public FileSystemEndpoint(String base) {
        this.base = base;
    }

    @Override
    public FileSystemReference get(String virtualPath) {
        return new FileSystemReference(base, virtualPath);
    }

    @Override
    public void store(FileSystemReference ref, byte[] data) throws IOException {
        InputStream bin = new ByteArrayInputStream(data);
        store(ref, bin);
    }

    @Override
    public void store(FileSystemReference ref, InputStream data) throws IOException {
        ref.create();
        OutputStream out = ref.getOutputStream();
        IOUtil.copyStream(data, out);
        IOUtil.close(out);
        IOUtil.close(data);
    }

    @Override
    public Class<FileSystemReference> getReferenceType() {
        return FileSystemReference.class;
    }

    public FileSystemReference createTemporaryFile() {
        return new FileSystemReference(base, TMP_FILE_PREFIX + ((int)Math.random() * 2000) + '.' + System.currentTimeMillis());
    }

    public String getBasePath() {
        return base;
    }
}
