package classes.activator;

import classes.model.ActiveService;

public interface ActivatorInterface {

    public void schedule(ActiveService activeService);

    public void unschedule(ActiveService activeService);

    public void reschedule(ActiveService activeService);

}
