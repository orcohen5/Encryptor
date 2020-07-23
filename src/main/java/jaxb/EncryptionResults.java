package jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EncryptionResults")
public class EncryptionResults {
    private String operationType;
    private String algorithmType;
    private String originalFileName;
    private String outputFileName;
    private long operationLengthInMilliseconds;

    public EncryptionResults() {

    }

    public EncryptionResults(String operationType, String algorithmType, String originalFileName, String outputFileName, long operationLengthInMilliseconds) {
        this.operationType = operationType;
        this.algorithmType = algorithmType;
        this.originalFileName = originalFileName;
        this.outputFileName = outputFileName;
        this.operationLengthInMilliseconds = operationLengthInMilliseconds;
    }

    public String getOperationType() {
        return operationType;
    }

    @XmlAttribute
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getAlgorithmType() {
        return algorithmType;
    }

    @XmlElement
    public void setAlgorithmType(String algorithmType) {
        this.algorithmType = algorithmType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    @XmlElement
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    @XmlElement
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public long getOperationLengthInMilliseconds() {
        return operationLengthInMilliseconds;
    }

    @XmlElement
    public void setOperationLengthInMilliseconds(long operationLengthInMilliseconds) {
        this.operationLengthInMilliseconds = operationLengthInMilliseconds;
    }
}
