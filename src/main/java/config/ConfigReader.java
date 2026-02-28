package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	
	// This holds all the key=values pairs from config.properites file
	private Properties properties;
	
	//This is single instance of class
	private static ConfigReader instance;
	
	//Path of the config file
	private static final String CONFIG_PATH=
			"src/test/resources/config.properties";
	
	//This run when ConfigReader is created
	//It opens the file and load everything into memory
	//Constructor created
	private  ConfigReader() {
		properties = new Properties();
		try {
			FileInputStream file = new FileInputStream(CONFIG_PATH);
			properties.load(file);
			System.out.println("Config file is loaded successfully");
		} catch(IOException e){
			throw new RuntimeException(
					"Could not find config.properties at:"+ CONFIG_PATH);
			
		}
	}		
		//Create a method to access Config Reader
		public static ConfigReader getInstance() {
			if(instance==null) {
				instance = new ConfigReader();
			}
			return instance;
		}
		
		//Method to get value from config file based on Key
		public String get(String key) {
			String value = properties.getProperty(key);
			if(value==null) {
				throw new RuntimeException(
						"Key not found in config.properties: "+ key);
			}
			return value.trim();
		}
		
		//Get number value for config file
		public int getInt(String key) {
			return Integer.parseInt(get(key));
		}
		
		
		//Get boolean value
		public boolean getBoolean(String key) {
			return Boolean.parseBoolean(get(key));
		}
		
	}


	
	

