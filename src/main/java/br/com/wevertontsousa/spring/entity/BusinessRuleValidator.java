package br.com.wevertontsousa.spring.entity;

import java.util.regex.Pattern;

import br.com.wevertontsousa.spring.error.InvalidResourceException;

public class BusinessRuleValidator {

    public static <T> T validateNotNull(T value) {
        if (value == null) throw new InvalidResourceException();
        return value;
    }

    public static String validateString(String value, int minLength, int maxLength, String regEx) {
        if (notPresent(value) || invalidLength(value, minLength, maxLength)) throw new InvalidResourceException();
        value = StringNormalizer.normalize(value);
        if (matchFound(regEx, value)) throw new InvalidResourceException();
        return value;
    }

    private static boolean notPresent(String value) {
        return value == null || value.isBlank();
    }

    private static boolean invalidLength(String value, int min, int max) {
        return value.length() < min || value.length() > max;
    }

    private static boolean matchFound(String regex, String value) {
        return Pattern.compile(regex).matcher(value).find();
    }

}
