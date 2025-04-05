package frontend.view.assets;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private final Properties properties;
    public String fileName = null;

    public PropertiesLoader(String fileName) {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.err.println("Sorry, unable to find " + fileName);
                return;
            }
            this.fileName = input.toString();
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Key not found in property:\n\tKey: " + key + "\n\tProperties: " + fileName);
        }
        return properties.getProperty(key);
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }
}
