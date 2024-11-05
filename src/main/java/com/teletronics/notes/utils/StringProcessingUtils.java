package com.teletronics.notes.utils;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringProcessingUtils {

    public static String replaceAllAlphaNumerics(String text) {
        return StringUtils.hasLength(text) ? text.replaceAll("[^a-zA-Z0-9]+", " ") : "";
    }

    public static String[] splitWithRegex(String text, String regex) {
        return Arrays.stream(text.split(regex))
                .filter(word -> word != null && !word.trim().isEmpty())
                .toArray(String[]::new);
    }

}
