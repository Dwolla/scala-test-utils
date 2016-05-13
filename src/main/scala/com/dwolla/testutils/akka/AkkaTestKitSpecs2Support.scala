package com.dwolla.testutils.akka

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKitBase}
import com.typesafe.config.Config
import org.specs2.mutable.After

import scala.annotation.tailrec

class AkkaTestKitSpecs2Support(config: Option[Config] = None) extends TestKitBase with After with ImplicitSender {
  implicit lazy val system: ActorSystem = ActorSystem(safeTypeName, config)
  def after = system.terminate()

  private def safeTypeName: String = enclosingClassName(this.getClass)
  @tailrec private def enclosingClassName(clazz: Class[_]): String = if (clazz.isLocalClass) enclosingClassName(clazz.getEnclosingClass) else clazz.getSimpleName
}
