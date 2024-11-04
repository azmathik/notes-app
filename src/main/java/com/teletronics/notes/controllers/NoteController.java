package com.teletronics.notes.controllers;

import com.teletronics.notes.dtos.NoteDto;
import com.teletronics.notes.mappers.NoteMapper;
import com.teletronics.notes.models.Note;
import com.teletronics.notes.services.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    private NoteMapper noteMapper;
    @Autowired
    private NoteService noteService;

    @GetMapping
    public ResponseEntity<Page<NoteDto>> findAll(@RequestParam(required = false) Set<String> tags,
                                                 @RequestParam(defaultValue = "0") final Integer pageNumber,
                                                 @RequestParam(defaultValue = "5") final Integer pageSize) {
        Page<NoteDto> page = noteService.findAll(tags, PageRequest.of(pageNumber,
                        pageSize,
                        Sort.by(Sort.Direction.DESC, "createdDate")))
                .map(entry -> noteMapper.mapFromNoteProjection(entry));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NoteDto> create(@Valid @RequestBody final NoteDto noteDto) {
            Note note = noteMapper.mapToNote(null, noteDto);
            note = noteService.save(note);
            return new ResponseEntity<>(noteMapper.mapFromNote(note), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> update(@PathVariable("id") final String id, @Valid @RequestBody final NoteDto noteDto) {
        Note note = noteService.findById(id);
        note = noteService.save(noteMapper.mapToNote(note, noteDto));
        return new ResponseEntity<>(noteMapper.mapFromNote(note), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final String id) {
        Note note = noteService.findById(id);
        noteService.delete(note);
        return ResponseEntity.noContent().build();
    }
}
