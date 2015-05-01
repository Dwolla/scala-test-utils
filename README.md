# Scala Test Utilities

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
