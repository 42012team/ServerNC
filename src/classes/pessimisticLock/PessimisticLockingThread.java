package classes.pessimisticLock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PessimisticLockingThread extends Thread {

    private static final int BLOCKING_TIME=20000;
    private static List<Long> removingTime = new ArrayList<Long>();
    private static List<Integer> prohibitedActionList = new ArrayList<Integer>();
    private static Object monitor = new Object();

    public static boolean contains(int id) {
        for (int i = 0; i < prohibitedActionList.size(); i++) {
            if (prohibitedActionList.get(i) == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        while (true) {
            List<Integer> listForDeleting = new ArrayList<Integer>();
            Date currentDate = new Date();
            long time = currentDate.getTime();
            for (int i = 0; i < prohibitedActionList.size(); i++) {
                if (time >= removingTime.get(i)) {
                    listForDeleting.add(i);
                } else {
                    break;
                }
            }
            for (int i = 0; i < listForDeleting.size(); i++) {
                prohibitedActionList.remove((int) listForDeleting.get(i));
                removingTime.remove((int) listForDeleting.get(i));
            }
            long sleepingTime = 0;
            if (!removingTime.isEmpty()) {
                sleepingTime = removingTime.get(0) - currentDate.getTime();
            }
            if (sleepingTime > 0) {
                synchronized (monitor) {
                    try {
                        System.out.println("Поток блокировки уснул на " + sleepingTime / 1000 + " секунд");
                        monitor.wait(sleepingTime);
                    } catch (InterruptedException ex) {
                        System.out.println("InrerruptedException при работе потока блокировки!");
                    }
                }
            } else {
                synchronized (monitor) {
                    try {
                        System.out.println("Поток блокировки уснул");
                        monitor.wait();
                    } catch (InterruptedException ex) {
                        System.out.println("InrerruptedException при работе потока блокировки!");
                    }
                }
            }

        }

    }

    public static void schedule(int id) {
        Date currentDate = new Date();
        Long time = currentDate.getTime();
        prohibitedActionList.add(id);
        removingTime.add(time + BLOCKING_TIME);
        if (prohibitedActionList.size() == 1) {
            synchronized (monitor) {
                monitor.notify();
            }
        }
    }

    public static void unschedule(int id) {
        if (prohibitedActionList.get(0) == id) {
            prohibitedActionList.remove(prohibitedActionList.get(0));
            removingTime.remove(removingTime.get(0));
            synchronized (monitor) {
                monitor.notify();
            }
        } else {
            for (Integer ID : prohibitedActionList) {
                if (ID.equals(id)) {
                    removingTime.remove(removingTime.get(prohibitedActionList.indexOf(ID)));
                    prohibitedActionList.remove(ID);
                    break;
                }
            }
        }
    }

}
