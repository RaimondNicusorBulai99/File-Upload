package Servizi.File.Upload.controllers;

import Servizi.File.Upload.services.FileStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;


    @PostMapping("/upload")
    public List<String> upload(@RequestParam MultipartFile[] files) throws IOException {
        List<String> nameFile = new ArrayList<>();
        for (MultipartFile file:files){
            String singleFile = fileStorageService.upload(file);
            nameFile.add(singleFile);
        }
        return nameFile;
    }

    @GetMapping("/download")
    public @ResponseBody byte[] download(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        String extension = FilenameUtils.getExtension(fileName);
        switch (extension) {
            case "gif" -> response.setContentType(MediaType.IMAGE_GIF_VALUE);
            case "jpg", "jpeg" -> response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            case "png" -> response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader("Content-Disposition","attachment, filename=\"" + fileName +"\"");
        return fileStorageService.download(fileName);
    }
}
