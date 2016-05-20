package com.github.feodorov.api

import com.github.feodorov.model.{DataType, Diff}
import com.github.feodorov.utils.DiffUtils

import scala.concurrent.{ExecutionContext, Future}
import scalaz._
import Scalaz._

/**
  * @author kfeodorov
  * @since 19.05.16.
  */
trait BusinessLogicv1 extends BusinessLogic { storage: Storage[String, String] =>

  override def diff(id: String)(implicit ec: ExecutionContext): Future[Diff] = Future {{(
      storage.get(s"${DataType.LEFT}$id").toSuccessNel("No left instance found") |@|
      storage.get(s"${DataType.RIGHT}$id").toSuccessNel("No right instance found")
      ) {_.wrapNel append _.wrapNel}} match {

    case Success(list) => DiffUtils.diff(list.head, list.last) match {
      case Success(d) => d
      case Failure(t) => throw new BusinessLogicException(t.list.toList.mkString(", "))
    }
    case Failure(m) => throw new BusinessLogicException(s"Not enough data: ${m.list.toList.mkString(", ")}")
  }}
}
