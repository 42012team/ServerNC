package classes.main;

import classes.controllers.ProviderController;
import java.net.Socket;

public class ClientThread implements Runnable {

    Socket clientSocket;
    ProviderController controller;

    ClientThread(Socket clientSocket, ProviderController controller) {
        this.clientSocket = clientSocket;
        this.controller = controller;
    }

    @Override
    public void run() {
        controller.indentifyObject(clientSocket);
    }

}
