package com.teletronics.notes.services;


import com.teletronics.notes.exceptions.ResourceNotFoundException;
import com.teletronics.notes.models.Note;
import com.teletronics.notes.repositories.NoteProjection;
import com.teletronics.notes.repositories.NoteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public Page<NoteProjection> findAll(Set<String> tags, Pageable pageable) {
        if(CollectionUtils.isEmpty(tags)){
            return noteRepository.findAllProjectedBy(pageable);
        }
        return noteRepository.findByTagsIn(tags, pageable);
    }

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

    public void delete(Note note) {
        noteRepository.delete(note);
    }

}
