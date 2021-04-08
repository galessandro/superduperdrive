package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.SuperDuperFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int storageFile(Integer userId, MultipartFile multipartFile) throws IOException {
        SuperDuperFile sdFile = new SuperDuperFile();
        sdFile.setUserId(userId);
        sdFile.setFileData(multipartFile.getBytes());
        sdFile.setFileSize(String.valueOf(multipartFile.getSize()));
        sdFile.setContentType(multipartFile.getContentType());
        sdFile.setFileName(multipartFile.getOriginalFilename());
        return fileMapper.insert(sdFile);
    }

    public List<SuperDuperFile> getFilesByUser(Integer userId){
        return fileMapper.getFilesByUser(userId);
    }

    public SuperDuperFile getFileById(Integer fileId){
        return fileMapper.getFileById(fileId);
    }

    public int deleteFile(Integer fileId){
        return fileMapper.delete(fileId);
    }

    public boolean isFileNameAvailable(String fileName, Integer userId){
        List<SuperDuperFile> files = fileMapper.getFilesByUser(userId);
        for (SuperDuperFile file: files) {
            if(file.getFileName().equals(fileName)){
                return false;
            }
        }
        return true;
    }

    public boolean isFileNameBlank(String fileName){
        return fileName == null || fileName.trim().equals("");
    }

    public boolean isFileSizeTooLarge(Long size){
        return size > 5242880L;
    }

    public String validateFile(Integer userId, String fileName, Long size){
        if(isFileNameBlank(fileName)){
            return "File name is invalid";
        }

        if(!isFileNameAvailable(fileName, userId)){
            return "File name is already used";
        }

        if(isFileSizeTooLarge(size)){
            //return "File size is too large";
            throw new MaxUploadSizeExceededException(size);
        }

        return null;
    }
}
