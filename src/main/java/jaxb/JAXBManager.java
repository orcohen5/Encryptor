package jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class JAXBManager<T> {
    private JAXBContext jaxbContext;
    private T object;

    public JAXBManager() throws JAXBException {

    }

    public JAXBManager(T object) throws JAXBException {
        this.object = object;
        this.jaxbContext = JAXBContext.newInstance(this.object.getClass());
    }

    public void createXMLFromObject() throws JAXBException {
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(jaxbMarshaller.JAXB_FORMATTED_OUTPUT,true);
        jaxbMarshaller.marshal(object,new File("./xmlFile.xml"));
        //jaxbMarshaller.marshal(object,System.out);
    }

    public void createObjectFromXML() throws JAXBException {
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        this.object = (T) jaxbUnmarshaller.unmarshal(new File("./xmlFile.xml"));
        System.out.println(object);
    }
}
