package org.practice;

import java.util.Map;
import java.util.Scanner;

public class MainEugene {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        var actionKey = scanner.nextLine();

        Map<String, Runnable> actions = Map.of(
                "action1", () -> {
                    System.out.println("Action 1");
                },
                "action2", () -> {
                    System.out.println("Action 2");
                });

        Runnable action = actions.get(actionKey);
        if (action != null) {
            action.run();
        }
    }
}
