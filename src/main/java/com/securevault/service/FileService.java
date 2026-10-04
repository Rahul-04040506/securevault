package com.securevault.service;

import com.securevault.entity.FileMetadata;
import com.securevault.repository.FileMetadataRepository;
import com.securevault.util.FileChunkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import com.securevault.util.FileMergeUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileMergeUtil fileMergeUtil;
    private final EncryptionService encryptionService;

    private final FileChunkUtil fileChunkUtil;
    private final StorageService storageService;
    private final FileMetadataRepository fileMetadataRepository;

    public String uploadFile(MultipartFile file) {

        try {

            String fileId = UUID.randomUUID().toString();

            List<byte[]> chunks = fileChunkUtil.splitFile(file);

            int chunkNumber = 1;

            for (byte[] chunk : chunks) {

                String chunkName = fileId + "_part_" + chunkNumber;

                byte[] encryptedChunk =
                        encryptionService.encrypt(chunk);

                String node =
                        storageService.saveChunk(
                                encryptedChunk,
                                chunkName,
                                chunkNumber
                        );

                FileMetadata metadata = FileMetadata.builder()
                        .fileId(fileId)
                        .originalFileName(file.getOriginalFilename())
                        .chunkFileName(chunkName)
                        .chunkNumber(chunkNumber)
                        .nodeName(node)
                        .chunkSize((long) chunk.length)
                        .uploadedAt(LocalDateTime.now())
                        .build();

                fileMetadataRepository.save(metadata);

                chunkNumber++;
            }

            return "Upload Successful\nFile ID: " + fileId;

        } catch (Exception e) {

            e.printStackTrace();
            return e.getMessage();

        }
    }
    public byte[] downloadFile(String fileId) {

        try {

            List<FileMetadata> metadataList =
                    fileMetadataRepository
                            .findByFileIdOrderByChunkNumber(fileId);

            List<byte[]> decryptedChunks = new ArrayList<>();

            for (FileMetadata metadata : metadataList) {

                byte[] encryptedChunk =
                        storageService.readChunk(
                                metadata.getNodeName(),
                                metadata.getChunkFileName());

                byte[] decryptedChunk =
                        encryptionService.decrypt(encryptedChunk);

                decryptedChunks.add(decryptedChunk);

            }

            return fileMergeUtil.merge(decryptedChunks);

        }

        catch (Exception e) {

            throw new RuntimeException(e);

        }

    }
    @Transactional
    public void deleteFile(String fileId) {

        try {

            List<FileMetadata> metadataList =
                    fileMetadataRepository.findByFileId(fileId);

            if (metadataList.isEmpty()) {
                throw new RuntimeException("File not found");
            }

            for (FileMetadata metadata : metadataList) {

                storageService.deleteChunk(
                        metadata.getNodeName(),
                        metadata.getChunkFileName()
                );

            }

            fileMetadataRepository.deleteByFileId(fileId);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }
}