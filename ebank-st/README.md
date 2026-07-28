# ebank system tests

To perform black box tests locally (uses http://localhost:8080 by default):

```
mvn verify
```

The suite expects a freshly started ebank instance (dev mode recreates the schema on start):
the reporting requirement R1.2 ("no accounts exist") runs first via JUnit class ordering and
fails against a server that already carries accounts from a previous run.

or

```
mvn clean test-compile failsafe:integration-test
```

To test against a remote environment, set the BASE_URI environment variable:

```
export BASE_URI=https://deployed.com
mvn clean test-compile failsafe:integration-test
```