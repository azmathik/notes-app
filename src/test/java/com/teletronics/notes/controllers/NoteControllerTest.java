package com.teletronics.notes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teletronics.notes.dtos.NoteDto;
import com.teletronics.notes.exceptions.ResourceNotFoundException;
import com.teletronics.notes.mappers.NoteMapper;
import com.teletronics.notes.models.Note;
import com.teletronics.notes.repositories.NoteProjection;
import com.teletronics.notes.services.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void on_delete_GivenNoteValidId_DeletesNote() throws Exception {
        String id = "1";
        Note note = new Note();
        note.setId(id);
        note.setTitle("Title");
        note.setText("Text");

        when(noteService.findById(id)).thenReturn(note);
        mockMvc.perform(delete("/api/notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(noteService).delete(note);
    }

    @Test
    void on_delete_GivenInvalidNoteId_ThrowsResourceNotFoundException() throws Exception {
        String invalidId = "invalid-id";
        when(noteService.findById(invalidId)).thenThrow(new ResourceNotFoundException("Note not found for the given id"));
        mockMvc.perform(delete("/api/notes/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNoTags_findAll_ReturnsAllProjectedNotes() throws Exception {
        Set<String> tags = Set.of();
        Pageable pageable = Pageable.unpaged();
        NoteProjection note1 = mock(NoteProjection.class);
        when(note1.getId()).thenReturn("1");
        when(note1.getTitle()).thenReturn("First Note");
        when(note1.getCreatedDate()).thenReturn(LocalDateTime.now());

        NoteProjection note2 = mock(NoteProjection.class);
        when(note2.getId()).thenReturn("2");
        when(note2.getTitle()).thenReturn("Second Note");
        when(note1.getCreatedDate()).thenReturn(LocalDateTime.now().minusDays(2));

        List<NoteProjection> projectedNotes = Arrays.asList(note1, note2);

        Page<NoteProjection> notePage = new PageImpl<>(projectedNotes, pageable, projectedNotes.size());

        Page<NoteProjection> projectionPage = new PageImpl<>(projectedNotes, PageRequest.of(0, 2), 5);

        List<NoteDto> noteDtos = projectedNotes.stream()
                .map(projection -> new NoteDto(projection.getId(), projection.getTitle(),null,null,null,null))
                .collect(Collectors.toList());
        Page<NoteDto> dtoPage = new PageImpl<>(noteDtos, PageRequest.of(0, 2), 5);

        when(noteService.findAll(any(), eq(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate")))))
                .thenReturn(projectionPage);
        for (int i = 0; i < projectedNotes.size(); i++) {
            when(noteMapper.mapFromNoteProjection(projectedNotes.get(i))).thenReturn(noteDtos.get(i));
        }

        mockMvc.perform(get("/api/notes")
                        .param("pageNumber", "0")
                        .param("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[1].id").value("2"))
                .andExpect(jsonPath("$.totalElements").value(5));
    }

    @Test
    void givenTags_findAll_ReturnsAllProjectedNotes() throws Exception {
        Set<String> tags = Set.of();
        Pageable pageable = Pageable.unpaged();
        NoteProjection note1 = mock(NoteProjection.class);
        when(note1.getId()).thenReturn("1");
        when(note1.getTitle()).thenReturn("First Note");
        when(note1.getCreatedDate()).thenReturn(LocalDateTime.now());

        NoteProjection note2 = mock(NoteProjection.class);
        when(note2.getId()).thenReturn("2");
        when(note2.getTitle()).thenReturn("Second Note");
        when(note1.getCreatedDate()).thenReturn(LocalDateTime.now().minusDays(2));

        List<NoteProjection> projectedNotes = Arrays.asList(note1, note2);

        Page<NoteProjection> notePage = new PageImpl<>(projectedNotes, pageable, projectedNotes.size());

        Page<NoteProjection> projectionPage = new PageImpl<>(projectedNotes, PageRequest.of(0, 2), 5);

        List<NoteDto> noteDtos = projectedNotes.stream()
                .map(projection -> new NoteDto(projection.getId(), projection.getTitle(),null,null,null,null))
                .collect(Collectors.toList());
        Page<NoteDto> dtoPage = new PageImpl<>(noteDtos, PageRequest.of(0, 2), 5);

        when(noteService.findAll(any(), eq(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate")))))
                .thenReturn(projectionPage);
        for (int i = 0; i < projectedNotes.size(); i++) {
            when(noteMapper.mapFromNoteProjection(projectedNotes.get(i))).thenReturn(noteDtos.get(i));
        }

        mockMvc.perform(get("/api/notes")
                        .param("tags", "IMPORTANT,BUSINESS")
                        .param("pageNumber", "0")
                        .param("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[1].id").value("2"))
                .andExpect(jsonPath("$.totalElements").value(5));
    }

    @Test
    public void givenAText_Get_Stats() throws Exception {
        String inputText = "note is just a note";
        Map<String, Integer> mockResponse = Map.of(
                "note", 2,
                "is", 1,
                "just", 1,
                "a", 1
        );
        when(noteService.findUniqueOccurrence(inputText)).thenReturn(mockResponse);
        mockMvc.perform(post("/api/notes/stats")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.note").value(2))
                .andExpect(jsonPath("$.is").value(1))
                .andExpect(jsonPath("$.just").value(1))
                .andExpect(jsonPath("$.a").value(1));
    }
}
