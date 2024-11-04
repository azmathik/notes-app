package com.teletronics.notes.services;

import com.teletronics.notes.models.Note;
import com.teletronics.notes.repositories.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidNote_whenSave_ReturnsSavedNote() {
        Note note = new Note();
        when(noteRepository.save(note)).thenReturn(note);
        Note result = noteService.save(note);
        assertNotNull(result);
        assertEquals(note, result);
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void givenValidNote_whenSave_ThrowsRuntimeException() {
        Note note = new Note();
        when(noteRepository.save(note)).thenThrow(new RuntimeException("Error in creating the new note"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> noteService.save(note));
        assertEquals("Error in creating the new note", exception.getMessage());
        verify(noteRepository, times(1)).save(note);
    }
}
