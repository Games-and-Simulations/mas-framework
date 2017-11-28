package aic.gas.mas.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to initialize logger
 */
public class MyLogger {

  private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public static Logger getLogger() {
    return LOGGER;
  }

  /**
   * Set logging level
   */
  public static void setLoggingLevel(Level loggingLevel) {
    LOGGER.setLevel(loggingLevel);
  }
}
