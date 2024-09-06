package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Entity
@Table(name = "files",uniqueConstraints = {@UniqueConstraint(columnNames = {"filename"},name = "unique_cont_filename")})
@Getter
@Setter
@ToString
public class FilesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_seq")
    @SequenceGenerator(name = "files_seq",sequenceName = "files_sequence",allocationSize = 1)
    @Column(name = "fid")
    private Long fid;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "filetype")
    private String fileType;

    @Column(name = "filecontent",columnDefinition = "TEXT")
    private String fileContent;

    @Column(name="filesize")
    private Double fileSize;

    @Column(name = "file_creator")
    private String fileCreator;

    @Column(name = "file_created_date")
    private LocalDateTime fileCreatedDate;

    @Column(name = "file_modified_date")
    private  LocalDateTime fileModifiedDate;
}
