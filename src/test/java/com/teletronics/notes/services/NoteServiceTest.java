package com.teletronics.notes.services;

import com.teletronics.notes.comparators.DescendingOrderIgnoringCaseComparator;
import com.teletronics.notes.dtos.NoteDto;
import com.teletronics.notes.exceptions.ResourceNotFoundException;
import com.teletronics.notes.models.Note;
import com.teletronics.notes.repositories.NoteProjection;
import com.teletronics.notes.repositories.NoteRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void findAll_WithNoTags_ReturnsAllProjectedNotes() {
        NoteProjection note1 = mock(NoteProjection.class);
        when(note1.getId()).thenReturn("1");
        when(note1.getTitle()).thenReturn("First Note");
        when(note1.getCreatedDate()).thenReturn(LocalDateTime.now());

        NoteProjection note2 = mock(NoteProjection.class);
        when(note2.getId()).thenReturn("2");
        when(note2.getTitle()).thenReturn("Second Note");
        when(note1.getCreatedDate()).thenReturn(LocalDateTime.now().minusDays(2));

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<NoteProjection> notePage = new PageImpl<>(List.of(note1,note2), pageable, 2);
        Set<String> tags = Set.of();
        when(noteRepository.findAllProjectedBy(pageable)).thenReturn(notePage);

        Page<NoteProjection> result = noteService.findAll(tags, pageable);

        verify(noteRepository).findAllProjectedBy(pageable);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("First Note");
    }
    @Test
    void findAll_WithTags_ReturnsAllProjectedNotes() {
        NoteProjection note1 = mock(NoteProjection.class);
        when(note1.getId()).thenReturn("1");
        when(note1.getTitle()).thenReturn("First Note");
        when(note1.getCreatedDate()).thenReturn(LocalDateTime.now());

        NoteProjection note2 = mock(NoteProjection.class);
        when(note2.getId()).thenReturn("2");
        when(note2.getTitle()).thenReturn("Second Note");
        when(note1.getCreatedDate()).thenReturn(LocalDateTime.now().minusDays(2));

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<NoteProjection> notePage = new PageImpl<>(List.of(note1,note2), pageable, 2);
        Set<String> tags = Set.of("PERSONAL", "BUSINESS");
        when(noteRepository.findByTagsIn(tags, pageable)).thenReturn(notePage);

        Page<NoteProjection> result = noteService.findAll(tags, pageable);

        verify(noteRepository).findByTagsIn(tags, pageable);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("First Note");
    }

    @Test
    public void testFindUniqueOccurrenceWhenInputIsValid() {
        String noteText = "John is a cat, John is a bat!";

        Map<String, Integer> result = noteService.findUniqueOccurrence(noteText);

        Map<String, Integer> expected = new TreeMap<>(new DescendingOrderIgnoringCaseComparator());
        expected.put("john", 2);
        expected.put("is", 2);
        expected.put("cat", 1);
        expected.put("a", 2);
        expected.put("bat", 1);

        assertEquals(expected, result);
    }

    @Test
    public void testFindUniqueOccurrenceWhenEmptyInput() {
        String noteText = "";
        Map<String, Integer> result = noteService.findUniqueOccurrence(noteText);
        System.out.println(result);
        Map<String, Integer> expected = new TreeMap<>(new DescendingOrderIgnoringCaseComparator());
        System.out.println(expected);
        assertEquals(expected, result);
    }

    @Test
    public void testFindUniqueOccurrenceWhenSpecialCharacters() {
        String noteText = "Hello, Hello!! World@@";
        Map<String, Integer> result = noteService.findUniqueOccurrence(noteText);
        Map<String, Integer> expected = new TreeMap<>(new DescendingOrderIgnoringCaseComparator());
        expected.put("hello", 2);
        expected.put("world", 1);

        assertEquals(expected, result);
    }
}
