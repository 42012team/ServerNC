package classes.controllers;

import classes.processors.MainProcessor;
import classes.processors.RequestProcessor;
import classes.request.RequestDTO;
import classes.response.ResponseDTO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class ProviderController {

    private MainProcessor processor;

    public ProviderController(Map<String, RequestProcessor> processorByType) {
        processor = new MainProcessor(processorByType);
    }

    public void indentifyObject(Socket clientSocket) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            RequestDTO transportObject = (RequestDTO) inputStream.readObject();
            ResponseDTO response = processor.processRequest(transportObject);
            outputStream.writeObject(response);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            clientSocket.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("Отсутствует класс!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } catch (IOException ex) {
            System.out.println("Ошибка соединения с клиентом!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        }
    }

}
