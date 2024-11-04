package com.teletronics.notes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teletronics.notes.dtos.NoteDto;
import com.teletronics.notes.mappers.NoteMapper;
import com.teletronics.notes.models.Note;
import com.teletronics.notes.services.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteController noteController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void on_create_GivenValidNoteDto_ReturnsCreatedNoteDto() throws Exception {
        NoteDto noteDto = new NoteDto();
        noteDto.setText("Text");
        noteDto.setTitle("Title");
        Note note = new Note();
        note.setText("Text");
        note.setTitle("Title");// Populate fields as necessary
        Note savedNote = new Note();
        savedNote.setId("1");
        savedNote.setText("Text");
        savedNote.setTitle("Title");
        NoteDto savedNoteDto = new NoteDto();
        savedNoteDto.setId("1");
        savedNoteDto.setText("Text");
        savedNoteDto.setTitle("Title");

        when(noteMapper.mapToNote(null, noteDto)).thenReturn(note);
        when(noteService.save(note)).thenReturn(savedNote);
        when(noteMapper.mapFromNote(savedNote)).thenReturn(savedNoteDto);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                        .andExpect(status().isCreated());
    }

    @Test
    void on_update_GivenValidIdAndNoteDto_ReturnsUpdatedNoteDto() throws Exception {
        // Arrange
        String id = "1";
        NoteDto noteDto = new NoteDto();
        noteDto.setTitle("Updated Title");
        noteDto.setText("Updated Text");

        Note existingNote = new Note();
        existingNote.setId(id);
        existingNote.setTitle("Original Title");
        existingNote.setText("Original Text");

        Note updatedNote = new Note();
        updatedNote.setId(id);
        updatedNote.setTitle("Updated Title");
        updatedNote.setText("Updated Text");

        NoteDto updatedNoteDto = new NoteDto();
        updatedNoteDto.setId(id);
        updatedNoteDto.setTitle("Updated Title");
        updatedNoteDto.setText("Updated Text");

        when(noteService.findById(id)).thenReturn(existingNote);
        when(noteMapper.mapToNote(existingNote, noteDto)).thenReturn(updatedNote);
        when(noteService.save(updatedNote)).thenReturn(updatedNote);
        when(noteMapper.mapFromNote(updatedNote)).thenReturn(updatedNoteDto);

        mockMvc.perform(put("/api/notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isOk());
    }
}
