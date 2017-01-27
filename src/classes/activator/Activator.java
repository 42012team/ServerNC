package classes.activator;

import classes.model.ActiveService;
import classes.model.behavior.managers.ActiveServiceManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Activator extends Thread implements ActivatorInterface {

    private static Monitor monitor = null;
    private ActiveServiceManager activeServiceManager;
    private ArrayList<ActiveService> activeServicePool;

    public Activator() {
        monitor = new Monitor();
        activeServicePool = new ArrayList<ActiveService>();
    }

    private void init() {
        List<ActiveService> activeServiceList = activeServiceManager.getAllActiveServices();
        Date currentDate = new Date();
        for (ActiveService activeService : activeServiceList) {
            if ((activeService.getNewStatus() != null)) {
                activeServicePool.add(activeService);
            }
        }
        Collections.sort(activeServicePool);
    }

    public void setActiveServiceManager(ActiveServiceManager activeServiceManager) {
        this.activeServiceManager = activeServiceManager;
    }

    @Override
    public void run() {
        init();
        while (true) {
            List<ActiveService> listForChange = new ArrayList<ActiveService>();
            Date currentDate = new Date();
            long sleepingTime = currentDate.getTime();
            for (ActiveService activeService : activeServicePool) {
                if ((currentDate.compareTo(activeService.getDate()) >= 0) && (activeService.getNewStatus() != null)) {
                    activeServiceManager.changeActiveServiceStatus(activeService, activeService.getNewStatus(), null);
                    activeServiceManager.changeActiveServiceDate(activeService, currentDate);
                    listForChange.add(activeService);
                } else {
                    break;
                }
            }
            if (listForChange.size() > 0) {
                activeServiceManager.storeActiveServices(listForChange);
                activeServicePool.removeAll(listForChange);
            }
            if (!activeServicePool.isEmpty()) {
                sleepingTime = activeServicePool.get(0).getDate().getTime() - currentDate.getTime();
            }
            synchronized (monitor) {
                try {
                    System.out.println("Поток активации уснул на " + sleepingTime / 1000 + " секунд");
                    monitor.wait(sleepingTime);
                } catch (Exception ex) {
                    System.out.println("InrerruptedException при работе потока активации!");
                    StackTraceElement[] stackTraceElements = ex.getStackTrace();
                    for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                        System.out.println(stackTraceElements[i].toString());
                    }
                }
            }
        }
    }

    @Override
    public void schedule(ActiveService activeService) {
        if (activeServicePool.size() == 0) {
            activeServicePool.add(activeService);
            synchronized (monitor) {
                monitor.notify();
            }
        } else if (activeServicePool.get(0).getDate().compareTo(activeService.getDate()) > 0) {
            activeServicePool.add(activeService);
            Collections.sort(activeServicePool);
            synchronized (monitor) {
                monitor.notify();
            }
        } else {
            activeServicePool.add(activeService);
            Collections.sort(activeServicePool);
        }
    }

    @Override
    public void unschedule(ActiveService activeService) {
        if (activeServicePool.get(0).getId() == activeService.getId()) {
            activeServicePool.remove(activeServicePool.get(0));
            synchronized (monitor) {
                monitor.notify();
            }
        } else {
            for (ActiveService active : activeServicePool) {
                if (active.getId() == activeService.getId()) {
                    activeServicePool.remove(active);
                    break;
                }
            }
        }
    }

    @Override
    public void reschedule(ActiveService activeService) {
        for (ActiveService active : activeServicePool) {
            if (active.getId() == activeService.getId()) {
                activeServicePool.remove(active);
                break;
            }
        }
        schedule(activeService);
    }
}
