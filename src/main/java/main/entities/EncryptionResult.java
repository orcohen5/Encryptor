package main.entities;

public class EncryptionResult {
    private String encryptedContent;
    private String encryptionKeys;

    public EncryptionResult(String encryptedContent, String encryptionKeys) {
        this.encryptedContent = encryptedContent;
        this.encryptionKeys = encryptionKeys;
    }

    public String getEncryptedContent() {
        return encryptedContent;
    }

    public void setEncryptedContent(String encryptedContent) {
        this.encryptedContent = encryptedContent;
    }

    public String getEncryptionKey() {
        return encryptionKeys;
    }

    public void setEncryptionKeys(String encryptionKeys) {
        this.encryptionKeys = encryptionKeys;
    }
}
