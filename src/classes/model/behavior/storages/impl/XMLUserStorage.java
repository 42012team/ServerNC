package classes.model.behavior.storages.impl;

import classes.model.behavior.storages.UserStorage;
import classes.model.User;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUserStorage implements UserStorage {

    private final String PATH = "D:\\nc\\users.xml";
    Document doc;

    public XMLUserStorage() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(new File(PATH));
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("Exception occured!");
            System.out.println("Ошибка при создании документа для записи!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        }

    }

    @Override
    public void storeUser(User user) {
        Element root = (Element) doc.getElementsByTagName("users").item(0);
        NodeList userNodes = doc.getElementsByTagName("user");
        boolean isExisting = false;
        for (int temp = 0; temp < userNodes.getLength(); temp++) {
            Element userElement = (Element) userNodes.item(temp);
            if (Integer.parseInt(userElement.getAttribute("id")) == user.getId()) {
                isExisting = true;
                userElement.setAttribute("name", user.getName());
                userElement.setAttribute("surname", user.getSurname());
                userElement.setAttribute("email", user.getEmail());
                userElement.setAttribute("phone", user.getPhone());
                userElement.setAttribute("address", user.getAddress());
                userElement.setAttribute("login", user.getLogin());
                userElement.setAttribute("password", user.getPassword());
                userElement.setAttribute("version", Integer.toString(user.getVersion()));
                userElement.setAttribute("privilege", user.getPrivilege());
                break;

            }
        }
        if (!isExisting) {
            Element rootElement = (Element) doc.getElementsByTagName("users").item(0);
            Element elementUser = doc.createElement("user");
            Attr attr = doc.createAttribute("name");
            attr.setValue(user.getName());
            elementUser.setAttributeNode(attr);
            attr = doc.createAttribute("id");
            attr.setValue(String.valueOf(user.getId()));
            elementUser.setAttributeNode(attr);
            attr = doc.createAttribute("surname");
            attr.setValue(user.getSurname());
            elementUser.setAttributeNode(attr);
            attr = doc.createAttribute("email");
            attr.setValue(user.getEmail());
            elementUser.setAttributeNode(attr);
            attr = doc.createAttribute("phone");
            attr.setValue(user.getPhone());
            elementUser.setAttributeNode(attr);
            attr = doc.createAttribute("address");
            attr.setValue(user.getAddress());
            elementUser.setAttributeNode(attr);
            attr = doc.createAttribute("login");
            attr.setValue(user.getLogin());
            elementUser.setAttributeNode(attr);
            attr = doc.createAttribute("password");
            attr.setValue(user.getPassword());
            elementUser.setAttributeNode(attr);
            attr = doc.createAttribute("version");
            attr.setValue(Integer.toString(user.getVersion()));
            elementUser.setAttributeNode(attr);
            attr = doc.createAttribute("privilege");
            attr.setValue(user.getPrivilege());
            elementUser.setAttributeNode(attr);
            rootElement.appendChild(elementUser);

        }
        write();
    }

    @Override
    public User getUser(String login, String password) {
        User user = null;
        NodeList userNodes = doc.getElementsByTagName("user");
        for (int temp = 0; temp < userNodes.getLength(); temp++) {
            Element userElement = (Element) userNodes.item(temp);
            if ((userElement.getAttribute("login").equals(login))
                    && (userElement.getAttribute("password").equals(password))) {
                user = new User(Integer.parseInt(userElement.getAttribute("id")),
                        userElement.getAttribute("name"), userElement.getAttribute("surname"),
                        userElement.getAttribute("email"), userElement.getAttribute("phone"),
                        userElement.getAttribute("address"), userElement.getAttribute("login"),
                        userElement.getAttribute("password"), Integer.parseInt(userElement.getAttribute("version")),
                        userElement.getAttribute("privilege"));
                break;
            }
        }
        return user;
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        NodeList userNodes = doc.getElementsByTagName("user");
        for (int temp = 0; temp < userNodes.getLength(); temp++) {
            Element userElement = (Element) userNodes.item(temp);
            if (Integer.parseInt(userElement.getAttribute("id")) == id) {
                user = new User(id,
                        userElement.getAttribute("name"), userElement.getAttribute("surname"),
                        userElement.getAttribute("email"), userElement.getAttribute("phone"),
                        userElement.getAttribute("address"), userElement.getAttribute("login"),
                        userElement.getAttribute("password"),
                        Integer.parseInt(userElement.getAttribute("version")),
                        userElement.getAttribute("privilege"));
                break;
            }

        }
        return user;

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

    @Override
    public User getUserByLogin(String login) {
        User user = null;
        NodeList userNodes = doc.getElementsByTagName("user");
        for (int temp = 0; temp < userNodes.getLength(); temp++) {
            Element userElement = (Element) userNodes.item(temp);
            if (userElement.getAttribute("login").equals(login)) {
                user = new User(Integer.parseInt(userElement.getAttribute("id")),
                        userElement.getAttribute("name"), userElement.getAttribute("surname"),
                        userElement.getAttribute("email"), userElement.getAttribute("phone"),
                        userElement.getAttribute("address"), userElement.getAttribute("login"),
                        userElement.getAttribute("password"),
                        Integer.parseInt(userElement.getAttribute("version")),
                        userElement.getAttribute("privilege"));
                break;
            }

        }
        return user;
    }

}
