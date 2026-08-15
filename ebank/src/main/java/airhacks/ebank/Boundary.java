package airhacks.ebank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Stereotype;
import jakarta.transaction.Transactional;

/// Marks coarse-grained entry points that expose application functionality
/// and start the transaction — the single place where `@Transactional` is
/// allowed ([bce.design](https://bce.design)). Fully optional.
@ApplicationScoped
@Transactional
@Stereotype
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Boundary {}
