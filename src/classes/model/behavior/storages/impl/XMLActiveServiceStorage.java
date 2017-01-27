package classes.model.behavior.storages.impl;

import classes.model.ActiveServiceStatus;
import classes.model.ActiveService;
import classes.model.behavior.storages.ActiveServiceStorage;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLActiveServiceStorage implements ActiveServiceStorage {

    private final String PATH = "D:\\nc\\activeServices.xml";
    Document doc;

    public XMLActiveServiceStorage() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(new File(PATH));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("Exception occured!");
            System.out.println("Ошибка при создании документа для записи!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        }
        doc.getDocumentElement().normalize();
    }

    @Override
    public void storeActiveServices(List<ActiveService> activeServicesList) {
        NodeList activeServiceNodes = doc.getElementsByTagName("activeService");
        for (int i = 0; i < activeServicesList.size(); i++) {
            boolean isExisting = false;
            for (int temp = 0; temp < activeServiceNodes.getLength(); temp++) {
                Element activeServiceElement = (Element) activeServiceNodes.item(temp);
                if (Integer.parseInt(activeServiceElement.getAttribute("id")) == activeServicesList.get(i).getId()) {
                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    String strDate = sdfDate.format(activeServicesList.get(i).getDate());
                    activeServiceElement.setAttribute("date", strDate);
                    activeServiceElement.setAttribute("currentStatus", activeServicesList.get(i).getCurrentStatus().toString());
                    if (activeServicesList.get(i).getNewStatus() != null) {
                        activeServiceElement.setAttribute("newStatus", activeServicesList.get(i).getNewStatus().toString());
                    } else {
                        activeServiceElement.setAttribute("newStatus", "");
                    }
                    activeServiceElement.setAttribute("serviceId", String.valueOf(activeServicesList.get(i).getServiceId()));
                    activeServiceElement.setAttribute("userId", String.valueOf(activeServicesList.get(i).getUserId()));
                    activeServiceElement.setAttribute("version", Integer.toString(activeServicesList.get(i).getVersion()));
                    isExisting = true;
                    break;
                }
            }
            if (!isExisting) {
                Element root = (Element) doc.getElementsByTagName("activeServices").item(0);
                Element activeServiceElement = doc.createElement("activeService");
                Attr attr = doc.createAttribute("id");
                attr.setValue(String.valueOf(activeServicesList.get(i).getId()));
                activeServiceElement.setAttributeNode(attr);
                attr = doc.createAttribute("serviceId");
                attr.setValue(String.valueOf(activeServicesList.get(i).getServiceId()));
                activeServiceElement.setAttributeNode(attr);
                attr = doc.createAttribute("userId");
                attr.setValue(String.valueOf(activeServicesList.get(i).getUserId()));
                activeServiceElement.setAttributeNode(attr);
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                String strDate = sdfDate.format(activeServicesList.get(i).getDate());
                attr = doc.createAttribute("date");
                attr.setValue(strDate);
                activeServiceElement.setAttributeNode(attr);
                attr = doc.createAttribute("currentStatus");
                attr.setValue("PLANNED");
                activeServiceElement.setAttributeNode(attr);
                attr = doc.createAttribute("newStatus");
                attr.setValue("ACTIVE");
                activeServiceElement.setAttributeNode(attr);
                attr = doc.createAttribute("version");
                attr.setValue(Integer.toString(activeServicesList.get(i).getVersion()));
                activeServiceElement.setAttributeNode(attr);
                root.appendChild(activeServiceElement);

            }
        }

        write(doc);
    }

    @Override
    public void deleteActiveService(int activeServiceId) {
        Element root = (Element) doc.getElementsByTagName("activeServices").item(0);
        NodeList activeServiceNodes = doc.getElementsByTagName("activeService");
        for (int temp = 0; temp < activeServiceNodes.getLength(); temp++) {
            Element activeServiceElement = (Element) activeServiceNodes.item(temp);
            if (Integer.parseInt(activeServiceElement.getAttribute("id")) == activeServiceId) {
                root.removeChild(activeServiceElement);
                break;
            }
        }
        write(doc);
    }

    @Override
    public List<ActiveService> getActiveServicesByUserId(int userId) {
        List<ActiveService> list = new ArrayList<>();
        NodeList activeServiceNodes = doc.getElementsByTagName("activeService");
        if (activeServiceNodes != null) {
            for (int temp = 0; temp < activeServiceNodes.getLength(); temp++) {
                Element activeServiceElement = (Element) activeServiceNodes.item(temp);
                if ((Integer.parseInt(activeServiceElement.getAttribute("userId")) == userId)) {

                    int activeServiceId = Integer.parseInt(activeServiceElement.getAttribute("id"));
                    int serviceId = Integer.parseInt(activeServiceElement.getAttribute("serviceId"));
                    int version = Integer.parseInt(activeServiceElement.getAttribute("version"));
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    Date date;
                    try {

                        date = format.parse(activeServiceElement.getAttribute("date"));

                        ActiveService us = null;
                        ActiveServiceStatus newStatus = null;
                        ActiveServiceStatus currentStatus = null;
                        switch (activeServiceElement.getAttribute("currentStatus")) {
                            case "ACTIVE":
                                currentStatus = ActiveServiceStatus.ACTIVE;
                                break;
                            case "PLANNED":
                                currentStatus = ActiveServiceStatus.PLANNED;
                                break;
                            case "SUSPENDED":
                                currentStatus = ActiveServiceStatus.SUSPENDED;
                                break;
                            case "DISCONNECTED":
                                currentStatus = ActiveServiceStatus.DISCONNECTED;
                                break;
                        }
                        switch (activeServiceElement.getAttribute("newStatus")) {
                            case "ACTIVE":
                                newStatus = ActiveServiceStatus.ACTIVE;
                                break;
                            case "PLANNED":
                                newStatus = ActiveServiceStatus.PLANNED;
                                break;
                            case "SUSPENDED":
                                newStatus = ActiveServiceStatus.SUSPENDED;
                                break;
                            case "DISCONNECTED":
                                newStatus = ActiveServiceStatus.DISCONNECTED;
                                break;
                        }
                        us = new ActiveService(activeServiceId, serviceId, userId, currentStatus, newStatus, date);
                        us.setVersion(version);
                        list.add(us);
                    } catch (ParseException ex) {
                        System.out.println("Exception occured!");
                        System.out.println("Ошибка в документе для записи!");
                        StackTraceElement[] stackTraceElements = ex.getStackTrace();
                        for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                            System.out.println(stackTraceElements[i].toString());
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public ActiveService getActiveServiceById(int activeServiceId) {
        ActiveService activeService = null;
        NodeList activeServiceNodes = doc.getElementsByTagName("activeService");
        if (activeServiceNodes != null) {
            for (int temp = 0; temp < activeServiceNodes.getLength(); temp++) {
                Element activeServiceElement = (Element) activeServiceNodes.item(temp);
                if ((Integer.parseInt(activeServiceElement.getAttribute("id")) == activeServiceId)) {
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    Date date;
                    try {
                        date = format.parse(activeServiceElement.getAttribute("date"));
                        ActiveServiceStatus newStatus = null;
                        ActiveServiceStatus currentStatus = null;
                        switch (activeServiceElement.getAttribute("currentStatus")) {
                            case "ACTIVE":
                                currentStatus = ActiveServiceStatus.ACTIVE;
                                break;
                            case "PLANNED":
                                currentStatus = ActiveServiceStatus.PLANNED;
                                break;
                            case "SUSPENDED":
                                currentStatus = ActiveServiceStatus.SUSPENDED;
                                break;
                            case "DISCONNECTED":
                                currentStatus = ActiveServiceStatus.DISCONNECTED;
                                break;
                        }
                        switch (activeServiceElement.getAttribute("newStatus")) {
                            case "ACTIVE":
                                newStatus = ActiveServiceStatus.ACTIVE;
                                break;
                            case "PLANNED":
                                newStatus = ActiveServiceStatus.PLANNED;
                                break;
                            case "SUSPENDED":
                                newStatus = ActiveServiceStatus.SUSPENDED;
                                break;
                            case "DISCONNECTED":
                                newStatus = ActiveServiceStatus.DISCONNECTED;
                                break;
                        }
                        int version = Integer.parseInt(activeServiceElement.getAttribute("version"));
                        activeService = new ActiveService(activeServiceId,
                                Integer.parseInt(activeServiceElement.getAttribute("serviceId")),
                                Integer.parseInt(activeServiceElement.getAttribute("userId")), currentStatus, newStatus, date);
                        activeService.setVersion(version);
                    } catch (ParseException ex) {
                        System.out.println("Exception occured!");
                        System.out.println("Ошибка в документе для записи!");
                        StackTraceElement[] stackTraceElements = ex.getStackTrace();
                        for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                            System.out.println(stackTraceElements[i].toString());
                        }
                    }
                }
            }
        }

        return activeService;
    }

    @Override
    public List<ActiveService> getAllActiveServices() {
        List<ActiveService> list = new ArrayList<>();
        NodeList activeServiceNodes = doc.getElementsByTagName("activeService");
        if (activeServiceNodes != null) {
            for (int temp = 0; temp < activeServiceNodes.getLength(); temp++) {
                Element activeServiceElement = (Element) activeServiceNodes.item(temp);
                int activeServiceId = Integer.parseInt(activeServiceElement.getAttribute("id"));
                int userId = Integer.parseInt(activeServiceElement.getAttribute("userId"));
                int serviceId = Integer.parseInt(activeServiceElement.getAttribute("serviceId"));
                int version = Integer.parseInt(activeServiceElement.getAttribute("version"));
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                Date date;
                try {

                    date = format.parse(activeServiceElement.getAttribute("date"));

                    ActiveService us = null;
                    ActiveServiceStatus newStatus = null;
                    ActiveServiceStatus currentStatus = null;
                    switch (activeServiceElement.getAttribute("currentStatus")) {
                        case "ACTIVE":
                            currentStatus = ActiveServiceStatus.ACTIVE;
                            break;
                        case "PLANNED":
                            currentStatus = ActiveServiceStatus.PLANNED;
                            break;
                        case "SUSPENDED":
                            currentStatus = ActiveServiceStatus.SUSPENDED;
                            break;
                        case "DISCONNECTED":
                            currentStatus = ActiveServiceStatus.DISCONNECTED;
                            break;
                    }
                    switch (activeServiceElement.getAttribute("newStatus")) {
                        case "ACTIVE":
                            newStatus = ActiveServiceStatus.ACTIVE;
                            break;
                        case "PLANNED":
                            newStatus = ActiveServiceStatus.PLANNED;
                            break;
                        case "SUSPENDED":
                            newStatus = ActiveServiceStatus.SUSPENDED;
                            break;
                        case "DISCONNECTED":
                            newStatus = ActiveServiceStatus.DISCONNECTED;
                            break;
                    }
                    us = new ActiveService(activeServiceId, serviceId, userId, currentStatus, newStatus, date);
                    us.setVersion(version);
                    list.add(us);
                } catch (ParseException ex) {
                    System.out.println("Exception occured!");
                    System.out.println("Ошибка в документе для записи!");
                    StackTraceElement[] stackTraceElements = ex.getStackTrace();
                    for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                        System.out.println(stackTraceElements[i].toString());
                    }
                }
            }
        }

        return list;

    }

    private void write(Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(PATH));
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            System.out.println("Exception occured!");
            System.out.println("Ошибка записи в документ!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        }
    }
}
