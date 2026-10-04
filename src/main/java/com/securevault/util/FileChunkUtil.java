package com.securevault.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileChunkUtil {

    // 1 MB Chunk Size
    private static final int CHUNK_SIZE = 1024 * 1024;

    public List<byte[]> splitFile(MultipartFile file) throws IOException {

        List<byte[]> chunks = new ArrayList<>();

        InputStream inputStream = file.getInputStream();

        byte[] buffer = new byte[CHUNK_SIZE];

        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {

            byte[] chunk = new byte[bytesRead];

            System.arraycopy(buffer, 0, chunk, 0, bytesRead);

            chunks.add(chunk);

        }

        inputStream.close();

        return chunks;
    }

}