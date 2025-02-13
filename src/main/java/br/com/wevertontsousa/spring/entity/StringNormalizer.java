package br.com.wevertontsousa.spring.entity;

import java.util.ArrayList;

public class StringNormalizer {

    public static String normalize(String value) {
        var valueSplit = value.split("\\s");

        if (valueSplit.length == 1) return value.trim();

        var newValue = new ArrayList<String>();

        for (String part : valueSplit) if (!part.isBlank()) newValue.add(part.trim());

        return String.join(" ", newValue);
    }

}
