package com.example.services;

import com.example.entity.FilesEntity;
import com.example.repository.FilesRepository;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class WordFileProcessor implements FileProcessor{

    private final FilesRepository filesRepo;

    // Spring automatically calls this constructor when creating the bean
    @Autowired
    public WordFileProcessor(FilesRepository filesRepo) {
        this.filesRepo = filesRepo;
    }
    @Override
    public void processFile(Path filePath) {
        File file=filePath.toFile();
        try(XWPFDocument document=new XWPFDocument(new FileInputStream(file));){
        StringBuilder sb=new StringBuilder();
        for(XWPFParagraph paragraph: document.getParagraphs()){
            sb.append(paragraph.getText());
        }
        
        POIXMLProperties.CoreProperties proprties=document.getProperties().getCoreProperties();

            long sizeInBytes= Files.size(filePath);
            Optional<FilesEntity> filesEntityOpt=filesRepo.findByFileName(filePath.getFileName().toString());
            FilesEntity filesEntity=null;
            if(filesEntityOpt.isEmpty()){
                filesEntity=new FilesEntity();
                filesEntity.setFileType("docx");
                filesEntity.setFileContent(sb.toString());
                filesEntity.setFileName(filePath.getFileName().toString());
                filesEntity.setFileCreator(proprties.getCreator());
                filesEntity.setFileCreatedDate(LocalDateTime.ofInstant(proprties.getCreated().toInstant(), ZoneId.systemDefault()));
                filesEntity.setFileModifiedDate(LocalDateTime.ofInstant(proprties.getModified().toInstant(), ZoneId.systemDefault()));
                filesEntity.setFileSize(Double.valueOf(sizeInBytes/1024.0));
            }else {
                filesEntity=filesEntityOpt.get();
                filesEntity.setFid(filesEntity.getFid());
                filesEntity.setFileContent(sb.toString());
                filesEntity.setFileModifiedDate(LocalDateTime.ofInstant(proprties.getModified().toInstant(), ZoneId.systemDefault()));
                filesEntity.setFileSize(Double.valueOf(sizeInBytes/1024.0));
            }


        filesRepo.save(filesEntity);
        System.out.println("**********Word File is Saved**********");
        }catch (IOException e){

        }
    }
}
