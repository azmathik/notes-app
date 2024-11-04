package com.teletronics.notes.services;


import com.teletronics.notes.exceptions.ResourceNotFoundException;
import com.teletronics.notes.models.Note;
import com.teletronics.notes.repositories.NoteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public Note save(Note note) {
        try {
            note = noteRepository.save(note);
            return note;
        } catch (Exception e) {
            throw new RuntimeException("Error in creating the new note");
        }
    }

    public Note findById(String id) {
        return noteRepository.findById(String.valueOf(new ObjectId(id)))
                .orElseThrow(()-> new ResourceNotFoundException("Note not found for the given id"));
    }

}
