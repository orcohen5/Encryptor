package main.jaxb;

import main.observer.EncryptionLogEventArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

@Component
public class JAXBManager {
    private JAXBContext jaxbContext;
    private EncryptionLogEventArgs object;

    @Autowired
    public JAXBManager(@Qualifier("encryptionLogEventArgs") EncryptionLogEventArgs object) throws JAXBException {
        this.object = object;
        this.jaxbContext = JAXBContext.newInstance(this.object.getClass());
    }

    public void createXMLFromObject(String xmlFilePath) throws JAXBException {
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(jaxbMarshaller.JAXB_FORMATTED_OUTPUT,true);
        jaxbMarshaller.marshal(object,new File(xmlFilePath));
    }

    public EncryptionLogEventArgs createObjectFromXML(String xmlFilePath) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        this.object = (EncryptionLogEventArgs) jaxbUnmarshaller.unmarshal(new File(xmlFilePath));
        return object;
    }

    public void validateXMLSchema(String xsdFilePath, String xmlFilePath) throws SAXException, IOException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema xsdSchema = factory.newSchema(new File(xsdFilePath));
            Validator validator = xsdSchema.newValidator();
            validator.validate(new StreamSource(new File(xmlFilePath)));
        } catch (SAXException e) {
            throw new SAXException("ERROR: One of the schemas is not valid!");
        } catch (IOException e) {
            throw new IOException("ERROR: One of the schema files does not exist!");
        }
    }
}
