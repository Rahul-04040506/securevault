package com.securevault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileId;

    private String originalFileName;

    private String chunkFileName;

    private Integer chunkNumber;

    private String nodeName;

    private Long chunkSize;

    private LocalDateTime uploadedAt;
}