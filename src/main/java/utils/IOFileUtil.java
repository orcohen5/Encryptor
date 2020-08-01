package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class IOFileUtil {

    private IOFileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static List<File> getFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        List<File> filesList = new ArrayList();
        Collections.addAll(filesList, directory.listFiles());

        return filesList;
    }

    public static String getFileNameByPath(String filePath) {
        int lastSlashIndex = getLastSlashIndexInPath(filePath);

        return filePath.substring(lastSlashIndex + 1);
    }

    public static String getFileExtension(File file) {
        String filePath = file.getPath();
        int lastPointIndex = getLastDotIndexInPath(filePath);

        return filePath.substring(lastPointIndex);
    }

    public static String readFile(File file) throws IOException {
        if(!isValidFile(file)) {
            throw new IOException("ERROR: invalid file!");
        }

        return getFileContent(file);
    }

    public static boolean writeToFile(String fileContent, String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(fileContent);
        writer.close();

        return true;
    }

    public static boolean isValidFile(File file) {
        return (file.exists() && file.length() > 0 && isValidExtension(file) && !file.isDirectory());
    }

    public static boolean isValidDirectory(File directory) {
        return directory.isDirectory();
    }

    public static int getLastDotIndexInPath(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');

        if(lastDotIndex < 0) {
            lastDotIndex = filePath.length();
        }

        return lastDotIndex;
    }

    public static int getLastSlashIndexInPath(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf('\\');

        if(lastSlashIndex < 0) {
            lastSlashIndex = filePath.lastIndexOf('/');

            if(lastSlashIndex < 0) {
                lastSlashIndex = filePath.length() - 1;
            }
        }

        return lastSlashIndex;
    }

    private static String getFileContent(File file) throws IOException {
        List<String> fileLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        StringBuilder contentBuilder = new StringBuilder();

        for(String fileLine : fileLines) {
            contentBuilder.append(fileLine + "\n");
        }

        contentBuilder.deleteCharAt(contentBuilder.lastIndexOf("\n"));

        return contentBuilder.toString();
    }

    private static boolean isValidExtension(File file) {
        boolean isValid = false;
        String fileExtension = getFileExtension(file);
        List<String> acceptedExtensions = Arrays.asList(".txt");

        if(acceptedExtensions.contains(fileExtension)) {
            isValid = true;
        }

        return isValid;
    }
}
