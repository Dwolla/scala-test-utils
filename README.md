# Scala Test Utilities

[![Travis](https://img.shields.io/travis/Dwolla/scala-test-utils.svg?style=flat-square)](https://travis-ci.org/Dwolla/scala-test-utils)
[![Bintray](https://img.shields.io/bintray/v/dwolla/maven/testutils.svg?style=flat-square)](https://bintray.com/dwolla/maven/testutils/view)
[![license](https://img.shields.io/github/license/Dwolla/scala-test-utils.svg?style=flat-square)]()

## Logging

The `com.dwolla.testutils.logging` package provides hooks into LogBack so that your tests can verify specific logging
events occurred without having to resort to mocking and method call verification, decoupling your tests from the 
main implementation. This is similar to the Akka TestKit’s `EventFilter` classes. (If you’re using Akka, use those
instead of this!)

To include the `InMemoryAppender` in the logging system, which will capture logging events made by your code, add an 
appender named `inMemory` of the class `com.dwolla.testutils.logging.InMemoryAppender` to the log configuration.
Here is example using `logback-test.xml` file in your project’s test `resources` folder.

    <?xml version="1.0" encoding="UTF-8"?>
    <configuration>
        <appender name="inMemory" class="com.dwolla.testutils.logging.InMemoryAppender"/>
    
        <root level="info">
            <appender-ref ref="inMemory"/>
        </root>
    </configuration>

Then write a test spec that extends `LoggingTest` and uses the `haveBeenLoggedAtLevel(…)` matcher.

    package com.dwolla.testutils.logging.examples
    
    import ch.qos.logback.classic.Level.INFO
    import com.dwolla.testutils.logging.LoggingTest
    import com.typesafe.scalalogging.LazyLogging
    import org.specs2.mock.Mockito
    import org.specs2.specification.Scope
    
    class LoggingExampleSpec extends LoggingTest with Mockito {
      "code under test" should {
        "log “Hello, World!” at the info level" in new Scope {
          CodeUnderTest.greet("World")
    
          "Hello, World!" must haveBeenLoggedAtLevel(INFO)
        }
      }
    }
    
    private object CodeUnderTest extends LazyLogging {
      def greet(entity: String): Unit = logger.info("Hello, {}!", entity)
    }

If your tests need to run sequentially instead of in parallel, create a new trait extending `Scope` to reset the log
buffer between each test.

    private trait Setup extends Scope {
      // only necessary if running tests sequentially
      resetCapturedLogs()
    }
  
This is necessary because the `InMemoryAppender` uses the current thread’s name to keep different tests separate. When
running tests sequentially, the thread name is always `main`, so this approach fails. (This approach will also fail
the multiple tests are being multiplexed onto the same thread.)

Using the [TypeSafe ScalaLogging](https://github.com/typesafehub/scala-logging) library is unnecessary, but it does
provide a nice Scala wrapper around the Javitude of LogBack and SLF4J.

## System Properties

Sometimes it’s helpful to test how your code reacts to different system property values. The `SystemPropertiesChangeGuard`
allows your code to change the values at-will, while isolating tests from each other. This includes restoring existing 
properties and clearing previously unset ones. Please note that the isolation only works when the tests are run 
sequentially, since ultimately they are all sharing the same global state.

Here is an example of a test using the utility. Note that the tests in this class are marked with the `sequential`
keyword.

    package com.dwolla.utils.proxy
    
    import java.lang.System
    import com.dwolla.testutils.systemproperties.SystemPropertiesChangeGuard._
    
    class HttpProxySettingsSpec extends org.specs2.mutable.Specification {

      sequential
      "HttpProxySettings" should {
        "return none if http.proxySet is false" >> {
          withSystemProperties("http.proxySet" -> "false", "http.proxyHost" -> None) {
            MaybeHttpProxySettings() must_== None
          }
        }
      }
    }

Also note that to enable the syntax above (with pairs of both types `String -> String` and `String -> Option[String]` in
one vararg), the `SystemPropertiesChangeGuard.string2OptionString` needs to be imported. In the above example, this is 
covered by the wildcard import `import com.dwolla.testutils.systemproperties.SystemPropertiesChangeGuard._`

It may be better to split caching the values and setting them to something new, in which case the `saveExisting` partial
function is for you.

    package com.dwolla.utils.proxy
    
    import java.lang.System
    import com.dwolla.testutils.systemproperties.SystemPropertiesChangeGuard
    
    class HttpProxySettingsSpec extends org.specs2.mutable.Specification {

      sequential
      "HttpProxySettings" should {
        "return none if http.proxySet is false" >> {
          SystemPropertiesChangeGuard.saveExisting("http.proxySet") {
            System.setProperty("http.proxySet", "false")

            MaybeHttpProxySettings() must_== None
          }
        }
      }
    }

## Java `InputStream`s

### `CloseTrackingInputStream`

`CloseTrackingInputStream` proxies another `java.io.InputStream` instance and tracks whether it has been closed. The class 
exposes a `isClosed` method. A matcher and DSL method are also provided.

    package com.dwolla.testutils.examples
    
    import com.dwolla.testutils.javaio.{CloseTrackingInputStream, IsClosedMatchers}
    import org.specs2.mutable.Specification
    import java.io.ByteArrayInputStream

    class CloseTrackingInputStreamSpec extends Specification with IsClosedMatchers {
      "CloseTrackingInputStream" should {
    
        "keep track of whether the proxied inputstream has been closed" >> {
          val stream = new CloseTrackingInputStream(new ByteArrayInputStream(Array()))
          stream must not(beClosed)
          stream.close()
          stream must beClosed
        }
      }
    }

## Concurrency

### `scala.concurrency.blocking` Function Calls

To ensure that the code under test invokes the `scala.concurrency.blocking` function, use the `BlockingMatcher`.
    
    import com.dwolla.testutils.concurrency.BlockingMatcher.invokeBlockingFunction
    import org.specs2.concurrent.{ExecutionEnv, NoImplicitExecutionContextFromExecutionEnv}
    import org.specs2.mutable.Specification
    
    import scala.concurrent.{ExecutionContext, Future, blocking}

    class BlockingMatcherSpec(implicit ee: ExecutionEnv) extends Specification with NoImplicitExecutionContextFromExecutionEnv {
      "blocking" should {
        "be blocked" in {
          { implicit ec: ExecutionContext ⇒
            Future {
              blocking {
                Thread.sleep(100)
              }
            }
          } must invokeBlockingFunction
        }
      }
    }

This test will pass as long as the `blocking` function is called, but will fail otherwise.

### Promise Completion

    import com.dwolla.testutils.concurrency.PromiseMatchers.beCompleted

    val promise = Promise[Unit]
    promise must beCompleted

## Akka

### `AkkaTestKitSpecs2Support`

Use the `AkkaTestKitSpecs2Support` class for the test case’s scope to make an `ActorSystem` available with an optional `Config`. The `ActorSystem` will be shutdown automatically after the test run.

    class TestSpec extends Specification {
      "test" should {
        "have access to an ActorSystem" in new AkkaTestKitSpecs2Support {
          …
        }
      }
    }

## Test Exceptions

### `NoStackTraceException`

Use `NoStackTraceException` whenever an intentionally-thrown exception is required in a test case. The exception will not have a stack trace and contains a message indicating it was intentionally thrown, keeping any logging that occurs much simpler.
