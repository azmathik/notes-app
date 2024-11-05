package com.teletronics.notes.comparators;

import java.util.Comparator;

public class DescendingOrderIgnoringCaseComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o2.compareToIgnoreCase(o1);
    }
}
