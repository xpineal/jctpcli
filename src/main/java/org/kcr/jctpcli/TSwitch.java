package org.kcr.jctpcli;

import org.jetbrains.annotations.NotNull;

public class TSwitch {
    public static void outputSwitch(@NotNull String s) {
        switch (s) {
            case "A":
                System.out.println("1");
                break;
            case "B":
                System.out.println("2");
                break;
            case "C":
                System.out.println("3");
                break;
            case "D":
                System.out.println("4");
            default:
                System.out.println("default");
                break;
        }
    }

    public static void main(String @NotNull [] args) {
        outputSwitch(args[0]);
    }
}
