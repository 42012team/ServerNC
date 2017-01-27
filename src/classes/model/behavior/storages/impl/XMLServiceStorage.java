package classes.model.behavior.storages.impl;

import classes.model.behavior.storages.ServiceStorage;
import classes.model.ServiceStatus;
import classes.model.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLServiceStorage implements ServiceStorage {

    private final String PATH = "D:\\nc\\services.xml";
    private Document doc;

    public XMLServiceStorage() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;

        try {
            docBuilder = dbFactory.newDocumentBuilder();
            doc = docBuilder.parse(new File(PATH));
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
    public List<Service> getAllServices() {
        ArrayList<Service> serviceList = new ArrayList<>();
        NodeList services = doc.getElementsByTagName("id");
        for (int i = 0; i < services.getLength(); i++) {
            Service service = new Service(Integer.parseInt(((Element) services.item(i)).getAttribute("value")),
                    ((Element) services.item(i)).getAttribute("name"),
                    ((Element) services.item(i)).getElementsByTagName("description").item(0).getTextContent(),
                    ((Element) services.item(i)).getAttribute("type"),
                    Integer.valueOf(((Element) services.item(i)).getAttribute("version")));
            switch (((Element) services.item(i)).getAttribute("status")) {
                case "ALLOWED":
                    service.setStatus(ServiceStatus.ALLOWED);
                    break;
                case "DEPRECATED":
                    service.setStatus(ServiceStatus.DEPRECATED);
                    break;
            }
            serviceList.add(service);
        }
        return serviceList;
    }

    @Override
    public Service getServiceById(int serviceId) {
        Service service = null;
        NodeList services = doc.getElementsByTagName("id");
        for (int i = 0; i < services.getLength(); i++) {
            if (Integer.parseInt(((Element) services.item(i)).getAttribute("value")) == serviceId) {
                service = new Service(serviceId,
                        ((Element) services.item(i)).getAttribute("name"),
                        ((Element) services.item(i)).getElementsByTagName("description").item(0).getTextContent(),
                        ((Element) services.item(i)).getAttribute("type"),
                        Integer.valueOf(((Element) services.item(i)).getAttribute("version")));
                switch (((Element) services.item(i)).getAttribute("status")) {
                    case "ALLOWED":
                        service.setStatus(ServiceStatus.ALLOWED);
                        break;
                    case "DEPRECATED":
                        service.setStatus(ServiceStatus.DEPRECATED);
                        break;
                }
            }
        }
        return service;
    }

    @Override
    public void storeService(Service service) {
        boolean isExisting = false;
        NodeList services = doc.getElementsByTagName("id");
        for (int i = 0; i < services.getLength(); i++) {
            if (Integer.parseInt(((Element) services.item(i)).getAttribute("value")) == service.getId()) {
                ((Element) services.item(i)).setAttribute("name", service.getName());
                ((Element) services.item(i)).setAttribute("type", service.getType());
                ((Element) services.item(i)).getElementsByTagName("description").item(0).setTextContent(service.getDescription());
                ((Element) services.item(i)).setAttribute("status", service.getStatus().toString());
                ((Element) services.item(i)).setAttribute("version", Integer.toString(service.getVersion()));
                isExisting = true;
                break;
            }
        }
        if (!isExisting) {
            NodeList serviceList = doc.getElementsByTagName("services");
            Element additionalService = doc.createElement("id");
            additionalService.setAttribute("value", Integer.toString(service.getId()));
            additionalService.setAttribute("name", service.getName());
            additionalService.setAttribute("type", service.getType());
            additionalService.setAttribute("status", service.getStatus().toString());
            additionalService.setAttribute("version", Integer.toString(service.getVersion()));
            Element serviceDescription = doc.createElement("description");
            serviceDescription.appendChild(doc.createTextNode(service.getDescription()));
            additionalService.appendChild(serviceDescription);
            serviceList.item(0).appendChild(additionalService);
        }
        write();
    }

    @Override
    public void deleteService(int serviceId) {
        Element allServices = (Element) doc.getElementsByTagName("services").item(0);
        NodeList serviceList = allServices.getElementsByTagName("id");
        for (int i = 0; i < serviceList.getLength(); i++) {
            if (((Element) serviceList.item(i)).getAttribute("value").equals(String.valueOf(serviceId))) {
                allServices.removeChild(serviceList.item(i));
            }
        }
        write();
    }

    private void write() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(PATH));
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            System.out.println("Exception occured!");
            System.out.println("Ошибка при записи в документ!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        }
    }

}
