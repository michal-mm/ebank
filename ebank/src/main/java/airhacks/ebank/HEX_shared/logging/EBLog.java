package airhacks.ebank.HEX_shared.logging;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

/// Application-facing façade over [System.Logger]: shrinks the API to the
/// levels actually used and makes logging injectable via [LogProducer], so
/// beans never construct loggers themselves.
public record EBLog(Logger systemLogger) {

    public EBLog(Class<?> clazz){
        this(System.getLogger(clazz.getName()));
    }

    public void info(String message){
        systemLogger.log(Level.INFO, message);
    }

    public void error(String message){
        systemLogger.log(Level.ERROR, message);
    }

    public void error(String message, Throwable exception){
        systemLogger.log(Level.ERROR, message, exception);
    }
}
