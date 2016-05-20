package com.github.feodorov.backend

import akka.actor.{Actor, Props}
import com.github.feodorov.api.APIv1
import com.typesafe.scalalogging.LazyLogging
import spray.routing.HttpServiceActor

import scala.concurrent.ExecutionContext

/**
  * @author kfeodorov
  * @since 19.05.16.
  */
class Router(val executionContext: ExecutionContext) extends HttpServiceActor with LazyLogging {

  val diffV1 = context.actorOf(Props(new DiffService with Actor with APIv1 {
    override implicit val ec = executionContext
    override def actorRefFactory = context
    override def receive = runRoute {route}
  }), "diffV1")

  override def receive: Receive = runRoute {
    pathPrefix("ping") {
      complete("OK. Up and running :)")
    } ~
    pathPrefix("v1") {
      diffV1 forward _
    }
  }
}
