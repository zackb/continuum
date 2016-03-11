package com.dlvr.continuum.core.io.file;

import com.dlvr.continuum.io.file.Endpoint;
import com.dlvr.continuum.io.file.Reference;
import com.dlvr.continuum.util.IO;
import com.dlvr.continuum.util.Strings;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Local file system file descriptor
 */
public class FileSystemReference implements Reference {

    private final String base;
    private final String path;
    private final FileSystemEndpoint endpoint;

    public FileSystemReference(String base, String path) {
        this.base = base;
        this.path = path;
        this.endpoint = new FileSystemEndpoint(base);
    }

    public FileSystemReference(String fullPath) {
        Path path = Paths.get(fullPath);
        this.base = path.toAbsolutePath().getParent().toAbsolutePath().toString();
        this.path = path.toAbsolutePath().getFileName().toString();
        this.endpoint = new FileSystemEndpoint(base);
    }

    @Override
    public String virtualPath() {
        return path;
    }

    public String basePath() {
        return base;
    }

    public String fullPath() {
        return path().toAbsolutePath().toString();
    }

    @Override
    public InputStream inputStream() throws IOException {
        if (!exists()) {
            throw new FileNotFoundException("No such file: " + path().toString());
        }
        return Files.newInputStream(path());
    }

    @Override
    public OutputStream outputStream() throws IOException {
        Path path = Paths.get(this.base, this.path);
        return Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @Override
    public long write(byte[] bytes) throws IOException {
        return write(new ByteArrayInputStream(bytes));
    }

    @Override
    public long write(InputStream ins) throws IOException {
        return write(ins, 0);
    }

    @Override
    public long write(InputStream ins, long size) throws IOException {
        OutputStream outs = null;
        try {
            outs = outputStream();
            size = IO.copyStream(ins, outs);
        } finally {
            IO.close(ins);
            IO.close(outs);
        }
        return size;
    }

    @Override
    public void delete() throws IOException {
        if (!exists()) return;
        Path path = path();
        if (!Files.isDirectory(path)) {
            Files.delete(path);
        } else {
            Files.walkFileTree(path, DeleteRecursively);
        }
    }

    @Override
    public boolean exists() throws IOException {
        return Files.exists(path());
    }

    public boolean mkdir() throws IOException {
        if (!exists()) {
            Files.createDirectories(path());
            return true;
        }
        return false;
    }

    public boolean create() throws IOException {
        boolean created = false;
        if (!exists()) {
            if (!Files.exists(path().getParent()))
                Files.createDirectories(path().getParent());
            Files.createFile(path());
            created = true;
        }
        return created;
    }

    public Endpoint<FileSystemReference> endpoint() {
        return endpoint;
    }

    @Override
    public Reference[] list() {
        File[] files = allFiles();

        if (files == null || files.length == 0) return new Reference[0];

        Reference[] refs = new Reference[files.length];

        int i = 0;
        for (File file : files) {
            String newVirtualPath = virtualPath() + '/' + file.getName();
            newVirtualPath = Strings.trim(newVirtualPath, '/');
            refs[i++] = new FileSystemReference(base, newVirtualPath);
        }
        return refs;
    }

    public void walkTree(FileVisitor<Path> visitor) throws IOException {
        Files.walkFileTree(path(), visitor);
    }

    public long size() throws IOException {
        return Files.size(path());
    }

    public Path path() {
        return Paths.get(this.base, this.path);
    }

    @Override
    public long getLastModified() throws IOException {
        if (!exists()) {
            return 0L;
        }
        return Files.getLastModifiedTime(path()).toMillis();
    }

    public File file() {
        return new File(path().toString());
    }

    public File[] allFiles() {
        return file().listFiles();
    }

    public FileSystemReference child(String path) {
        return new FileSystemReference(fullPath(), path);
    }

    @Override
    public String toString() {
        return fullPath();
    }

    private static final FileVisitor<Path> DeleteRecursively = new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };
}
