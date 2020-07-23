package observer;

import entities.EventType;

public class EncryptionLogEventArgs {
    private String algorithmName;
    private String fileName;
    private String filePath;
    private EventType eventType;
    private long processTime;

    public EncryptionLogEventArgs(String algorithmName, String fileName, String filePath, EventType eventType, long processTime) {
        this.algorithmName = algorithmName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.eventType = eventType;
        this.processTime = processTime;
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
