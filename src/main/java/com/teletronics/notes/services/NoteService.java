package com.teletronics.notes.services;


import com.teletronics.notes.models.Note;
import com.teletronics.notes.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public Note save(Note note) {
        note = noteRepository.save(note);
        return note;
    }

}
