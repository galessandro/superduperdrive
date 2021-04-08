package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/home", "/"})
public class HomeController {

    private final NoteService noteService;
    private final CredentialService credentialService;
    private final FileService fileService;

    public HomeController(NoteService noteService, CredentialService credentialService, FileService fileService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping
    public String homeView(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        model.addAttribute("notes", this.noteService.getNotesByUser(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUser(user.getUserId()));
        model.addAttribute("files", this.fileService.getFilesByUser(user.getUserId()));
        model.addAttribute("tab", "files");
        return "home";
    }
}
