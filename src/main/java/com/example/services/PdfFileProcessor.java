package com.example.services;

import com.example.entity.FilesEntity;
import com.example.repository.FilesRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Optional;

@Service
public class PdfFileProcessor implements FileProcessor{

    private final FilesRepository filesRepo;

    // Spring automatically calls this constructor when creating the bean
    @Autowired
    public PdfFileProcessor(FilesRepository filesRepo) {
        this.filesRepo = filesRepo;
    }
    @Override
    public void processFile(Path filePath) {
        File f=filePath.toFile();
        try(PDDocument document=PDDocument.load(f)){//Represents the PDF document. It loads the PDF file into memory.
            PDFTextStripper textStrpper=new PDFTextStripper();//Extracts the text from the PDF document. This class is used to convert the content of the PDF into a plain text format.
            String pdfText=textStrpper.getText(document);
            PDDocumentInformation pdfMetaData=document.getDocumentInformation();//extract pdf metadata

            FilesEntity file=new FilesEntity();


            Calendar createdCalendar=pdfMetaData.getCreationDate();
            LocalDateTime localCreatedDateTime=LocalDateTime.ofInstant(createdCalendar.toInstant(), ZoneId.systemDefault());
            Calendar modifiedCalendar=pdfMetaData.getModificationDate();
            LocalDateTime localModifiedDateTime=LocalDateTime.ofInstant(modifiedCalendar.toInstant(), ZoneId.systemDefault());



            long sizeInBytes= Files.size(filePath);
            Optional<FilesEntity> filesEntityOpt=filesRepo.findByFileName(filePath.getFileName().toString());
            FilesEntity filesEntity=null;
            if(filesEntityOpt.isPresent()){
                filesEntity=filesEntityOpt.get();
                filesEntity.setFid(filesEntity.getFid());
                filesEntity.setFileContent(pdfText);
                filesEntity.setFileSize(Double.valueOf(sizeInBytes/1024));// fetching file size and converting to mb.
                filesEntity.setFileModifiedDate(localModifiedDateTime);
            }else {
                filesEntity=new FilesEntity();
                filesEntity.setFileName(filePath.getFileName().toString());
                filesEntity.setFileContent(pdfText);
                filesEntity.setFileCreator(pdfMetaData.getAuthor());
                filesEntity.setFileType("pdf");
                filesEntity.setFileSize(Double.valueOf(sizeInBytes/1024));// fetching file size and converting to mb.
                filesEntity.setFileModifiedDate(localModifiedDateTime);
                filesEntity.setFileCreatedDate(localCreatedDateTime);
            }

            filesRepo.save(filesEntity);
            System.out.println("**********PDF is Saved**********");

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
