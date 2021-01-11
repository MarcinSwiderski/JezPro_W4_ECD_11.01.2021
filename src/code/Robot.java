package code;

public class Robot implements Runnable {

    private final Object lock = new Object();
    private RobotStatus robotStatus;
    private final String robotName;
    private final int robotSize;
    private final ConsoleCharger consoleCharger;
    private static final int BASE_WORKING_MULTIPLAYER = 40;


    public enum RobotStatus {
        WORKING,
        CHARGING,
        WAITING,
        WANT_TO_CHARGE
    }

    public Robot( String name, int size, ConsoleCharger consoleCharger) {
        this.robotName = name;
        this.robotSize = size;
        this.consoleCharger = consoleCharger;
    }

    public String getRobotName() {
        return robotName;
    }

    public int getRobotSize() {
        return robotSize;
    }

    private void setRobotStatus(RobotStatus s) {
        synchronized (lock) {
            robotStatus = s;
        }
        consoleCharger.display();
    }

    private RobotStatus getRobotStatus() {
        synchronized (lock) {
            return robotStatus;
        }
    }

    private int getWorkingTime() {
        return BASE_WORKING_MULTIPLAYER * getRobotSize();
    }

    private int getChargingTime() {
        return BASE_WORKING_MULTIPLAYER * 2 * getRobotSize();
    }
    @Override
    public synchronized void run() {
        try {
            while(true) {
                setRobotStatus(RobotStatus.WORKING);
                Thread.sleep(100 * getWorkingTime());

                setRobotStatus(RobotStatus.WAITING);
                while(!consoleCharger.requestAccessToCharger(this)) {
                    setRobotStatus(RobotStatus.WANT_TO_CHARGE);
                    wait();
                    setRobotStatus(RobotStatus.WAITING);
                }

                setRobotStatus(RobotStatus.CHARGING);
                Thread.sleep(100 * getChargingTime());
                consoleCharger.disconnectRobotFromCharger(this);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void isChargerFree() {
        if(getRobotStatus() == RobotStatus.WANT_TO_CHARGE) {
            synchronized (this) {
                notify();
            }
        }
    }
    @Override
    public String toString() {
        return String.format("%s     %d      %s", robotName, robotSize, getRobotStatus());
    }
}