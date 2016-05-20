package com.github.feodorov.backend

import com.github.feodorov.api.{API, BusinessLogicException}
import com.github.feodorov.backend.JsonProtocol._
import com.github.feodorov.model.DiffOutcome._
import com.github.feodorov.model.{DataType, Diff}
import com.typesafe.scalalogging.LazyLogging
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._
import spray.routing.{ExceptionHandler, HttpService, Route}

import scala.concurrent.{ExecutionContext, Future}
import scalaz.Scalaz._
import scalaz._

/**
  * @author kfeodorov
  * @since 19.05.16.
  */
trait DiffService extends HttpService with LazyLogging { api: API =>
  implicit val ec: ExecutionContext

  implicit val eh = ExceptionHandler {
    case BusinessLogicException(msg) => complete(StatusCodes.OK, Diff(outcome = ERROR, message = msg.some))
    case e: Exception =>
      logger.error(e.toString, e)
      complete(e)
  }

  def route: Route =
    pathPrefix("diff" / Segment) { id =>
      path(Map("left" -> DataType.LEFT, "right" -> DataType.RIGHT)) { choice =>
        post {
          decompressRequest() {
            entity(as[String]) { payload =>
              complete(Future {
                api.put(s"$choice$id", payload)
              } map { _ => Diff(outcome = OK, message = "Saved".some)})
            }
          }
        }
      } ~ pathEndOrSingleSlash {
        complete(api.diff(id))
      }
    }
}
