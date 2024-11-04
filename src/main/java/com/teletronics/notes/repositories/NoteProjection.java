package com.teletronics.notes.repositories;

import java.time.LocalDateTime;

public interface NoteProjection {
    String getId();
    String getTitle();
    LocalDateTime getCreatedDate();
}
