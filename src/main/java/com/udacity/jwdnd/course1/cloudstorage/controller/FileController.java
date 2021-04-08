package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.SuperDuperFile;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller

public class FileController{

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload-file")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload")MultipartFile fileUpload, Model model) {
        User user = (User) authentication.getPrincipal();
        String fileUploadedError = null;

        fileUploadedError = fileService.validateFile(user.getUserId(), fileUpload.getOriginalFilename(), fileUpload.getSize());

        if(fileUploadedError == null){
            try {
                int rowsAdded = fileService.storageFile(user.getUserId(), fileUpload);
                if (rowsAdded < 0){
                    fileUploadedError = "There was an error storing the file. Please try again.";
                }
            } catch (IOException e) {
                fileUploadedError = "There was an error storing the file. Please try again.";
            }
        }

        if(fileUploadedError == null){
            model.addAttribute("fileUploadSuccess", "File Uploaded successfully");
        } else {
            model.addAttribute("fileError", fileUploadedError);
        }

        model.addAttribute("tab", "files");
        model.addAttribute("files", fileService.getFilesByUser(user.getUserId()));
        return "home";
    }

    @GetMapping("view-file/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable("fileId") Integer fileId){
        SuperDuperFile sdFile = fileService.getFileById(fileId);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + sdFile.getFileName());
        header.add(HttpHeaders.CONTENT_TYPE, sdFile.getContentType());
        header.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        header.add(HttpHeaders.PRAGMA, "no-cache");
        header.add(HttpHeaders.EXPIRES, "0");

        ByteArrayResource resource = new ByteArrayResource((sdFile.getFileData()));
        return ResponseEntity.ok()
                .headers(header)
                .body(resource);
    }

    @GetMapping("delete-file/{fileId}")
    public String deleteFile(Authentication authentication, @PathVariable("fileId") Integer fileId, Model model){
        User user = (User) authentication.getPrincipal();
        String fileDeletedError = null;
        Integer rowsDeleted = fileService.deleteFile(fileId);
        if(rowsDeleted < 0){
            fileDeletedError = "There was an error deleting the file.";
        }

        if(fileDeletedError == null){
            model.addAttribute("fileDeleteSuccess", "File deleted successfully");
        } else {
            model.addAttribute("fileError", fileDeletedError);
        }

        model.addAttribute("tab", "files");
        model.addAttribute("files", fileService.getFilesByUser(user.getUserId()));
        return "home";
    }
}
