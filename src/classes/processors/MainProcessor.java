package classes.processors;

import classes.exceptions.TransmittedException;
import classes.request.RequestDTO;
import classes.response.ResponseDTO;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainProcessor {

    private Map<String, RequestProcessor> processorsByTypesMap = new HashMap< String, RequestProcessor>();

    public MainProcessor(Map<String, RequestProcessor> processorsByTypesMap) {
        this.processorsByTypesMap = processorsByTypesMap;
    }

    public void addProcessorByType(String type, String className, String classpath) {
        //public void addProcessorByType(String type, String className, String classpath,SettingParams manager)or Manager<List>
        //если нужно и несколько managers, по нашей логике нужно в service и activeService, от SettingParams наследуем все
        try {
            ClassLoader loader = new DynamicClassOverloader(classpath);
            Class clazz = Class.forName(className, true, loader);
            Object object = clazz.newInstance();

            //Manager класс или интерфейс от которого будем наследовать 
            //Всех Managers
            Method m = clazz.getMethod("setValue", int.class);//  Method m=clazz.getMethod("setManager", Manager.class);
            m.invoke(object, 1);//m.invoke(object,manager);
            //   System.out.println(object);
            /* Method l[] = clazz.getMethods();
            for (int i = 0; i < l.length; i++) {
                if ((l[i].getParameters().length > 0) && l[i].getName() != "process") {
                    Parameter[] p = l[i].getParameters();
                    if (p[2].getName().equals("activeServiceManager")) {
                        processorByType.put(type, new CreateUserProcessor(userManager));
                    } else if (p[0].getName().equals("userManager")) {
                        l[i].invoke(object, userManager);
                        processorByType.put(type, (RequestProcessor) object);
                    }
                }
            }*/

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchMethodException | InvocationTargetException ex) {
            System.out.println("Exception occured!");
            System.out.println("Ошибка при добавлении процессора!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        }
    }

    public ResponseDTO processRequest(RequestDTO request) {
        String type = request.getRequestType();
        try {
            return processorsByTypesMap.get(type).process(request);
        } catch (NullPointerException ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
            return TransmittedException.create("ОШИБКА 404!").withExceptionType("exception");
        }
    }

}
