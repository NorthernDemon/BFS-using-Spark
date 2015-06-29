package it.unitn.bd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads property file with service configuration
 */
public abstract class ServiceConfiguration {

    private static final Logger logger = LogManager.getLogger();

    public static final String CONFIGURATION_FILE = "service.properties";

    private static String appName;

    private static String ip;

    private static int port;

    private static String jar;

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(CONFIGURATION_FILE));
            appName = properties.getProperty("app-name");
            ip = properties.getProperty("ip");
            port = Integer.parseInt(properties.getProperty("port"));
            jar = properties.getProperty("jar");
        } catch (IOException e) {
            logger.error("Failed to load service configuration!", e);
        }
    }

    public static String getAppName() {
        return appName;
    }

    public static String getIp() {
        return ip;
    }

    public static int getPort() {
        return port;
    }

    public static String getJar() {
        return jar;
    }
}
