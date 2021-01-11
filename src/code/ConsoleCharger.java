package code;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class ConsoleCharger {
    private List<Optional<Robot>> chargerPorts = new ArrayList<>();
    private List<Robot> allRobots;
    int maxRobotSize;

    public ConsoleCharger(int size) {
        for (int i = 0; i < size; i++)
            chargerPorts.add(Optional.empty());

        allRobots = new ArrayList<>();
    }

    public synchronized void addRobot(Robot robot) {
        allRobots.add(robot);
        if (robot.getRobotSize() > maxRobotSize)
            maxRobotSize = robot.getRobotSize();
    }

    private OptionalInt getEmptySlotForSize(int size) {
        int emptyRangeSize = 0;
        for (int i = 1; i < chargerPorts.size() - 1; i++) {
            if (chargerPorts.get(i).isPresent()) {
                emptyRangeSize = 0;
                continue;
            }

            emptyRangeSize++;
            if (emptyRangeSize >= size)
                return OptionalInt.of(i - size + 1);
        }

        if (chargerPorts.get(0).isEmpty())
            return OptionalInt.of(0);

        if (chargerPorts.get(chargerPorts.size() - 1).isEmpty())
            return OptionalInt.of(chargerPorts.size() - 1);

        return OptionalInt.empty();
    }

    public synchronized boolean requestCharge(Robot robot) {
        OptionalInt maybeSlot = getEmptySlotForSize(robot.getRobotSize());
        if (maybeSlot.isEmpty())
            return false;
        int slot = maybeSlot.getAsInt();

        if (slot == 0 || slot == chargerPorts.size() - 1) {
            chargerPorts.set(slot, Optional.of(robot));
        } else {
            for (int i = slot; i < slot + robot.getRobotSize(); i++) {
                chargerPorts.set(i, Optional.of(robot));
            }
        }
        return true;
    }

    public synchronized void disconnect(Robot robot) {
        for (int i = 0; i < chargerPorts.size(); i++) {
            Optional<Robot> robotOnPort = chargerPorts.get(i);
            if (robotOnPort.isPresent() && robotOnPort.orElseThrow() == robot)
                chargerPorts.set(i, Optional.empty());
        }

        allRobots.forEach(Robot::isChargerAvailable);
    }

    public synchronized void display() {
        System.out.println("\nNazwa  Rozmiar  Status");
        allRobots.stream().map(Robot::toString).forEach(System.out::println);

        System.out.println("Stanowiska");
        for (int i = 0; i < maxRobotSize - 1; i++) {
            if (chargerPorts.get(0).isPresent()
                    && chargerPorts.get(0).orElseThrow().getRobotSize() >= maxRobotSize - i)
                System.out.printf("_: %s\n", chargerPorts.get(0).orElseThrow().getRobotName());
            else
                System.out.println("_:");
        }
        for (int i = 0; i < chargerPorts.size(); i++) {
            if (chargerPorts.get(i).isPresent())
                System.out.printf("%d: %s\n", i + 1, chargerPorts.get(i).orElseThrow().getRobotName());
            else
                System.out.printf("%d:\n", i + 1);
        }
        for (int i = 0; i < maxRobotSize - 1; i++) {
            if (chargerPorts.get(chargerPorts.size() - 1).isPresent()
                    && chargerPorts.get(chargerPorts.size() - 1).orElseThrow().getRobotSize() > i + 1)
                System.out.printf("_: %s\n", chargerPorts.get(chargerPorts.size() - 1).orElseThrow().getRobotName());
            else
                System.out.println("_:");
        }
    }
}
