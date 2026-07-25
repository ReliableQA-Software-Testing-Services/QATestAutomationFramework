/**
 * 
 */
package com.qatest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Divyang
 *
 */
public class ConfigReader {
    private Properties prop;

    public Properties init_prop() {
        prop = new Properties();
        
        // System.getProperty("user.dir") returns the current project root path
        String projectPath = System.getProperty("user.dir");
        
        // Use File.separator to ensure it works on both Windows (\) and Linux/Mac (/)
        String configFilePath = projectPath + File.separator + "test_config" + 
                           File.separator + "config.properties";
        try {
            FileInputStream ip = new FileInputStream(configFilePath);
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
