package com.example.services;

import org.apache.pdfbox.util.filetypedetector.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is a Spring-managed bean of FileFactory that helps to get the correct FileProcessor based on the file type.
 * Implemented a factory pattern to get object at runtime.
 */
@Service
public class FileFactory {

    private Map<String,FileProcessor> processorMap=new HashMap<>();

    @Autowired
    public FileFactory(List<FileProcessor> processorList) {
        for (FileProcessor processor : processorList) {
            if (processor instanceof PdfFileProcessor) {
                processorMap.put("pdf", processor);
            } else if (processor instanceof WordFileProcessor) {
                processorMap.put("docx", processor);
            } else if (processor instanceof TextFileProcessor) {
                processorMap.put("txt", processor);
            }
        }
        System.out.println(processorMap);
    }


    public FileProcessor getFileProcessor(String type) {
        FileProcessor processor = processorMap.get(type.trim().toLowerCase());
        if (processor == null) {
            throw new IllegalArgumentException("No processor found for type: " + type);
        }
        return processor;
    }
}
