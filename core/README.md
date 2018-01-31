# Test Utilities Core

## Test Exceptions

### `NoStackTraceException`

Use `NoStackTraceException` whenever an intentionally-thrown exception is required in a test case. The exception will not have a stack trace and contains a message indicating it was intentionally thrown, keeping any logging that occurs much simpler.

