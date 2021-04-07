package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CredentialController {

    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping("/register-credential")
    public String registerCredential(Authentication authentication,
                               @ModelAttribute Credential credential,
                               Model model){
        String credentialRegisterError = null;
        User user = (User) authentication.getPrincipal();
        credential.setUserId(user.getUserId());

        if (credential.getCredentialId() == null){
            Integer rowsAdded = credentialService.insert(credential);
            if(rowsAdded < 0){
                credentialRegisterError = "There was an error creating the credential.";
            }
            if (credentialRegisterError == null){
                model.addAttribute("credentialCreatedSuccessfully", "Credential created successfully");
            } else {
                model.addAttribute("credentialError", credentialRegisterError);
            }
        } else {
            Integer rowsUpdated = credentialService.update(credential);
            if(rowsUpdated < 0){
                credentialRegisterError = "There was an error updating the credential.";
            }
            if(credentialRegisterError == null){
                model.addAttribute("credentialCreatedSuccessfully", "Credential updated successfully");
            } else {
                model.addAttribute("credentialError", credentialRegisterError);
            }
        }

        model.addAttribute("tab", "credentials");
        model.addAttribute("credentials", credentialService.getCredentialsByUser(user.getUserId()));
        return "home";
    }

    @GetMapping("delete-credential/{credentialId}")
    public String deleteCredential(Authentication authentication, @PathVariable("credentialId") Integer credentialId, Model model){
        User user = (User) authentication.getPrincipal();
        String credentialDeletedError = null;
        Integer rowsDeleted = credentialService.delete(credentialId);
        if(rowsDeleted < 0){
            credentialDeletedError = "There was an error deleting the credential.";
        }

        if(credentialDeletedError == null){
            model.addAttribute("credentialDeletedSuccessfully", "Credential deleted successfully");
        } else {
            model.addAttribute("credentialError", credentialDeletedError);
        }

        model.addAttribute("tab", "credentials");
        model.addAttribute("credentials", credentialService.getCredentialsByUser(user.getUserId()));
        return "home";
    }

    @GetMapping("/decryptPassword/{credentialId}")
    @ResponseBody
    public Map<String, Object> decryptPassword(@PathVariable(value = "credentialId") Integer credentialId) {
        Map<String, Object> response = new HashMap<>();
        response.put("decryptedPassword", credentialService.decryptPassword(credentialId));
        return response;
    }

}
