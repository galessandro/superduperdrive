package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int insert(Credential credential){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));
        credential.setKey(encodedKey);
        return credentialMapper.insert(credential);
    }

    public int delete(Integer credentialId){
        return credentialMapper.delete(credentialId);
    }

    public List<Credential> getCredentialsByUser(Integer userId){
        return credentialMapper.getCredentialsByUser(userId);
    }

    public Integer update(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));
        credential.setKey(encodedKey);
        return credentialMapper.update(credential);
    }

    public String decryptPassword(Integer credentialId){
        Credential credential = credentialMapper.getCredentialsById(credentialId);
        String encryptedPassword = credential.getPassword();
        String encodedKey = credential.getKey();
        return encryptionService.decryptValue(encryptedPassword, encodedKey);
    }
}
