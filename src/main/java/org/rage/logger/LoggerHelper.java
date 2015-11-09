package org.rage.logger;

public class LoggerHelper {

	private final static Boolean DEBUG_ENABLED = System.getProperty("rage.log.enabled") != null
			&& Boolean.valueOf(System.getProperty("rage.log.enabled"));
	
	public static void printException(final Exception exception){
		if(DEBUG_ENABLED){
			System.err.println(exception.getMessage());
			exception.printStackTrace();
		}
	}
}
