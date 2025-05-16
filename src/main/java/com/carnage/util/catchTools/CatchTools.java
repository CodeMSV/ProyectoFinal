package com.carnage.util.catchTools;

import java.util.Scanner;

public class CatchTools {

    /**
     * This method is used to catch the exception and print the message.
     *
     * @param sc      Scanner object to read input
     * @param message Message to be printed
     */
    public static void cleanScannerAndPrint(Scanner sc, String message) {
        sc.nextLine();
        System.out.println(String.format("Error: %s", message));
    }
}
