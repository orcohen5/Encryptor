package main.utils;

import java.util.ArrayList;
import java.util.List;

public class AsciiStringConverterUtil {

    private AsciiStringConverterUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Long> convertStringToAsciiCodes(String contentToConvert) {
        List<Long> asciiCodesList = new ArrayList();

        for(char charInContent : contentToConvert.toCharArray()) {
            long charCode = (long) charInContent;
            asciiCodesList.add(charCode);
        }

        return asciiCodesList;
    }

    public static String convertAsciiCodesToString(List<Long> contentToConvert) {
        StringBuilder stringContentBuilder = new StringBuilder();

        for(long asciiCode : contentToConvert) {
            char regularChar = (char) asciiCode;
            stringContentBuilder.append(regularChar);
        }

        return stringContentBuilder.toString();
    }

    public static List<Long> convertAsciiCodesStringToAsciiCodes(String contentToConvert) {
        List<Long> asciiCodesList = new ArrayList();
        String[] asciiCodes = contentToConvert.split(",");

        for(String asciiCode : asciiCodes) {
            asciiCodesList.add(Long.parseLong(asciiCode));
        }

        return asciiCodesList;
    }

    public static String convertAsciiCodesToAsciiCodesString(List<Long> asciiCodesList) {
        StringBuilder asciiStringBuilder = new StringBuilder();

        for(long asciiCode : asciiCodesList) {
            asciiStringBuilder.append(asciiCode + ",");
        }

        asciiStringBuilder.deleteCharAt(asciiStringBuilder.length() - 1);

        return asciiStringBuilder.toString();
    }
}
