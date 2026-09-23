package airhacks.ebank.HEX_shared.logging;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

/// Produces [EBLog] instances named after the injection point's declaring
/// class, so beans never repeat their own class literal and logger naming
/// stays consistent application-wide.
public class LogProducer {


    @Produces
    public EBLog create(InjectionPoint injectionPoint){
        var clazz = injectionPoint.getMember().getDeclaringClass();
        return new EBLog(clazz);
    }
}
