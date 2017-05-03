package com.dwolla.testutils.akka

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestKitBase}
import com.typesafe.config.Config
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.After

import scala.annotation.tailrec

class AkkaTestKitSpecs2Support(config: Option[Config] = None)(implicit executionEnv: ExecutionEnv = null) extends TestKitBase with After with ImplicitSender {
  implicit lazy val system: ActorSystem = ActorSystem(safeTypeName, config, defaultExecutionContext = Option(executionEnv).map(_.executionContext))
  def after: Unit = TestKit.shutdownActorSystem(system)

  private def safeTypeName: String = enclosingClassName(this.getClass)
  @tailrec private def enclosingClassName(clazz: Class[_]): String = if (clazz.isLocalClass) enclosingClassName(clazz.getEnclosingClass) else clazz.getSimpleName
}
