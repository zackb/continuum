package continuum.io.file;

import continuum.file.Reference;

import java.io.IOException;
import java.io.InputStream;

/**
 * Abstraction of a file based endpoint. HTTP server, Amazon services, local file system, etc
 */
public interface Endpoint<F extends Reference> {
    Reference get(String virtualPath);
    void store(F ref, byte[] data) throws IOException;
    void store(F ref, InputStream data) throws IOException;
    Class<F> getReferenceType();
}
