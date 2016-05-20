package com.github.feodorov.backend

import java.util.concurrent.Executors

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import spray.can.Http
import scalaz._, Scalaz._

import scala.concurrent.ExecutionContext

/**
  * @author kfeodorov 
  * @since 19.05.16
  */

object Main extends App with LazyLogging {

  val conf = ConfigFactory.load(s"application")

  val sprayEC = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(Math.min(2, Runtime.getRuntime.availableProcessors())))
  val businessLogicEC = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime.availableProcessors() - 2)))
  implicit val system = ActorSystem("rest-service", conf.some, none, sprayEC.some)

  val service = system.actorOf(Props(new Router(businessLogicEC)), "service-actor")
  val host = conf getString "service.host"
  val port = conf getInt "service.port"
  IO(Http) ! Http.Bind(service, host, port)

  println(s"Server started ${system.name}, $host:$port")

  scala.sys.addShutdownHook {
    println("Terminating...")
    system.terminate()
    println("Have a nice day!")
  }
}