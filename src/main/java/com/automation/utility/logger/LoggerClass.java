package com.automation.utility.logger;



import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerClass {

	public void info(String className, String logInformation) {
		// Here we need to create logger instance so we need to pass Class name for
		// which we want to create log file in my case Google is classname
		 Logger logger = Logger.getLogger(className);
		// configure log4j properties file
		PropertyConfigurator.configure("Log4j.properties");
		logger.info(logInformation);

	}

}
