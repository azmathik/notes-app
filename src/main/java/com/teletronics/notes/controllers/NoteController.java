package com.teletronics.notes.controllers;

import com.teletronics.notes.dtos.NoteDto;
import com.teletronics.notes.mappers.NoteMapper;
import com.teletronics.notes.models.Note;
import com.teletronics.notes.services.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    private NoteMapper noteMapper;
    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteDto> create(@Valid @RequestBody final NoteDto noteDto) {
        Note note = noteMapper.mapToNote(null, noteDto);
        note = noteService.save(note);
        return new ResponseEntity<>(noteMapper.mapFromNote(note), HttpStatus.CREATED);
    }
}
