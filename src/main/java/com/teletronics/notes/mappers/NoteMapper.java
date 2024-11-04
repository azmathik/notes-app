package com.teletronics.notes.mappers;

import com.teletronics.notes.dtos.NoteDto;
import com.teletronics.notes.models.Note;
import com.teletronics.notes.models.Tag;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NoteMapper {

    public Note mapToNote(Note note, NoteDto source) {
        if(ObjectUtils.isEmpty(note)) {
            note = new Note();
        }
        note.setTitle(source.getTitle());
        note.setText(source.getText());

        if(!CollectionUtils.isEmpty(source.getTags())){
            Set<Tag> tags = source.getTags()
                    .stream()
                    .map(Tag::valueOf)
                    .collect(Collectors.toSet());
            note.setTags(tags);
        }
        return note;
    }

    public NoteDto mapFromNote(Note source) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(source.getId());
        noteDto.setTitle(source.getTitle());
        noteDto.setText(source.getText());

        if(!CollectionUtils.isEmpty(source.getTags())){
            Set<String> tags = source.getTags()
                    .stream()
                    .map(Enum::name)
                    .collect(Collectors.toSet());
            noteDto.setTags(tags);
        }
        return noteDto;
    }
    
}
