package classes.idgenerator;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class IdGeneratorSingleton implements IdGenerator {

    private static volatile IdGeneratorSingleton instance = null;
    private int currentId;
    private int maxId;
    private final String PATH = "D:\\id.xml";
    private Document doc;

    private IdGeneratorSingleton() {
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
        Element currentIdElem = (Element) doc.getElementsByTagName("currentId").item(0);
        currentId = Integer.parseInt(currentIdElem.getTextContent());
        maxId = currentId;
    }

    public static IdGeneratorSingleton getInstance() {
        if (instance == null) {
            synchronized (IdGeneratorSingleton.class) {
                if (instance == null) {
                    instance = new IdGeneratorSingleton();
                }
            }
        }
        return instance;
    }

    @Override
    public int generateId() {
        synchronized (instance) {
            if (maxId == currentId) {
                maxId += 100;
                Element currentIdElem = (Element) doc.getElementsByTagName("currentId").item(0);
                currentIdElem.setTextContent(String.valueOf(maxId));
                write(doc);
            }
            currentId++;
            return currentId - 1;
        }
    }

    private void write(Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(PATH));
            transformer.transform(source, result);
        }
        catch (TransformerException ex) {
            System.out.println("Exception occured!");
            System.out.println("Ошибка при записи в документ!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        }
        
    }
}
