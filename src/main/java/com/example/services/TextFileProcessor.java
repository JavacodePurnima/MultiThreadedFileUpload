package com.example.services;

import com.example.entity.FilesEntity;
import com.example.repository.FilesRepository;
import org.hibernate.HibernateException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class TextFileProcessor implements FileProcessor{
    private final FilesRepository filesRepo;

    // Spring automatically calls this constructor when creating the bean
    @Autowired
    public TextFileProcessor(FilesRepository filesRepo) {
        this.filesRepo = filesRepo;
    }
    @Override
    public void processFile(Path filePath) {


        File file=filePath.toFile();
        try(FileReader fr=new FileReader(file);
                    BufferedReader br=new BufferedReader(fr)){
            StringBuilder sb=new StringBuilder();
            String line=null;
            while((line=br.readLine())!=null){
                sb.append(line);
            }
            long sizeInBytes=Files.size(filePath);
            BasicFileAttributes fileAttribute= Files.readAttributes(filePath,BasicFileAttributes.class);//fetch the basic metadata of file.

            Optional<FilesEntity> filesEntityOpt=filesRepo.findByFileName(filePath.getFileName().toString());
            FilesEntity filesEntity=null;
            if(filesEntityOpt.isPresent()){
                filesEntity=new FilesEntity();
                filesEntity.setFid(filesEntity.getFid());
                filesEntity.setFileContent(sb.toString());
                filesEntity.setFileSize(Double.valueOf(sizeInBytes / 1024.0));
                FileTime modifiedDateFileTime = fileAttribute.lastModifiedTime();
                LocalDateTime localDateTime2 = LocalDateTime.ofInstant(modifiedDateFileTime.toInstant(), ZoneId.systemDefault());
                filesEntity.setFileModifiedDate(localDateTime2);
            }else {
                filesEntity=new FilesEntity();
                filesEntity.setFileType("txt");
                filesEntity.setFileContent(sb.toString());
                filesEntity.setFileName(filePath.getFileName().toString());
                filesEntity.setFileSize(Double.valueOf(sizeInBytes / 1024.0));
                filesEntity.setFileCreator("NA");
                FileTime createdDateFileTime = fileAttribute.creationTime();
                LocalDateTime localDateTime = LocalDateTime.ofInstant(createdDateFileTime.toInstant(), ZoneId.systemDefault());
                filesEntity.setFileCreatedDate(localDateTime);
                FileTime modifiedDateFileTime = fileAttribute.lastModifiedTime();
                LocalDateTime localDateTime2 = LocalDateTime.ofInstant(modifiedDateFileTime.toInstant(), ZoneId.systemDefault());
                filesEntity.setFileModifiedDate(localDateTime2);
            }
            filesRepo.save(filesEntity);
            System.out.println("**********Text File is Saved**********"+filesEntity);

        }catch (IOException | HibernateException e) {
            System.out.println(e.getMessage());
        }
    }
}
