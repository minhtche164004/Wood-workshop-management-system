package com.example.demo.Service.Impl;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;
@Data
public class CustomMultipartFile implements MultipartFile {

    private final byte[] imgContent;
    private final String originalFileName;

    public CustomMultipartFile(byte[] imgContent, String originalFileName) {
        this.imgContent = imgContent;
        this.originalFileName = originalFileName;
    }

    @Override
    public String getName() {
        // Returns the name of the parameter in the multipart form.
        return originalFileName;
    }

    @Override
    public String getOriginalFilename() {
        // Returns the original filename in the client's filesystem.
        return originalFileName;
    }

    @Override
    public String getContentType() {
        // Returns the content type passed by the browser or null if not defined.
        return null;
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("Transfer to file is not supported.");
    }
}
