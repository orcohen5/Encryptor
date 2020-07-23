package entities;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PropertiesReader {
    private static Properties properties = new Properties();

    private PropertiesReader() {
        throw new IllegalStateException("Utility class");
    }

    public static Properties initializeProperties() throws IOException {
        ClassLoader classLoader = PropertiesReader.class.getClassLoader();
        URL resource = classLoader.getResource("application.properties");
        FileReader reader = new FileReader(resource.getPath());
        properties.load(reader);

        return properties;
    }

    public static String getPropertyValueAsString(String propertyKey) {
        return properties.getProperty(propertyKey);
    }

    public static int getPropertyValueAsInt(String propertyKey) {
        return Integer.parseInt(getPropertyValueAsString(propertyKey));
    }
}
