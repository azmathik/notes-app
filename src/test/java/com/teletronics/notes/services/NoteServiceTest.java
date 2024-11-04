package com.teletronics.notes.services;

import com.teletronics.notes.exceptions.ResourceNotFoundException;
import com.teletronics.notes.models.Note;
import com.teletronics.notes.repositories.NoteRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

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

    @Test
    void givenValidId_findById_ReturnsNote() {
        String id = new ObjectId().toString();
        Note expectedNote = new Note();
        expectedNote.setId(id);
        expectedNote.setTitle("Title");
        expectedNote.setText("Text");

        when(noteRepository.findById(new ObjectId(id).toString())).thenReturn(Optional.of(expectedNote));
        Note actualNote = noteService.findById(id);
        assertEquals(expectedNote, actualNote);
    }

    @Test
    void givenInvalidId_findById_ThrowsResourceNotFoundException() {
        String invalidId = new ObjectId().toString();
        when(noteRepository.findById(new ObjectId(invalidId).toString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> noteService.findById(invalidId));
    }

    @Test
    void givenNote_DeletesNote() {
        Note note = new Note();
        note.setId("1");
        note.setTitle("Title");
        note.setText("Text");
        noteService.delete(note);
        verify(noteRepository, times(1)).delete(note); // Verify delete is called once with the correct note
    }
}
