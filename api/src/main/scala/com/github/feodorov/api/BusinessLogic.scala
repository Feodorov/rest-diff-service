package com.github.feodorov.api

import com.github.feodorov.model.Diff

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author kfeodorov
  * @since 19.05.16.
  */
trait BusinessLogic { storage: Storage[String, String] =>

  /**
    * Calculate diff for data by id
    * @param id id of left and right data
    * @param ec execution context to run calculations in
    * @return
    */
  def diff(id: String)(implicit ec: ExecutionContext): Future[Diff]
}
