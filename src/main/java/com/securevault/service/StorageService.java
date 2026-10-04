package com.securevault.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import io.minio.RemoveObjectArgs;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    public String saveChunk(byte[] chunkData,
                            String chunkFileName,
                            int chunkNumber) throws Exception {

        String nodeName;

        switch (chunkNumber % 3) {
            case 1 -> nodeName = "node1";
            case 2 -> nodeName = "node2";
            default -> nodeName = "node3";
        }

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(nodeName + "/" + chunkFileName)
                        .stream(
                                new ByteArrayInputStream(chunkData),
                                chunkData.length,
                                -1
                        )
                        .build()
        );

        return nodeName;
    }

    public byte[] readChunk(String nodeName,
                            String chunkFileName) throws Exception {

        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(nodeName + "/" + chunkFileName)
                        .build())) {

            return inputStream.readAllBytes();
        }
    }
    public void deleteChunk(String nodeName,
                            String chunkFileName) throws Exception {

        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(nodeName + "/" + chunkFileName)
                        .build()
        );
    }
}