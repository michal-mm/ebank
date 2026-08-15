package airhacks.ebank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.enterprise.inject.Stereotype;

/// Marks components implementing procedural business logic and workflows,
/// joining the transaction started by a [Boundary]
/// ([bce.design](https://bce.design)). Fully optional.
@Stereotype
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Control {}
