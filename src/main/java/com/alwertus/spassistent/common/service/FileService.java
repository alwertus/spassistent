package com.alwertus.spassistent.common.service;

import com.alwertus.spassistent.common.view.ResponseError;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Log4j2
@Service
public class FileService {
    private static final String partSeparator = ",";

    @Value("${front.filestore.path}")
    private String storePath;

    public String saveFile(Long id, String content, String fileExt) {

        if (!content.contains(partSeparator)) {
            log.error("Error while parse file. File data not contains separator");
            throw new RuntimeException("Error while parse file");
        }

        String encodedImg = content.split(partSeparator)[1];
        byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));

        Date d = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS.");

        Path destinationFile = Paths.get(storePath, df.format(d) + fileExt);
        try {
            Files.write(destinationFile, decodedImg);
        } catch (IOException e) {
            log.error("Can't save file. " + e.getMessage());
            throw new RuntimeException("Can't save file");
        }

        return "URL" + destinationFile;
    }

    public Resource getFile(String fileName) {
        try {
            Path path = Paths.get(storePath, fileName);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists())
                throw new RuntimeException("File '" + fileName + "' not found");
            return resource;
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File '" + fileName + "' read error");
        }
    }
}
