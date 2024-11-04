package com.teletronics.notes.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teletronics.notes.validators.NotEmptyIfString;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NotEmptyIfString(targetField = "title", dependentField = "text", condition = NotEmptyIfString.Condition.EMPTY)
@NotEmptyIfString(targetField = "text", dependentField = "title", condition = NotEmptyIfString.Condition.EMPTY)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteDto implements Serializable {
    private String id;
    private String title;
    private String text;
    private Set<@Pattern(regexp = "BUSINESS|PERSONAL|IMPORTANT", message = "Tag should be BUSINESS, PERSONAL or IMPORTANT") String> tags;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
