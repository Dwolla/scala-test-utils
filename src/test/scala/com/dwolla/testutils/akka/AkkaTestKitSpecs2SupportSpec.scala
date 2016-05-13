package com.dwolla.testutils.akka

import akka.actor.Terminated
import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.{After, Specification}
import org.specs2.specification.Scope
import concurrent.duration._

class AkkaTestKitSpecs2SupportSpec(implicit ee: ExecutionEnv) extends Specification {

  trait ShutdownActorSystemAfterTest extends After {
    val testClass: AkkaTestKitSpecs2Support

    override def after: Any = testClass.system.terminate()
  }

  "AkkaTestKitSpecs2Support" should {
    "extend Scope and After" in new ShutdownActorSystemAfterTest {
      val testClass = new AkkaTestKitSpecs2Support

      testClass must beAnInstanceOf[Scope]
      testClass must beAnInstanceOf[After]
    }

    "pass the given Config to the ActorSystem" in new ShutdownActorSystemAfterTest {
      private val config = ConfigFactory.empty().withValue("testKey", ConfigValueFactory.fromAnyRef("testValue"))
      val testClass = new AkkaTestKitSpecs2Support(Option(config))

      testClass.system.settings.config.getString("testKey") must_== "testValue"
    }

    "name the ActorSystem after the top-level test class in which it appears" in new ShutdownActorSystemAfterTest {
      val testClass = new AkkaTestKitSpecs2Support() {}

      testClass.system.name must_== "AkkaTestKitSpecs2SupportSpec"
    }

    "shutdown the ActorSystem after the test run" in new ShutdownActorSystemAfterTest {
      val testClass = new AkkaTestKitSpecs2Support()

      testClass.after

      testClass.system.whenTerminated must beAnInstanceOf[Terminated].awaitFor(2.seconds)
    }
  }
}
