package com.carnage.util.textEnter;

import com.carnage.util.catchTools.CatchTools;

import java.util.Scanner;

import static java.awt.SystemColor.text;

public class TextEnterCreator {

    private static Scanner sc = new Scanner(System.in);
    private static String text;


    /**
     * This method is used to register a text input from the user.
     * It validates the input using the TextEnterValidation class.
     *
     * @param question The question to be displayed to the user.
     * @return The validated text input from the user.
     */
    public static String registerTextEnter(String question) {

        try {
            System.out.println(question);
            text = TextEnterValidation.validateTextEnter(sc.nextLine());

        } catch (InvalidTextEnterException e) {
            CatchTools.cleanScannerAndPrint(sc, e.getMessage());
        }

        return text;
    }
}
