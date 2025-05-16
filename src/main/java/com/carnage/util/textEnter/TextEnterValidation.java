package com.carnage.util.textEnter;

public class TextEnterValidation {

    private static final String REGEX = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+( [a-zA-ZáéíóúÁÉÍÓÚñÑ]+)+$";


    public static String validateTextEnter(String text) throws InvalidTextEnterException {
        if (text == null || text.isEmpty()) {
            throw new InvalidTextEnterException("Input cannot be null or empty");
        }

        if (!text.matches(REGEX)) {
            throw new IllegalArgumentException("Invalid input format. Only letters and spaces are allowed.");
        }

        return text;
    }
}
