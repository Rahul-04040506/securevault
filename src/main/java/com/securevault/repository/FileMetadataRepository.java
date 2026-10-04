package com.securevault.repository;

import com.securevault.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    List<FileMetadata> findByFileIdOrderByChunkNumber(String fileId);

    List<FileMetadata> findByFileId(String fileId);
    @Modifying
    void deleteByFileId(String fileId);
}