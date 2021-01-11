package code;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    /**
     * Used as a shortcut for value input
     * @param scanner
     * @param question
     * @param defaultInputValue
     * @return defaultInputValue or input given by user
     */
    private static int universalInputIntValues(Scanner scanner, String question, int defaultInputValue) {
        System.out.printf(question + "?");
        String line = scanner.nextLine();
        if (line.isEmpty())
            return defaultInputValue;
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException ex) {
            System.out.println("Not integer. Initializing default value");
            return defaultInputValue;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleCharger consoleCharger = new ConsoleCharger(4);
        List<Thread> threads = new ArrayList<>();
        int robotAmount = universalInputIntValues(scanner, "Ile robotów?", 6);

        for(int i = 0; i < robotAmount; i++) {
            String nameOfTheRobot = Character.toString(65 + i);
            System.out.println("Rozmiar robota?");
            int size = universalInputIntValues(scanner, "Rozmiar robota " + nameOfTheRobot, 1 + i);
            Robot robot = new Robot( nameOfTheRobot, size, consoleCharger);
            consoleCharger.addRobot(robot);
            threads.add(new Thread(robot));
        }
//        threads.forEach(Thread::start);
        threads.forEach(thread -> thread.start());
    }
}
