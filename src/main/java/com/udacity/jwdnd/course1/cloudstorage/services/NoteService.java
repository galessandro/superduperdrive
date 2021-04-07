package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int insert(Note note){
        return noteMapper.insert(note);
    }

    public int update(Note note){
        return noteMapper.update(note);
    }

    public int delete(Integer noteId){
        return noteMapper.delete(noteId);
    }

    public List<Note> getNotesByUser(Integer userId){
        return noteMapper.getNotesByUser(userId);
    }
}
