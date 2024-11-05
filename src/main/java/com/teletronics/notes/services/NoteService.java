package com.teletronics.notes.services;


import com.teletronics.notes.comparators.DescendingOrderIgnoringCaseComparator;
import com.teletronics.notes.exceptions.ResourceNotFoundException;
import com.teletronics.notes.models.Note;
import com.teletronics.notes.repositories.NoteProjection;
import com.teletronics.notes.repositories.NoteRepository;
import com.teletronics.notes.utils.StringProcessingUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public Page<NoteProjection> findAll(Set<String> tags, Pageable pageable) {
        try {
            if(CollectionUtils.isEmpty(tags)){
                return noteRepository.findAllProjectedBy(pageable);
            }
            return noteRepository.findByTagsIn(tags, pageable);
        } catch(Exception e) {
            throw new RuntimeException("Error in getting notes page");
        }
    }

    public Note save(Note note) {
        try {
            note = noteRepository.save(note);
            return note;
        } catch (Exception e) {
            throw new RuntimeException("Error in creating or updating the note");
        }
    }

    public Note findById(String id) {
        try {
            return noteRepository.findById(String.valueOf(new ObjectId(id)))
                    .orElseThrow(()-> new ResourceNotFoundException("Note not found for the given id"));
        } catch (Exception e) {
            throw new RuntimeException("Error in finding a note");
        }

    }

    public String getNoteText(String id) {
        try {
            Note note = findById(id);
            return note.getText();
        } catch (Exception e) {
            throw new RuntimeException("Error in getting the note text");
        }
    }

    public void delete(Note note) {
        try {
        noteRepository.delete(note);
        } catch (Exception e) {
            throw new RuntimeException("Error in deleting a note");
        }
    }

    public Map<String, Integer> findUniqueOccurrence(String noteText) {
        Map<String, Integer> stringOccurrencesMap = new TreeMap<>(new DescendingOrderIgnoringCaseComparator());

        if(!StringUtils.hasLength(noteText)) {
            return stringOccurrencesMap;
        }
        String noteTextWithoutAlphaNumerics = StringProcessingUtils.replaceAllAlphaNumerics(noteText);

        String[] words = StringProcessingUtils.splitWithRegex(noteTextWithoutAlphaNumerics, " ");

        for (String word : words) {
            stringOccurrencesMap.merge(word.toLowerCase(), 1, Integer::sum);
        }
        return stringOccurrencesMap;
    }
}
