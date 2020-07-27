package observer;

import entities.EventType;
import entities.OperationType;

import javax.xml.bind.annotation.*;

@XmlType(namespace = "Encryption_Results")
@XmlRootElement(name = "EncryptionResults")
@XmlAccessorType(XmlAccessType.FIELD)
public class EncryptionLogEventArgs {
    @XmlAttribute(name = "operationType")
    private OperationType operationType;

    @XmlElement(name = "algorithmName")
    private String algorithmName;

    @XmlElement(name = "fileName")
    private String fileName;

    @XmlElement(name = "filePath")
    private String filePath;

    @XmlElement(name = "eventType")
    private EventType eventType;

    @XmlElement(name = "processTime")
    private long processTime;

    public EncryptionLogEventArgs() {
    }

    public EncryptionLogEventArgs(OperationType operationType, String algorithmName, String fileName, String filePath, EventType eventType, long processTime) {
        this.operationType = operationType;
        this.algorithmName = algorithmName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.eventType = eventType;
        this.processTime = processTime;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }
}
