package airhacks.ebank.health.boundary;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import airhacks.ebank.logging.control.EBLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/// Liveness probe that always reports up: it only proves the runtime answers
/// HTTP. Deliberately dependency-free, so an orchestrator never restarts the
/// instance just because a downstream system is down.
@Liveness
@ApplicationScoped
public class NoOpProbe implements HealthCheck {

    @Inject
    EBLog log;

    @Override
    public HealthCheckResponse call() {
        this.log.info("liveness checked");
        return HealthCheckResponse
                .up("Basic Availability");
    }

}
