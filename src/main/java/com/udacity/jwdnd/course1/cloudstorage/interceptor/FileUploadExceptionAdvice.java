package com.udacity.jwdnd.course1.cloudstorage.interceptor;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("fileError", "File size is too large");
        return "redirect:/home";
    }
}