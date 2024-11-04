package com.teletronics.notes.repositories;

import com.teletronics.notes.models.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface NoteRepository extends MongoRepository<Note, String > {
    Page<NoteProjection> findAllProjectedBy(Pageable pageable);
    Page<NoteProjection> findByTagsIn(Set<String> tags, Pageable pageable);
}
