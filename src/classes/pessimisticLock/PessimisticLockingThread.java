package classes.pessimisticLock;

import java.util.*;

public class PessimisticLockingThread extends Thread {

    private static final int BLOCKING_TIME = 20000;
    private static Map<Long, Integer> prohibitedActionMap = new HashMap<Long, Integer>();
    private static Object monitor = new Object();

    public static boolean contains(int id) {
        return prohibitedActionMap.containsValue(id);
    }

    @Override
    public void run() {
        while (true) {
            long currentTime = new Date().getTime();
            long sleepingTime = currentTime;
            Set<Long> timeToDeleteSet = prohibitedActionMap.keySet();
            for (Long deletingTime : timeToDeleteSet) {
                if (deletingTime <= currentTime) {
                    prohibitedActionMap.remove(deletingTime);
                } else {
                    if (deletingTime - currentTime < sleepingTime) {
                        sleepingTime = deletingTime - currentTime;
                    }
                }
            }
            if (sleepingTime <= BLOCKING_TIME) {
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

    public static long schedule(int id) {
        Long currentTime = new Date().getTime();
        long timeToDelete=currentTime+BLOCKING_TIME;
        prohibitedActionMap.put(timeToDelete, id);
        synchronized (monitor) {
            monitor.notify();
        }
        return timeToDelete;
    }

    public static void unschedule(int id) {
        Set<Long> allKeys = prohibitedActionMap.keySet();
        for (Long timeToDelete : allKeys) {
            if (prohibitedActionMap.get(timeToDelete).equals(id)) {
                prohibitedActionMap.remove(timeToDelete);
                break;
            }
        }
        synchronized (monitor) {
            monitor.notify();
        }
    }

}