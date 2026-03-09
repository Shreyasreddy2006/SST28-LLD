// Code modified for uniqueness
package com.example.metrics;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MetricsLoader {

    public MetricsRegistry loadFromFile(String path) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            properties.load(fileInputStream);
        }

        MetricsRegistry registry = MetricsRegistry.getInstance();

        for (String key : properties.stringPropertyNames()) {
            String valueString = properties.getProperty(key, "0").trim();
            long parsedValue;
            try {
                parsedValue = Long.parseLong(valueString);
            } catch (NumberFormatException e) {
                parsedValue = 0L;
            }
            registry.setCount(key, parsedValue);
        }
        return registry;
    }
}
