package code;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static int inputNumber(Scanner scanner, String question, int defaultVal) {
        System.out.printf("%s? [%d] ", question, defaultVal);
        while(true) {
            String line = scanner.nextLine();
            if (line.isEmpty())
                return defaultVal;
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                // loop again
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleCharger chargingStation = new ConsoleCharger(inputNumber(scanner, "Ilość portów w ładowarce", 8));

        List<Thread> threads = new ArrayList<>();

        int robotAmount = inputNumber(scanner, "Ilość robotów", 6);
        for(int i = 0; i < robotAmount; i++) {
            String name = Character.toString((int)'A' + (i % 26));
            int workTime = inputNumber(scanner, "Czas pracy robota " + name, 1 + (int) (Math.random() * 20));
            int size = inputNumber(scanner, "Rozmiar robota " + name, 1 + (int) (Math.random() * 6));
            Robot robot = new Robot(name, size, chargingStation);
            chargingStation.addRobot(robot);
            threads.add(new Thread(robot));
        }
        threads.forEach(Thread::start);
    }
}
