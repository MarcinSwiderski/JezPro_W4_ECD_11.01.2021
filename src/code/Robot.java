package code;

public class Robot implements Runnable {
    private final ConsoleCharger consoleCharger;
    private static final int BASE_TIME_ON_CHARGE = 300;
    private final Object Lock = new Object();
    private final String robotName;
    private final int robotSize;
    private RobotStatus status;

    public Robot(String name, int robotSize, ConsoleCharger consoleCharger) {
        this.robotName = name;
        this.robotSize = robotSize;
        this.consoleCharger = consoleCharger;
    }

    public String getRobotName() {
        return robotName;
    }

    private enum RobotStatus {
        WORKING,
        WAITING,
        CHARGING,
        WAITING_FOR_FREE_CHARGER

    }

    public RobotStatus getRobotStatus() {
        synchronized (Lock) {
            return status;
        }
    }

    public int getRobotSize() {
        return robotSize;
    }

    public void setRobotStatus(RobotStatus robotStatus) {
        synchronized (Lock) {
            status = robotStatus;
        }
    }

    private int getOnChargeTime() {
        return robotSize * BASE_TIME_ON_CHARGE;
    }

    private int getChargeingTime() {
        return robotSize * 2 * BASE_TIME_ON_CHARGE;
    }

    @Override
    public void run() {

        try {
            while(true) {
                setRobotStatus(RobotStatus.CHARGING.WORKING);
                Thread.sleep(100 * getOnChargeTime());

                setRobotStatus(RobotStatus.WAITING);
                while(!consoleCharger.requestCharge(this)) {
                    setRobotStatus(RobotStatus.WAITING_FOR_FREE_CHARGER);
                    wait();
                    setRobotStatus(RobotStatus.WAITING);
                }

                setRobotStatus(RobotStatus.CHARGING);
                Thread.sleep(100 * getChargeingTime());

                consoleCharger.disconnect(this);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void isChargerAvailable() {
        if (getRobotStatus() == RobotStatus.WAITING)
            synchronized (this) {
                notify();
            }
    }

}
