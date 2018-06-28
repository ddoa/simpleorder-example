package oose.dea.exceptions;

import java.util.logging.Logger;

public class NoImplementException extends RuntimeException {
    private static Logger logger = Logger.getLogger(NoImplementException.class.getName());

    public NoImplementException() {
        logger.severe("Not implemented yet");
    }
}
