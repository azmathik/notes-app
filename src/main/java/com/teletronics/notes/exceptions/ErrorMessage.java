package com.teletronics.notes.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;


}
