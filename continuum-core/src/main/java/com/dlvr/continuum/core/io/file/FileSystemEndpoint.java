package com.dlvr.continuum.core.io.file;

import com.dlvr.continuum.io.file.Endpoint;
import com.dlvr.continuum.util.IO;

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

    /**
     * Get a handle to the filesystem givena base path
     * @param base path
     */
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
        OutputStream out = ref.outputStream();
        IO.copyStream(data, out);
        IO.close(out);
        IO.close(data);
    }

    @Override
    public Class<FileSystemReference> getReferenceType() {
        return FileSystemReference.class;
    }

    /**
     * Create a temporary file in {#TMP_FILE_PREFIX}
     * @return
     */
    public FileSystemReference createTemporaryFile() {
        return new FileSystemReference(base, TMP_FILE_PREFIX + ((int)Math.random() * 2000) + '.' + System.currentTimeMillis());
    }

    public String getBasePath() {
        return base;
    }
}
