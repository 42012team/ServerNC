package classes.processors;

import java.util.Map;

public interface Configuration {

    public Map<String, RequestProcessor> getMap(Initializer initializer);

}
