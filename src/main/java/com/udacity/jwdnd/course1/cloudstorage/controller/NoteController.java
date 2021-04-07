package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/register-note")
    public String registerNote(Authentication authentication,
                               @ModelAttribute Note note,
                               Model model){
        String noteRegisterError = null;
        User user = (User) authentication.getPrincipal();
        note.setUserId(user.getUserId());

        if (note.getNoteId() == null){
            Integer rowsAdded = noteService.insert(note);
            if(rowsAdded < 0){
                noteRegisterError = "There was an error creating the note.";
            }
            if (noteRegisterError == null){
                model.addAttribute("noteCreatedSuccessfully", "Note created successfully");
            } else {
                model.addAttribute("noteError", noteRegisterError);
            }
        } else {
            Integer rowsUpdated = noteService.update(note);
            if(rowsUpdated < 0){
                noteRegisterError = "There was an error updating the note.";
            }
            if(noteRegisterError == null){
                model.addAttribute("noteCreatedSuccessfully", "Note updated successfully");
            } else {
                model.addAttribute("noteError", noteRegisterError);
            }
        }
        model.addAttribute("tab", "notes");
        model.addAttribute("notes", noteService.getNotesByUser(user.getUserId()));
        return "home";
    }

    @GetMapping("delete-note/{noteId}")
    public String deleteNote(Authentication authentication, @PathVariable("noteId") Integer noteId, Model model){
        User user = (User) authentication.getPrincipal();
        String noteDeletedError = null;
        Integer rowsDeleted = noteService.delete(noteId);
        if(rowsDeleted < 0){
            noteDeletedError = "There was an error deleting the note.";
        }

        if(noteDeletedError == null){
            model.addAttribute("noteDeletedSuccessfully", "Note deleted successfully");
        } else {
            model.addAttribute("noteError", noteDeletedError);
        }
        model.addAttribute("tab", "notes");
        model.addAttribute("notes", noteService.getNotesByUser(user.getUserId()));
        return "home";
    }
}
