package observer;

import entities.EventType;
import entities.OperationType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "Encryption_Results")
@XmlRootElement(name = "EncryptionResults")
public class EncryptionLogEventArgs {
    private OperationType operationType;
    private String algorithmName;
    private String fileName;
    private String filePath;
    private EventType eventType;
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

    @XmlAttribute
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    @XmlElement
    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getFileName() {
        return fileName;
    }

    @XmlElement
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    @XmlElement
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public EventType getEventType() {
        return eventType;
    }

    @XmlElement
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public long getProcessTime() {
        return processTime;
    }

    @XmlElement
    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }
}
